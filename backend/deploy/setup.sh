#!/bin/bash
GITHUB_SHA=$1
PROFILE_ACTIVE=$2
DOCKERHUB_USER_NAME=$3
DOCKER_IMAGE_NAME=$4

# parameter check
if [ -z "$GITHUB_SHA" ] || [ -z "$PROFILE_ACTIVE" ] || [ -z "$DOCKERHUB_USER_NAME" ] || [ -z "$DOCKER_IMAGE_NAME" ]; then
  echo "사용법: $0 <GITHUB_SHA> <PROFILE_ACTIVE> <DOCKERHUB_USER_NAME> <DOCKER_IMAGE_NAME>"
  exit 1
fi

# intialize blue & gren
INITIAL_INSTANCE_NAME="chongdae_backend_green"
INITIAL_BLUE_GREEN_NETWORK_NAME="blue_green_network"
NGINX_DEFAULT_CONF_PATH="default.conf"
NGINX_NEW_CONF_PATH="new-default.conf"

# 1. ADD DOCKER NETWORK
if ! docker network ls | grep -q ${INITIAL_BLUE_GREEN_NETWORK_NAME}; then
	echo "[+] Create Docker Netowrk ${INITIAL_BLUE_GREEN_NETWORK_NAME}"
	docker network create --driver bridge "${INITIAL_BLUE_GREEN_NETWORK_NAME}"
fi

echo "[+] stop and remove intial container"

# 2. RUN INITIAL_CONTAINER
echo "[+] add chongdae_backend container"
docker rm -f ${INITIAL_INSTANCE_NAME} >/dev/null 2>&1

docker run -d \
	--network ${INITIAL_BLUE_GREEN_NETWORK_NAME} \
       	--name ${INITIAL_INSTANCE_NAME} \
	-v /logs:/logs \
	-e SPRING_PROFILES_ACTIVE=${PROFILE_ACTIVE} \
	${DOCKERHUB_USER_NAME}/${DOCKER_IMAGE_NAME}:${GITHUB_SHA:0:7}

if [ $? -ne 0 ]; then
	exit 1
fi

# 3. CHANGE INITIAL UP STREAM SERVER
cp ${NGINX_DEFAULT_CONF_PATH} ${NGINX_NEW_CONF_PATH}

if grep -q "server chongdae_backend:" "${NGINX_NEW_CONF_PATH}"; then
	sed -i "s/server chongdae_backend:/server ${INITIAL_INSTANCE_NAME}:/" "${NGINX_NEW_CONF_PATH}"
fi

# 4. RUN DOCKER COMPOSE NGINX
echo "[+] RUN nginx server"
docker run -d \
	--network ${INITIAL_BLUE_GREEN_NETWORK_NAME} \
	--name nginx \
       	-p 80:80 \
     	-v ./${NGINX_NEW_CONF_PATH}:/etc/nginx/conf.d/default.conf \
	nginx:latest

# 5. clean up
rm -rf ${NGINX_NEW_CONF_PATH}

