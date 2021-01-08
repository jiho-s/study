## RESTFul

> Richardson Maturity Model

### Rest(REpresentational State Transfer)

데이터를 가져오거나 변경하기 위한 웹서비스를 설계하는데 널리 사용되는 아키텍처 스타일

사용하기 쉽고 유연성 및 상호 운용성을 제공한다.

![RMM](./Asset/RMM.png)

### Richardson Maturity Model(RMM)

Rest 프레임워크에 대한 API 적합성 정도를 나타내는 4단계 척도

URL, HTTP Methods, HATEOAS를 기준으로 나눈다.

#### RMM의 단계

RMM은 4가지 레벨로 나뉜다.

- 0 단계: POX(Plain Old XML)

  REST 스타일을 가장 적게 사용한다. 일반적으로 전체 애플리케이션에 대하여 한 개의 URI만 노출한다. 모든 작업에 HTTP POST를 사용한다. SOAP, XML-RPC가 이에 해당한다

- 1 단계: Resource-Based Address

  0 단계와 달리 여러개의 URL을 사용한다. 그러나 여전히 HTTP POST  만 사용한다. 리소스기반지정은 API가 RESTFul의 시작이다.

- 2 단계: HTTP method

  GET, POST, PUT, DELETE 같은 모든 HTTP method를 활용한다. 응답 body가 아닌 상태코드로 에러를 확인할 수 있다.

- 3 단계: HATEOAS(Hypertext As The Engine Of Application State)

  HATEOAS를 사용하는 가장 높은 단계이다. 리소스와 리소스에 대한 링크를 동시에 제공한다. 이런 리소스간의 연결은 client-driven 자동화를 지원하기 쉬워진다.

### 출처

[https://devopedia.org/richardson-maturity-model#summary]

