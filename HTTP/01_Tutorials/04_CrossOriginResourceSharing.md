## 교차 출처 리소스 공유(CORS)

CORS는 브라우저가 리소스로더를 허용해야하는 자신의 origin이외의 다른 origin을 서버가 알려주는 HTTP 헤더기반 메커니즘이다. CORS는 또한 서버가 실제 요청을 허용하는지 확인하기 위해 브라우저가 교차 출처 리소스를 호스팅하는 서버에 preflight 요청을 보내는 메커니즘에 의존한다. preflight에서 브라우저는 HTTP 메소드를 나타내는 헤더와 실제 요청에 사용되는 헤더를 보낸다.

교차 출처 요청의 예시로는 `https://domain-a.com`의 프론트 엔드 JavaScript 코드가 [`XMLHttpRequest`](https://developer.mozilla.org/ko/docs/Web/API/XMLHttpRequest)를 사용하여 `https://domain-b.com/data.json`을 요청하는 경우 등이 있다.

보안 상의 이유로, 브라우저는 스크립트에서 시작한 교차 출처 HTTP 요청을 제한합니다. 예를 들어, `XMLHttpRequest`와 [Fetch API](https://developer.mozilla.org/ko/docs/Web/API/Fetch_API)는 [동일 출처 정책](https://developer.mozilla.org/ko/docs/Web/Security/Same-origin_policy)을 따른다. 즉, 이 API를 사용하는 웹 애플리케이션은 자신의 출처와 동일한 리소스만 불러올 수 있으며, 다른 출처의 리소스를 불러오려면 그 출처에서 올바른 CORS 헤더를 포함한 응답을 반환해야 한다.

CORS 체제는 브라우저와 서버 간의 안전한 교차 출처 요청 및 데이터 전송을 지원한다. 최신 브라우저는 `XMLHttpRequest` 또는 [Fetch](https://developer.mozilla.org/ko/docs/Web/API/Fetch_API)와 같은 API에서 CORS를 사용하여 교차 출처 HTTP 요청의 위험을 완화한다.

### CORS를 사용하는 요청

- [`XMLHttpRequest`](https://developer.mozilla.org/ko/docs/Web/API/XMLHttpRequest)와 [Fetch API](https://developer.mozilla.org/ko/docs/Web/API/Fetch_API) 호출.
- 웹 폰트(CSS 내 `@font-face`에서 교차 도메인 폰트 사용 시)
- [WebGL 텍스쳐](https://developer.mozilla.org/ko/docs/Web/API/WebGL_API/Tutorial/Using_textures_in_WebGL).
- [`drawImage()` (en-US)](https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/drawImage)를 사용해 캔버스에 그린 이미지/비디오 프레임.
- [이미지로부터 추출하는 CSS Shapes.](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_Shapes/Shapes_From_Images)

### 기능적 개요

교차 출처 리소스 공유 표준은 웹 브라우저에서 해당 정보를 읽는 것이 허용된 출처를 서버에서 설명할 수 있는 새로운 HTTP 헤더를 추가함으로써 동작한다. 추가적으로, 서버 데이터에 부수 효과(side effect)를 일으킬 수 있는 HTTP 요청 메서드(GET을 제외한 HTTP 메서드)에 대해, CORS 명세는 브라우저가 요청을 OPTION 메서드로 "프리플라이트"(preflight, 사전 전달)하여 지원하는 메서드를 요청하고, 서버의 "허가"가 떨어지면 실제 요청을 보내도록 요구하고 있다. 또한 서버는 클라이언트에게 요청에 "인증정보"([쿠키](https://developer.mozilla.org/ko/docs/Web/HTTP/Cookies), [HTTP 인증](https://developer.mozilla.org/ko/docs/Web/HTTP/Authentication))를 함께 보내야 한다고 알려줄 수도 있다.

### 예제

#### 단순 요청

일부요청은 [CORS preflight](https://wiki.developer.mozilla.org/ko/docs/Web/HTTP/Access_control_CORS$edit#Preflighted_requests) 를 트리거하지 않는다.  "simple requests"는 **다음 조건을 모두 충족하는 요청이다:**

- 다음 중 하나의 메서드인 경우
  - GET
  - HEAD
  - POST
- 유저 agent가 자동으로 설정 한 헤더( [`Connection`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Connection), [User-Agent (en-US)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/User-Agent), [Fetch 명세에서 “forbidden header name”으로 정의한 헤더](https://fetch.spec.whatwg.org/#forbidden-header-name))외에 수동으로 설정한 헤더가  [Fetch 명세에서 “CORS-safelisted request-header”로 정의한 헤더](https://fetch.spec.whatwg.org/#cors-safelisted-request-header) 인 경우
  - [`Accept`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept)
  - [`Accept-Language`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language)
  - [`Content-Language`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Language)
  - [`Content-Type`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type) 
- [`Content-Type`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Type) 은 다음의 값들만 허용된다.
  - `application/x-www-form-urlencoded`
  - `multipart/form-data`
  - `text/plain`
- 요청이 `XMLHttpRequest` 객체로 만들어졌을때, `XMLHttpRequest.upload` 에서 반환된 값에 이벤트 리스너가 등록되 있지 않은 경우
- 요청에 `ReadableStream` 객체가 없는 경우

예를들어, `https://foo.example` 의 웹 컨텐츠가  `https://bar.other` 도메인의 컨텐츠를 호출하길 원한다. `foo.example`에 배포된 자바스크립트에는 아래와 같은 코드가 사용될 수 있다.

```javascript
const xhr = new XMLHttpRequest();
const url = 'https://bar.other/resources/public-data/';

xhr.open('GET', url);
xhr.onreadystatechange = someHandler;
xhr.send();
```

이 경우 브라우저가 서버로 전송하는 내용을 살펴보고, 서버의 응답을 확인해보자.

```
GET /resources/public-data/ HTTP/1.1
Host: bar.other
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:71.0) Gecko/20100101 Firefox/71.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-us,en;q=0.5
Accept-Encoding: gzip,deflate
Connection: keep-alive
Origin: https://foo.example
```

요청 헤더의 [`Origin`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Origin)을 보면, `https://foo.example`로부터 요청이 왔다는 것을 알 수 있습니다.

```
HTTP/1.1 200 OK
Date: Mon, 01 Dec 2008 00:23:53 GMT
Server: Apache/2
Access-Control-Allow-Origin: *
Keep-Alive: timeout=2, max=100
Connection: Keep-Alive
Transfer-Encoding: chunked
Content-Type: application/xml

[…XML Data…]
```

서버는 이에 대한 응답으로 [`Access-Control-Allow-Origin`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Access-Control-Allow-Origin) 헤더를 `Access-Control-Allow-Origin: *`로 다시 전송한다. 이는 **모든** 도메인에서 접근할 수 있음을 의미한다. `https://bar.other` 의 리소스 소유자가 *오직* `https://foo.example` 의 요청만 리소스에 대한 접근을 허용하려는 경우 다음을 전송한다.

```
Access-Control-Allow-Origin: https://foo.example
```

#### 프리플라이트 요청

"preflighted" request는 위에서 논의한 [simple request](#단순-요청) 와는 달리, 먼저 OPTIONS 메서드를 통해 다른 도메인의 리소스로 HTTP 요청을 보내 실제 요청이 전송하기에 안전한지 확인한다. Cross-site 요청은 유저 데이터에 영향을 줄 수 있기 때문에 이와같이 미리 전송(preflighted)한다.

다음은 preflighted 할 요청의 예제이다.

```javascript
const xhr = new XMLHttpRequest();
xhr.open('POST', 'https://bar.other/resources/post-here/');
xhr.setRequestHeader('Ping-Other', 'pingpong');
xhr.setRequestHeader('Content-Type', 'application/xml');
xhr.onreadystatechange = handler;
xhr.send('<person><name>Arun</name></person>');
```

위의 예제는 `POST` 요청과 함께 함께 보낼 XML body를 만든다. 또한 비표준 HTTP `Ping-Other` 요청 헤더가 설정된다. Content-Type 이 `application/xml`이고, 사용자 정의 헤더가 설정되었기 때문에 이 요청은 preflighted 처리된다.

클라이언트와 서버간의 완전한 통신을 살펴보자. 첫 번째 통신은 *preflight request/response*이다.

```
OPTIONS /resources/post-here/ HTTP/1.1
Host: bar.other
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:71.0) Gecko/20100101 Firefox/71.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-us,en;q=0.5
Accept-Encoding: gzip,deflate
Connection: keep-alive
Origin: http://foo.example
Access-Control-Request-Method: POST
Access-Control-Request-Headers: X-PINGOTHER, Content-Type


HTTP/1.1 204 No Content
Date: Mon, 01 Dec 2008 01:15:39 GMT
Server: Apache/2
Access-Control-Allow-Origin: https://foo.example
Access-Control-Allow-Methods: POST, GET, OPTIONS
Access-Control-Allow-Headers: X-PINGOTHER, Content-Type
Access-Control-Max-Age: 86400
Vary: Accept-Encoding, Origin
Keep-Alive: timeout=2, max=100
Connection: Keep-Alive
```

첫 번째 예제의 1 - 10 행은 OPTIONS 메서드를 사용한 preflight request를 나타냅니다. 브라우저는 위의 자바스크립트 코드 스니펫이 사용중인 요청 파라미터를 기반으로 전송해야 한다. 그렇게 해야 서버가 실제 요청 파라미터로 요청을 보낼 수 있는지 여부에 응답할 수 있다. OPTIONS는 서버에서 추가 정보를 판별하는데 사용하는 HTTP/1.1 메서드입니다. 또한 [safe (en-US)](https://developer.mozilla.org/en-US/docs/Glossary/Safe) 메서드이기 때문에, 리소스를 변경하는데 사용할 수 없다. OPTIONS 요청과 함께 두 개의 다른 요청 헤더가 전송된다.

```
Access-Control-Request-Method: POST
Access-Control-Request-Headers: X-PINGOTHER, Content-Type
```

위의 13 - 22 행은 서버가 요청 메서드와 (`POST`) 요청 헤더를 (`X-PINGOTHER`) 받을 수 있음을 나타내는 응답다. 이중에 16 - 19행을 살펴보자.

```
Access-Control-Allow-Origin: http://foo.example
Access-Control-Allow-Methods: POST, GET, OPTIONS
Access-Control-Allow-Headers: X-PINGOTHER, Content-Type
Access-Control-Max-Age: 86400
```

서버 응답은 `Access-Control-Allow-Origin:` 을 `http://foo.example`로 하는데, 요청 origin 도메인을 제한한다. 또한, `Access-Control-Allow-Methods` 로 응답하고 `POST` 와 `GET` 이 리소스를 쿼리하는데 유용한 메서드라고 가르쳐준다. 이 헤더는 [`Allow`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Allow) 응답 헤더와 유사하지만, 접근 제어 컨텍스트 내에서 엄격하게 사용된다.

또한 `Access-Control-Allow-Headers` 의 값을 "`X-PINGOTHER, Content-Type`" 으로 전송하여 실제 요청에 헤더를 사용할 수 있음을 확인해준다. `Access-Control-Allow-Methods`와 마찬가지로 `Access-Control-Allow-Headers` 는 쉼표로 구분된 허용 가능한 헤더 목록이다.

마지막으로[Access-Control-Max-Age (en-US)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Max-Age)는 다른 preflight request를 보내지 않고, preflight request에 대한 응답을 캐시할 수 있는 시간(초)을 제공합니다. 위의 코드는 86400 초(24시간) 입니다. 각 브라우저의 [최대 캐싱 시간 ](https://wiki.developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Max-Age)은 `Access-Control-Max-Age` 가 클수록 우선순위가 높습니다.

preflight request가 완료되면 실제 요청을 전송한다.

```
POST /resources/post-here/ HTTP/1.1
Host: bar.other
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:71.0) Gecko/20100101 Firefox/71.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-us,en;q=0.5
Accept-Encoding: gzip,deflate
Connection: keep-alive
X-PINGOTHER: pingpong
Content-Type: text/xml; charset=UTF-8
Referer: https://foo.example/examples/preflightInvocation.html
Content-Length: 55
Origin: https://foo.example
Pragma: no-cache
Cache-Control: no-cache

<person><name>Arun</name></person>


HTTP/1.1 200 OK
Date: Mon, 01 Dec 2008 01:15:40 GMT
Server: Apache/2
Access-Control-Allow-Origin: https://foo.example
Vary: Accept-Encoding, Origin
Content-Encoding: gzip
Content-Length: 235
Keep-Alive: timeout=2, max=99
Connection: Keep-Alive
Content-Type: text/plain

[Some GZIP'd payload]
```

##### Preflighted requests 와 리다이렉트

모든 브라우저가 preflighted request 후 리다이렉트를 지원하지는 않는다. preflighted request 후 리다이렉트가 발생하면 일부 브라우저는 다음과 같은 오류 메시지를 띄운다.

> 요청이 'https://example.com/foo'로 리다이렉트 되었으며, preflight가 필요한 cross-origin 요청은 허용되지 않습니다.

> 요청에 preflight가 필요합니다. preflight는 cross-origin 리다이렉트를 허용하지 않습니다.

CORS 프로토콜은 본래 그 동작(리다이렉트)이 필요했지만, [이후 더 이상 필요하지 않도록 변경되었다](https://github.com/whatwg/fetch/commit/0d9a4db8bc02251cc9e391543bb3c1322fb882f2). 그러나 모든 브라우저가 변경 사항을 구현하지는 않았기 때문에, 본래의 필요한 동작은 여전히 나타난다.

브라우저가 명세를 따라잡을 때 까지 다음 중 하나 혹은 둘 다를 수행하여 이 제한을 해결할 수 있습니다.

- preflight 리다이렉트를 방지하기 위해 서버측 동작을 변경
- preflight를 발생시키지 않는 [simple request](https://wiki.developer.mozilla.org/ko/docs/Web/HTTP/Access_control_CORS$edit#Simple_requests) 가 되도록 요청을 변경

#### 인증정보를 포함한 요청

`XMLHttpRequest` 혹은 Fetch API 를 사용할 때 CORS 에 의해 드러나는 가장 흥미로운 기능은 "credentialed" requests 이다. credentialed requests는 [HTTP cookies](https://wiki.developer.mozilla.org/en-US/docs/Web/HTTP/Cookies) 와 HTTP Authentication 정보를 인식한다. 기본적으로 cross-site `XMLHttpRequest` 나 Fetch호출에서 브라우저는 자격 증명을 보내지 **않는다.** `XMLHttpRequest` 객체나 Request 생성자가 호출될 때 특정 플래그를 설정해야 한다.

이 예제에서 원래 `http://foo.example` 에서 불러온 컨텐츠는 쿠키를 설정하는 `http://bar.other` 리소스에 simple GET request를 작성한다. foo.example의 내용은 다음과 같은 자바스크립트를 포함할 수 있다.

```javascript
const invocation = new XMLHttpRequest();
const url = 'http://bar.other/resources/credentialed-content/';

function callOtherDomain() {
  if (invocation) {
    invocation.open('GET', url, true);
    invocation.withCredentials = true;
    invocation.onreadystatechange = handler;
    invocation.send();
  }
}
```

7행은 쿠키와 함께 호출하기위한 XMLHtttpRequest 의 플래그를 보여준다. 이 플래그는 `withCredentials` 라고 불리며 부울 값을 갖는다. 기본적으로 호출은 쿠키 없이 이루어진다. 이것은 simple `GET` request이기 때문에 preflighted 되지 않는다. 그러나 브라우저는 [`Access-Control-Allow-Credentials`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Access-Control-Allow-Credentials): `true` 헤더가 없는 응답을 **거부한다**. 따라서 호출된 웹 컨텐츠에 응답을 제공하지 **않는다**.

클라이언트와 서버간의 통신 예제는 다음과 같다.

```
GET /resources/credentialed-content/ HTTP/1.1
Host: bar.other
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:71.0) Gecko/20100101 Firefox/71.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
Accept-Language: en-us,en;q=0.5
Accept-Encoding: gzip,deflate
Connection: keep-alive
Referer: http://foo.example/examples/credential.html
Origin: http://foo.example
Cookie: pageAccess=2

HTTP/1.1 200 OK
Date: Mon, 01 Dec 2008 01:34:52 GMT
Server: Apache/2
Access-Control-Allow-Origin: https://foo.example
Access-Control-Allow-Credentials: true
Cache-Control: no-cache
Pragma: no-cache
Set-Cookie: pageAccess=3; expires=Wed, 31-Dec-2008 01:34:53 GMT
Vary: Accept-Encoding, Origin
Content-Encoding: gzip
Content-Length: 106
Keep-Alive: timeout=2, max=100
Connection: Keep-Alive
Content-Type: text/plain

[text/plain payload]
```

10행에는 `http://bar.other`의 컨텐츠를 대상으로 하는 쿠키가 포함되어 있다. 하지만 17행의 [`Access-Control-Allow-Credentials`](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Access-Control-Allow-Credentials): `true` 로 응답하지 않으면, 응답은 무시되고 웹 컨텐츠는 제공되지 않는다.

##### Preflight requests 와 자격증명

Preflight 요청은 자격증명을 포함하지 못한다. Preflight의 요청에 대한 읃답은 실제 응답에 자격증명이 포함될수 있다는 것을 나타내기 위해  `Access-Control-Allow-Credentials: true` 여야 한다.

##### 자격증명 요청 및 와일드카드

credentialed request 에 응답할 때 서버는 `Access-Control-Allow-Origin` 헤더 "`*`" 와일드카드를 사용하는 대신에 **반드시** 에 값을 지정해야 한다.

위 예제의 요청 헤더에 `Cookie` 헤더가 포함되어 있기 때문에 `Access-Control-Allow-Origin` 헤더의 값이 "*"인 경우 요청이 실패한다. 위 요청은 `Access-Control-Allow-Origin` 헤더가 "`*`" 와일드 카드가 아니라 "`http://foo.example`" 본래 주소이기 때문에 자격증명 인식 컨텐츠는 웹 호출 컨텐츠로 리턴된다.

위 예제의 `Set-Cookie` 응답 헤더는 추가 쿠키를 설정한다. 실패한 경우 사용한 API에 따라 예외가 발생한다.

##### Third-party cookies

CORS 응답에 설정된 쿠키에는 일반적인 third-party cookie 정책이 적용된다. 위의 예제는 `foo.example`에서 페이지를 불러지만 20행의 쿠키는 `bar.other` 가 전송한다. 때문에 사용자의 브라우저 설정이 모든 third-party cookies를 거부하도록 되어 있다면, 이 쿠키는 저장되지 않는다.

### HTTP 응답 헤더

이 섹션에서는 Cross-Origin 리소스 공유 명세에 정의된 대로 서버가 접근 제어 요청을 위해 보내는 HTTP 응답 헤더가 나열되어 있다. 

#### Access-Control-Allow-Origin

리턴된 리소스에는 하나의 Access-Control-Allow-Origin 헤더가 있을 수 있다.

```
Access-Control-Allow-Origin: <origin> | *
```

`Access-Control-Allow-Origin` 은 origin을 지정하여 브라우저가 해당 출처가 리소스에 접근하도록 허용한다. 또는 자격 증명이 **없는** 요청의 경우 "`*`" 와일드 카드는 브라우저의 origin에 상관없이 모든 리소스에 접근하도록 허용힌다.

예를들어 `https://mozilla.org` 의 코드가 리소스에 접근 할 수 있도록 하려면 다음과 같이 지정할 수 있다.

```
Access-Control-Allow-Origin: https://mozilla.org
Vary: Origin
```

서버가 "`*`" 와일드카드 대신에 하나의 origin을 지정하는 경우(이 origin은 화이트 리스트의 일부로 요청 orgin에 따라 동적으로 변경될 수 있다), 서버는 Vary 응답 헤더에 `Origin` 을 포함해야 한다. 서버 응답이 Origin 요청 헤더에 따라 다르다는것을 클라이언트에 알려준다.

#### Access-Control-Expose-Headers

Access-Control-Expose-Headers 헤더를 사용하면 브라우저가 접근할 수 있는 헤더를 서버의 화이트리스트에 추가할 수 있다.

```
Access-Control-Expose-Headers: <header-name>[, <header-name>]*
```

예를들면 다음과 같다.

```
Access-Control-Expose-Headers: X-My-Custom-Header, X-Another-Custom-Header
```

#### Access-Control-Max-Age

Access-Control-Max-Age 헤더는 preflight request 요청 결과를 캐시할 수 있는 시간을 나타낸다.

```
Access-Control-Max-Age: <delta-seconds>
```

`delta-seconds` 파라미터는 결과를 캐시할 수 있는 시간(초)를 나타낸다.

#### Access-Control-Allow-Credentials

Access-Control-Allow-Credentials 헤더는 `credentials` 플래그가 true일 때 요청에 대한 응답을 표시할 수 있는지를 나타낸다. preflight request에 대한 응답의 일부로 사용하는 경우, credentials을 사용하여 실제 요청을 수행할 수 있는지를 나타낸다. simple `GET` requests는 preflighted되지 않으므로 credentials이 있는 리소스를 요청하면, 이 헤더가 리소스와 함께 반환되지 않는다. 이 헤더가 없으면 브라우저에서 응답을 무시하고 웹 컨텐츠로 반환되지 않는다.

```
Access-Control-Allow-Credentials: true
```

#### Access-Control-Allow-Methods

Access-Control-Allow-Methods 헤더는 리소스에 접근할 때 허용되는 메서드를 지정한다. 이 헤더는 preflight request에 대한 응답으로 사용된다. 요청이 preflighted 되는 조건은 위에 설명되어 있다.

```
Access-Control-Allow-Methods: <method>[, <method>]*
```

#### Access-Control-Allow-Headers

preflight request 에 대한 응답으로 Access-Control-Allow-Headers 헤더가 사용된다. 실제 요청시 사용할 수 있는 HTTP 헤더를 나타낸다.

```
Access-Control-Allow-Headers: <header-name>[, <header-name>]*
```

### HTTP 요청 헤더

이 섹션에는 cross-origin 공유 기능을 사용하기 위해 클라이언트가 HTTP 요청을 발행할 때 사용할 수 있는 헤더가 나열되어 있다. 이 헤더는 서버를 호출할 때 설정된다. cross-site `XMLHttpRequest`기능을 사용하는 개발자는 프로그래밍 방식으로 cross-origin 공유 요청 헤더를 설정할 필요는 없다.

#### Origin

`Origin` 헤더는 cross-site 접근 요청 또는 preflight request의 출처를 나타낸다.

```
Origin: <origin>
```

origin 은 요청이 시작된 서버를 나타내는 URI 이다. 경로 정보는 포함하지 않고, 오직 서버 이름만 포함한다.

접근 제어 요청에는 **항상 **`Origin` 헤더가 전송된다.

#### Access-Control-Request-Method

`Access-Control-Request-Method` 헤더는 실제 요청에서 어떤 HTTP 메서드를 사용할지 서버에게 알려주기 위해, preflight request 할 때에 사용된다.

```
Access-Control-Request-Method: <method>
```

#### Access-Control-Request-Headers

`Access-Control-Request-Headers` 헤더는 실제 요청에서 어떤 HTTP 헤더를 사용할지 서버에게 알려주기 위해, preflight request 할 때에 사용된다.

```
Access-Control-Request-Headers: <field-name>[, <field-name>]*
```