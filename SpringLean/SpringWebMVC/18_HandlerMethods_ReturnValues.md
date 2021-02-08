## Handler Method Return

- ResponseBody

  리턴 값을 HttpMessageConverter를 사용해 응답 본문으로 사용

- HttpEntity,ResponseEntity

  응답 본문 뿐 아니라 전체 응답을 만들 때  사용한다 응답 헤더, 본문 status 값 모두 설정 할 수 있다

- HttpHeaders

  응답 헤더만 리턴하고 싶을 때 사용

- String

  String의 이름에 해당하는 뷰를 ViewResolver가 찾는다

- View

  암묵적인 모델 정보를 렌더링 할 뷰 인스턴스

- Map, Model

  암묵적으로 판단한 뷰 렌더링 할 때 사용할 모델 정보

- @ModelAttribute

  암묵적으로 판단한 뷰 렌더링 할 때 사용할 모델 정보에 추가한다, 생략가능

