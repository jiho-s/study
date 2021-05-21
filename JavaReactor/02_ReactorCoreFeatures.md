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

#### `Disposable` 로 `subscribe()` 취소

이러한 모든 람다 기반의 ` subscirbe()` 에는 `Disposable` 을 반환한다. `Disposable` 인터페이스는 `dispose()` 메소드를 호출하여 구독을 취소할 수 있다.

`Flux` 와 `Mono` 에서 취소는 소시가 아이템을 생성하는 것을 멈추어야 한다는 신호이다. 그러나 즉시 동작하는 것을 보장하지 않는다. 일부 소스는 취소 명령을 받기 전에 아이템을 빠르게 생성하여 완료할 수 있다.

`Disposables` 클래스에서 `Disposable` 과 관련한 여러 도구를 제공해 준다. 이 중, `Disposables.swap()` 은 존재하는 `Disposable` 을 원자적으로 취소하거나 교체할 수 있게 해주는 `Disposable` 랩퍼를 만들어준다. 예를 들어, UI 시나리오에서 요청을 취소시카고 이를 사용자가 버튼을 클릭할 때마다 새로운 요청으로 바꿀때 유용할 수 있다. 랩퍼를 취소하면 랩처 자체가 닫힌다. 이렇게하면 현재 존재하는 값과 바뀔 값도 모두 없어진다.

다른 도구는 `Disposables.composite(...)` 가 있다. 이를 사용하면 여러개의 	`Disposable` 을 모을 수 있다. 예를들어 서비스 호출과 관련된 여러개의 진행중인 요청이 있을 때 이것들을 한꺼번에 삭제할수 있다. `composite` 에 `dispose()` 메소드가 호출되면 다른 `Disposable` 을 추가하려는 모든 시도는 그것을 사라지게 한다.

#### `BaseSubscriber` 를 사용하여 람다가 아닌 방법으로 사용

랃다를 이용해 구성하는 것보다 일반적인 방식으로 `Subscirber` 를 추가하는 `subscribe` 메소드가 있다. 이런 `Subscriber` 작성을 돕기위해, `BaseSubscriber` 클래스가 제공된다.

> `BaseSubscriber` 인스턴스는 일회용이다. 즉 `BaseSubscriber` 가 두번째 `Publisher` 를 구독하면 ,첫번째 구독을 취소한다.

```java
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

public class SampleSubscriber<T> extends BaseSubscriber<T> {
    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("Subscribed");
        request(1);
    }

    @Override
    protected void hookOnNext(T value) {
        System.out.println(value);
        request(1);
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }
}
```

```java
SampleSubscriber<Integer> ss = new SampleSubscriber<>();
Flux<Integer> ints = Flux.range(1, 4).map(i -> {
    if (i == 4) {
        throw new RuntimeException("it is 4");
    }
    return i;
});
ints.subscribe(ss);
```

```shell
Subscribed
1
2
3
it is 4
```

`SampleSubscriber` 는 `BaseSubscriber` 를 상속했으며, `BaseSubscriber` 는 `Reactor` 에서 사용자 정의 `Subscribers`를 만드는데 권장되는 추상클래스이다. 이 클래스는 `subscriber`의 동작을 설정하기 위해 오버라이딩 할 수 있는 후크를 제공한다. 기본 값으로 bound가 없는 요청을 시작하게하고, `subscribe()` 처럼 행동한다. `BaseSubscriber` 는 커스텀 request의 양을 설정하고 싶을때 유용하다.

request의 양을 설정할 때 최소한의 방법은 `hookOnSubscribe` 과 `hookOnNext`를 구현하는 것이다. 

`BaseSubscriber` 또한 unbounded 모드로 전환하기 위한 `requestUnbounded` 메소드와 `cancel()` 메소드 또한 제공한다.

#### 역압력과 요청 재구성 방법

`Reactor`에서 역압력을 구현할때 consumer의 압력이 소스로 다시 전파되는 방식은 `request` 를 업스트림 연산자에게 보내는 것이다. 현재 요청의 합계는 demand 또는 pending request이라고 한다. Demand는 `Long.MAX_VALUE` 로 제한되어 있으며, 이는 unbounded 요청을 나타낸다.

