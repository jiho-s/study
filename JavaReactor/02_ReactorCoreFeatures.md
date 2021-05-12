## Reactor Core 특징

**Reactor** 프로젝트의 주요 아티팩트는 **reactor-core** 로 리액티브 스트림 명세에 초점을 맞추고 Java 8을 대상으로 하는 리액티브 라이브러리이다.

**Reactor**는 `Publisher`를 구현하면서 `Flux`, `Mono` 등 다양한 연산자 또한 제공하는 복합적인 리액티브 타입을 가지고 있다. `Flux`  객체는 0...N개의 아이템을 가진 리액티브 시퀀스를 나타내며, `Mone` 객체는 하나 또는 없음(0...1)의 결과를 나타낸다.

이 구별은 약간의 정보를 비동기 처리의 대략적인 양을 타입에서 얻게 해준다. 예를 들어 HTTP 요청은 하나의 응답 만 생성하므로 `count`작업에 별 의미가 없다. 따라서 이러한 HTTP 호출의 결과를 0개 아이템 또는 1개의 이이템과 관련된 연산자를 제공하는  `Mono<HttpResponse>` 로 표현하는 것이 `Flux<HTTPResponse>` 로 표현하는 것보다 더 합리적이다.

최대 양을 변경하는 연산자도 관련 타입으로 바꾼다. 예를 들어 `count` 연산자는 `Flux` 에 존재하지만 `Mono` 를 반환한다.

### `Flux` 0-N 아이템의 비동기 시퀀스

`Flux` 는 0에서 N까지의 방출 된 항목의 비동기 시퀀스와 완료 신호 또는 오류 신호에 의해 종료되는 것을 표현한 `Publisher<T>` 표준이다. 리액티브 스트림 명세와 같이, 이러한 3가지 신호는 다운 스트림 Subscriber의 `onNext`, `onComplete`, `onErrorr` 메소드 호출로 전달한다.

가능한 신호의 범위가 크기 때문에, `Flux`  는 범용 리액티브 타입이다. 종료되는 이벤트도 포함해서 모든 이벤트는 선택 사항이다. `onNext` 없이 `onComplete`  이벤트를 사용하여 비어 있는 유한한 시퀀스를 나타내거나, `onComplete` 도 없애 무한한 빈 시퀀스를 나타낼 수 있다. 마찬가지로 무한한 시퀀스가 비어있지 않을 수 도 있다. 예를 들어 `Flux.interval(Duration)` 은 무한한 clock에서 규칙적인 tick을 방출하는 `Flux<Long>` 을 제공한다.

### `Mono` 0-1의 비동기

`Mono<T>` 는 `onNext` 신호를 통해 거의 하나의 아이템을 방출하고 `onComplete` 신호를 통해 종료하거나 `onError` 신호를 방출하는 특수한 형태의 `Publisher<T>` 이다.

대부분의 `Mono` 구현은 `Subscriber` 를 통해 `onNext` 가 호출된 후에 `onComplete` 가 호출될것을 예상한다.

`Mono.never()` 은 잘 사용되지 않는다. 신호를 방출하지 안흐므로 기술적으로 금지되지는 않지만 테스트하는 경우를 제외하고는 사용되지 않는다. 반면 `onNext` 와 `onError` 을 같이 사용하는 것은 명시적으로 금지된다.

`Mono` 에 사용할 수 있는 연산자는 `Flux` 가 제공하는 것에 일부만 제공한다. 또한 몇몇의 연산자(`Mono` 를 다른 `Publisher`  와 조합하는 경우)는 `Flux` 로 전환해 준다. 예를 들어 `Mono#concatWith(Publisher)` 는  `Flux` 를 반환하고  `Mono#then(Mono)` 는 다른 `Mono` 를 반환한다.

`Mono` 를 사용하여 리턴 값이 없는 비동기 프로세스도 표현할 수 있다. 이경우 `Mono<Void>` 를 선언하면 된다.

### `Flux` 또는 `Mono` 를 생성하고 구독하는 간단한 방법

`Flux` ,  `Mono` 를 생성하는 가장 쉬운 방법은 각각의 클래스에 있는 수 많은 팩토리 메소드 중 하난를 사용하는 것이다.

예를 들어 `String`의 시퀀스를 만들려면 다음과 같이 열거하거나 컬렉션에 넣고 `Flux`를 만들 수 있다.

```java
Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

List<String> iterable = Arrays.asList("foo", "bar", "foobar");

Flux<String> seq2 = Flux.fromIterable(iterable);
```

```java
Mono<String> noData = Mono.empty(); 

Mono<String> data = Mono.just("foo");

Flux<Integer> numbersFromFiveToSeven = Flux.range(5, 3); 
```

구독을 하는 경우 `Flux` 와 `Mono` 는 Java8의 람다를 사용한다.다음 메서드 시그니처에 표시된대로 다양한 콜백 조합에 대해 람다를 사용 하는 다양한 `.subscribe()` 변형을 선택할 수 있다.

```java
subscribe(); 

subscribe(Consumer<? super T> consumer); 

subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer); 

subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer,
          Runnable completeConsumer); 

subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer,
          Runnable completeConsumer,
          Consumer<? super Subscription> subscriptionConsumer); 
```

#### `subscribe` 의 예

다음은 인자가 없는 기본 메소드의 예시이다.

```java
Flux<Integer> ints = Flux.range(1, 3); 
ints.subscribe(); 
```

`subscribe`메서드 의 다음 예제 는 값을 표시하는 메소드를 보여준다.

```java
Flux<Integer> ints = Flux.range(1, 3); 
ints.subscribe(i -> System.out.println(i));
```

다음은 의도적으로 예외를 발생시키고 이를 처리하는 예제이다

```
Flux<Integer> ints = Flux.range(1, 4) 
      .map(i -> { 
        if (i <= 3) return i; 
        throw new RuntimeException("Got to 4"); 
      });
ints.subscribe(i -> System.out.println(i), 
      error -> System.err.println("Error: " + error));
```

다음은 오류 처리와 완료 처리가 모두 포함된다.

```java
Flux<Integer> ints = Flux.range(1, 4); 
ints.subscribe(i -> System.out.println(i),
    error -> System.err.println("Error " + error),
    () -> System.out.println("Done")); 
```