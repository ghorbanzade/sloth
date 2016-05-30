# Introduction

Human activity recognition is one of the well-established applications of pervasive computing.
Sensor-based activity recognition blends wearable computing with the internet of things (IoT) to create a wireless network of small-sized embedded sensor platforms that can detect activities of daily living (e.g. walking, eating, cooking, writing) in real-time and over a long period of time.
Such a wearable system can be a crucial element of any mobile healthcare monitoring system.
With advances in wearble computing and IoT, activity recognition systems are promised to have an important role in shaping technologies of next few years.

It is not difficult to classify human physical activities using a stream of body motion data.
With the built-in accelerometer of our smart phones and the computational power of today's typical personal computers, any machine learning package can classify primary activities of daily living with a reasonable classification accuracy.
The research community is saturated with papers on activity recognition, many of which lack innovation and address classification of only primitive activities in a laboratory setting.

Developing a real-time activity recognition system that can endure challenges of real-world scenarios, however, is no easy task.
Fine-grained classification of activties of daily living, solely based on human body posture, in real-time and with an operation lifetime of up to a few days requires tackling a variety of challenges in embedded system design, distributed sensor networks and advanced signal processing techniques.

In order for the system to be as unobtrusive as possible, wireless sensor platforms should be custom-designed to be small enough to be integrated into human casual clothes.
This requirement imposes non-negligible limitations on power resources of sensor nodes.
Given that power consumption of wireless sensor platforms is largely due to network radio communications, the problem would be how to minimize network radio activities while ensuring real-time functionality of the system.

This project aims to address the mentioned problem by distributing the classification algorithm across a network of eight sensor platforms and a sink node, leveraging computational capacity of all platforms.
Aa a result, instead of having to send raw sensor data with a high transmission rate, wireless platforms transmit a partially processed vector of features for sensor data sampled at high frequency and over a relatively long time frame.
As the network radio activities decrease significantly, limited power resources will be conserved and a longer operational lifetime is guaranteed.

Sloth is an end-to-end real-time activity monitoring system.
It consists of 8 Arduino boards as wireless sensor platform, one Raspberry Pi as a sink node, a cloud-hosted API engine, and a static website that allows authorized users to monitor your activities in real-time.
Sloth is written in C, Java, PHP and AngularJs.
It uses a powerful multi-threaded classification algorithm, mostly written in Java, to recognize user activities and log them to a remote server where they can be queried by authorized users.

Are you impressed? Learn more about how to [get started](https://github.com/ghorbanzade/sloth/wiki/Getting-Started) with Sloth!
