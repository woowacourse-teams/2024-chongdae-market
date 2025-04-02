#!/bin/bash
NGINX_CONTAINER_NAME="nginx"
BLUE_CONTAINER="chongdae_backend"
GREEN_CONTAINER="chongdae_backend_new"

change_blue_green_container() {
  local current_container=$1
	local next_container=$2
	local old_container="$1_old"
	echo "[+] change container name $next_container"
	docker rename $current_container $old_container
	docker rename $next_container $current_container

  echo "$old_container"
}

finish_check() {
  local container="$1"
  local interface="eth0"
  local interval="3"      # 기본 3초
  local max_retry="10"   # 기본 100회

  echo "finish check $container for idle network activity..."

  get_eth_stat() {
    docker exec "$container" cat /proc/net/dev 2>/dev/null | grep "$interface" | awk '{print $2, $10}'
  }


  if ! docker inspect "$container" &>/dev/null; then
    echo "$container container not found"
    return 1
  fi

  local prev_stat
  prev_stat=$(get_eth_stat)

  if [[ -z "$prev_stat" ]]; then
    echo "interface '$interface' not found"
    return 1
  fi

  for ((i=1; i<=max_retry; i++)); do
    sleep "$interval"
    local curr_stat
    curr_stat=$(get_eth_stat)

    echo "[INFO] Attempt $i: $curr_stat"

    if [[ "$curr_stat" == "$prev_stat" ]]; then
      echo "[INFO] network traffic finished!!! $curr_stat)"
      return 0
    fi

    prev_stat="$curr_stat"
  done

  echo "[ERROR] All $max_tries health check attempts failed for $container. Exiting..."
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

OLD_CONTAINER=$(change_blue_green_container "$BLUE_CONTAINER" "$GREEN_CONTAINER")
finish_check $OLD_CONTAINER

if [ $? -eq 0 ]; then
   remove_container $OLD_CONTAINER
else
   echo "[-] health check fail"
   exit 1
fi

echo "[-] clean docker image"
docker image prune -a -f
