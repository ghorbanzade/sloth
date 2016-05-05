## Sloth: An Energy-Efficient Activity Recognition System

[![Build Status](https://travis-ci.org/ghorbanzade/sloth.svg?branch=master)](https://travis-ci.org/ghorbanzade/sloth)

Human activity recognition is one of the well-established applications of pervasive computing applications.
Sensor-based activity recognition blends wearable computing with internet of things to create a wireless network of small-sized embedded sensor platforms that can detect activities of daily living (e.g. walking, eating, cooking, writing) in real-time and over a long period of time.

Even though, recognition of human physical activities in laboratory conditions is fairly straight forward, there are many technical challenges, still to address, in order to meet real-world application requirements.
One of these important issues is improving energy efficiency of the wearable system to ensure its long operational lifetime without renewal of its power resources.
As the power consumption of sensor platforms is largely due to network radio communications, minimizing network communication rate while maintaining real-time functionality of the system will have a great impact in improving network operational lifetime.

This project proposes a distributed classification algorithm across a network of wearable devices that will greatly reduces required radio communications for fine-grained classification of activities.
Using this algorithm, wearable sensor platforms sample accelerations with very high sampling rates, continually updating a vector of features to be transmitted only every few seconds.

Sloth is an end-to-end real-time activity monitoring system.
It has been tested on a network with 8 Arduino boards as sensor platforms, one raspberry PI as the sink node, a cloud server API and a static website that allows authorized users to monitor user's activities as they are being performed.

Are you impressed? Check out the [Wiki Pages] to learn more about Sloth and what it takes to build your own activity recognition system that can last for days without charging!

### Contribution

Bug reports and pull requests are immensely appreciated.

### License

Source code is released under the [MIT License].

### Contact

For questions or further information please contact [Pejman Ghorbanzade].

[MIT License]: https://github.com/ghorbanzade/sloth/blob/master/LICENSE
[Wiki Pages]: https://github.com/ghorbanzade/sloth/wiki
[Pejman Ghorbanzade]: http://www.ghorbanzade.com
