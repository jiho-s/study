## HTTP Messages

HTTP 메시지는 서버와 클라이언트 간에 데이터가 교환되는 방식이다. 메시지 타입은 두 가지가 있다. 요청(*request)은* 클라이언트가 서버로 전달해서 서버의 액션이 일어나게끔 하는 메시지고, 응답(*response)은 요청*에 대한 서버의 답변이다.

HTTP 메시지는 ASCII로 인코딩된 텍스트 정보이며 여러 줄로 되어 있다. HTTP 프로토콜 초기 버전과 HTTP/1.1에서는 클라이언트와 서버 사이의 연결을 통해 공개적으로 전달되었습니다. 이렇게 한 때 사람이 읽을 수 있었던 메시지는 HTTP/2에서는 최적화와 성능 향상을 위해 HTTP 프레임으로 나누어진다.

웹 개발자, 또는 웹 마스터가 손수 HTTP 메시지를 텍스트로 작성하는 경우는 드뭅다. 대신에 소프트웨어, 브라우저, 프록시, 또는 웹 서버가 그 일을 한다. HTTP 메시지는 설정 파일(프록시 혹은 서버의 경우), API(브라우저의 경우), 혹은 다른 인터페이스를 통해 제공된다.

HTTP/2의 이진 프레이밍 메커니즘(binary framing mechanism)은 사용 중인 API나 설정 파일 등을 변경하지 않아도 되도록 설계 되었기 때문에, 사용자가 보고 이해하기 쉽습니다.

HTTP 요청과 응답의 구조는 서로 닮았으며, 그 구조는 다음과 같습니다.

1. 시작 줄(start-line)에는 실행되어야 할 요청, 또은 요청 수행에 대한 성공 또는 실패가 기록되어 있다. 이 줄은 항상 한 줄로 끝난다.
2. 옵션으로 HTTP 헤더 세트가 들어갑니다. 여기에는 요청에 대한 설명, 혹은 메시지 본문에 대한 설명이 들어간다.
3. 요청에 대한 모든 메타 정보가 전송되었음을 알리는 빈 줄(blank line)이 삽입된다.
4. 요청과 관련된 내용(HTML 폼 콘텐츠 등)이 옵션으로 들어가거나, 응답과 관련된 문서(document)가 들어간다. 본문의 존재 유무 및 크기는 첫 줄과 HTTP 헤더에 명시된다.

HTTP 메시지의 시작 줄과 HTTP 헤더를 묶어서 요청 *헤드(head)라고 부르며*, 이와 반대로 HTTP 메시지의 페이로드는 *본문(body)*이라고 합니다.

### HTTP 요청

#### 시작 줄

HTTP 요청은 서버가 특정 동작을 취하게끔 만들기 위해 클라이언트에서 전송하는 메시지입니다. 시작 줄은 다음과 같이 세 가지 요소로 이루어져 있습니다.

1. 첫번째는 *HTTP 메서드*로  `GET`, `PUT`,`POST`, `HEAD`, `OPTIONS`를 사용해 서버가 수행해야 할 동작을 나타낸다. 예를 들어, `GET`은 리소스를 클라이언트로 가져다 달라는 것을 뜻하며, `POST`는 데이터가 서버로 들어가야 함을 의미한다.

2. 두번째로 오는 요청 타겟은 주로 URL, 또는 프로토콜, 포트, 도메인의 절대 경로로 나타낼 수도 있으며 이들은 요청 컨텍스트에 의해 특정지어 진다. 요청 타겟 포맷은 HTTP 메소드에 따라 달라진다. 포맷에는 다음과 같은 것들이 있다.

   - origin 형식: 끝에 `'?'`와 쿼리 문자열이 붙는 절대 경로이다. 이는 가장 일반적인 형식이며, `GET`, `POST`, `HEAD`, `OPTIONS` 메서드와 함께 사용된다.
     `POST / HTTP 1.1`
     `GET /background.png HTTP/1.0`
     `HEAD /test.html?query=alibaba HTTP/1.1`
     `OPTIONS /anypage.html HTTP/1.0`
     
   - *absolute 형식: 완전한 URL 형식이다.* 프록시에 연결하는 경우 대부분 `GET`과 함께 사용된다.
     `GET http://developer.mozilla.org/en-US/docs/Web/HTTP/Messages HTTP/1.1`
     
   - *authority 형식:* 도메인 이름 및 옵션 포트(`':'`가 앞에 붙는다)로 이루어진 URL의 authority component 이다. HTTP 터널을 구축하는 경우에만 `CONNECT`와 함께 사용할 수 있다.
     `CONNECT developer.mozilla.org:80 HTTP/1.1`
     
   - *asterisk 형식:* `OPTIONS`와 함께 별표(`'*'`) 하나로 간단하게 서버 전체를 나타낸다. 
     `OPTIONS * HTTP/1.1`

3. 마지막으로 *HTTP 버전*이 들어간다. 메시지의 남은 구조를 결정하기 때문에, 응답 메시지에서 써야 할 HTTP 버전을 알려주는 역할을 한다.

#### 헤더

요청에 들어가는 HTTP 헤더는 HTTP 헤더의 기본 구조를 따른다. 대소문자 구분없는 문자열 다음에 콜론(`':'`)이 붙으며, 그 뒤에 오는 값은 헤더에 따라 달라진다. 헤더는 값까지 포함해 한 줄로 구성되지만 꽤 길어질 수 있다.

다양한 종류의 요청 헤더가 있는데, 이들은 다음과 같이 몇몇 그룹으로 나눌 수 있다.

