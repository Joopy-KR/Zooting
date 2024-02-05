# node 20.10.0 이미지 사용
FROM node:20.10.0-alpine

# package.json, package-lock.json 복사
COPY package*.json ./

# 의존성 설치
RUN npm install

# 현재 폴더를 workdir폴더에 복사한다.
COPY ./ .

# 빌드하기 !
RUN npm run build-only