첫번째 요청은 subscription시 최종 subscriber로 부터 나오지만 가장 직접적인 구독 방법은 `Long.MAX_VALUE` 로 unbounded 요청을 통해 직접적으로 트리거 된다.

```java
Flux.range(1, 10)
    .doOnRequest(r -> System.out.println("request of " + r))
    .subscribe(new BaseSubscriber<Integer>() {

      @Override
      public void hookOnSubscribe(Subscription subscription) {
        request(1);
      }

      @Override
      public void hookOnNext(Integer integer) {
        System.out.println("Cancelling after having received " + integer);
        cancel();
      }
    });
```

```java
equest of 1
Cancelling after having received 1
```

##### 다운스트림에서 Demand를 변경하는 연산자

subscribe 레벨에서 표현된 Demand는 업스트림 체인의 각각의 연산자에 의해 재설정 될 수 있다. 대표적인 예로는 `buffer(N)` 연산자이다. `request(2)` 를 받으면, Demand가 두개의 가득찬 버퍼로 해석된다. 결과적으로 버퍼는 가득차기 위해 `N` 개의 요소가 필요하므로 `buffer` 연산자는 `request` 를 `2 * N` 으로 재구성 한다.

또한 몇개의 연산자는 `prefetch` 라 불리는 `int` 의 입력을 받는 경우가 있다. 이는 다운스트림 요청을 수정하는 다른 종류의 연산자이다. 일반적으로 내부 시퀀스를 다루는 연산자로, `flatMap` 처럼 들어오는 각 요소에서 `Publisher`를 가져온다.

`Prefetch` 는 이러한 내부 시퀀스에 대한 초기 요청을 설정하는 방법이다. 기본적으로 `32`로 설정된다.

이러한 연산자는 보통 보충 최적화도 같이 구현한다. 연산자가 `Prefetch` 요청의 75%로가 차 있는 것을 확인하면, 업스트림에 75%를 다시 요청한다.

마지막으로 `limitRate` 와 `limitRequest`를 사용하여 요청을 조정할 수 있다.

`limitRate(N)` 은 다운 스트림 요청을 분할하여 더 작은 배치로 업스트림에 전파할 수 있다. 예를 들어 `limitRate(10)` 으로 만든 `request(100)` 는 결과적으로 `request(10)` 을 10번 요청하게 된다. 또한 `limitRate` 는 앞에서 설명한 보충 최적화도 구현되어 있다.

연산자에는 보충량을 설정할 수 도 있다(`limitRate(highTide, lowTide)`).

`limitRequest(N)` 은 반면에 다운스트림 요청을 최대 총 Demand로 제한한다. N까지 요청을 추가한다. 하나의 요청으로 N에 대한 총 Demand가 넘치지 않으면 해당 요청은 업스트림으로 전바된다. 해당하는 양이 소스에서 방출된 후 `limitRequest` 는 시퀀스 완료를 하고 다운스트림에 `onComplete` 신호를 보낸다. 그리고 소스를 취소한다.

### 프로그래밍적으로 시퀀스 만들기

관련 이벤트(`onNext`, `onError`, `onComplete`)를 프로그래밍 방식으로 정의하여 `Flux` 또는 `Mono` 생성을 다룬다. 이러한 모든 메소드는 **sink**라고 부르는 이벤트를 트리거 하기 위해 API를 노출한다.

#### `generate` 로 synchronous

프로그래밍 방식으로 생성하는 가장 간단한 형태는 `generate` 를 이용해 `Flux`를 생성하는 것이다.

`generate`는 **synchronous**, **one-by** 방출을 위한 것이고, **sink**는 `SynchronousSink` 이고 `next()` 메소드는 콜백호출당 최대 한번만 호출 될 수 있다. 그 다음 추가적으로 `error()` 나 `complete()` 를 선택적으로 호출할 수 있다.

