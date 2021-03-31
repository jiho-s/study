# Validation, Data Binding and Type Conversion

validation은 웹 계층에 고정되면 안되고 쉽게 localize 되어야 하고 사용가능한 모든 validator를 연결할 수 있어야 한다. Spring은 애플리케이션의 모든 계층에서 사용할 수 있는 `Validator` 를 제공한다.

데이터 바인딩은 사용자의 입력을 응용프로그램의 도매인 모델에 연결하는데 유용하다. `Validator` 과 `DataBinder` 는 `validation` 패키지에 정의 되어 있고 웹 계층에서 주로 사용되지만 제한되지 않았다.

`BeanWrapper` 는 Spring Framwork의 기본 개념이며 많은 곳에서 사용된다. 그러나 `BeanWrapper` 를 직접사용하지 않는다. `BeanWrapper` 는 데이터 바인딩시 가장 많이 사용된다.

`DataBinder`과 `BeanWrapper` 모두 `PropertyEditorSupport` 구현체를 사용하여 속성 값을 해석하고 포맷팅한다. Spring 3 부터   `core.convert`  패키지일반적인 타입 변환 기능과 UI 필드 값을 형식화하기위한 상위 레벨 "포맷" 패키지를 도입했다. 이러한 패키지를 `PropertyEditorSupport`구현에 대한 더 간단한 대안으로 사용할 수 있다.

