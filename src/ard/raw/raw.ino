//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

/**
 * This program simply reads sensor data from MPU-6050 module
 * and transmits the triaxial acceleration data in raw format.
 *
 * A sample packet has the form: |3|1084|140|15576|
 */

#define MY 3                  // Identifier for each sensor board
#define INTERVAL 200          // Sampling Time in (ms)
#define MSG_LEN 32            // maximum length of the message to be transmitted
#define MPU 0x68              // I2C address of the MPU-6050

#include <Wire.h>

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
 * this function reads mpu6050 sensor and updates a given array with accelartion
 * data.
 *
 * @param accs the array that should be initialized with acceleration data
 */
void read_raw_sensor_data(int16_t *accs)
{
  Wire.beginTransmission(MPU);
  Wire.write(0x3B);               // starting with register 0x3B (ACCEL_XOUT_H)
  Wire.endTransmission(false);
  Wire.requestFrom(MPU, 14, true);// request a total of 14 registers
  accs[ACC_X] = Wire.read() << 8 | Wire.read(); // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)
  accs[ACC_Y] = Wire.read() << 8 | Wire.read(); // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
  accs[ACC_Z] = Wire.read() << 8 | Wire.read(); // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
  Wire.read() << 8 | Wire.read(); // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
  Wire.read() << 8 | Wire.read(); // 0x43 (GYRO_XOUT_H) & 0x44 (GYRO_XOUT_L)
  Wire.read() << 8 | Wire.read(); // 0x45 (GYRO_YOUT_H) & 0x46 (GYRO_YOUT_L)
  Wire.read() << 8 | Wire.read(); // 0x47 (GYRO_ZOUT_H) & 0x48 (GYRO_ZOUT_L)
}

/**
 * This function prepares the board for reading MPU6050 and transmitting
 * via zigbee module.
 */
void setup() {
    Wire.begin();
    Wire.beginTransmission(MPU);
    Wire.write(0x6B);                     // PWR_MGMT_1 register
    Wire.write(0);                        // set to zero (wakes up the MPU-6050)
    Wire.endTransmission(true);
    Serial.begin(9600);
}

/**
 * This function reads acceleration data from MPU-6050 and transmits them
 * via the zigbee module. It is called repeatedly as long as the board is
 * turned on.
 */
void loop() {
    unsigned long tic = millis();
    int16_t raw[ACC_COUNT];
    char message[MSG_LEN];
    read_raw_sensor_data(&raw[0]);
    sprintf(message, "|%d|%d|%d|%d|\n", MY, raw[ACC_X], raw[ACC_Y], raw[ACC_Z]);
    Serial.write(message);
    delay(INTERVAL - (millis() - tic));
}
