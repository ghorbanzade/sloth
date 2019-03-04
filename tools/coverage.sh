#!/bin/bash

__log () {
    if [ $# -lt 2 ]; then return; fi
    local msg="${@:2}"
    printf "%-10s %s\n" $1 "${msg}"
}
log_debug () { [[ ${DEBUG} -eq 1 ]] && __log 'debug' $@; }
log_info  () { __log 'info' $@; }
log_error () { __log 'error' $@; }

task_codacy () {
    if [ -z ${CODACY_PROJECT_TOKEN} ]; then
        log_error "codacy project token is not set"
        return;
    fi
    wget -q -O ~/codacy-coverage-reporter-assembly-latest.jar $(curl https://api.github.com/repos/codacy/codacy-coverage-reporter/releases/latest | jq -r .assets[0].browser_download_url)
    [ "${TRAVIS_PULL_REQUEST}" = "false" ] && java -cp ~/codacy-coverage-reporter-assembly-latest.jar com.codacy.CodacyCoverageReporter -l Java -r build/reports/jacoco/test/jacocoTestReport.xml
}

task_codecov () {
    bash <(curl -s https://codecov.io/bash)
}

for arg in "$@"; do
    case $arg in
        codacy)
            log_debug "reporting coverage to codacy"
            task_codacy
            ;;
        codecov)
            log_debug "reporting coverage to codecov"
            task_codecov
            ;;
    esac
done
