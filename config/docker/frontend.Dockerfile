# ---- frontend (production version) ----

FROM jekyll/jekyll:3.8 as frontend_builder
LABEL maintainer="pghorbanzade@vitalimages.com"

COPY frontend /srv/jekyll
RUN jekyll build --verbose \
    && cp -r _site /opt/dist

# ---- production ----

FROM nginx:1.15-alpine
LABEL maintainer="pghorbanzade@vitalimages.com"

COPY --from=frontend_builder /opt/dist /www/data
COPY config/nginx/default.conf /etc/nginx/conf.d/default.conf
