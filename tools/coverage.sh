#!/bin/bash

task_codacy () {
    if [ -z "${CODACY_PROJECT_TOKEN}" ]; then
        echo "codacy project token is not set"
        exit 1
    fi
    wget -q -O ~/codacy-coverage-reporter-assembly-latest.jar "$(curl https://api.github.com/repos/codacy/codacy-coverage-reporter/releases/latest | jq -r .assets[0].browser_download_url)"
    [ "${TRAVIS_PULL_REQUEST}" = "false" ] && java -cp ~/codacy-coverage-reporter-assembly-latest.jar com.codacy.CodacyCoverageReporter -l Java -r build/reports/jacoco/test/jacocoTestReport.xml
}

task_codecov () {
    bash <(curl -s https://codecov.io/bash)
}

for arg in "$@"; do
    case $arg in
        codacy)
            echo "reporting coverage to codacy"
            task_codacy
            ;;
        codecov)
            echo "reporting coverage to codecov"
            task_codecov
            ;;
    esac
done
