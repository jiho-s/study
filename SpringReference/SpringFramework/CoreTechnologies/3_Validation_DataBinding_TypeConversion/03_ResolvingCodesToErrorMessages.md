## Resolving Codes to Error Messages

이 섹션에서는 유효성 검사 오류에 해당하는 메시지 출력에 대해 살펴본다. `MessageSource` 를 사용하여 오류 메시지를 출력하려면, 필드를 거부할 때 입력한 오류 코드를 사용하여 출력할 수 있다. `Errors` 인터페이스에서 `rejectValue` 또는 `reject` 메소드를 호출하면 기본적인 구현은 전달한 코드를 등록할뿐 아니라 여러 추가적인 코드도 등록한다. `MessageCodesResolver` 는 `Errors` 인터페이스가 등록하는 오류코드를 결정한다. 기본적으로 `DefaultMessageCodesResolver`가 사용되는데, 이는 사용자가 제공 한 코드로 메세지를 등록 할뿐만 아니라 reject 메소드에 전달한 필드 이름을 포함하는 메세지도 등록한다.

`rejectValue("age", "too.darn.old")` 를 사용하여 필드를 거부하면, `too.darn.old`코드와 따로 `too.darn.old.age` and `too.darn.old.age.int` 도 등록한다.

