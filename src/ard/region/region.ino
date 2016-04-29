//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

/**
 * This program simply reads sensor data from MPU-6050 module,
 * converts the acceleratiton data to the number assigned to a
 * region of the recognition sphere the acceleration data points
 * to and transmits the region number via zigbee module.
 *
 * A sample packet has the form: |3|16|
 */

#define MY 3                  // Identifier for each sensor board
#define INTERVAL 200          // Sampling Time in (ms)
#define THETA_REGIONS 8       // Number of sphere segments in zenith direction
#define MATH_PI 3.14159       // Number PI
#define MSG_LEN 16            // maximum length of the message to be sent
#define MPU 0x68              // I2C address of the MPU-6050

#include <Wire.h>

/**
 * a recognition model structure holds the total number of regions that
 */
struct recognition_model
{
	int num_region;
	int *prsm;
	int *aprm;
};

/**
 * accelerations are read in three x, y, z orientations.
 */
enum acc
{
	ACC_X,
	ACC_Y,
	ACC_Z,
	ACC_COUNT
};

/**
 * global variables include the recognition model
 */
struct recognition_model model;

/**
 * this function will initialize the recognition model with information
 * that will be used to quickly infer the sphere region based on obtained
 * acceleration data.
 *
 * @param model the object whose members should be initialized
 * @return zero for success and non-zero for failure
 */
int init_model(struct recognition_model *model)
{
	double trsm[THETA_REGIONS + 1];
	double strm[THETA_REGIONS];
	model->prsm = (int*) calloc(THETA_REGIONS, sizeof(int));
	model->aprm = (int*) calloc(THETA_REGIONS, sizeof(int));
	if (model->prsm == NULL)
		return EXIT_FAILURE;
	if (model->aprm == NULL)
		return EXIT_FAILURE;
	for (int i = 0; i < THETA_REGIONS + 1; i++)
		trsm[i] = i * MATH_PI / THETA_REGIONS;
	for (int i = 0; i < THETA_REGIONS; i++) {
		strm[i] = cos(trsm[i]) - cos(trsm[i + 1]);
		model->prsm[i] = round(strm[i] / strm[0]);
	}
	model->aprm[0] = 0;
	for (int i = 1; i < THETA_REGIONS; i++)
		model->aprm[i] = model->aprm[i - 1] + model->prsm[i - 1];
	model->num_region = model->aprm[THETA_REGIONS - 1] + model->prsm[THETA_REGIONS - 1];
	return EXIT_SUCCESS;
}

/**
 * this function reads mpu6050 sensor and updates a given array with
 * accelartion data.
 *
 * @param accs the array that should be initialized with acceleration data
 */
void read_raw_sensor_data(int16_t *accs)
{
	Wire.beginTransmission(MPU);
	// starting with register 0x3B (ACCEL_XOUT_H)
	Wire.write(0x3B);
	Wire.endTransmission(false);
	// request a total of 14 registers
	Wire.requestFrom(MPU, 14, true);
	// 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)
	accs[ACC_X] = Wire.read() << 8 | Wire.read();
	// 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
	accs[ACC_Y] = Wire.read() << 8 | Wire.read();
	// 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
	accs[ACC_Z] = Wire.read() << 8 | Wire.read();
	// 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
	Wire.read() << 8 | Wire.read();
	// 0x43 (GYRO_XOUT_H) & 0x44 (GYRO_XOUT_L)
	Wire.read() << 8 | Wire.read();
	// 0x45 (GYRO_YOUT_H) & 0x46 (GYRO_YOUT_L)
	Wire.read() << 8 | Wire.read();
	// 0x47 (GYRO_ZOUT_H) & 0x48 (GYRO_ZOUT_L)
	Wire.read() << 8 | Wire.read();
}

/**
 * this function converts raw acceleration data to the number assigned to the
 * region they are mapped to. It uses a previously initialized model to reduce
 * the amount of required calculations.
 *
 * @param raw raw acceleration data read from the sensor
 * @param region the variable to be updated based on acceleration data
 * @param model the recognition model to use to calculate the sphere region
 */
void process_region(
	int16_t *raw,
	int16_t *region,
	struct recognition_model const *const model
) {
	double phi, theta;
	int16_t reg_phi, reg_theta;
	phi = atan2(raw[ACC_Y], raw[ACC_X]) * 180.0 / MATH_PI + 180.0;
	theta = atan2(sqrt(pow(raw[ACC_X], 2) + pow(raw[ACC_Y], 2)), raw[ACC_Z]) * 180 / MATH_PI;
	reg_theta = floor(theta * THETA_REGIONS / 180.0) + 1;
	if (phi >= 360.0 - 180.0 / model->prsm[reg_theta - 1])
		reg_phi = 1;
	else
		reg_phi = floor(phi * model->prsm[reg_theta - 1] / 360.0 + 0.5) + 1;
	*region = model->aprm[reg_theta - 1] + reg_phi;
}

/**
 * This function initializes the recognition model and prepares the board
 * for reading MPU6050 and transmitting via zigbee module.
 */
void setup() {
	init_model(&model);
	Wire.begin();
	Wire.beginTransmission(MPU);
	Wire.write(0x6B);           // PWR_MGMT_1 register
	Wire.write(0);              // set to zero (wakes up the MPU-6050)
	Wire.endTransmission(true);
	Serial.begin(9600);
}

/**
 * This function reads acceleration data from MPU-6050, converts them to the
 * number assigned to a region of recognition sphere that the acceleration data
 * is mapped to and transmits the region number via the zigbee module. This
 * function is called repeatedly as long as the board is turned on.
 */
void loop()
{
	unsigned long tic = millis();
	int16_t raw[ACC_COUNT];
	int16_t region;
	char message[MSG_LEN];
	read_raw_sensor_data(&raw[0]);
	process_region(&raw[0], &region, &model);
	sprintf(message, "|%d|%d|\n", MY, region);
	Serial.write(message);
	delay(INTERVAL - (millis() - tic));
}
