## Reactive 프로그래밍 소개

Reactor는 Reactive 프로그래밍 패러디임의 구현이며 다음과 같이 요약할 수 있다.

> 리액티브 프로그래밍은 데이터 스트림과 변화의 전달을 다루는 비동기 프로그래밍 패러이임이다. 즉 사용된 프로그래밍 언어를 통해 쉽게 정적(배열) 또는 동적(이벤트 발행) 데이터 스트림을 표현할 수 있음을 의미한다.

리액티브 프로그래밍 페러다임은 종종 Observer 디자인 패턴의 확장으로 객체지향 언어로 제공된다. 또한 리액티브 스트림 패턴과 Iterator 디자인 패턴 모두 **Iterable-Iterator** 을 가지고 있으므로 이 둘을 비교할 수 있다. 이 둘의 가장 큰 차이점은 **Iterator** 은 pull 기반이지만 리액티브 스트림은 push기반이다.

값에 접근하는 메소드는 전적으로 **Iterable** 의 책임임에도, Iterator를 사용하는 것은 명령형 프로그래밍 패턴이다. 실제로 시퀀스 의 항목에서 `next()` 액세스 할시기를 선택하는 것은 개발자다. 리액티브 스트림에서 위의 쌍에 해당하는 것은 **Publisher-Subscriber** 이다.  **Publisher-Subscriber** 에서는 **Publisher** 가 새로 사용할 수 있는 값이 들어오면 **Subscriber** 에게 알려주는 것이 되며, 이러한 push 기반이 리액티브의 핵심이다.

값을 push하는 것 의외에도 오류 처리와 완료도 잘 정의 된 방법으로 다룰 수 있다. **Publisher** 는 새 값을 **Subscriber** 에게(`onNext` 를 호출하여) 푸시 할 수 있고 또한 오류(`onError` 를 호출) 그리고 완료(`onComplete` 를 호출) 를 보낼 수 있다. 이것은 다음과 같이 요약할 수 있다.

```
onNext x 0..N [onError | onComplete]
```

이 패턴은 값이 없가나, 하나만 있거나 또는 n개의 값을 가진 경우를 다룰 수 있다.

먼저 비동기 리액티브 라이브러리가 필요한 이유를 살펴보자

### Blocking은 낭비 일 수 있다.

최신 애플리케이션은 엄청난 수의 사용자에게 동시에 도달 될 수 있으며, 최신 소프트웨어의   perfomance는 여전히 주요 관심사이다.

프로그램의 성능을 향상시킬수 있는 방법에는 크게 두가지가 있다.

- 더 많은 스레드와 하드웨어 리소스를 사용하도록 병렬화 한다.
- 현재 리소스를 사용하는 방식을 더 효율성 있게 한다.

일반적으로 Java 개발자는 Blocking 코드를 사용하여 프로그램을 작성한다. 이 방법은 성능 병목 현상이 발생할 때까지는 괜찮다. 그리고 유사한 Blocking 코드를  실행하는 추가 스레드를 도입할 때 문제가 발생할 수있다. 리소스 활용의 확장은 동시성 문제를 유발할 수 있다.

Blocking의 단점은 또한 리소스를 낭비한다는 것이다. 프로그램에 약간의 대기시간(데이터베이스 요청 또는 네트워크 호출과 같은 I/O)이 포함되는 즉시 스레드가 유휴 상테로 데이터를 기다리고 있기 때문에 리소스가 낭비된다.

따라서 병렬화 접근방식은 완벽한 해결책이 아니다.

### 비동기로 해결?

더 많은 효율성을 추구하는 방식은 리소스 낭비 문제에 대한 해결책이 될 수 있다. asynchronous, non-blockin 방식의 코드를 작성하면 실행이 동일한 기본 리소스를 사용하는 다른 작업으로 전환되고 나중에 비동기 처리가 완료되면 현재 프로세스로 돌아온다.

자바에서는 두 가지 비동기 프로그래밍 모델을 제공한다.

- **Callbacks** : asynchronous 메서드에는 반환 값이 없지만 `callback`결과를 사용할 수있을 때 호출 되는 추가 매개 변수 (람다 또는 익명 클래스)를 사용합니다.
- **Futures** : asynchronous 메소드는 즉시`Future<T>` 를 반환한다. 비동기 프로세스는 `T`값을 계산 하지만 `Future`객체 는 값에 대한 액세스를 래핑한다. 값은 즉시 사용할 수 없으며 값을 사용할 수있을 때까지 개체를 폴링 할 수 있다.

두 접근 방식 모두 제한이 있다.

콜백은 함께 작성하기가 어렵고 빠르게 읽고 유지 관리하기 어려운 코드로 이어진다.

`CompletableFuture` 에 의해 Java 8에서 개선되었음에도 불구하고, `Future` 객체는 콜백보다 약간 났지만 잘 사용되지 않는다.

또한 `Future` 은 여러가지 다른 문제가 있다.

- `Future` 에서 `get()` 같은 함수를 부를때 다른 blocking 상황이 생길 수 있따.
- lazy 연산을 지원하지 않는다.
- 다중 값과 오류 처리에 대한 지원이 부족하다

### 명령형에서 리액티브 프로그래밍으로

**Reactor**와 같은 리액티브 라이브러리는 JVM에서 고전적인 비동기식 접근 방식의 이러한 단점을 해결하는 동시에 몇 가지 추가적인 측면에 초점을 맞추는 것을 목표로 한다

- 구성가능성 및 가독성
- 풍부한 연산자로 조작되는 흐름으로서의 데이터
- 구독하기 전에는 아무 일도 일어나지 않는다.
- 역압력 또는 *consumer* 가 *producer*에게 배출 속도가 너무 빠르다는 신호를 보내는 능력
- 동시성에 구에 받지 않은 높은 레벨 높은 가치의 추상화

