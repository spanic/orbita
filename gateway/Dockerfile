# syntax=docker/dockerfile:1
FROM nginxinc/nginx-unprivileged:1.27-alpine

COPY proxy-common.conf /etc/nginx/snippets/proxy-common.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 8080
