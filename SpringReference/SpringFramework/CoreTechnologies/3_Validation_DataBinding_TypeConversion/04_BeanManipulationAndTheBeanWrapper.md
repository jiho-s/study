## Bean Manipulation and the `BeanWrapper`

`org.springframework.beans` 패키지는 JavaBeans 표준을 준수한다. JavaBean은 다음 규칙을 따르는 클래스를 말하는데, 매개변수가 없는 디폴트 생성자를 가지고, 예를 들어 필드가 `bingoMadness` 인 경우 `setBingoMadness(..)` 이름의 setter를 메소드로 가지고 `getBingoMadness()` 를 getter로 가진다.

Bean 패키지에서 매우 중요한 클래스 중 하나는 `BeanWrapper`인터페이스와 해당 구현 ( `BeanWrapperImpl`)이다.