가장 유용한 예는 다음에 내 보낼 항목을 결정하기 위해 sink 사용량을 참조하기 위해 sink 상태를 가지고 있는 것이다. generator 함수는 `BitFunction<S, SynchronousSink<T>, S>` 의 형태이고 `<s>`는 상태 객체의 타입이고 `Supplier<S>` 로 처음 상태를 제공해야한다. generator 함수는 각각의 라운드에서 새로운 상태를 리턴한다.

```java
Flux<String> flux = Flux.generate(
    () -> 0, 
    (state, sink) -> {
      sink.next("3 x " + state + " = " + 3*state); 
      if (state == 10) sink.complete(); 
      return state + 1; 
    });
```

mutable한 `<S>` 도 사용할 수 있다. 다음 예시는 `AtomicLong`을 사용하여 수정할 수 있는 스태이트를 표현한 예시이다.

```java
Flux<String> flux = Flux.generate(
    AtomicLong::new, 
    (state, sink) -> {
      long i = state.getAndIncrement(); 
      sink.next("3 x " + i + " = " + 3*i);
      if (i == 10) sink.complete();
      return state; 
    });
```

다음은 `Consumer`를 포함한 `generate`예시이다.

```java
Flux<String> flux = Flux.generate(
    AtomicLong::new,
      (state, sink) -> { 
      long i = state.getAndIncrement(); 
      sink.next("3 x " + i + " = " + 3*i);
      if (i == 10) sink.complete();
      return state; 
    }, (state) -> System.out.println("state: " + state)); 
```

프로새스가 끝날 때 처리해야하는 리소스가 포함된 상태를 가진 경우(데이터베이스), `Consumer` 람다는 연결을 닫는 등 프로세스가 끝날 때 수행해야 할 작업을 처리할 수 있다.

#### `create` Asynchronous, 멀티-스레드

`create` 는 더 발전된 형태의 `Flux` 생성방법으로 멀티스레드로 라운드당 여러개를 방출하는 경우에 적합하다.

`create` 는 `FluxSink`가 `next`, `error`, `complete` 메소드와 함깨 노출된다. `generate`와 달리 상태 기반 변형히 없다. 대신에 콜백에서 멀티 스레드 이벤트를 트리거 시킬 수 있다.

리스너 기반 API를 연결 한다고 하면 다음과 같이 구현할 수 있다.

```java
interface MyEventListener<T> {
    void onDataChunk(List<T> chunk);
    void processComplete();
}
```

```java
Flux<String> bridge = Flux.create(sink -> {
    myEventProcessor.register( 
      new MyEventListener<String>() { 

        public void onDataChunk(List<String> chunk) {
          for(String s : chunk) {
            sink.next(s); 
          }
        }

        public void processComplete() {
            sink.complete(); 
        }
    });
});
```

`create` 는 비동기 API로 연동할 수 있으며, 역압력을 설정할 수 있다.  `OverflowStrategy`를 사용하여 역압력 방식을 재 정의 할 수 있다.

- `IGNORE` 다운스트림의 역압력 요청을 완전히 무시한다. 이는 다운스트림의 큐가 가득 찰 시 `IllegalStateException` 을 발생시킬 수 있다.
- `ERROR` 다운스트림이 따라 올수 없으면 `IllegalStateException`를 발생시킨다.
- `Drop` 다운 스트림이 수신 할 준비가 되지 않은 경우 신호를 삭제한다.
- `LATEST` 다운 스트림이 업스트림에서 최신 신호만 가져 오도록한다.
- `BUFFER` 기본값으로 다운 스트림이 따라 잡을 수 없는 경우 모든 신호를 버퍼링한다.

#### `push` 비동기 단일 스레드

`generate` 와 `create` 사이의 중간 역할로 하나의 공급자로 부터 이벤트를 처리하기에 적합하다. 비동기일 수 있고 `create` 가 지원하는 오버플로우 전략으로 역압력을 관리할 수 있다는 점에서 `create`와 유사하다. 그러나 한번에 단 하나의 스레드만 `next` , `complete`, `error` 을 수행한다.

