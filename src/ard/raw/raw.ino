//
// Sloth: An Energy-Efficient Activity Recognition System
// Copyright 2016 Pejman Ghorbanzade <mail@ghorbanzade.com>
// Released under the terms of MIT License
// https://github.com/ghorbanzade/sloth/blob/master/LICENSE
//

/*
 * This program simply reads sensor data from MPU-6050 module
 * and transmits the triaxial acceleration data in raw format.
 *
 * A sample packet has the form: |3|1084|140|15576|
 */

#define MY 3                 // Identifier for each sensor board
#define INTERVAL 200         // Sampling Time in (ms)

#include <Wire.h>

const int MPU = 0x68;  // I2C address of the MPU-6050
char message[64];
int16_t AcX;           // Acceleration in X axis
int16_t AcY;           // Acceleration in Y axis
int16_t AcZ;           // Acceleration in Z axis
unsigned long tic;

void setup() {
    Wire.begin();
    Wire.beginTransmission(MPU);
    Wire.write(0x6B);                     // PWR_MGMT_1 register
    Wire.write(0);                        // set to zero (wakes up the MPU-6050)
    Wire.endTransmission(true);
    Serial.begin(9600);
}

void loop() {
    tic = millis();
    Wire.beginTransmission(MPU);
    Wire.write(0x3B);                     // starting with register 0x3B (ACCEL_XOUT_H)
    Wire.endTransmission(false);
    Wire.requestFrom(MPU, 14, true);      // request a total of 14 registers
    AcX = Wire.read() << 8 | Wire.read(); // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)
    AcY = Wire.read() << 8 | Wire.read(); // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
    AcZ = Wire.read() << 8 | Wire.read(); // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
    Wire.read() << 8 | Wire.read(); // 0x41 (TEMP_OUT_H) & 0x42 (TEMP_OUT_L)
    Wire.read() << 8 | Wire.read(); // 0x43 (GYRO_XOUT_H) & 0x44 (GYRO_XOUT_L)
    Wire.read() << 8 | Wire.read(); // 0x45 (GYRO_YOUT_H) & 0x46 (GYRO_YOUT_L)
    Wire.read() << 8 | Wire.read(); // 0x47 (GYRO_ZOUT_H) & 0x48 (GYRO_ZOUT_L)
    sprintf(message, "|%d|%d|%d|%d|\n", MY, AcX, AcY, AcZ);
    Serial.write(message);
    delay(INTERVAL - (millis() - tic));
}
