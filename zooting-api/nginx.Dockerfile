FROM nginx

RUN rm -rf /etc/nginx/conf.d/*

COPY nginx/prod/nginx.conf /etc/nginx/conf.d/nginx.conf

EXPOSE 80