```java
Flux<String> bridge = Flux.push(sink -> {
    myEventProcessor.register(
      new SingleThreadEventListener<String>() { 

        public void onDataChunk(List<String> chunk) {
          for(String s : chunk) {
            sink.next(s); 
          }
        }

        public void processComplete() {
            sink.complete(); 
        }

        public void processError(Throwable e) {
            sink.error(e); 
        }
    });
});
```

##### 하이브리드 push/pull 모델

대부분의 `create` 같은 대부분의 Reactor 연산자는 하이브리드 push/poll 모델을 따른다. 이는 대부분의 처리가 비동기식 임에도 불구하고 작은 pull 구성 요소인 요청이 있다는 것이다.

소비자는 처음 요청하지 않으면 아무것도 보내지 않는 다는 의미에서는, 소스에서 데이터를 가져온다. 소스는 소비자가 사용할 수 있게 될때마다 소비자에게 데이터를 보낸지만, 요청 된 양의 범위에서만 보낸다.

`push`와 `create`모두 `onRequest`를 설정할 수 있게 하여 request  앵을 설정할수 있으며, 보류중인 요청이 있는 경우에만 데이터가 싱크를 통하여 들어갈 수 있도록 보장한다.

```java
Flux<String> bridge = Flux.create(sink -> {
    myMessageProcessor.register(
      new MyMessageListener<String>() {

        public void onMessage(List<String> messages) {
          for(String s : messages) {
            sink.next(s); 
          }
        }
    });
    sink.onRequest(n -> {
        List<String> messages = myMessageProcessor.getHistory(n); 
        for(String s : messages) {
           sink.next(s); 
        }
    });
});

```

##### `push()` , `create()` 후 정리

`onDispose`, `onCancel` 두 콜백은 취소 또는 종료에 대한 정리 작업을 수행한다. `onDispose`는 `Flux`가 `complete`, `error`, `cancel` 시 정리작업을 수행할 수 있다. `onCancle` 은 `onDispose` 를 사용하여 정리하기 전에 취소 관련 작업을 수행하는데 사용할 수 있다.

#### Handle

`handle` 메소드는 조금 다르다. 인스턴스 메소드로, 기존에 존재하는 것에 체인으로만 사용할 수 있다. `Mono`와 `Flux` 모두 가능하다.

`SynchronousSink`를 사용하고 one-by-one 방출 만을 허용한다는 점에서 `generate`와 유사하다. 그러나, `handle`은 각각의 소스 요소에서 임의의 값을 생성하는 대 사용할 수 있다. 이러한 방식으로 `map`과 `filter`의 조합으로 사용된다.

```java
Flux<R> handle(BiConsumer<T, SynchronousSink<R>>);
```

예를 들어, `map`을 사용하고 싶지만 때때로 `null`을 반환하는 경우 `null`을 반환하지 않기 위해 사용할 수 있다.

```java
public String alphabet(int letterNumber) {
	if (letterNumber < 1 || letterNumber > 26) {
		return null;
	}
	int letterIndexAscii = 'A' + letterNumber - 1;
	return "" + (char) letterIndexAscii;
}

```

```java
Flux<String> alphabet = Flux.just(-1, 30, 13, 9, 20)
    .handle((i, sink) -> {
        String letter = alphabet(i); 
        if (letter != null) 
            sink.next(letter); 
    });

alphabet.subscribe(System.out::println);
```

### 스레딩과 스케줄러

Reactor는 RxJava 처럼 동시성에 구애받지 않는다고 간주될 수 있다. 즉 동시석 모델을 강제하지 않는다. 오히려 개발자가 명령을 내리게 된다. 그러나 이것이 라이브러리가 동시성을 도와주는 것을 막지는 않는다.

`Flux` 또는 `Mono` 를 얻는다고 해서 받는다고 해서 전용 스레드에 실행되야 한다는 것은 아니다. 대부분의 연산자는 이전의 연산자가 실행된 스레드에서 계속 실행된다. 명시하지 않는한, 최상위 연산자(소스) 자체는 `subscirbe()`호출이 이루어진 스레드에서 실행된다. 다음 예제는 `Mono`가 새로운 스레드에서 실행되는 것을 보여준다.

