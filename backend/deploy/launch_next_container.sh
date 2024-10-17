#!/bin/bash

GITHUB_SHA=$1
PROFILE_ACTIVE=$2
DOCKERHUB_USER_NAME=$3
DOCKER_IMAGE_NAME=$4

INITIAL_BLUE_GREEN_NETWORK_NAME="blue_green_network"
BLUE_CONTAINER="chongdae_backend_blue"
GREEN_CONTAINER="chongdae_backend_green"
NGINX_CONTAINER="nginx"

# parameter check
if [ -z "$GITHUB_SHA" ] || [ -z "$PROFILE_ACTIVE" ] || [ -z "$DOCKERHUB_USER_NAME" ] || [ -z "$DOCKER_IMAGE_NAME" ]; then
  echo "사용법: $0 <GITHUB_SHA> <PROFILE_ACTIVE> <DOCKERHUB_USER_NAME> <DOCKER_IMAGE_NAME>"
  exit 1
fi

get_active_container() {
  local active_container_name=$(docker exec ${NGINX_CONTAINER} cat /etc/nginx/conf.d/default.conf | \
    grep -P '^\s*server\s+\S+:' | \
    grep -oP '(?<=server\s)[^:;]+' | \
    sed 's/^[[:space:]]*//;s/[[:space:]]*$//')

  echo "$active_container_name"
}

get_next_container() {
  local active_container=$1
  if [[ "$active_container" == "$GREEN_CONTAINER" ]]; then
    echo "$BLUE_CONTAINER"
    return 0
  fi

  if [[ "$active_container" == "$BLUE_CONTAINER" ]]; then
    echo "$GREEN_CONTAINER"
    return 0
  fi

  return 1
}

health_check() {
	local next_container=$1
	local sleep_seconds="10s"
	
	echo "[INFO] sleep $sleep_seconds for waiting container up"
	sleep $sleep_seconds

	local status=$(docker exec ${NGINX_CONTAINER} curl -H "Host: localhost" -o /dev/null -s -w "%{http_code}\n" http://$next_container:8080)

	if [ $? -eq 0 ]; then
		echo "server response code: $status"
		return 0
	fi
	return 1
}

ACTIVE_CONTAINER=$(get_active_container)
NEXT_CONTAINER=$(get_next_container $ACTIVE_CONTAINER)

echo "[+] launch $NEXT_CONTAINER"

docker rm -f ${NEXT_CONTAINER} >/dev/null 2>&1

docker run -d \
	--network ${INITIAL_BLUE_GREEN_NETWORK_NAME} \
       	--name ${NEXT_CONTAINER} \
	-v /logs:/logs \
	-e SPRING_PROFILES_ACTIVE=${PROFILE_ACTIVE} \
	${DOCKERHUB_USER_NAME}/${DOCKER_IMAGE_NAME}:${GITHUB_SHA:0:7}

health_check $NEXT_CONTAINER

if [ $? -ne 0 ]; then
	exit 1
fi

