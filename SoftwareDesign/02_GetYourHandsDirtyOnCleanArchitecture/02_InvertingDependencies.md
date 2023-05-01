## 02 의존성 역전하기

### 단일 책임 원칙

컴포넌트를 변경하는 이유는 오직 하나 뿐이여야 한다

### 부수효과에 관한 이야기

??? 뭔 말임

### 의존성 역전 원칙

계층형 아키텍처에서 단일 책임 원칙을 고수준에서 적용할 때 상위 계층들이 하위 계층들에 비해 변경할 이유가 더 많다

의존성을 제거하는 방법 의존성 역전 법칙

도메인 계층에 리포지토리에 대한 인터페이스를 만들고, 실제 리포지토리는 영속성 계층에서 구현하게 하는 것이다.

>  영속성에 `ORM-Managed` 는 어떤 것을 의미 여전히 도메인의 엔티티에 즉시로딩, 지연로딩, 등등 영속성 계층과 관련된 작업을 하는것이 아닌가?

### 클린 아키텍처

설계가 비즈니스 규칙의 테스트를 용이하게 하고, 비즈니스 규칙은 프레임 워크, 데이터베이스 그 밖의 외부 애플리케이션이나 인터페이스로부터 독립적일 수 있다고 얘기함

이는 도메인 코드가 바깥으로 향하는 어떤 의존성도 없어야 함을 의미한다

코어 주변으로 비즈니스 규칙을 지원하는 애플리케이션의 다른 모든 컴포넌트들을 확인할 수 있다. 여기서 지원은 영속성을 제공하거나 UI 를 제공하는 것 등을 의미한다.

**도메인 계층이 영속성이나 UI 같은 외부 계층과 철저하게 분리돼야 하므로 애플리케이션의 엔티티에 대한 모델을 각 계층에서 유지 보수 해야 한다**

ORM 을 사용한다고 할때 도메인 계층은 영속성 계층을 모르기 때문에 도메인 계층에서 사용한 엔티티 클래스를 영속성 계층에서 함께 사용할 수 없고 두 계층에서 각각 엔티티를 만들어야한다. 즉 도메인 계층과 영속성 계층이 데이터를 주고받을 때, 두 엔티티를 서로 변환해야한다.

### 육각형 아키텍처(헥사고날 아키텍처)

육각형 안에는 도메인 엔티티와 이와 상호작용하는 유스케이스가 있다. 모든 의존성은 코어를 향한다.

왼쪽에 있는 어댑터들은 애플리케이션을 주도하는 어댑터들이다. 반면 오른쪽에 있는 어댑터들은 애플리케이션에 의해 주도되는 어댑터들이다.

애플리케이션 코어와 어댑터들 간의 통신을 하려면 코어가 포트를 제공해야함

**주도하는 어댑터는 그러한 포트가 코어에 있는 유스케이스클래스들에 의해 구현되고 호출되는 인터페이스가 될 것이고, 주도되는 어댑터에게는 그러한 포트가 어댑터에 의해 구현되고 코어에 의해 호출되는 인터페이스가 될것이다**

### 유지보수 가능한 소프트웨어를 만드는 데 어떻게 도움이 될까?

의존성을 역전시켜 도메인 코드가 다른 바깥쪽 코드에 의존하지 않게 함으로써 영속성과 UI에 특화된 모든 문제로부터 도메인 로직의 결합을 제거하고 코드를 변경할 이유으이 수를 줄일 수 있다.