```java
public static void main(String[] args) throws InterruptedException {
  final Mono<String> mono = Mono.just("hello "); 

  Thread t = new Thread(() -> mono
      .map(msg -> msg + "thread ")
      .subscribe(v -> 
          System.out.println(v + Thread.currentThread().getName()) 
      )
  )
  t.start();
  t.join();

}
```

```shell
hello thread Thread-0
```

Reactor에서 실행 모델과 실행 위치는 사용하는 `Scheduler`에 의해 결정된다. `Scheduler` 는 `ExecutorService`와 비슷하게 스캐줄링일 담당하지만 전용의 추상화를 사용해서 더 많은 작업을 수행할 수 있다. 특히 클락 역할을하고 더 넒은 범위의 구현을 가능하게 한다.(테스트를 위한 가상시간 또는 트램폴린 또는 즉시 스캐툴링)

`Schedulers` 클래스에는 다음 실행 컨텍스트에 대한 접근을 제공하는 static 메소드가 있다.

- 실행 컨텍스트 없음(`Schedulers.immediate()`) : 프로세싱 시, 제출된 `Runnable`이 직접 실행되어 현제 `Thread` 내에서 효과적으로 실행된다.(null 객체 이거나 no-op `Scheduler`로 볼수 있다)

- 재사용 가능한 단일 스레드(`Schedulers.single()`): 이메서드는 스케줄러가 삭제 될때까지 모든 호출자에 대해 동일한 스레드를 재사용한다. 호출자별 전용 스레드를 원하는 경우 `Schedulers.newSingle()`을 각각의 호출시 사용할 수있다.

- unbounded 탄력적 스레드 풀(`Schedulers.elastic()`) : `Schedulers.boundedElastic()`  도입 이후에는 사용되지 않는다. `Schedulers.elastic()` 은 역압력을 숨기는 문제와 너무 많은 스래드를 유발한다.

- bounded 탄력적 스레드 풀(`Schedulers.bondedElastic()`) : `elastic()`과 마찬가지로, 필요에 따라 새 워커 풀을 생성하고 비어있는 풀을 재사용한다. 너무 오랫동안 유휴 상태(기본 60초)인 작업자 풀도 삭제된다. `elastic() `과 달리 생성할 수 있는 백업 스레드 수에 제한(기본 CPU 코어수 * 10)이 있다. 

  `Schedulers.boundedElastic()`은 다른 리소스를 묶지 않도록 blocking 프로레스에 자체 스레드를 제공하는 편리한 방법이다. 하지만 너무 많은 스레드로 시스템에 부담을 주지는 않는다.

- 병렬 작업에 맞게 설정된 고정 워커 풀(`Schedulers.parallel()`)  : CPU 코어수 만큼 작업자를 생성한다.

추가적으로 `Schedulers.fromExecutorService(ExecutorService)`를 사용하여 기존 `ExecutorService`를 사용하여 `Scheduler`를 생성할 수 있다.

또한 `newXXX` 메소드를 사용하여 다양한 스캐줄러 타입의 새로운 인스턴스를 만들 수도 있다. 예를들어 `Schedulers.newParallel(youreScheduleName)`은 새로운 병렬 스캐줄러를 만든다.

일부 연산자들은 기본적으로 `Schedulers`의 특정 스케줄러를 사용한다. 예를들어 `Flux.interval(Duration.ofMillis(300))` 팩토리 메소드를 호출 하면 300ms 마다 `Flux<Long>` 틱이 생성된다. 기본적으로 `Schedulers.parallel()`을 사용한다.

Reactor는 리엑티브 체인에서 실행 컨텍스트(또는 스케줄러)를 전환하는 두가지 수단인 `publishOn`과 `subscribeOn`을 제공한다. 둘다 `Scheduler`를 사용하여 실행 컨텍스트를 해당 스캐줄러로 전환할 수 있다. 그러나 `publishOn`을 채인에 배치하는 것은 중요하지만, `subscribeOn`을 배치하는 것은 중요하지 않다. 이유는 구독하기 전까지는 아무 일도 일어나지 않기 때문이다.

