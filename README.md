# 월급쟁이 부자들 백엔드 과제

## How to run

```shell
Gradle:       8.8
Kotlin:       1.9.22
Groovy:       3.0.21
JVM:          21.0.4 (Oracle Corporation 21.0.4+8-LTS-274)
```

### 서버 실행 방법

```shell
./gradlew clean build
./gradlew bootRun
```

### 테스트 실행 방법

```shell
./gradlew clean build
./gradlew test -Dspring.profiles.active=test  
```

## API 문서

서버 실행 후 `http://localhost:8080/swagger-ui/index.html` 접속

## 구현 내용

### 환경 변수

중요한 보안사항이 담긴 환경변수가 없고, 과제를 위한 레포이기 때문에 환경변수 설정 파일을 git에 업로드했습니다.

- application.yaml
- application-test.yaml
-

### 구현 API 목록

- 인증
    - 회원가입 API
    - 로그인 API
    - 토큰 리프레시 API
- 유저
    - 내가 수강하고있는 강의 목록 조회 API
    - 수강신청 API
        - 여러개의 강의에 대해 수강신청 가능
            - 한 개의 강의라도 오류 발생 시, 에러처리
- 강의
    - 강의 등록 API
        - 강사 유저만 가능
    - 모든 강의 조회 API
        - 정렬, 페이지네이션 적용
    - 강의 단건 조회 API

## 개선하고 싶은 부분

1. token만료 에러를 따로 처리해, 클라이언트가 언제 refresh API를 호출해줘야 하는지 명확하게 할 필요가 있습니다.
2. 수강신청 API의 성능을 위한 메세지 큐 적용
    - 강좌별로 최대수강인원을 초과하지 않도록 선착순으로 수강신청을 하기 위해 `PESSIMISTIC_WRITE lock`을 사용했습니다.
    - 하지만, 트래픽이 증가할 경우, 대기 상태가 길어져 성능이 저하됩니다.
    - 따라서, 메시지 큐(kafka, GCP Pub/Sub 등)을 추가로 적용해 수강신청 API를 비동기 처리하면
    - 트래픽이 많은 상황에서도 대기 시간을 줄이고 성능을 향상시킬 수 있습니다.
3. 수강신청 API의 성능을 위한 캐싱 적용
    - 자주 조회되는 데이터를 캐싱해 잦은 DB접근으로 인한 성능 개선
        - 강좌별 신청률 `Course.currentEnrollmentPercent`
        - 강좌별 현재 수강인원 `Course.currentEnrollment`