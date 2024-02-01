version: "4"
services:
  springboot:
    image: duckbill413/zooting-docker-api:latest
    container_name: zooting-api
    ports:
      - "8080:8080"
    environment:
      TZ: "Asia/Seoul"
    networks:
      - zooting_net
    restart: "always"

  redis:
    image: redis:alpine
    container_name: zooting-redis
    command: redis-server --port 6379
    hostname: redis
    ports:
      - "6379:6379"
    networks:
      - zooting_net
    restart: always

#  nginx:
#    image: duckbill413/zooting-docker-nginx:latest
#    container_name: zooting-nginx
#    ports:
#      - "80:80"
#    build:
#      - ./nginx
#    depends_on:
#      - springboot
#    networks:
#      - zooting_net
#    restart: "always"

networks:
  zooting_net:
    driver: bridge