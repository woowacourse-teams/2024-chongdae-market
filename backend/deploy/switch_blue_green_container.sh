#!/bin/bash
NGINX_CONTAINER_NAME="nginx"
BLUE_CONTAINER="chongdae_backend_blue"
GREEN_CONTAINER="chongdae_backend_green"
NGINX_DEFAULT_CONF="default.conf"
NGINX_CHANGED_DEFAULT_CONF="new-default.conf"

get_active_container() {
  local active_container_name=$(docker exec nginx cat /etc/nginx/conf.d/default.conf | \
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

change_blue_green_container() {
	local next_container=$1
	cp ${NGINX_DEFAULT_CONF} ${NGINX_CHANGED_DEFAULT_CONF}

	if grep -q "server chongdae_backend:" "${NGINX_CHANGED_DEFAULT_CONF}"; then
		sed -i "s/server chongdae_backend:/server ${next_container}:/" "${NGINX_CHANGED_DEFAULT_CONF}"
	fi
	echo "[+] change container $next_container"
	docker cp ./${NGINX_CHANGED_DEFAULT_CONF} ${NGINX_CONTAINER_NAME}:/etc/nginx/conf.d/${NGINX_CHANGED_DEFAULT_CONF}
	docker exec ${NGINX_CONTAINER_NAME} cp /etc/nginx/conf.d/${NGINX_CHANGED_DEFAULT_CONF} /etc/nginx/conf.d/default.conf
	docker exec ${NGINX_CONTAINER_NAME} rm -rf /etc/nginx/conf.d/${NGINX_CHANGED_DEFAULT_CONF}
	docker exec ${NGINX_CONTAINER_NAME} nginx -s reload
	
	# clean up
	rm -rf ${NGINX_CHANGED_DEFAULT_CONF}
}


health_check() {
	local status=$(curl -o /dev/null -s -w "%{http_code}\n" http://localhost)
	if [ $? -eq 0 ]; then
		echo "server response code: $status"			
		return 0
	fi	
	return 1	
}

remove_container() {
	local removed_container_name=$1
	
	docker rm -f $removed_container_name
	
	if [ $? -eq 0 ]; then
		echo "[-] stop $removed_container_name"
		return 0
	fi
	return 1
}

ACTIVE_CONTAINER=$(get_active_container)
NEXT_CONTAINER=$(get_next_container $ACTIVE_CONTAINER)

if [ $? -eq 0 ]; then
   change_blue_green_container $NEXT_CONTAINER
fi

health_check

if [ $? -eq 0 ]; then
   remove_container $ACTIVE_CONTAINER
fi

