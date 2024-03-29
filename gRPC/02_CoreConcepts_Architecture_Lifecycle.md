## 핵심 개념, 아키텍쳐, 라이프사이클

### 개요

#### 서비스 정의

gRPC는 서비스 정의 개념을 기반으로 하여 매개변수 및 반환 타입을 사용하여 원격으로 호출할 수 있는 메서드를 지정한다. 

```protobuf
service HelloService {
  rpc SayHello (HelloRequest) returns (HelloResponse);
}

message HelloRequest {
  string greeting = 1;
}

message HelloResponse {
  string reply = 1;
}
```

gRPC를 사용하여 4가지 종류의 서비스 메소드를 정의할 수 있다.

- 클라이언트가 서버에 단일 요청을 보내고 단일 응답을 받는 단항 RPC

  ```protobuf
  rpc SayHello(HelloRequest) returns (HelloResponse);
  ```

- 클라이언트가 서버에 스트림 RPC를 보내고 스트림 시퀀스의 메시지를 응답으로 받는 RPC. 클라이언트는 더 이상 메시지가 없을 때까지 반환 된 스트림에서 읽는다. gRPC는 개별 RPC 호출 내에서 메시지 순서를 보장한다.

  ```protobuf
  rpc LotsOfReplies(HelloRequest) returns (stream HelloResponse);
  ```

- 클라이언트가 메시지 시퀀스를 작성하고 제공된 스트림을 사용해서 이를 서버에 보내는 방식이다. 클라이언트가 메시지 쓰기를 마치면 서버가 메시지를 읽고 응답할 때까지 기다린다. gRPC는 개별 RPC 호출 내에서 메시지 순서를 보장한다.

  ```protobuf
  rpc LotsOfGreetings(stream HelloRequest) returns (HelloResponse);
  ```

- 양방향 스트리밍 RPC로 양측이 스트림을 사용하여 여러개의 메시지를 전송한다. 두 스트림은 독립적으로 작동하므로 클라이언트와 서버는 원하는 순서대로 읽고 쓸 수 있다.

  ```
  rpc BidiHello(stream HelloRequest) returns (stream HelloResponse);
  ```

#### API 사용

*proto file* 에서 서비스 정의를 시작할때, gRPC는 클라이언트 및 서버 측 코드를 생성하는 프로토콜 버퍼 컴파일러 플러그인을 제공한다. gRPC 사용자는 일반적으로 클라이언트 측에서 이러한 API를 호출하고 서버측에서 해당 API를 구현한다.

- 서버는 서비스에서 선언한 메소드를 구현하고 gRPC 서버를 실행하여 클라이언트 호출을 처리한다. gRPC 인프라는 수신 요청을 디코딩하고, 서비스 메소드를 실행하고, 서비스 응답을 인코딩한다.
- 클라이언트에는 서비스와 동일한 메소드를 구현하슨 *stub* 이라는 로컬 객체를 가지고 있다. 클라이언트에서는 로컬 객체에서 해당 메서드를 호출하여 적절한 프로토콜 버퍼 메시지 유형으로 호출 매개변수를 래핑한다. gRPC는 요청을 서버로 보내고 서버의 프로토콜 버퍼 응답을 반환한다.

#### Synchronous vs. asynchronous

대부분의 언어에서 gRPC 프로그래밍 API는 동기 및 비동기 버전으로 제공된다.

### RPC life cycle

#### 단 RPC

1. 클라이언트가 *stub* 메서드를 호출하면 호출에 대한 클라이언트의 메소드 네임, 지정된 기한(설정된 경우) 등의 호출에 대한 메타메이터를 포함하여 RPC가 호출되었음을 서버에 알린다.
2. 그 후 서버는 자체 초기 메타 데이터를 즉시 보내거나 클라이언트의 요청 메소드를 기다릴 수 있다.
3. 서버가 클라이언트의 요청 메시지를 받으면 응답을 만들고, 응답은 상태 세부 정보 및 후행 메타 데이터(옵션)와 함께 클라이언트에 반환된다.
4. 응답 상태가 OK이면 클라이언트가 응답을 받고 클라이언트 측에서 호출을 완료한다.

#### 서버 스트리밍 RPC

서버 스트리밍 RPC는 서버가 클라이언트의 요청에 대한 응답으로 메시지 스트림을 반환한다는 점을 제외하면 당한 RPC와 유사하다. 클라이언트는 서버의 모든 메시지를 받으면 완료된다.

#### 클라이언트 스트리밍 RPC

클라이언트 스트리밍 RPC는 클라이언트가 단일 메시지 대신 서버로 메시지 스트림을 보내는 것을 제외하고는 단항 RPC와 유사하다.

#### 양방향 스트리밍 RPC

양방향 스트리밍 RPC에서 호출은 메소드를 호출하는 클라이언트에서 시작된다. 그리고 서버는 클라이언트의 메타데이터, 메소드 이름, 지정된 기한의 정보를 받는다.

클라이언트, 서버의 스트림 처리는 애플리케이션에 따라 다르다. 두 스트림이 독립적으로 메시지를 읽고 쓸수 있다.