`Reactor`에서 연산자를 연결할때, 필요한 만큼 여러개의 `Flux`와 `Mono`구현을 서로 랩핑할 수 있다. 한번 구독하면, 첫번체 공급자 뒤로 `Subscriber` 객체의 체인이 만들어진다. 볼수 있는 것은 밖의 `Flux` 또는 `Mono` 와 `Subscription`이지만 그러나 이러한 중간 연산자별 구독자들이 실제 작업이 이루어지는 곳이다. 

#### `publishOn` 메소드

`publishOn`은 subscriber 체인의 중간지점에서 다른 연산자와 같은 방식으로 적용된다. 업스트림에서 신호를 가져와 연결된 스케줄러의 워커에 대해 콜백을 실행하는 동안 다운 스트림으로 신호를 재생한다. 따라서 다음과 같이 후속 연산자가 실행되는 위치에 영향을 준다.

- 실행 컨텍스트를 `Scheduler`가 선택한 하나의 `Thread`로 변경한다.
- 명세에 따라 `onNext`호출은 순서대로 발생하므로 단일 스레드를 사용한다.
- 특정 `Scheduler`에서 작동하지 않는한, 연산자는 `publishOn` 이후에 같은 스레드에서 실행을 계속한다.

```java
Scheduler s = Schedulers.newParallel("parallel-scheduler", 4); 

final Flux<String> flux = Flux
    .range(1, 2)
    .map(i -> 10 + i)  
    .publishOn(s)  
    .map(i -> "value " + i);  

new Thread(() -> flux.subscribe(System.out::println)); 
```

첫번째 map은 마지막에 생성된 익명 스레드에서 실행

두번째 map은 처음에 생성된 스케줄러의 스레드에서 실행

프린트는 마지막에 실행된 컨텍스트에서 실행된다.

#### `subscribeOn` 메소드

`subscribeOn`은 이전 체인이 구성될 때 subscription 프로세스에 적용된다. 결과적으로  `subscribeOn`이 체인에 어디에 배치하든 항상 소스 방출의 컨텍스트에 영향을 준다. 그러나 이것은 이후의 `publishOn`의 호출에 영향을 주지 않는다. `publishOn`을 사용하여 계속해서 체인의 일부에 대한 실행 컨텍스트를 전환할 수 있다.

- 전체 연산자가 구독하는 `Thread`를 변경한다
- `Scheduler`에서 스레드를 하나 선택한다.

```java
Scheduler s = Schedulers.newParallel("parallel-scheduler", 4); 

final Flux<String> flux = Flux
    .range(1, 2)
    .map(i -> 10 + i)  
    .subscribeOn(s)  
    .map(i -> "value " + i);  

new Thread(() -> flux.subscribe(System.out::println)); 
```

첫번째 map은 처음 생성된 스레드 중 하나에 의해 실행

두번째 map도 동일한 스레드에서 실행

구독이 처음으로 발생하지만 `subscribeOn`에 의해 즉시 네개의 스케줄러 스레드 중 하나로 이동한다.

### 오류 처리

리액티브 스트림에서는 오류가 발생하자 마자 시퀀스를 중지하고 연산자 체인을 따라 마지막 단계인 사용자가 정의한 `Subscriber`의 `onError` 메소드로 전파된다.

오류는 애플리케이션 레벨에서 처리해야한다. 따라서 `Subscriber`의 `onError` 메서드를 항상 정의해야한다.

Reactor는 또한 오류 처리 연산자로 체인 중간의 오류를 처리하는 방법을 제공한다.

```java
Flux.just(1, 2, 0)
    .map(i -> "100 / " + i + " = " + (100 / i)) //this triggers an error with 0
    .onErrorReturn("Divided by zero :("); // error handling example
```

#### 오류 처리 연산자

`try-catch` 블록에서 예외를 다루는 것에 익숙할 수 있다. 이는 다음을 포함한다.

