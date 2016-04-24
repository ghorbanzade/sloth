# Sloth

Integrated Distributed Activity Recognition System with Minimal Network
Radio Communications

[![Build Status](https://travis-ci.org/ghorbanzade/sloth.svg?branch=master)](https://travis-ci.org/ghorbanzade/sloth)

## Installation

Packages required for building source code:

```
sudo apt-get install oraclejdk8
sudo apt-get install librxtx-java
```

Packages required for building documentations:

```
sudo apt-get install texlive-latex-base
sudo apt-get install texlive-latex-extra
```

Once you have all packages installed, you can simply build the software using the following command.

```
./gradlew build
```

## Configuration

Before running the software, you should update the file `/src/main/resources/server.properties` with correct ftp credentials.

## Contribution

Bug reports and pull requests are immensely appreciated.

## License

Source code is released under the [MIT License] agreement.

All documents are under [Creative Commons] Attribution-ShareAlike 4.0
International License.

## Contact

For questions or further information please contact [Pejman Ghorbanzade].

[MIT License]: https://github.com/ghorbanzade/sloth/blob/master/LICENSE
[Creative Commons]: https://github.com/ghorbanzade/sloth/blob/master/src/doc/LICENSE
[Pejman Ghorbanzade]: http://www.ghorbanzade.com
