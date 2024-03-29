## 커넥션 관리

- HTTP는 어떻게 TCP 커넥션을 사용하는가
- TCP 커넥션의 지연, 병목, 막힘
- 병렬커넥션, keep-alive 커넥션, 커넥션 파이프라인을 활용한 HTTP 의 최적화
- 커넥션 관리를 위해 따라야 할 규칙들

### 4.1 TCP 커넥션

#### 4.1.1 신뢰 할 수 있는 데이터 전송 통로인 TCP

HTTP 커넥션은 몇몇 사용 규칙을 제외하고는 TCP 커넥션에 불과하다

#### 4.1.2 TCP 스트림을 세그먼트로 나뉘어 IP 패킷을 통해 전송된다

TCP는 IP 패킷이라고 불리는 작은 조각을 통해 데이터를 전송한다

> TCP 는 세그먼트, UDP 면 데이터그램

TCP는 세그먼트라는 단위로 데이터 스트림을 잘게 나누고, 세그먼트를 IP 패킷이라고 불리는 봉투에 담아서 인터넷을 통해 데이터를 전송한다

IP 패킷은 각각 다음을 포함한다

- IP 패킷 헤더
- TCP 세그먼트 헤더
- TCP 데이터 조각

#### 4.1.3 TCP 커넥션 유지하기

TCP 커넥션은 4가지 값으로 식별한다

```
<발신지 IP, 발신지 포트, 수신지 IP, 수신지 포트>
```

#### 4.1.4 TCP 소켓 프로그래밍

그림 4-6

### 4.2 TCP의 성능에 대한 고려

TCP 커넥션의 성능에 관련된 주요 고려사항을 집중적으로 다룬다

#### 4.2.1 HTTP 트랜잭션 지연

HTTP 트랜잭션을 지연 시키는 원인 

1. DNS 이름 분석을 통한 URI 에 있는 호스트 명을 IP 주소로 변환하는 시간
2. TCP 커넥션 요청을 서버에게 보내고 서버가 커넥션 허가 응답을 회신하는데 걸리는 시간
3. HTTP 요청을 새로 생성된 TCP 파이프를 통해 전송하는데 요청 메시지가 인터넷을 통해 전달되고 서버에 의해서 처리되는데 걸리는 시간

#### 4.2.2 성능관련 중요요소

- TCP 커넥션의 핸드세이크 설정
- TCP의 느린 시작
- TCP 편승(piggyback) 확인 응답(ack)을 위한 왁인응답 지연 알고리즘
- TIME_WAIT 지연과 포트고갈

#### 4.2.3 TCP 커넥션 핸드셰이크 지연

그림 4-8

#### 4.2.4 TCP 느린 시작(slow start)

> **TCP 가 지원 하는것**
>
> - 흐름제어 - 송신과 수신의 데이터 처리 속도 차이를 제어할수 있음(Sliding Window)
> - 오류제어 - 오류가 나면 수신 측에서 NACK, ACK 등을 통해 재전송 요청
> - 혼잡 제어  - (RWND), (CWND) 의 크기를 고려해 전송함

연결초기에 CWND 의 값을 1로 설정 최대값이 될때까지 1씩 증가 결과적으로 전송되는 세그먼트 수는 1,2,4,8,16 으로 증가

#### 4.2.5 확인 응답 지연

ACK 등을 보내야하는데 가는 데이터 패킷이 있는지 기다리다가 지연이됨

#### 4.2.6 네이글 알고리즘과 TCP_NODELAY

네이글 알고리즘은 세그먼트가 최대 크기가 되지 않으면 전송을하지 않고 모든 패킷이 확인응답을 받았으면 전송한다.

크기가 작은 HTTP 메시지는 생기지도 않을 추가적인 데이터를 기다리며 지연 확인 응답 지연 알고리즘과 만나면...

HTTP 애플리케이션은 성능 향상을 위해서  HTTP 스택에 TCP_NODELAY 를 설정하여 네이글 알고리즘을 비활성화 하기도 함

#### 4.2.7 TIME_WAIT의 누적과 포트고갈

> **TIME_WAIT 가 필요한 경우**
>
> 1. 지연 패킷
>    1. 연결 끊기 전에 지연패킷 발생
>    2. 연결 끊음
>    3. 같은 포트로 다시 연결
>    4. 지연패킷 도착
> 2. 마지막 ACK가 유실된 경우
>    1. FIN -> FIN, ACK -> ACK 보내는 중 ACK 가 손실
>    2. 다시연결 
>    3. RST 보냄

클라이언트 입장에서 서버에 접속할 때마다 새로운 발신지 포트를 써야하는데 고갈되거나 대기상태로 있는 제어블록이 많아지는 문제가 있다

### 4.3 HTTP 커넥션 관리

커넥션을 생성하고 최적화하는 HTTP 기술을 설명

#### 4.3.1 Connection 헤더

두 개의 인접한 HTTP 어플리케이션이 현제 맺고 있는 커넥션에 만 적용될 옵션을 지정해야하는 경우 사용

Connection 헤더에 전달될수 있는 값들

- HTTP 헤더 필드 명
  - 이 커넥션에만 헤당되는 헤더들
- 임시적인 값
  - 커넥션에 대한 비표준 옵션
- `close`
  - 커넥션이 작업이 완료되면 종료되어야 함을 의미

Connection 헤더의 값에 HTTP 헤더 필드명을 가지고 있으면, 해당 필드들은 현재 커넥션만을 위한 정보이므로 다음 커넥션에 전달하면 안된다

