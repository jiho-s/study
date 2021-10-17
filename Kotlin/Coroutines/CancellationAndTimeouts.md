## Cancellation and timeouts

### 코루틴 실행 취소

`launch` 함수는 실행중인 코루틴을 취소할 수 있는 `Job`을 리턴한다

```kotlin
val job = launch {
    repeat(1000) { i ->
        println("job: I'm sleeping $i ...")
        delay(500L)
    }
}
delay(1300L) // delay a bit
println("main: I'm tired of waiting!")
job.cancel() // cancels the job
job.join() // waits for job's completion 
println("main: Now I can quit.")
```

`job.cancel`을 호출하여 코루틴을 취소할 수 있다

### 실행 취소는 협조적이여야한다.

코루틴 실행 취소는 협조적이여야 한다. 코루틴 코드는 취소될수 있게 협조해야한다. `kotlinx.coroutines`의 모든 suspencding 함수는 *cancellable*하다. 그들은 모두 코루틴의 취소를 체크하고 취소되었을때 `CancellationException`을 던진다. 코루틴이 계산중에 있고 취소를 체크하지 않는다면 아래와 같이 취소될수 없다

```kotlin
val startTime = System.currentTimeMillis()
val job = launch(Dispatchers.Default) {
    var nextPrintTime = startTime
    var i = 0
    while (i < 5) { // computation loop, just wastes CPU
        // print a message twice a second
        if (System.currentTimeMillis() >= nextPrintTime) {
            println("job: I'm sleeping ${i++} ...")
            nextPrintTime += 500L
        }
    }
}
delay(1300L) // delay a bit
println("main: I'm tired of waiting!")
job.cancelAndJoin() // cancels the job and waits for its completion
println("main: Now I can quit.")
```

### 코드를 취소가능하게 만들기

코드를 취소가능하게 만들기 위해서는 2가지 방법이 있다. 첫번째 방법은 주기적으로 취소를 체크하는 suspending 함수를 호출하는 것이다. `yield`는 그런 목적을 위해서는 좋은 선책이다. 또 다른 하나는 명시적으로 취소 상태를 살펴 보는 것이다.

`CoroutineScope.isActive`를 사용해 명시적으로 취소 상태를 살펴 볼수 있다

```kotlin
val startTime = System.currentTimeMillis()
val job = launch(Dispatchers.Default) {
    var nextPrintTime = startTime
    var i = 0
    while (isActive) { // cancellable computation loop
        // print a message twice a second
        if (System.currentTimeMillis() >= nextPrintTime) {
            println("job: I'm sleeping ${i++} ...")
            nextPrintTime += 500L
        }
    }
}
delay(1300L) // delay a bit
println("main: I'm tired of waiting!")
job.cancelAndJoin() // cancels the job and waits for its completion
println("main: Now I can quit.")
```

###  finally로 리소스 종료

Cancellable suspending 함수는 취소시 `CancellationException`을 던진다. try {...} finally{..}, use 를 이용해 처리할수 있다.

```kotlin
val job = launch {
    try {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    } finally {
        println("job: I'm running finally")
    }
}
delay(1300L) // delay a bit
println("main: I'm tired of waiting!")
job.cancelAndJoin() // cancels the job and waits for its completion
println("main: Now I can quit.")
```

### non-cancellable block 실행

취소된 코루틴에서 suspend 해야하는 경우 `withContext(NonCancellable){...}`을 사용할 수 있다

```kotlin
val job = launch {
    try {
        repeat(1000) { i ->
            println("job: I'm sleeping $i ...")
            delay(500L)
        }
    } finally {
        withContext(NonCancellable) {
            println("job: I'm running finally")
            delay(1000L)
            println("job: And I've just delayed for 1 sec because I'm non-cancellable")
        }
    }
}
delay(1300L) // delay a bit
println("main: I'm tired of waiting!")
job.cancelAndJoin() // cancels the job and waits for its completion
println("main: Now I can quit.")
```

### Timeout

withTimeout 함수를 이용해서 특정 delay 후에 취소하는 것을 트랙하기 위해 관련 있는 Job을 트랙하고 각각의 분리된 코루틴을 실행할수 있다

```kotlin
withTimeout(1300L) {
    repeat(1000) { i ->
        println("I'm sleeping $i ...")
        delay(500L)
    }
}
```

시간을 초과하면 `TimeoutCancellationException`을 반환한다

시간을 초과하면 `null`을 반환하는 함수도 있다

```kotlin
val result = withTimeoutOrNull(1300L) {
    repeat(1000) { i ->
        println("I'm sleeping $i ...")
        delay(500L)
    }
    "Done" // will get cancelled before it produces this result
}
println("Result is $result")
```

### 비동기 timeout과 리소스

`withTimeout`의  타임아웃 이벤트는 반복되는 블럭의 코드와 비동기적이며 timeout block에서 리턴 되기 전을 포함해서 어느 시점에서든 발생할수 있다. 안쪽에서 리소스를 얻고 밖에서 리소스를 해재하는 경우 이를 염두해 두어야 한다.

```kotlin
var acquired = 0

class Resource {
    init { acquired++ } // Acquire the resource
    fun close() { acquired-- } // Release the resource
}

fun main() {
    runBlocking {
        repeat(100_000) { // Launch 100K coroutines
            launch { 
                val resource = withTimeout(60) { // Timeout of 60 ms
                    delay(50) // Delay for 50 ms
                    Resource() // Acquire a resource and return it from withTimeout block     
                }
                resource.close() // Release the resource
            }
        }
    }
    // Outside of runBlocking all coroutines have completed
    println(acquired) // Print the number of resources still acquired
}
```

이를 해결하기 위해 변수에 값을 저장하는 것으로 해결할 수 있다

```kotlin
runBlocking {
    repeat(100_000) { // Launch 100K coroutines
        launch { 
            var resource: Resource? = null // Not acquired yet
            try {
                withTimeout(60) { // Timeout of 60 ms
                    delay(50) // Delay for 50 ms
                    resource = Resource() // Store a resource to the variable if acquired      
                }
                // We can do something else with the resource here
            } finally {  
                resource?.close() // Release the resource if it was acquired
            }
        }
    }
}
// Outside of runBlocking all coroutines have completed
println(acquired) // Print the number of resources still acquired
```

