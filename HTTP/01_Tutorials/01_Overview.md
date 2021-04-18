## HTTP 개요

HTTP는 리소스들을 가져올수 있도록 하는 프로토콜이다. HTTP는 웹에서 이루어지는 모든 데이터 교환의 기초이며, 클라이언트-서버 프로토콜이기도 한다. 하나의 완전한 문서는 텍스트, 레이아웃 설명, 이미지, 비디오, 스크립트 등 불러온(fetched) 하위 문서들로 재구성된다.

클라이언트와 서버들은 개별적인 메시지 교환에 의해 통신한다. 클라이언트에 의해 전송되는 메시지를 *요청(requests)*이라고 부르며, 그에 대해 서버에서 응답으로 전송되는 메시지를 응답(*responses)*이라고 부른다.

HTTP는 애플리케이션 계층의 프로토콜로, 신뢰 가능한 전송 프로토콜이라면 이론상으로는 무엇이든 사용할 수 있으나 [TCP](https://developer.mozilla.org/en-US/docs/Glossary/TCP) 혹은 암호화된 TCP 연결인 [TLS](https://developer.mozilla.org/en-US/docs/Glossary/TLS)를 통해 전송된다. HTTP의 확장성 덕분에, 오늘날 하이퍼텍스트 문서 뿐만 아니라 이미지와 비디오 혹은 HTML 폼 결과와 같은 내용을 서버로 포스트(POST)하기 위해서도 사용된다.

### HTTP 기반 시스템의 구성요소

요청은 하나의 개체, 사용자 에이전트(또는 그것을 대신하는 프록시)에 의해 전송된다.

각각의 개별적인 요청들은 서버로 보내지며, 서버는 요청을 처리하고 *response*라고 불리는 응답을 제공한다. 이 요청과 응답 사이에는 여러 개체들이 있는데, 예를 들면 다양한 작업을 수행하는 게이트웨이 또는 [캐시](https://developer.mozilla.org/en-US/docs/Glossary/Cache) 역할을 하는 [프록시](https://developer.mozilla.org/en-US/docs/Glossary/Proxy) 등이 있다.

#### 클라이언트

클라이언트는 사용자를 대신하여 동작하는 모든 도구이다. 이 역할은 주로 브라우저에 의해 수행된다.

브라우저는 **항상** 요청을 보내는 개체이다. 그것은 결코 서버가 될 수 없다.

#### 웹 서버

통신 채널의 반대편에는 클라이언트에 의한 요청에 대한 문서를 *제공*하는 서버가 존재한다. 서버는 사실 상 논리적으로 단일 기계이다.

서버는 반드시 단일 머신일 필요는 없지만, 여러 개의 서버를 동일한 머신 위에서 호스팅 할 수는 있습니다. HTTP/1.1과 [`Host`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Host) 헤더를 이용하여, 동일한 IP 주소를 공유할 수도 있다.

#### 프록시

웹 브라우저와 서버 사이에서는 수많은 컴퓨터와 머신이 HTTP 메시지를 이어 받고 전달한다. 이러한 컴퓨터/머신 중에서도 애플리케이션 계층에서 동작하는 것들을 일반적으로 **프록시**라고 부른다. 프록시는 다음의 기능을 제공할 수 있다.

- 캐싱 (캐시는 공개 또는 비공개가 될 수 있습니다 (예: 브라우저 캐시))
- 필터링 (바이러스 백신 스캔, 유해 컨텐츠 차단(자녀 보호) 기능)
- 로드 밸런싱 (여러 서버들이 서로 다른 요청을 처리하도록 허용)
- 인증 (다양한 리소스에 대한 접근 제어)
- 로깅 (이력 정보를 저장)

### HTTP의 기초적인 특징

#### HTTP는 심플하다

HTTP는 사람이 읽을 수 있으며 간단하게 고안되었다.

#### HTTP는 확장 가능하다

 [HTTP 헤더](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers)는 HTTP를 확장하고 시도하기 쉽게 만들어주었다. 클라이언트와 서버가 새로운 헤더의 시맨틱에 대해 간단한 합의만 한다면, 언제든지 새로운 기능을 추가할 수 있다.

#### HTTP는 상태가 없지만, 세션이 있다

HTTP는 상태를 저장하지 않는다(Stateless). 하지만, HTTP의 핵심은 상태가 없는 것이지만 HTTP 쿠키는 상태가 있는 세션을 만들도록 해준다. 

헤더 확장성을 사용하여, 동일한 컨텍스트 또는 동일한 상태를 공유하기 위해 각각의 요청들에 세션을 만들도록 HTTP 쿠키가 추가될 수 있다.

#### HTTP와 연결

연결은 전송 계층에서 제어되므로 근본적으로 HTTP 영역 밖이다. HTTP는 연결될 수 있도록 하는 근본적인 전송 프로토콜을 요구하지 않는다. 다만 그저 신뢰할 수 있거나 메시지 손실이 없는(최소한의 오류는 표시) 연결을 요구한다. TCP는 신뢰할 수 있으며 UDP는 그렇지 않다. 그러므로 HTTP는 연결이 필수는 아니지만 연결 기반인 TCP 표준에 의존한다.

클라이언트와 서버가 HTTP를 요청/응답으로 교환하기 전에  TCP 연결을 설정해야 한다. HTTP/1.0의 기본 동작은 각 요청/응답에 대해 별도의 TCP 연결을 만드는 것이다. 이 동작은 여러 요청을 연속해서 보내는 경우에는 단일 TCP 연결을 공유하는 것보다 효율적이지 못하다.

이러한 결함을 개선하기 위해, HTTP/1.1은 (구현하기 어렵다고 입증된) 파이프라이닝 개념과 지속적인 연결의 개념을 도입했다. 기본적인 TCP 연결은 [`Connection`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Connection) 헤더를 사용해 부분적으로 제어할 수 있다. HTTP/2는 연결을 좀 더 지속되고 효율적으로 유지하는데 도움이 되도록, 단일 연결 상에서 메시지를 다중 전송(multiplex)하여 한 걸음 더 나아갔다.

HTTP에 더 알맞은 좀 더 나은 전송 프로토콜을 설계하는 실험이 진행 중에 있다. 예를 들어, 구글은 좀 더 신뢰성있고 효율적인 전송 프로토콜을 제공하기 위해 UDP기반의 [QUIC](https://en.wikipedia.org/wiki/QUIC)를 실험 중에 있다.

### HTTP로 제어할 수 있는것

HTTP의 확장 가능한 특성은 수년 간에 걸쳐 웹의 점점 더 많은 기능들을 제어하도록 허용되어 왔다. 캐시 혹은 인증 메서드는 HTTP에 초기부터 제어해왔던 기능이며, 반면에 `origin`*제약사항*을 완화시키는 조치는 2010년에 들어서 추가되었다.

다음은 HTTP 사용하여 제어 가능한 일반적인 기능 목록이다.

- 캐시
  HTTP로 문서가 캐시되는 방식을 제어할 수 있다. 서버는 캐시 대상과 기간을 프록시와 클라이언트에 지시할 수 있고 클라이언트는 저장된 문서를 무시하라고 중간 캐시 프록시에게 지시할 수 있다.
- *origin 제약사항을 완화하기*
  스누핑과 다른 프라이버시 침해를 막기 위해, 브라우저는 웹 사이트 간의 엄격한 분리를 강제한다. **동일한 origin**으로부터 온 페이지만이 웹 페이지의 전체 정보에 접근할 수 있다. HTTP 헤더를 통해 그것을 완화시킬 수 있다. 그런 덕분에 문서는 다른 도메인으로부터 전달된 정보를 패치워크할 수 있다.
- *인증*
  어떤 페이지들은 보호되어 오로지 특정 사용자만이 그것에 접근할 수도 있다. 기본 인증은 HTTP를 통해 [WWW-Authenticate (en-US)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/WWW-Authenticate) 또는 유사한 헤더를 사용해 제공되거나, HTTP 쿠키를 사용해 특정 세션을 설정하여 이루어질 수도 있다.
- 프록시와 터널링
  서버 혹은 클라이언트 혹은 그 둘 모두는 종종 인트라넷에 위치하며 다른 개체들에게 그들의 실제 주소를 숨기기도 한다. HTTP 요청은 네트워크 장벽을 가로지르기 위해 프록시를 통해 나가게 된다. 
- *세션*
  쿠키 사용은 서버 상태를 요청과 연결하도록 해준다. 이것은 HTTP가 기본적으로 상태없는 프로토콜임에도 세션을 만들어주는 계기가 된다.

### HTTP 흐름

1. TCP 연결을 만든다

   TCP 연결은 요청을 보내거나(혹은 여러개의 요청) 응답을 받는데 사용된다. 클라이언트는 새 연결을 열거나, 기존 연결을 재사용하거나, 서버에 대한 여러 TCP 연결을 열 수 있다.

2. HTTP 메시지를 전송한다

   HTTP 메시지(HTTP/2 이전의)는 인간이 읽을 수 있다. HTTP/2에서는 이런 간단한 메시지가 프레임 속으로 캡슐화되어, 직접 읽는게 불가능하지만 원칙은 동일하다.

   ```html
   GET / HTTP/1.1
   Host: developer.mozilla.org
   Accept-Language: fr
   ```

3. 서버에 의해 전송된 응답을 읽는다

   ```html
   HTTP/1.1 200 OK
   Date: Sat, 09 Oct 2010 14:28:02 GMT
   Server: Apache
   Last-Modified: Tue, 01 Dec 2009 20:18:22 GMT
   ETag: "51142bc1-7449-479b075b2891b"
   Accept-Ranges: bytes
   Content-Length: 29769
   Content-Type: text/html
   
   <!DOCTYPE html... (here comes the 29769 bytes of the requested web page)
   ```

4. 연결을 닫거나 다른 요청들을 위해 재사용한다.

HTTP 파이프라이닝이 활성화되면, 첫번째 응답을 완전히 수신할 때까지 기다리지 않고 여러 요청을 보낼 수 있다. HTTP 파이프라이닝은 오래된 소프트웨어와 최신 버전이 공존하고 있는, 기존의 네트워크 상에서 구현하기 어렵다는게 입증되었으며, 프레임안에서 보다 활발한 다중 요청을 보내는 HTTP/2로 교체되고 있다

### HTTP 메시지

HTTP/1.1와 초기 HTTP 메시지는 사람이 읽을 수 있다. HTTP/2에서, 이 메시지들은 새로운 이진 구조인 프레임 안으로 임베드되어, 헤더의 압축과 다중화와 같은 최적화를 가능케 한다. 본래의 HTTP 메시지의 일부분만이 이 버전의 HTTP 내에서 전송된다고 할지라도, 각 메시지의 의미들은 변화하지 않으며 클라이언트는 본래의 HTTP/1.1 요청을 (가상으로) 재구성한다.

HTTP 메시지의 두 가지 타입인 요청(requests)과 응답(responses)은 각자의 특성있는 형식을 가지고 있다.

#### 요청

- HTTP 메소드

    [`GET`](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/GET), [`POST`](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/POST), [`OPTIONS`](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/OPTIONS), [`HEAD`](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/HEAD) 등이 있다. 일반적으로, 클라이언트는 리소스를 가져오거나(`GET`을 사용하여) [HTML 폼](https://developer.mozilla.org/en-US/docs/Learn/Forms)의 데이터를 전송(`POST`를 사용하여)하려고 하지만, 다른 경우에는 다른 동작이 요구될 수도 있다.

- 가져오려는 리소스의 **경로**

  예를 들면 [프로토콜](https://developer.mozilla.org/en-US/docs/Glossary/Protocol) (`http://`), [도메인](https://developer.mozilla.org/en-US/docs/Glossary/Domain) (여기서는 `developer.mozilla.org`), 또는 TCP [포트](https://developer.mozilla.org/en-US/docs/Glossary/Port) (여기서는 `80`)인 요소들을 제거한 리소스의 URL이다.

- HTTP 프로토콜의 버전.

- 서버에 대한 추가 정보를 전달하는 선택적 [헤더들](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers).

- `POST`와 같은 몇 가지 메서드를 위한, 전송된 리소스를 포함하는 응답의 본문과 유사한 본문

#### 응답

- HTTP 프로토콜의 버전.
- 요청의 성공 여부와, 그 이유를 나타내는 [상태 코드](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status).
- 아무런 영향력이 없는, 상태 코드의 짧은 설명을 나타내는 상태 메시지.
- 요청 헤더와 비슷한, HTTP [헤더들](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers).
- 선택 사항으로, 가져온 리소스가 포함되는 본문.

