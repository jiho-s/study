## gRPC 소개

gRPC는 protocol buffer를 **IDL**(Interface Definition Language) 및 기본 메시지 교환 형식으로 사용할 수 있다.

### 개요

gRPC에서 클라이언트 애플리케이션은 다른 머신에 있는 서버 애플리케연의 메서드를 마치 로컬 객체에 있는 것처럼 직접 호출할 수 있다. 이를 이용해 분산 애플리케이션과 서비스를 더욱 쉽게 만들수 있다. 많은 RPC 시스템 처럼, gRPC는 서비스를 정의한다는 개념에 기반을 두고 있으며 매개변수와 반환 타입을 사용하여 원격으로 호출할 수 있는 메소드를 지정한다. 서버 측에서 서버는 인터페이스를 구현하고 gRPC 서버를 실행하여 클라이언트 호출을 처리한다. 클라이언트 측은 서버와 동일한 메소드를 제공하는 스텁을 가지고 있다.

gRPC 클라이언트와 서버는 다양한 환경에서 실행되고 서로 통신할 수 있으며, gRPC에서 지원하는 다양한 언어로 작성될수 있다. 또한 최신 Google API에는 인터페이스의 gRPC 버전이 있으므로 애플리케이션에 Google 기능을 쉽게 구축할 수 있다.

### Protocol Buffer

기본적으로 gRPC는 프로토콜버퍼를 사용한다.

프로토콜 버퍼로 작업할때는 가장 먼저 *proto file* 에서 직렬화 하려는 데이터의 구조를 정의하는 것이다. 이건은 `.proto` 확장자가 있는 텍스트 파일이다. 프로토콜 버퍼 데이터는 메시지로 구성되며 각 메시지는 필드라고 하는 일련의 이름-값을 포함하는 정보이다.

```protobuf
message Person {
  string name = 1;
  int32 id = 2;
  bool has_ponycopter = 3;
}
```

그런다음 프로토콜 버퍼 컴파일러 `protoc` 를 사용하여 프로토 정의에서 원하는 언어로 데이터 접근 클래스를 생성한다. 접근 클래스는 `name()`, `set_name()` 같은 각각의 필드에 대한 접근자와 바이트로부터 전체구조를 인코딩/디코딩하는 방법을 제공한다. 

일반 *proto file*에서 gRPC 서비를 정의할 수 있고, 프로토콜 버퍼 메시지를 명세한 리턴타입과 RPC 메소드 매개변수를 사용하여 정의한다.

```protobuf
service Greeter {
  // Sends a greeting
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}

// The request message containing the user's name.
message HelloRequest {
  string name = 1;
}

// The response message containing the greetings
message HelloReply {
  string message = 1;
}
```

gRPC는 특정 gRPC 플러그인을 사용한 `protoc` 를 사용하여 *proto file* 에서 코드를 생성한다. 이를 통해 생성된 gRPC 클라이언트와 서버코드를 얻을 수 있고 또한 메시지 타입을 바꾸고, 직렬화시키고, 찾기 위한 프로토콜 버퍼 코드도 얻을 수 있다.