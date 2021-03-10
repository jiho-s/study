## Stream

> [참고자료](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)

### `java.util.stream` 

`map-reduce` 변환 같은 요소의 스트림에 함수형 프로그래밍 작업을 지원하는 클래스이다.

```java
int sum = widgets.stream()
                      .filter(b -> b.getColor() == RED)
                      .mapToInt(b -> b.getWeight())
                      .sum();
```

### Stream

`Stream`,  `IntStream`,  `LongStream`, `DoubleStream` 클래스 등으로 `collections`와 차이가 있다.

#### Stream과 Collectons의 차이

- 저장공간이 없다

  스트림은 요소를 저장하는 데이터 구조가 아니다. 데이터구조, 배열 I/O 채널과 같은 소스에서 파이프라인을 통해 요소를 전달한다

- 기본적으로 함수형 프로그래밍이다.

  스트림은 결과를 생성하지만 소스를 변경하지 않는다.

- Lazy 추구

  필터, 맵, 중복제거 등 많은 스트림이 lazy로 구현되어 최적화가 적용될수 있다.

- 제한이 없을수 있다

  컬랙션의 크기는 한계가 있지만 스트림은 없다

- 한번만 사용가능

  스트림의 요소는 한번만 사용가능하다

#### Stream의 생성방법

- `Collection`에서 `stream()`, `parallelStream()` 메소드 사용
- 배열에서 `Arrays.stream(Object[])` 사용
- `Stream.of(IObject[])`, `IntStream.range(int, int)`, `Stream.iterate(Object, UnaryOperator)` 같은 스트림 클래스의 static 팩토리 메소드 사용
- `BufferedReader.lines()`를 이용한 파일의 행
- `Random.ints()`를 이용한 난수 생성

### 스트림 작업과 파이프라인

스트림 작업은 중간 작업과 최종 작업으로 나뉘 스트림 작업들은 결합되어 스트림 파이프라인을 만든다.스트림 파이프라인은 소스로 구성되며 0개이상의 중간 작업과 한개의 최종 작업으로 구성된다.

중간 작업은 새로운 스트림을 반환하며, lazy로 수행된다. 파이프라인 소스는 최종 작업이 실행되기 전까지는 실행되지 않는다.

lazy로 수행하는것은 상당한 효율성을 얻을수  있다. 위의 filter-map-sum 예제에서 최소한의 중간 상태를 통해 단일 패스로 작업을 수행 할 수 있다. 또한 lazy를 통해 필요하지 않을 때는 모든 데이터를 검사하지 않도록할 수 있다.

중간 작업은 상태 비 저장과 상태 저장 작업으로 나뉜다. 상태 비 저장 연산은 `filter`와 `map`같은 새로운 요소의 작업을 할 때 이전의 요소 값이 필요하지 않는 경우를 말한며 각각의 요소는 다른 요소에 독립적으로 처리될수 있다. 상태 저장 작업은 `distinct`, `sorted` 등 새로운 요소를 처리할때 이전의 요소 값이 필요하다.

### 병렬

for-루프의 작업은 직렬적이다. 스트림은 각각의 작업을 파이프라인으로 재구성하여 병렬 처리를 쉽게할 수 있다. 모든 스트림은 직렬 또는 병렬로 실행할 수 있다. 

```java
int sumOfWeights = widgets.parallelStream()
                               .filter(b -> b.getColor() == RED)
                               .mapToInt(b -> b.getWeight())
                               .sum();
```