#### 4.3.2 순차적인 트랜잭션 처리에 의한 지연

각 트랜잭션이 새로운 커넥션을 필요로 한다면, 커넥션을 맺는데 발생하는 지연과 함께 느린 시작 지연이 발생

이를 향상시킬수 있는 기술들

- 병렬 커넥션
  - 여러 개의 TCP 커넥션
- 지속 커넥션
  - TCP 커넥션의 재활용
- 파이프라인 커넥션
  - 공유 TCP 커넥션을 통한 병렬 HTTP 요청
- 다중 커넥션
  - 뒤에 안나옴

### 4.4 병렬 커넥션

브라우저에서 여러게의 TCP 연결을 만든다

### 4.5 지속 커넥션

HTTP 요청에 대한 처리가 완료된 후 TCP 커넥션을 유지하여 앞으로 있을 HTTP 요청에 재사용할 수 있다

#### 4.5.1 지속 커넥션 vs 병렬 커넥션

병렬 커넥션의 단점

- 각 트랜젝션마다 새로운 커넥션을 맺고 끊기 때문에 시간과 대역폭이 소요된다
- 각각의 새로운 커넥션은 TCP 느린 시작 때문에 성능이 떨어진다
- 실제로 연결할 수 있는 병렬 커넥션의 수에 제한이 있다(브라우저에서 4개로 제한)

지속커넥션은 병렬커넥션과 함께 사용될 때 가장 효과적, 방법으로는 HTTP/1.0+ 에 keep-alive 커넥션과 HTTP/1.1의 지속 커넥션이 있음

#### 4.5.2 HTTP/1.0+ 의 Keep-Alive 커넥션

##### 4.5.2.1 Keep-Alive 의 동작

클라이언트는 커넥션을 유지하기 위해 요청에 `Connection: Keep-Alive` 헤더를 포함시킨다. 이 요청을 받은 서버는 그다음 요청도 이 커넥션을 통해 받고자 한다면 응답 메시지에 같은 헤더를 포함시켜 응답한다. 응답에 `Connection: Keep-Alive` 헤더가 없으면 클라이언트는 서버가 keep-alive 를 지원하지 않으며, 응답메시지가 전송되고 나면 서버 커넥션을 끊을 것이라고 추정한다

##### 4.5.2.2 Keep-Alive 옵션

Keep-alive 의 동작은 `Keep-Alive` 헤더의 쉼표로 구분된 옵셜들로 제어할수 있다

- timeout
  - 커넥션이 얼마나 유지될 것인지를 의미한다
- max
  - 커넥션이 몇 개의 HTTP 트랜잭션을 처리할 때까지 유지될 것인지를 의미한다
- 임의의 속성
  - 진단이나 디버깅 목적

##### 4.5.2.3 Keep-Alive와 멍청한 프락시

Connection 헤더를 이해하지 못하는 프락시가 중간에 있으면 이상해짐

Proxy-Connection 도 만들었지만 프락시가 중간에 여러개 있으면 이상해짐

#### 4.5.3 HTTP/1.1 의 지속 커넥션

HTTP/1.1. 의 지속 커넥션은 기본적으로 활성화 되어 있다. HTTP/1.1 에서 별도 설정을 하지 않는 한, 모든 커넥션을 지속 커넥션으로 취급한다. 커넥션을 끊으려면 `Conection: close` 를 명시해야함

### 4.6 파이프라인 커넥션

HTTP/1.1 은 지속 커넥션을 통해서 요청을 파이프라이닝할 수 있다

파이프라인의 제약 사항

- 클라이언트는 커넥션이 지속 커넥션인지 확인하기 전까지는 파이프라인을 이어서는 안된다
- HTTP 응답은 요청 순서와 같게 와야 한다
- 클라이언트는 완료되지 않은 요청이 파이프라인에 있으면 커넥션이 끊어지더라도 언제든 요청읃 다시 보낼 준비가 되어 있어야 한다
- POST 요청과 같은 반복해서 보낼 경우 문제가 생기는 요청은 파이프라인을 통해 보내면 안된다

### 4.7 커넥션 끊기에 대한 미스터리

커넥션 관리에는 명확한 기준이 없다

#### 4.7.1 마음데로 커넥션 끊기

HTTP 애플리케이션은 언제든지 지속 커넥션을 임의로 끊을수 있다. 하지만 서버가 유휴상태에 있는 커넥션을 끊는 시점에 서버는 클라이언트가 데이터를 전송하지 않을것이라고 확신하지 못한다

#### 4.7.2 Content-Length 와 Truncation

Content-Lenght 는 본문에 정확한 크기 값을 가지게 구현되어 있어야 하지만 잘못 구현되어 있는 경우도 있어서 정확하지 않다

#### 4.7.3 커넥션 끊기의 허용 재시도, 멱등성

커넥션은 언제든 끊을수 있으므로 커넥션이 끊어졌을떄를 적절히 대응할 준비가 되어 있어야한다. 멱등 하지 않은 POST 같은 요청은 파이프라인을 통해 보내면 안된다

#### 4.7.4 우아한 커넥션 끊기

TCP 는 half 끊기가 가능하므로 클라이언트 서버가 서로 자신이 보낼 데이터가 없으면 출력을 끊으면 되지 않을까?

하지만 상대방이 half 끊기를 구현 했다고 보장할수 없다