## Spring IoC Container와 Beans

### IoC(Inversion of Control)

DI(dependency injection)이라고도 하며 객체가 의존 객체를 만들어서 사용하는 것이 아니라, 생성자, 팩토리 메서드, setter를 통해 객체가  의존성을 주입 받는 것

### BeanFactory

- 빈 설정 소스로부터 빈 정의를 읽음
- 빈을 구성 또는 제공, 관리
- 빈 간의 의존관계 설정, 빈 주입

### ApplicationContext

BeanFactory를 상속받음 Spring에서 실제 사용, 아래의 기능 추가

- Spring AOP 기능과 쉽게 통합
- Message resource handling(메세지 다국화)
- 이벤트 발행