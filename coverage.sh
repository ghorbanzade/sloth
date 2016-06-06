#!/bin/bash

# codecov.io
echo -n "Reporting coverage to codecov... "
bash <(curl -s https://codecov.io/bash)
echo "OK"

# codacy
echo -n "Reporting coverage to codacy... "
curl https://www.jpm4j.org/install/script | sh
jpm install com.codacy:codacy-coverage-reporter:assembly
[ "${TRAVIS_PULL_REQUEST}" = "false" ] && codacy-coverage-reporter -l Java -r build/reports/jacoco/test/jacocoTestReport.xml --prefix src/main/java/
echo "OK"