- catch 한후 정적인 기본 값을 리턴한다.
- catch 한후 대안의 메소드를 실행한다.
- catch 한후 동적인 값을 계산한다.
- catch한후 `BusinessException`으로 랩핑한후 다시 던진다
- catch 한후 로그를 기록하고 다시 던진다.
- `finally` 블록을 사용하여 `try-with-resource`를 사용한다

이것들은 모두 Reactor에서 오류 처리 연산자의 형태와 동일하다. 이러한 연산자를 살펴보기 전에 먼저 리액티브 체인과 `try-catch` 블록의 병렬을 설정한다.

구독시 체인의 끝에서 `onError` 콜백은 `catch` 블록과 유사하다. 다음 예와 같이 `Exception`이 발생할 경우 실행은 `catch`로 건너 뛴다.

```java
Flux<String> s = Flux.range(1, 10)
    .map(v -> doSomethingDangerous(v)) 
    .map(v -> doSecondTransform(v)); 
s.subscribe(value -> System.out.println("RECEIVED " + value), 
            error -> System.err.println("CAUGHT " + error) 
);
```

앞의 예제는 개념적으로 다음의 `try-catch`블록과 유사하다.

```java
try {
    for (int i = 1; i < 11; i++) {
        String v1 = doSomethingDangerous(i); 
        String v2 = doSecondTransform(v1); 
        System.out.println("RECEIVED " + v2);
    }
} catch (Throwable t) {
    System.err.println("CAUGHT " + t); 
}
```

##### 정적 대체 값

Catch후 정적인 기본값을 반환하는 것은 `onErrorReturn`이다.

```java
try {
  return doSomethingDangerous(10);
}
catch (Throwable error) {
  return "RECOVERED";
}
```

다음은 Reactor에서 해당하는 것을 보여준다.

```java
Flux.just(10)
    .map(this::doSomethingDangerous)
    .onErrorReturn("RECOVERED");
```

`Predicate`를 이용하여 예외를 복구할지 선택할 수 있다.

```java
Flux.just(10)
    .map(this::doSomethingDangerous)
    .onErrorReturn(e -> e.getMessage().equals("boom10"), "recovered10"); 
```

##### 대체 메소드 

하나 이상의 기본 값을 원하고 데이터를 처리하는 대체 방법이 있는 경우 `onErrorResume`을 사용할 수 있다. 이는 catch 한후 대안의 메소드를 실행하는 것과 같다.

예를 들어, 프로세스가 외부의 신뢰할 수 ㅇ벗는 서비스에서 데이터를 가져 오지만, 좀더 오래 되었을 수 있지만, 더 안정적인 동일한 데이터의 로컬 캐시도 유지하는 경우 다음을 실행 할 수 있다.

```java
String v1;
try {
  v1 = callExternalService("key1");
}
catch (Throwable error) {
  v1 = getFromCache("key1");
}

String v2;
try {
  v2 = callExternalService("key2");
}
catch (Throwable error) {
  v2 = getFromCache("key2");
}
```

다음 예제는 Reactor에서 해당하는 것을 보여준다.

```java
Flux.just("key1", "key2")
    .flatMap(k -> callExternalService(k) 
        .onErrorResume(e -> getFromCache(k)) 
    );
```

`onErrorReturn`과 마찬가지로 `onErrorResume`에서도 `Predicate`를 이용해 적용할 예외를 선택할 수 있다. 발생하는 오류에 따라 전환 할 다른 대체 시퀀스를 선택할 수 있다.

```java
Flux.just("timeout1", "unknown", "key2")
    .flatMap(k -> callExternalService(k)
        .onErrorResume(error -> { 
            if (error instanceof TimeoutException) 
                return getFromCache(k);
            else if (error instanceof UnknownKeyException)  
                return registerNewEntry(k, "DEFAULT");
            else
                return Flux.error(error); 
        })
    );
```

##### 동적 대체 값

데이터를 처리하는 대체 방법이 없더라도 받은 예외에서 대체 값을 계산할 수 있다. 이는 대체값을 찾아 동적 값을 찾는 것과 동일하다.

```java
try {
  Value v = erroringMethod();
  return MyWrapper.fromValue(v);
}
catch (Throwable error) {
  return MyWrapper.fromError(error);
}
```

