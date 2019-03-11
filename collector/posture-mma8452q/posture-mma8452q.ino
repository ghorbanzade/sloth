//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <pejman@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

/**
 * This program reads sensor data from MPU-6050 module, converts the
 * acceleratiton data to the number assigned to a region of the
 * recognition sphere the acceleration data points to, increments the
 * element of activity code whose index corresponds to that region and
 * after an interval of multiple sensor reading, transmits that array in
 * one single packet.
 *
 * A sample packet has the form:
 * |3|015|003|000|006|013|014|00a|02e|000|000|001|
 *
 * size of the array being transmitted depends on the number of regions
 * on the recognition sphere which is calculated based on THETA_REGIONS
 * macro.
 */

#define MY 3                    // Identifier for each sensor board
#define INTERVAL 5000           // Sampling Time in (ms)
#define THETA_REGIONS 8         // Number of sphere segments in zenith direction
#define MATH_PI 3.14159         // Number PI
#define MSG_LEN 128             // maximum length of the message to be transmitted

// function to convert nibble to hex character
#define TO_HEX(i) (i <= 9 ? '0' + i : 'A' - 10 + i)
// offset to correct acceleration reading
#define ACC_OFFSET_X 0
#define ACC_OFFSET_Y 0
#define ACC_OFFSET_Z 0
// the threshold acceleration to interprete sensor reading as currupted
#define MAX_ACCELERATION 1.2 * 16384

#include <Wire.h>
#include <SparkFun_MMA8452Q.h>

/**
 * a recognition model structure holds the total number of regions that
 */
struct recognition_model
{
	uint16_t num_region;
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
 * global variable to store sensor readings
 */
MMA8452Q accel;

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
 * this function reads mpu6050 sensor and updates a given array with accelartion
 * data.
 *
 * @param accs the array that should be initialized with acceleration data
 */
void read_raw_sensor_data(int16_t *accs)
{
	if (accel.available()) {
		accel.read();
		accs[ACC_X] = (int16_t) (accel.cx * 16384);
		accs[ACC_Y] = (int16_t) (accel.cy * 16384);
		accs[ACC_Z] = (int16_t) (accel.cz * 16384);
	} else {
		accs[ACC_X] = MAX_ACCELERATION;
		accs[ACC_Y] = MAX_ACCELERATION;
		accs[ACC_Z] = MAX_ACCELERATION;
	}
}

/**
 * Depending on how the sensor module is soldered and what resistors have
 * been used on the module, sensor acceleration readings may have significant
 * accuracies. To resolve this issue, the following functions adds an offset
 * value to the sensor readings. These values defined as macros, should be
 * obtained by uploading the calibrate.ino sketch on each board and monitoring
 * received packets from the board while it has a steady state (e.g. placed
 * on a table).
 *
 * @param accs acceleration data to be calibrated
 */
void calibrate(int16_t *accs)
{
	accs[ACC_X] += ACC_OFFSET_X;
	accs[ACC_Y] += ACC_OFFSET_Y;
	accs[ACC_Z] += ACC_OFFSET_Z;
}

/**
 * Acceleration data read from the sensor are static acceleration together
 * with dynamic acceleration caused by body movement. In case dynamic
 * acceleration is not negligible (e.g. the subject is running), recognition
 * model being used fails to correctly interpret the region numer. This
 * function makes sure packets affected by dynamic acceleration are discarded
 * and do not affect the activity code being constructed.
 *
 * @param accs acceleration data read from the sensor
 * @return whether sensor data are severely affected by dynamic acceleration
 */
int is_currupt(int16_t *accs)
{
	int i;
	for (i = 0; i < ACC_COUNT; i++) {
		if (accs[i] >= MAX_ACCELERATION)
			return 1;
	}
	return 0;
}

/**
 * this function converts raw acceleration data to the number assigned to the
 * region they are mapped to. It uses a previously initialized model to reduce
 * the amount of required calculations.
 *
 * @param raw raw acceleration data read from the sensor
 * @param region the variable to be updated based on acceleration data
 * @param model the recognition model to be used to calculate the sphere region
 */
void process_region(
	int16_t *raw,
	uint8_t *region,
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
 * This function resets the posture to all zeros every time a packet is
 * transmitted.
 *
 * @param the array representing posture
 * @param the size of the array
 */
void reset_posture(uint16_t *posture, uint8_t posture_size)
{
	for (int i = 0; i < posture_size; i++) {
		posture[i] = 0;
	}
}

/**
 * This function takes a 1 byte integer and converts it to string
 * representation of its hexa decimal equivalent.
 *
 * @param num the integer which should be converted to hexa
 * @param ch a pointer to the string being built
 */
void uint16_to_hexstr(uint16_t num, char *ch)
{
	// most significant nibble is always 0
	// ch[0] = TO_HEX(((num & 0xF000) >> 12));
	ch[0] = TO_HEX(((num & 0x0F00) >> 8));
	ch[1] = TO_HEX(((num & 0x00F0) >> 4));
	ch[2] = TO_HEX((num & 0x000F));
}

/**
 * After the listening time window is over, a string repreesntation of the
 * activity code should be constructed for transmission. This function
 * constructs the message to be sent which contains the id of the board and
 * the activity code for passed time window.
 *
 * @param ch a pointer to the string that should be constructed
 * @param posture the array of activity code
 * @param size of the posture array (equal to number of regions in sphere)
 */
void build_message(char *ch, uint16_t *posture, uint16_t posture_size)
{
	int i;
	sprintf(ch, "|%d|", MY);
	while (*ch != '\0')
		ch++;
	for (i = 0; i < posture_size; i++) {
		uint16_to_hexstr(posture[i], ch);
		ch[3] = '|';
		ch = ch + 4;
	}
	*ch++ = '\n';
	*ch = '\0';
}

/**
 * This function initializes the recognition model and prepares the board
 * for reading MPU6050 and transmitting via zigbee module.
 */
void setup()
{
	init_model(&model);
	Serial.begin(9600);
	accel.init();
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
	uint16_t posture[model.num_region];
	uint8_t region;
	char message[MSG_LEN];
	reset_posture(posture, model.num_region);
	while (millis() - tic < INTERVAL) {
		read_raw_sensor_data(&raw[0]);
		calibrate(&raw[0]);
		if (is_currupt(&raw[0]))
			continue;
		process_region(&raw[0], &region, &model);
		posture[region - 1]++;
	}
	build_message(message, &posture[0], model.num_region);
	Serial.write(message);
}
