FROM php:7.3-fpm
LABEL maintainer="pejman@ghorbanzade.com"
ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update \
  && apt-get install -y --no-install-recommends \
    libzip-dev unzip zlib1g-dev \
  && rm -rf /var/lib/apt/lists/*

RUN docker-php-ext-install pdo_mysql zip

COPY backend /var/www/html

RUN curl -sS https://getcomposer.org/installer | \
   php -- --install-dir=/usr/local/bin --filename=composer

RUN groupadd -r sloth && useradd --no-log-init -r -g sloth sloth
RUN chown -v -R sloth:sloth /var/www/html
USER sloth

RUN composer --no-ansi --no-interaction --working-dir=/var/www/html install
