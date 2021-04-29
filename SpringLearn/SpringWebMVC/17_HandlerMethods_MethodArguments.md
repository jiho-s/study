## Handler Method Argument

요청 그 자체 또는 요청에 들어있는 정보를 받아오는데 사용

- WebRequest, NativeWebRequest

  요청 또는 응답 자체에 접근 가능한 API 이다

  NativeWebRequest의 경우 HTTPServletRequest,Response로 변환할 수 있다

  잘 사용하지 않는다

- HttpServletRequest(Response), ServletRequest(Response)

  요청 또는 응답 자체에 접근 가능한 API 이다

  HttpServlet이나 Servlet을 그대로 받아서 사용할 수 있다.

  잘 사용하지 않는다

- InputStream, OutputStream or Reader, Writer

  요청 본문을 읽어오거나, 응답 본문을 쓸 때 사용 할 수 있는 API

  서블릿 API를 사용하지 않고 Request 본문의 내용을 사용할 때 사용

  잘 사용하지 않는다

- PushBuilder

  스프링5에 추가, HTTP2 리소스 푸시에 사용

  기존에서 요청 후 뷰를 리턴하고 사용자가 다시 뷰의 리소스에 관해 요청을 보낸다 PushBuilder는 요청을 받았을 때 pushBuilder가 렌더링 할 때 필요한 추가적인 데이터를 미리 받아온다

- HttpMethod

  GET, POST 등 HttpMethod의 정보를 받아 올 수 있다

  Mapping을 여러 HttpMethod에  매핑한경우 HttpMethod의 정보를 받아와 나누어 작업을 할 수 있다

- Locale, TimeZone, ZoneId

  LocaleResolver가 분석한 요청의 Locale 정보를 받아 올 수 있다

- @PathVariable

  URI 탬플릿 변수 읽을 때 사용

- @MatrixVariable

  URI 경로 중에 키/값 쌍을 읽어 올 때 사용

- @RequestParam

  서블릿 요청 매개변수 값을 선언한 메소드의 아규먼트 타입으로 변환해 준다

  단순 타입인 경우에 생략 가능

- @RequestHeader

  요청 헤더 값을 메소드의 아규먼트 타입으로 변환

- @RequestBody

  HTTP 본문에 있는 값을 HTTPMessageConverter를 이용하여 아규먼트 타입으로 가져온다

  RestAPI에 많이 사용

- @RequestPart

  파일 업로드를 할 때 사용한다

- Map, Model, ModelMap

  Model 정보를 담을 때 사용할 수 있다

- RedirectAttributes

  리다이렉트시 모델정보를 URI에 노출시키지 않고 전달 할 수 있다

- ModelAttribute

  여러 개의 단순 타입 데이터를 복합 타입 객체로 받아오거나 해당 객체를 새로 만들 때 사용