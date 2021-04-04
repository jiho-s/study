## Initialization

클래스 또는 인터페이스의 초기화는 클래스 또는 인터페이스의 초기화 메소드 실행으로 초기화 할수 있다.

클래스 또는 인터페이스는 아래의 방법으로 초기화 될수 있다.

- C를 참조 하는 Java Virtual Machine 명령어 *new* , *getstatic* , *putstatic* 또는 *invokestatic* 중 하나의 실행 .
- 메소드 핸들 resolution의 결과가  2 (`REF_getStatic`), 4 (`REF_putStatic`), 6 (`REF_invokeStatic`), or 8 (`REF_newInvokeSpecial`)인 `java.lang.invoke.MethodHandle` 인스턴스의 첫번째 실행.
- `Class` , `java.lang.reflect` 같은 클래스 라이브러리의 reflective 메소드 호출시
- C가 클래스 일 떄, C의 서브클래스중 하나를 초기화 할때
- C가 인터페이스이고, 추상 메소드, static 메소드가 아닌 메소드를 선언하면, 인터페이스 C를 직접적으로 또는 직접적이 아니게 구현한 클래스를 생성한다.
- Java Virtual Machine 시작시 초기 클래스 또는 인터페이스로 지정된경우

초기화 전에 클래스 또는 인터페이스를 연결해야합니다. 즉, 검증, 준비 및 선택적으로 resolve해야 한다.