다음과 같이  `onErrorResume`를 사용하여 대체값을 찾아 동적 값을 찾는 방법과 동일한 방식으로 리액티브 방식으로 수행 할 수 있습니다 .

```java
erroringFlux.onErrorResume(error -> Mono.just( 
        MyWrapper.fromError(error) 
));
```

##### Catch와 다시 던지기

catch한후 `BusinessException`으로 랩핑한후 다시 던지기는 명령형 방식으로는 다음과 같다.

```java
try {
  return callExternalService(k);
}
catch (Throwable error) {
  throw new BusinessException("oops, SLA exceeded", error);
}
```

`onErrorResume`을 사용해 다음과 같이 리액티브 방식으로 수행할 수 있다.

```java
Flux.just("timeout1")
    .flatMap(k -> callExternalService(k))
    .onErrorResume(original -> Flux.error(
            new BusinessException("oops, SLA exceeded", original))
    );
```

`onErrorMap`을 사용해 더 간단히 표현할 수 있다.

```java
Flux.just("timeout1")
    .flatMap(k -> callExternalService(k))
    .onErrorMap(original -> new BusinessException("oops, SLA exceeded", original));
```

##### Catch후 오류 메시지 기록 및 다시 던지기

오류가 계속 전파되길 원하지만 시퀀스를 수정하지 않고 계속 하려는 경우 `doOnError`

연산자를 사용할 수 있다. 명령형 프로그래밍에서는 다음과 같이 작성한다.

```java
try {
  return callExternalService(k);
}
catch (RuntimeException error) {
  //make a record of the error
  log("uh oh, falling back, service failed for key " + k);
  throw error;
}
```

`doOn` 이 붙은 연산자를 이용하면, 시퀀스의 수정없이 안에 내용을 `peek`하는 것이 가능하다.

다음 예제는 오류를 계속해서 전파하지만, 오류가 있음을 기록할 수 있다.

```java
LongAdder failureStat = new LongAdder();
Flux<String> flux =
Flux.just("unknown")
    .flatMap(k -> callExternalService(k) 
        .doOnError(e -> {
            failureStat.increment();
            log("uh oh, falling back, service failed for key " + k); 
        })
        
    );

```

##### 리소스와 finally 블록사용

`finally` 블록을 사용하여 리소스 정리를 사용하거나, Java 7의 `try-with-resource` 구성을 사용하여 수행하는 방법이다.

```java
Stats stats = new Stats();
stats.startTimer();
try {
  doSomethingDangerous();
}
finally {
  stats.stopTimerAndRecordTiming();
```

```java
try (SomeAutoCloseable disposableInstance = new SomeAutoCloseable()) {
  return disposableInstance.toString();
}
```

Reactor의 `doFinally`와 `using`을 사용하여 표현할 수 있다.

`doFinally`를 이용하여 스퀀스가 종료될때(`onComplete` 또는 `onError`) 또는 취소 될때  실행을 원하는 함수를 설정할 수 있다. 어떤 종류의 종료가 호출되 었는지도 알 수 있다.

```java
Stats stats = new Stats();
LongAdder statsCancel = new LongAdder();

Flux<String> flux =
Flux.just("foo", "bar")
    .doOnSubscribe(s -> stats.startTimer())
    .doFinally(type -> { 
        stats.stopTimerAndRecordTiming();
        if (type == SignalType.CANCEL) 
          statsCancel.increment();
    })
    .take(1); 
```

`using`은 리소스에서 `Flux`가 만들어지고 처리가 완료 될 때마다 해당 리소스가 처리되어야 하는 경우 사용한다. 

```java
AtomicBoolean isDisposed = new AtomicBoolean();
Disposable disposableInstance = new Disposable() {
    @Override
    public void dispose() {
        isDisposed.set(true); 
    }

    @Override
    public String toString() {
        return "DISPOSABLE";
    }
};
```

```java
Flux<String> flux =
Flux.using(
        () -> disposableInstance, 
        disposable -> Flux.just(disposable.toString()), 
        Disposable::dispose 
);
```

