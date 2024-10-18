# blue-green 스크립트

무중단 배포를 하기 위한 blue green 스크립트 입니다.

## 초기 설정

blue green을 처음 구축할 때 사용됩니다.

스크립트 실행 시 nginx 컨테이너와 총대마켓 컨테이너가 각각 80, 8080 포트로 구동되고, nginx는 총대마켓 컨테이너로 proxy_pass가 설정되도록 구축이됩니다.
외부의 요청을 받기 위해 nginx는 외부 80포트가 오픈됩니다.

### 스크립트 실행 방법

``` bash
./setup.sh <최근 배포 성공한 commit 해시> <적용 프로파일> <컨테이너 계정 명> <컨테이너 이미지 명>
```

## 이후 전략

CI에서 배포가 끝나면 배포 단계에서 `launch_next_container.sh`을 실행합니다.
실행 시 최신 빌드 성공한 commit이 반영 된 컨테이너가 실행됩니다.
컨테이너가 올라가면, `sleep second` 동안 대기 후 성공적으로 컨테이너가 실행됬는지 확인합니다.

이후 성공적으로 컨테이너가 구동 완료 되면 `switch_blue_green_container.sh`를 실행시켜 nginx가 새로운 컨테이너로 proxy_pass하도록 설정을 변경합니다.

### 스크립트 실행 과정

``` bash
./launch_next_container.sh <PR commit hash> <적용 프로파일> <컨테이너 계정 명> <컨테이너 이미지 명>

# blue -> green , green -> blue 전환
./switch_blue_green_container.sh 
```