#### 구성가능성과 가독성

구성가능성이란 이전 작업의 결과를 사용하여 후속 작업에 입력을 제공하는 여러 비동기 작업을 조정하는 기능을 의미한다. 또는 포크 조인 스타일로 여러 작업을 실행할 수 있다. 또한 더 높은 레벨의 시스템에서 비동기 작업을 개별 구성 요소로 재사용할 수 있다.

작업을 오케스트레이트 하는 능력은 코드의 가독성 및 유지관리성과 밀접한 관련이 있다. 비동기 프로세스의 계층과 복잡성이 증가함에 따라 코드를 작성하고 읽는 것이 점점 어려워 진다.

**Reactor** 는 코드가 추상 프로세스의 구성을 반영하여  풍부한 구성 옵션을 제공한다.

#### 조립 라인 비유

반응 형 애플리케이션에서 처리 된 데이터는 조립 라인을 통해 이동하는 것으로 생각할 수 있다. Reactor는 컨베이어 벨트이자 워크 스테이션이다. 원료는 소스 (원본 `Publisher`) 에서 쏟아져 소비자 (또는 `Subscriber`) 에게 전달 될 준비가 된 완제품으로 끝난다.

원료는 다양한 변형 및 기타 중간 단계를 거치거나 중간 조각을 함께 모으는 더 큰 조립 라인의 일부가 될 수 있다. 한 지점에서 결함이나 막힘이 발생하면 (아마도 제품을 박싱하는 데 시간이 너무 오래 걸릴 수 있음) 문제가 발생한 워크 스테이션은 업스트림 신호를 보내 원료의 흐름을 제한 할 수 있다.

#### 연산자

**Reactor**에서 연산자는 조립라인 비유에서 워크 스테이션이다. 각 연산자는 `Publisher`에 동작을 추가 하고 이전 단계의 `Publisher`를 새 인스턴스로 래핑한다 . 따라서 전체 체인이 연결되어 데이터가 첫 번째에서 시작 `Publisher`되어 각 링크에 의해 변환 된 체인 아래로 이동한다.  `Subscriber`가 프로세스를 완료합니다. 곧 볼 수 있듯이 `Subscriber`가 `Publisher`를 구독 할 때까지 아무 일도 일어나지 않는다 .

Reactive Streams 명세는 연산자를 전혀 지정하지 않지만 **Reactor**와 같은 반응 라이브러리의 가장 좋은 추가 값 중 하나는 그들이 제공하는 풍부한 연산자이다. 단순한 변환 및 필터링에서 복잡한 오케스트레이션 및 오류 처리에 이르기까지 많은 영역이 포함된다.

#### `subscribe()` 될 때까지 아무 일도 일어나지 않는다

**Reactor**에서 `Publisher`체인을 작성할 때 기본적으로 데이터가 펌핑을 시작하지 않는다. 대신 비동기 프로세스에 대한 추상적 인 설명을 생성한다 (재사용과 구성에 도움이 될 수 있음).

**subscribing** 을 시작해서, `Publisher` 에서 `Subscriber`로 전체 체인의 데이터 흐름을 만든다. 이는 내부적으로 `Subscriber` 에서 업스트림으로 전달되는 하나의 `request` 신호로 달성된다.

#### 역압력

업스트림으로 신호 전달은 역압력을 구현하는데도 사용된다. 조립 라인 비유에서 워크스테이션이 업스트림 워크 스테이션보다 더 느리게 처리 될 때 라인으로 전송되는 피드백 신호로 볼 수 있다.

Reactive Streams 사양에 정의 된 실제 메커니즘은 비유에 매우 가깝다. 구독자는 *unbounded* 모드 에서 작업 하고 소스가 모든 데이터를 가장 빠른 속도로 푸시하도록하거나 `request`메커니즘을 사용 하여 소스에 `n` 개의 요소를 처리할 준비되었음을 알릴 수 있다.

중개 연산자는 전송중인 요청을 변경할 수도 있다. `buffer` 요소를 10 개 단위로 그룹화 하는 연산자등이 가능하다 . 구독자가 하나의 버퍼를 요청하면 소스가 10 개의 요소를 생성하는 것이 허용된다. 일부 연산자는  요소를 생성하는 데 너무 많은 비용이 들지 않는 경우에 `request(1)`을 반복하는 것을 피하는것에  도움이 되는 **프리 페치** 전략도 구현한다.

이는 푸시 모델을 **푸시-풀 하이브리드** 로 변환하여 다운 스트림이 쉽게 사용할 수있는 경우 업스트림에서 n 개의 요소를 가져올 수 있습니다. 그러나 요소가 준비되지 않은 경우 생성 될 때마다 업스트림에 의해 푸시됩니다.

#### Hot vs Cold

Rx 리액티브 라이브러리 제품군은 두 가지의 반응 시퀀스 인 **Hot** 과 **Cold** 를 구분 한다. 이 구분은 주로 리액티브 스트림이 subscriber에가 반응하는 방식과 관련이 있다.

- **Cold** 시퀀스는 각각의 `Subscriber` 에 대해 데이터의 소스를 포함하여 새롭개 만든다. 예를 들어 소스가 HTTP 호출을 랩핑하면 각 subscription에 대해 새 HTTP요청이 만들어 진다.
- **Hot** 시퀀스는 각각의 `Subscriber` 에 대해 처음부터 다시 시작하지 않는다. subscriber는 구독한 이후의 신호만 받을 수 있다. 그러나 일부 Hot 리액티브 스트림은 방출한 신호를 전체 또는 부분적으로 캐시하거나 다시 보낼수 있다. 또한 Hot 시퀀스는 `subscriber` 가 구독하지 않는 경우에도 방출할 수 있다.