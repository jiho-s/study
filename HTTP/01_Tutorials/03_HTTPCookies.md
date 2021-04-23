## HTTP 쿠키

HTTP 쿠키(웹 쿠키, 브라우저 쿠키)는 서버가 사용자의 웹 브라우저에 전송하는 작은 데이터 조각이다. 브라우저는 그 데이터 조각들을 저장해 놓았다가, 동일한 서버에 재 요청 시 저장된 데이터를 함께 전송한다. 쿠키는 두 요청이 동일한 브라우저에서 들어왔는지 아닌지를 판단할 때 주로 사용한다. 이를 이용하면 사용자의 로그인 상태를 유지할 수 있다.

쿠키는 다음의 목적을 위해 사용된다.

- 세션 관리

  서버에 저장해야 할 로그인, 장바구니, 게임 스코어 등의 정보 관리

- 개인화

  사용자 선호, 테마 등의 세팅

- 트래킹

  사용자 행동을 기록하고 분석하는 용도

과거엔 클라이언트 측에 정보를 저장하는 유일한 방법이 쿠키 밖에 없었다. 하지만, 지금은modern storage APIs를 사용해 정보를 저장할수 있고 이를 권장한다. 모든 요청마다 쿠키가 함께 전송되기 때문에, (특히 mobile data connections에서) 성능이 떨어지는 원인이 될 수 있다. 정보를 클라이언트 측에 저장하려면 Modern APIs의 종류인 [웹 스토리지 API](https://developer.mozilla.org/en-US/docs/Web/API/Web_Storage_API) (`localStorage`와 `sessionStorage`) 와[ IndexedDB](https://developer.mozilla.org/en-US/docs/Web/API/IndexedDB_API)를 사용하면 된다

### 쿠키 만들기

HTTP 요청을 수신할 때, 서버는 응답과 함께 `Set-Cookie` 헤더를 전송할 수 있다. 쿠키는 보통 브라우저에 의해 저장되며, 그 후 쿠키는 같은 서버에 의해 만들어진 요청(Request)들의 `Cookie` HTTP 헤더안에 포함되어 전송된다. 만료일 혹은 지속시간(duration)도 명시될 수 있고, 만료된 쿠키는 더이상 보내지지 않는다. 추가적으로, 특정 도메인 혹은 경로 제한을 설정할 수 있으며 이는 쿠키가 보내지는 것을 제한할 수 있다.

#### `Set-Cookie`와 `Cookie` 헤더

`Set-Cookie` HTTP 응답 헤더는 서버로부터 사용자 에이전트로 전송된다.

```
Set-Cookie: <cookie-name>=<cookie-value>
```

```
HTTP/1.0 200 OK
Content-type: text/html
Set-Cookie: yummy_cookie=choco
Set-Cookie: tasty_cookie=strawberry
```

이제, 서버로 전송되는 모든 요청과 함께, 브라우저는 `Cookie` 헤더를 사용하여 서버로 이전에 저장했던 모든 쿠키를 회신한다.

```
GET /sample_page.html HTTP/1.1
Host: www.example.org
Cookie: yummy_cookie=choco; tasty_cookie=strawberry
```

#### 쿠키의 라이프타임

쿠키의 라이프타임은 두가지 종류가 있다.

- 세션 쿠키는 현재 세션이 끝날 때 삭제된다.
- 영속정인 쿠키는 `Expires` 속성에 명시된 날짜에 삭제되거나, `Max-Age` 속성에 명시된 기간 이후에 삭제된다.

```
Set-Cookie: id=a3fWa; Expires=Wed, 21 Oct 2015 07:28:00 GMT;
```

#### 쿠키에 대한 제한된 접근

쿠키과 전송된때 안전하고 다른 부분에서 접근을 막기를 보장하는 방법에는 `Secure` 속성과 `HttpOnly` 속성이 있다.

Secure 쿠키는 HTTPS 프로토콜 상에서 암호화된(encrypted ) 요청일 경우에만 전송된다.  안전하지 않은 HTTP 에서는 전송되지 않기 때문에, 중간자 공격에 의해 쉽게 접근할 수 없다  하지만 `Secure`일지라도 민감한 정보는 절대 쿠키에 저장되면 안된다. `Secure` 이 모든 쿠키안에 있는 모든 민감한 정보에 대한 접근을 막아주지는 않는다. 예를 들어 클라이언트에 하드디스크(또는 `HttpOnly`가 설정되지 않은 경우 JavaScript를 통해서)에 누군가 접근하여 정보를 읽거나 수정할 수 있다.

`HttpOnly` 속성이 설정된 쿠키는 자바스크립트의 `Document.cookie` API를 통하여 접근할 수 없다. 쿠키는 서버에 전송되기만 한다. 예를들어 서버 쪿에서 지속되고 있는 세션의 쿠키는 자바스크립트를 사용할 필요가 없다. 따라서 `HttpOnly` 속성을 설정해야한다. 이런 조치는 XSS 공격을 방지할 수 있다.

```
Set-Cookie: id=a3fWa; Expires=Thu, 21 Oct 2021 07:28:00 GMT; Secure; HttpOnly
```

#### 쿠키가 어디로 보내질지 정의

`Domain` 그리고 `Path` 속성은 쿠키의 스코프를 정의하고, 어떤 URL을 통하여 쿠키가 보내져야 하는지 정한다.

##### `Domain` 속성

`Domain` 속성은 어떤 hosts들이 쿠키를 받는것을 허용할지 명시한다. 만약 명시되지 않는다면, 기본적으로  쿠키를 설정한 서브도매인은 포함하지 않는 host와 같다. `Domain` 이 명시되면, 서브 도매인들은 항상 포함된다.  서브도매인이 유저에대한 정보를 공유할 필요가 있을때 유용하다.

예를들어 만약 `Domain=mozilla.org`이 설정되면, 쿠키들은 `developer.mozilla.org`와 같은 서브도메인 상에 포함되게 된다.

##### `Path` 속성

`Path`는 `Cookie` 헤더를 전송하기 위하여 요청되는 URL 내에 반드시 존재해야 하는 URL 경로이다.  `%x2F` ("/") 문자는 디렉토리 구분자로 해석되며 서브 디렉토리들과 잘 매치될 것이다.

만약 `Path=/docs`이 설정되면, 다음의 경로들은 모두 매치될 것이다.

- `/docs`
- `/docs/Web/`
- `/docs/Web/HTTP`

##### `SameSite` attribute

`SameSite` 속성은 서버가 쿠키가 cross-site 요청으로 보내질때, 언제/어디로 보내질지 명시할 수 있다. 이는 cross-site request forgery 공격을 예방할 수 있다.

`SameSite` 속성은 `Strict`, `Lax`, `None` 세가지 상태를 가진다. `Strict` 는 쿠키가 쿠키가 만들어진 것과 같은 사이트에만 전송되게 허용한다. `Lax` 는 외부 사이트의 링크를 따라 쿠키의 원래 사이트로 이동할때 쿠키가 발송되는 것을 허용하는 것을 제외함면 `Strict`  와 비슷하다. `None`  은 쿠키가 원래의 사이트와 `cross-site` 요청에도 보내질수 있게한다. 그러나, 보안 컨텍스트(`Secure` 속성이 반드시 설정되어야 한다) 일경우에만 가능하다. `SameSite`  속성이 설정되지 않는 경우 쿠키는 `Lax`로 취급된다.

```java
Set-Cookie: mykey=myvalue; SameSite=Strict
```

##### 쿠키 접두사

쿠키 메커니즘의 설계에서 서버가 쿠키가 안전한 origin 설정되어있는지 확인하거나, 쿠키가 원래설정된 위치를 확신할 수 없다.

서브도메인에 취약한 애플리케이션은 `Domain` 속성을 사용하여 쿠키를 설정할 수 있고,  이러한 쿠리를 다른 모든 서브도메인에서 접근할 수 있다. 이러한 메커니즘은 *session fixation* 공격에 이용될 수 있다

그러나, 해결방안으로 쿠키 접두사를 이용해 특정 사실을 명시할 수 있다.

###### `__Host-`

쿠키 이름이 다음의 접두사가 있는 경우, `Secure` 속성이 있는 경우에만 허용되고,  Secure origin에서만 보낼수 있고, `Domain` 속성이 포함되지 않으며, `Path` 속성은 `/` 로 설정되어야 한다.  이런 경우를 쿠기가 도메인 잠금 되었다고 볼수 있다.

###### `__Secure-`

쿠키 이름에 다음 접두사가 있는경우 , `Secure` 속성이 있는 경우에만 허용되고,  Secure origin에서만 보낼수 있다. `__Host-`보다 약하다

제한사항을 준수하지 않는 이러한 접두사가 있는 쿠키는 브라우저에서 거부된다. 이렇게하면 서브도메인이 접두사를 사용하여 쿠키를 생성하는 경우 하위 도메인으로 제한되거나 무시된다. 애플리케이션 서버는 사용자가 인증되었는지 또는 CSRF 토큰이 올바른지 판단할때만 특정 쿠키 이름을 확인하므로, *session fixation* 공격에 효과적으로 방어할 수 있다.

##### Document.cookie를 사용한 자바스크립트 접근

새로운 쿠키들은 [`Document.cookie`](https://developer.mozilla.org/ko/docs/Web/API/Document/cookie)를 사용해 만들어질 수도 있으며, `HttpOnly` 플래그가 설정되지 않은 경우 기본의 쿠키들은 자바스크립트로부터  접근될 수 있다.

```javascript
document.cookie = "yummy_cookie=choco";
document.cookie = "tasty_cookie=strawberry";
console.log(document.cookie);
// logs "yummy_cookie=choco; tasty_cookie=strawberry"
```

자바스크립트를 통해 생성된 쿠키는 `HttpOnly` 플래그를 포함할 수 없다.

### Security

쿠키와 관련된 공격을 완화하는 방법

- `HttpOnly` 속성을 이용한 자바스크립트를 통한 쿠키값 접근 막기
- 쿠키에 민감한 정보가 포함된 경우, 라이프타임은 짧게, 그리고 `SameSite` 속성의 `Strict` 또는 `Lax`로 설정되어야 한다

### 추적과 프라이버시

#### 서드파티 쿠키

쿠니는 도메인과 관련이 있다. 도메인이 현재 페이지의 도메인과 일치하는 경우 퍼스트파티 쿠키라고한다. 도메인이 다른 경우는 서드파티 쿠키다. 웹 페이지를 호스팅하는 서버가 퍼스트파티 쿠키를 설정하는 동안 페이지에는 서드파티 쿠키를 설정할수 있는 다른 도메인의 서버에 저장된 이미지 또는 기타 구성요소가 포함될 수 있다.

서드파티 서버는 여러 사이트에 접근할 때 동일한 브라우저에서 보낸 쿠키를 기반으로 사용자의 검색 기록 및 습관에 대한 프로필을 만들수 있다.

### 브라우저에 정보를 저장하는 다른 방법

브라우저에 데이터를 저장하는 또 다른 방법은 `Web Storage API`를 이용하는 것이다. `window.sessionStorage`와 `window.localStorage` 속성은 세션 쿠키와 영속적인 쿠키에 해당한다. 그러나 쿠키보다 용량제한이 크고, 서버에 전송되지 않는다. `IndexedDB API`를 통해 더 구조화되고 더 많은 양의 데이터를 저장할 수 있다.