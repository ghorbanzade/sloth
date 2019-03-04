FROM php:7.2-apache
LABEL maintainer="pejman@ghorbanzade.com"

RUN docker-php-ext-install pdo_mysql

RUN a2enmod rewrite
