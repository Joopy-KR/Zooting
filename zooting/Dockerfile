FROM node:20.10.0-alpine as build-stage
WORKDIR /app
COPY package*.json ./

RUN rm -rf node_modules package-lock.json
RUN npm install

COPY ./ .
ARG VITE_SERVER_API_URL
ENV VITE_SERVER_API_URL $VITE_SERVER_API_URL
RUN npm run build-only

FROM nginx:stable-alpine as production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]