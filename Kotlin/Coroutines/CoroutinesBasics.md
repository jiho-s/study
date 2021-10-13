## 코루틴 기본

### 코루틴

코루틴은 중단가능한 연산으로 스레드의 개념과 비슷하다. 하지만 코루틴은 하나의 스레드에 속하지 않고 한 스레드에서 실행을 일시 중지하고 다른 스레드에서 다시 시작할 수 있다

코루틴은 경량의 스레드로 생각할 수 있으나 스레드와 다른 많은 차이점이 있다

```kotlin
fun main() = runBlocking { // this: CoroutineScope
    launch { // launch a new coroutine and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!") // print after delay
    }
    println("Hello") // main coroutine continues while a previous one is delayed
}
```

- `runBlocking`

  coroutine builder 중 하나로 코루틴과 일반 함수를 연결 해주는 역할

  스레드가 runBlokcing 안의 것들의 실행을 끝날때까지 블럭한다는 의미이다. `runBlocking` 가장 최상위 레벨의 어플리케이션에만 사용되고 실제의 코드 안에서는 잘 사용되지 않는다. 스레드는 비싼 리소스 이므로 블럭하는 것은 효율적이지 못하다.

- `launch`

  coroutine builder로 코드의 나머지 부분과 동시에 새 coroutine을 생성하고 coroutine은 독립적으로 동작한다.

- `delay`

  suspending 함수로 특정한 시간 동안 coroutine을 잡고 있는다. 코루틴을 잡고 있는 것은 스레드를 블럭시키지 않는다. 다른 코루틴이 스레드를 사용할수 있다

#### 구조화된 동시성

코루틴은 **structured cocurrency** 원리를 따르는데 이는 새로운 코루틴은 코루틴의 라이프타임을 제한하는 특정한 `CoroutineScope` 안에서만 생성할 수 있다

실제 어플리케이션에서 수많은 코루틴을 실행할때 구조화된 동시성은 코루틴이 잃어버리거나 새지 않는 것을 보장한다. 또한 코드의 에러도 리포트되고 잃어버리지 않는 것을 보장한다

### Scope builder

다른 빌더에 의해 코틀린 스코프가 제공될수 있다 `coroutineScope` 를 통해 스코프를 선언하는 것도 가능하다.

`runBlocking` 과의 차이점은 `runBloking`은 작업을 기다리는 동안 현재 스레드를 block 시키는 반면, `coroutineScope`는 일시 중단되어 스레드에 다른 작업이 실행될수 있다. 이 차이점으로 `runBlocking`은 일반적인 함수이고 `coroutineScope`는 suspending 함수 이다

```kotlin
fun main() = runBlocking {
    doWorld()
}

suspend fun doWorld() = coroutineScope {  // this: CoroutineScope
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello")
}
```

### Scope builder와 동시성

`corutineScope`안에 여러게의 suspending 함수가 실행될 수 있다

```kotlin
// Sequentially executes doWorld followed by "Done"
fun main() = runBlocking {
    doWorld()
    println("Done")
}

// Concurrently executes both sections
suspend fun doWorld() = coroutineScope { // this: CoroutineScope
    launch {
        delay(2000L)
        println("World 2")
    }
    launch {
        delay(1000L)
        println("World 1")
    }
    println("Hello")
}
```

### Job

`launch` 코루틴 빌더는 `Job`을 리턴한다. `Job`을 이용해 코루틴을 실행하고 완료까지 기다릴수 있다.

```kotlin
val job = launch { // launch a new coroutine and keep a reference to its Job
    delay(1000L)
    println("World!")
}
println("Hello")
job.join() // wait until child coroutine completes
println("Done") 
```

