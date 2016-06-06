#!/bin/bash

# codecov.io
echo -n "Reporting coverage to codecov... "
bash <(curl -s https://codecov.io/bash)
echo "OK"

# codacy
echo -n "Reporting coverage to codacy... "
mkdir --parents target/jpm && curl -sL http://bit.ly/jpm4j > target/jpm/jpm4j.jar && java -jar target/jpm/jpm4j.jar -u init && export PATH=$PATH:$HOME/jpm/bin && jpm install com.codacy:codacy-coverage-reporter:assembly
[ "${TRAVIS_PULL_REQUEST}" = "false" ] && codacy-coverage-reporter -l Java -r build/reports/jacoco/test/jacocoTestReport.xml --prefix src/main/java/
echo "OK"
