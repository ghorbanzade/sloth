#!/bin/bash

set -e
set -o pipefail

# initialize logging

__log () {
    if [ $# -lt 2 ]; then return 1; fi
    printf "%-10s %s\\n" "$1" "${@:2}"
}
log_debug () { [[ "${DEBUG}" -eq 1 ]] && __log 'debug' "$@"; }
log_info  () { __log 'info' "$@"; }
log_warning () { __log 'warn' "$@"; }
log_error () { __log 'error' "$@"; return 1; }

# ensure that a given environment variable is set
require_env_var () {
    if [ $# -lt 1 ]; then return 1; fi
    for env in "$@"; do
        if [ -z "${!env}" ]; then
            log_error "environment variable $env is not set"
        fi
    done
}

# takes the name of a function as an argument and
# calls that function if it exists passing all given
# arguments to that function
call_if_exists() {
    if [ $# -lt 1 ]; then
        log_error "expecting at least one argument";
    fi
    if [ "$(type -t "$1")" == "function" ]; then
        eval "$1" "${@:2}"
    fi
}

before_install_linux() {
    log_info "building docker image ghorbanzade/sloth"
    docker-compose build
}

script_linux() {
    docker-compose --version
}

after_success_linux() {
    require_env_var "TRAVIS_BRANCH" "DOCKER_USERNAME" "DOCKER_PASSWORD"
    if [ "$TRAVIS_BRANCH" == "master" ]; then
        docker login -u "$DOCKER_USERNAME" -p "$DOCKER_PASSWORD";
        docker push ghorbanzade/sloth:aggregator;
    fi
}

if [ $# -ne 1 ]; then
    log_error "expecting one and only one argument";
fi

require_env_var "TRAVIS_OS_NAME"

call_if_exists "$1_${TRAVIS_OS_NAME}"
