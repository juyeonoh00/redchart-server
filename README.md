# 업비트 종목 토론방 프로젝트

[2024.08.01-2024.10.01]

## 목차

- [OverView](#overview)
- [기술스택 및 아키텍처](#기술-스택-및-아키텍처)
    - [기술스택](#기술-스택)
    - [아키텍처](#아키텍처)
- [명세서](#명세서)
- [주요 기능](#주요-기능)
- [기술적 의사 결정](#기술적-의사-결정)
- [트러블 슈팅 및 성능 개선](#트러블-슈팅-및-성능-개선)

## OverView

- 업비트에서 제공하는 웹소켓의 ticker 정보를 기반으로 한 실시간 암호 화폐 차트
- 소셜 시스템(팔로잉, 뉴스피드)을 바탕으로 한 암호 화폐 종목 토론 방을 구현

## 기술 스택 및 아키텍처

### 기술 스택

**Language & Library** &nbsp; : &nbsp;
<img src="https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java 17">
<img src="https://img.shields.io/badge/SpringBoot-3.3.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3.3.2">
<img src="https://img.shields.io/badge/Spring%20Cloud-2023.0.3-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Cloud">
<img src="https://img.shields.io/badge/Spring%20Webflux-6DB33F?style=for-the-badge&logo=Spring&logoColor=white" alt="Spring Webflux">
<img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Data JPA">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=Spring%20Security&logoColor=white" alt="Spring Security">
<img src="https://img.shields.io/badge/OAuth2.0-4285F4?style=for-the-badge&logo=oauth&logoColor=white" alt="OAuth 2.0">
<img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white" alt="JWT">
<img src="https://img.shields.io/badge/Netflix%20Eureka-302E31?style=for-the-badge&logo=Netflix&logoColor=white" alt="Netflix Eureka">
<img src="https://img.shields.io/badge/Feign%20Client-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Feign Client">

**Database & Caching** &nbsp; : &nbsp;
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis">
<img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=nginx&logoColor=white" alt="NGINX">

**Testing & Monitoring** &nbsp; : &nbsp;
<img src="https://img.shields.io/badge/Jmeter-D22128?style=for-the-badge&logo=apachejmeter&logoColor=white" alt="Jmeter">
<img src="https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white" alt="Prometheus">
<img src="https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white" alt="Grafana">

**etc** &nbsp; : &nbsp;
<img src="https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white" alt="Apache Kafka">
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle">
<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white" alt="Git">

### 아키텍처

<img src="img\odiro_architecture.png" alt="RedChart Architecture" width="600">

## 명세서

<details><summary><strong>ERD</strong></summary>

<img src="img\odiro_ERD.png" alt="RedChart ERD" width="600">
</details>

<details><summary><strong>API 명세서</strong></summary>

스웨거 링크 추가 예정
</details>

## 주요 기능

### 로그인 서비스

- Spring Cloud Gateway와 Spring WebFlux Security를 활용한 인가 관리 구현
- OAuth 2.0 프로토콜을 활용한 소셜 로그인 기능 구현 및 보안 강화
- JWT 토큰 기반으로 Redis에 Refresh Token을 저장하고, 암호화된 Access Token만을 클라이언트에 전달하여 토큰 탈취 리스크 최소화

### 소셜 서비스

- **Apache Kafka 사용**
    - 비동기 이벤트 기반 데이터 처리를 구현
- **Redis 캐싱 및 fan-out-on-write 전략**
    - 조회가 잦고, 조회 성능이 우선시 되는 뉴스피드의 특성을 고려하여 fan-out-on-write push 모델을 사용
    - 데이터 일관성을 유지하면서 데이터베이스 부하를 최소화하기 위해 중간 테이블과 TTL 기법을 적용

### 주식 서비스

- **WebSocket 사용**
    - 실시간 데이터 수신을 위해 WebSocket을 사용하여 Upbit 거래소에서 실시간 ticker 정보를 받아옴
    - WebSocket Stomp를 사용하여 클라이언트에서 구독한 암호 화폐의 정보를 실시간으로 전달
- **Apache Kafka 사용**
    - 데이터 분석에 대한 확장성과 비동기적인 대용량 데이터를 실시간으로 안전하게 처리하기 위해 사용
    - 빠른 데이터 처리를 위해 종목 별 topic을 생성하여 병렬적으로 작업
- **Nginx 정적 페이지 저장 및 캐싱**
    - 데이터 변경이 적은 페이지의 경우 동적 처리의 경우 비효율적이라 판단
    - nginx를 통해 매일 일봉 페이지를 저장하고 캐싱을 하여 서버에 도달하기 전에 데이터를 받을 수 있도록 처리

## 기술적 의사결정

1. **마이크로서비스 아키텍처(MSA) 도입:**
    - Spring Cloud와 Netflix Eureka를 사용하여 서비스 디스커버리와 로드 밸런싱을 구현
    - API Gateway 서비스에서 JWT 기반 인증 및 Spring Security를 통한 보안 모듈을 설정

2. **Nginx 정적 페이지 캐싱**
    - 데이터 변경이 적은 페이지의 경우 동적 처리의 경우 비효율적이라 판단
    - nginx를 통해 매일 일봉 페이지를 저장하고 캐싱을 하여 서버에 도달하기 전에 데이터를 받을 수 있도록 처리

3. **Redis fan-out-on-write (push 모델) 채택**
   - 조회가 잦고, 조회 성능이 우선시 되는 뉴스피드의 특성을 고려하여 fan-out-on-write push 모델을 사용
   - 실제 운영 시 hotkey에 대한 제약 조건 구현 예정
     
      ex) 신규 개설 계정에 대한 팔로잉 수 제한, 1일 팔로잉 제한, 장기 미접속자에 대한 빠른 계정 비활성화


## 트러블 슈팅 및 성능 개선

1. **Apache Kafka 사용**
  
   : 데이터 분석에 대한 확장성과 비동기적인 대용량 데이터를 실시간으로 안전하게 처리하기 위해 사용
   
    - **역직렬화 문제**:
        - 문제 상황:
            - Kafka Consumer와 Producer의 DTO 패키지가 다르면 자동 역직렬화가 실패하여 `LinkedHashMap`타입으로 변환됨
            - MSA의 의존성 최소화 및 개별 서비스의 독립성 강화를 위해 공통 모듈 없이 메세지 처리
        - 해결 방법:
            - ConsumerRecord<String, Object>로 메시지 수신 후 ObjectMapper를 이용한 동적 역직렬화 구현
            -  Kafka Listener에서 DTO 타입에 관계없이 객체 변환 가능하도록 구성
      
   - **빠른 데이터 처리를 위해 종목 별 topic을 생성하여 병렬적으로 작업** :  수치화 예정


4. **Redis 사용**
    - **Redis의 quick list 사용을 통한 성능 개선**
        - 문제 상황:
            - 뉴스피드의 push 모델로 Post 생성 시 Redis에 팔로워 수만큼의 lpush가 생성
            - 기존 zip list 타입의 경우 압축된 ArrayList() 타입으로 lpush 시 Q(n)만큼의 공간 복잡도를 갖음
        - 해결 방안:
            - redis config를 통해 기존 512까지는 zip list를 사용하는 redis의 설정을 바꾸어 이중 linked-list의 하이브리드 타입인 quick list를 사용함으로써 성능 개선
        - 성과:
            - 1000개 게시물 기준 20mb → 600kb → 1.3mb / 476ms → 133ms→16ms
