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

![](C:\Users\Administrator\Downloads\RedChart\img\architecture.png)

## 명세서

<details><summary><strong>ERD</strong></summary>

![](C:\Users\Administrator\Downloads\RedChart\img\ERD.png)
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

---

### 마이크로서비스 아키텍처(MSA) 도입:

- 각 서비스의 독립성을 보장하고, 확장성을 극대화하기 위해 MSA를 도입
- Spring Cloud와 Netflix Eureka를 사용하여 서비스 디스커버리와 로드 밸런싱을 구현
- API Gateway 서비스에서 JWT 기반 인증 및 보안 모듈을 설정
- 각 마이크로서비스 간의 통신은 Feign Client를 사용하여 REST API 기반으로 비동기 처리하였으며, 서비스 간의 높은 응답 시간을 해결하기 위해 병렬 처리(CompletableFuture)를
  활용

## 트러블 슈팅 및 성능 개선

---

카프카 토픽 1개 컨슈머 1개
카프카 토픽 10개 컨슈머 1개 + 컨텍스트 스위칭
카프카 토픽 10개 컨슈머 10개

동기 처리:

- 순차적으로 실행되는 방식
- 블로킹 방식
- 단순성


- 뉴스 피드 성능 개선
    - 뉴스피드 속도 문제
    - fegin client의 동기 처리의 속도 문제
    - non-blocking 처리 시 에러 처리 롤백 문제 인지
    - computable future를 통해 비동기 방식으로 변경
    - 서비스의 독립성을 위해 포스트 서비스 및 뉴스피드를 분리하고
    - 뉴스피드에 redis 중간 테이블을 두어 각 유저의 뉴스피드 post id 정보를 저장


- 인덱싱 문제
    - 순환, 해시, 테이블 분리X
    - 트랜잭션을 통해 한번에 인덱스를 삭제 생성을 고민
    - b-tree 시간으로 인덱스 생성 및 삭제를 통한 실시간 데이터 저장
    - 주식 데이터의 특성 상 최대한 빠른 데이터 삭제 및 체결 틱 기준으로 데이터를 가져오다보니 시간이 불규칙

- websocket을 통한 구독 정보
    - 클라이언트가 구독 정보에 따라 원하는 코인의 정보를 받을 수 있도록 처리

- 뉴스피드 속도 비교