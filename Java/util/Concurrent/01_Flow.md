## Flow

`Flow` 클래스는 인스턴스를 만들수 있고, 리액티브 스트림 프로젝트의 표준에 따라 프로그래밍 발행-구독 모델을 지원할 수 있도록 다음의 네 개의 인터페이스를 포함한다.

- [`Publisher`](#publisher)
- [`Subscriber`](#subscriber)
- [`Subscription`](#subscription)
- [`Processor`](#processor)

`Publisher`가 항목을 발행하면 `Subscriber`가 항목을 한 개 또는 여래개 씩 소비를 하는데 ` Subscription`이 이 과정을 관리할 수 있도록 `Flow`클래스는 관련된 인터페이스와 정적 메소드를 제공한다.

### `Publisher`

여러개의 이벤트를 제공할 수 있지만, `Subscriber`의 요청에 따라 역압력 기법에 의해 이벤트 제공 속도가 제한된다. `Publisher`는 함수형 인터페이스로 `Subscriber`를 `Publisher`가 발행한 이벤트의 리스너로 등록하는 메소드를 제공한다.

```java
    @FunctionalInterface
    public static interface Publisher<T> {
        public void subscribe(Subscriber<? super T> subscriber);
    }

```

### `Subscriber`

`Publisher`가 관련 이벤트를 발행할 때 호출할 수 있는 네 개의 메서드를 정의한다. `Publisher`는 시작시 `onSubscribe`메소드를 호출해 `Subscription` 객체를 전달한다. 그 후 `onNext` 메소드로 항목을 전달하며, `onComplete` 메소드를 통해 더 이상의 데이터가 없고 종료됨을 알리거나, `onError`를 호출해 장애가 발생함을 알릴 수 있다.

```java
public static interface Subscriber<T> {

        public void onSubscribe(Subscription subscription);

        public void onNext(T item);

        public void onError(Throwable throwable);

        public void onComplete();
    }
```

### `Subscription`

```java
    public static interface Subscription {

        public void request(long n);

        public void cancel();
    }
```

`request` 메소드를 호출해 `Publisher`에게 주어진 개수의 이벤트를 처리할 준비가 되었음을 알릴수 있다. `cancel`메소드로 `Subscription`을 취소, 즉 `Publisher`에게 더 이상 이벤트를 받지 않음을 통지한다.