# 20장 리다이렉션과 부하 균형

**리다이렉션: HTTP 메시지의 최종 목적지를 결정하는 네트워크 도구, 기법, 프로토콜**

- HTTP 리다이렉션
- DNS 리다이렉션
- 임의 캐스트 라우팅
- 정책 라우팅
- 아이피 맥 포워딩
- 아이피 주소 포워딩
- 웹 캐시 조직 프로토콜
- 인터캐시 커뮤니케이션 프로토콜
- 하이퍼텍스트 캐싱 프로토콜
- 네트워크 요소 제어 프로토콜
- 캐시배열 라우팅 프로토콜
- 웹 프락시 자동발견 프로토콜

## 20.1  왜 리다이렉트 인가

**HTTP 애플리케이션이 원하는 것**

- 신뢰할 수있는 HTTP 트랜잭션
- 지연 최소화
- 네트워크 대역폭 절약

리다이렉션이란 최적의 분산된 콘텐츠를 찾는 것을 도와주는 기법의 집합

리다이렉션의 구현에는 부하 균형의 과제가 포함된다

## 20.2 리다이렉트를 할 곳

서버, 프락시, 캐시, 게이트웨이 등 클라이언트에게 서버라고 할수 있는것

## 20.3 리 다이렉션 프로토콜의 개요

리다이렉션의 목표는 HTTP 메시지를 가용한 웹 서버로 가급적 빨리 보내는 것

- 브라우저가 클라이언트 메시지를 프락시 서버로 보내도록 설정할 수 있다
- DNS 분석자는 메시지의 주소를 지정할 떼 사용될 아이피 주소를 선택한다
- 스위치와 라우터들은 패킷의 TCP/IP 주소를 검증하고 그에 근거하여 패킷을 어떻게 라우팅 할것인지 결겅한다
- 웹 서버는 HTTP 리다이렉트를 이용해 요청이 다른 웹 서버로 가도록 할 수 있다

### 메시지를 서버로 리다이렉트 하기 위해 사용되는 리다이렉션 방법

- HTTP 리다이렉션
- DNS 리다이렉션
- 임의 캐스트 어드레싱
- 아이피 맥 포워딩
- IP 주소 포워딩

### 메시지를 프락시 서버로 리다이렉트 하기 위해 사용되는 리다이렉션 방법

- 명시적인 브라우저 설정
- 프락시 자동 설정 프로토콜
- 웹 프락시 자동발견 프로토콜
- 웹 캐시 조직 프로토콜
- 인터넷 캐시 프로토콜
- 캐시 배열 라우팅 프로토콜
- 하이퍼텍스트 캐싱 프로토콜

## 20.5 일반적인 리다이렉션 방법

### 20.4.1 HTTP 리다이렉션

그림 20-1 p 527

HTTP 리다이렉션의 단점

- 원 서버는 거의 페이지 자체를 제공할 때 필요한 것과 거의 같은 양의 처리가 필요하다
- 두번의 왕복이 필요하기 때문에, 사용자가 더 오래 기다리게 된다
- 리다이렉트 서버가 SPOF 가 된다

### 20.4.2 DNS 리다이렉션

DNS는 하나의 도메인에 여러 아이피 주소가 결부되는 것을 허용하는데 이를 이용한다

그림 20-2 p 528

DNS 결정 알고리즘에는 여러가지가 있다

### 20.4.3 임의 캐스트 어드레싱

여러 지리적으로 흩어진 웹 서버들은 정확히 같인 아이피 주소를 갖고 클라이언트의 요청을 클라이언트에서 가장 가까운 서버로 보내주기 위해 백본 라우터의 최단거리 라우팅 능력에 의지한다

그림 20-5 533

인터넷 주소는 기본적으로 한 서버에 하나임을 가지고 있는데 처리 곤란

### 20.4.4 아이피 맥 포워딩

스위치는 맥, ip 포트를 보고 특정 ip 주소, 특정 포트로 오는 경우에 프락시 캐시로 보낼수 있고 프락시 캐시는 캐시된 콘텐츠가 유효하면 전달하고 유효하지 않으면 원서버에 요청한다

### 20.4.5 아이피 주소 포워딩

스위치에서 다음 목적지 주소를 맥주소가 아니라 목적지 아이피 주소의 변경에 따라 라우팅한다

### 20.4.6 네트워크 구성요서 제어 프로토콜

라우터나 스위치 같은 네트워크 구성요소들이 웹 서버나 프락시 캐시와 같이 애플리케이션 계층 요청을 처리하는 서버 구성요소들과 대화할 수 있게 해준다

## 20.5 프락시 리다이렉션 방법

웹브라우저 같은 클라이언트들이 프락시로 가게 하기 위한 방법

### 20.5.1 명시적 브라우저 설정

사용자가 프락시 서버에 접촉하기 위해 프락시 이름, 아이피 주소, 포트번호 설정

**단점**

- 프락시가 다운되거나 브라우저가 잘못 설정되면 사용자는 접속문제를 경험
- 변경 했을때  EndUser가 다시 설정해야함

### 20.5.2 프락시 자동 설정

프락시 서버에 접촉하기 위해 브라우저가 동적으로 자신을 설정하게 하는 자동 설정 방법 PAC 이라고 부른다

그림 20-10 540

### 20.5.3 웹 프락시 자동발견 프로토콜

WPAD 웹브라우저가 근처의 프락시를 찾아내어 사용할수 있게

## 20.6 캐시 리다이렉션 방법

캐싱 프락시 서버를 위해 사용되는 복잡한 리다이렉션 기법

### 20.6.1 WWCP 리다이렉션

라우터들과 캐시들 사이의 대화를 관리하여 라우터가 캐시를 실행되어 있고 동작 중임을 검사하고 특정 종류의 트래픽을 특정 캐시로 보낼수 있다

### 20.7 인터넷 캐시 프로토콜

ICP는 캐시가 요청한 콘텐츠를 갖고 있지 않다면, 캐시는 근처 캐시 중 콘텐츠를 갖고 있는 것이 있는지 찾아보고 있으면 캐시에서 콘텐츠를 가져온다. 일종의 캐시 클러스팅 프로토콜

### 20.8 캐시 배열 라우팅 프로토콜

사용자가 많아지면 캐시를 제공하는 프락시 서버에 부하를 줄수 있는데 여러게의 프락시 서버가 하나의 논리적인 캐시처럼 보이도록 관리 인터넷 캐시 프로토콜의 대용으로 ICP 와 달리 각각의 프락시 서버가 전체 캐시된 문서의 일부만 갖고 있어 중복된 엔트리가 없다

### 20.9 하이퍼 텍스트 캐싱 프로토콜

ICP는 HTTP/0.9를 염두에 두고 설계 해서 URL 을 통해서만 리소스의 존재여부를 판단한다. 하이퍼 텍스트 캐싱 프로토콜은 요청 헤더 응답 헤더를 사용하여 서로간의 질의를 하게 해준다