- General 헤더: [`Via`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Via)와 같은 *헤더는* 메시지 전체에 적용된다.
- Request 헤더: [User-Agent (en-US)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/User-Agent), [`Accept-Type`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Accept-Type)와 같은 헤더는 요청의 내용을 좀 더 구체화 시키고([`Accept-Language`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Accept-Language)), 컨텍스를 제공하기도 하며([`Referer`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Referer)), 조건에 따른 제약 사항을 가하기도 하면서([`If-None`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/If-None)) 요청 내용을 수정한다.
- Entity 헤더: [`Content-Length`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Length)와 같은 헤더는 요청 본문에 적용된다. 당연히 요청 내에 본문이 없는 경우 entity 헤더는 전송되지 않는다.

#### 본문

본문은 요청의 마지막 부분에 들어간다. 모든 요청에 본문이 들어가지는 않는다. `GET`, `HEAD`, `DELETE` , `OPTIONS`처럼 리소스를 가져오는 요청은 보통 본문이 필요가 없다. 일부 요청은 업데이트를 하기 위해 서버에 데이터를 전송한다.

넓게 보면 본문은 두가지 종류로 나뉜다.

- 단일-리소스 본문(single-resource bodies): 헤더 두 개([`Content-Type`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Type)와 [`Content-Length`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Length))로 정의된 단일 파일로 구성됩니다.
- 다중-리소스 본문(multiple-resource bodies): 멀티파트 본문으로 구성되는 [다중 리소스 본문](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types#multipartform-data)에서는 파트마다 다른 정보를 지니게 된다. 보통 [HTML 폼](https://developer.mozilla.org/en-US/docs/Learn/Forms)과 관련이 있다.

### HTTP 응답

#### 상태 줄

HTTP 응답의 시작 줄은 *상태 줄(status line)*이라고 불리며, 다음과 같은 정보를 가지고 있습니다.

1. *프로토콜 버전:* 보통 `HTTP/1.1`이다.
2. 상태 코드: 요청의 성공 여부를 나타낸다.
3. 상태 텍스트: 짧고 간결하게 상태 코드에 대한 설명을 글로 나타내어 사람들이 HTTP 메시지를 이해할 때 도움이 된다.

상태 줄은 일반적으로 `HTTP/1.1 404 Not Found.` 같이 생겼습니다.

#### 헤더

응답에 들어가는 HTTP 헤더는 다른 헤더와 동일한 구조를 따른다. 대소문자를 구분하지 않는 문자열 다음에 콜론(`':'`)이 오며, 그 뒤에 오는 값은 구조가 헤더에 따라 달라진다. 헤더는 값을 포함해 전체를 한 줄로 표시한다.

다양한 종류의 응답 헤더가 있는데, 이들은 다음과 같이 몇몇 그룹으로 나눌 수 있습니다.

- General 헤더: [`Via`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Via)와 같은 *헤더는* 메시지 전체에 적용됩니다.
- Response 헤더: [`Vary`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Vary)와 [`Accept-Ranges`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Accept-Ranges)와 같은 헤더는 상태 줄에 미처 들어가지 못했던 서버에 대한 추가 정보를 제공한다.
- Entity 헤더: [`Content-Length`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Length)와 같은 헤더는 요청 본문에 적용됩니다. 당연히 요청 내에 본문이 없는 경우 entity 헤더는 전송되지 않습니다.

### [본문](https://developer.mozilla.org/ko/docs/Web/HTTP/Messages#본문_2)

본문은 응답의 마지막 부분에 들어간다. 모든 응답에 본문이 들어가지는 않는다. [`201`](https://developer.mozilla.org/ko/docs/Web/HTTP/Status/201), [`204`](https://developer.mozilla.org/ko/docs/Web/HTTP/Status/204)과 같은 상태 코드를 가진 응답에는 보통 본문이 없다.

넓게 보면 본문은 세가지 종류로 나뉜다.

- 이미 길이가 알려진 단일 파일로 구성된 단일-리소스 본문: 헤더 두개([`Content-Type`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Type)와 [`Content-Length`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Length))로 정의 된다.
- 길이를 모르는 단일 파일로 구성된 단일-리소스 본문: [`Transfer-Encoding`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Transfer-Encoding)가 `chunked`로 설정되어 있으며, 파일은 청크로 나뉘어 인코딩 되어 있다.
- 서로 다른 정보를 담고 있는 멀티파트로 이루어진 [다중 리소스 본문](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types#multipartform-data): 이 경우는 상대적으로 위의 두 경우에 비해 보기 힘들다.

### HTTP/2 프레임

HTTP/1.x 메시지는 성능상의 결함을 몇가지 내포하고 있습니다.

- 본문은 압축이 되지만 헤더는 압축이 되지 않는다.
- 연속된 메시지들은 비슷한 헤더 구조를 띄기 마련인데, 그럼에도 불구하고 메시지마다 반복되어 전송되고 있다.
- 다중전송(multiplexing)이 불가능하다. 서버 하나에 연결을 여러개 열어야 한다.

HTTP/2에서는 추가적인 단계가 도입되었다. HTTP/1.x 메시지를 프레임으로 나누어 스트림에 끼워 넣는 것이다. 데이터와 헤더 프레임이 분리 되었기 때문에 헤더를 압축할 수 있다. 스트림 여러개를 하나로 묶을 수 있어서(이러한 과정을 멀티플렉싱이라 합니다), 기저에서 수행되는 TCP 연결이 좀 더 효율적이게 이루어진다.

이제 웹 개발자 입장에서는 HTTP 프레임을 매우 쉽게 살펴볼 수 있게 되었다. HTTP 프레임은 HTTP/2에서 추가된 단계이며, HTTP/1.1 메시지와 그 기저를 이루는 전송 프로토콜 사이를 메워주는 존재이다.