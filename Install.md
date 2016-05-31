## Building Software

Following are the required package(s) to build this software:

```
sudo apt-get install oraclejdk8
```

Once all packages are installed, run the following command to build the software.

```
./gradlew build
```

You will also need the following package to allow the software to communicate with the sensor nodes through serial port.

```
sudo apt-get install librxtx-java
```

To run the software, run the following command from Sloth's root directory.

```
java -jar build/libs/sloth-v0.x.jar
```
