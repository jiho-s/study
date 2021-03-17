## 기본 타입과 값

JVM에서 지원되는 기본 타입에는 *numeric types*, `boolean` type, `returnAddress` type이 있다

### 정수 타입

- `byte`

  8비트로 기본 값은 0

- `int`

  32비트로 기본 값은 0

- `long`

  64비트로 기본 값은 0

- `char`

  16비트로 기본 값은 null('\u0000') 이다

### 부동 소수점 타입

- `float`

  32비트로 기본 값 0

- `double`

  64비트로 기본 값 0

### `returnAddress` 타입

`returnAddress`타입은 JVM의 *jsr*, *ret*, and *jsr_w* 명령에 사용되는 타입이다. `returnAddress` 타입의 값은 JVM의 opcode에 대한 포인터이다.  *numeric types* 과는 달리 Java 언어의 타입에는 해당하지 않으며 실행중인 프로그램에서 수정할 수 없다.

### `boolean` 타입

 `boolean`에 대해 작동하는 Java 프로그래밍 은 Java Virtual Machine `int` 타입의 값을 사용하도록 컴파일된다.