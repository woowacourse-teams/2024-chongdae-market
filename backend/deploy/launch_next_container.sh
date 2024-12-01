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
    local max_tries=$2
    local sleep_seconds=$3

    for ((i=1; i<=max_tries; i++)); do
        echo "[INFO] Attempt $i: Checking health of $next_container"
        local status=$(docker exec nginx curl -H "Host: localhost" -o /dev/null -s -w "%{http_code}\n" http://$next_container:8080/health-check)

	      if [[ "$status" -eq "200" ]]; then
            echo "[INFO] Health check successful."
            return 0
        fi

        echo "[WARNING] Health check failed. Attempt $i of $max_tries."
        sleep $sleep_seconds
    done

    echo "[ERROR] All $max_tries health check attempts failed for $next_container. Exiting..."
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
	-v /uploads:/uploads \
	-e SPRING_PROFILES_ACTIVE=${PROFILE_ACTIVE} \
	${DOCKERHUB_USER_NAME}/${DOCKER_IMAGE_NAME}:${GITHUB_SHA:0:7}

MAX_TRIES=5
SLEEP_SECONDS=10
health_check "$NEXT_CONTAINER" "$MAX_TRIES" "$SLEEP_SECONDS"
if [ $? -ne 0 ]; then
    exit 1
fi
