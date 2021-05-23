## Blocking, Non-Blocking / Synchronous, Asynchronous

[참고]: https://djkeh.github.io/articles/Boost-application-performance-using-asynchronous-IO-kor/

### Blocking, Non-blocking의 의미

#### Blocking

프로세스가 시스템을 호출하고나서 결과가 반환되기 전까지 다음처리로 넘어가지 않는다.

#### NonBlocking

프로세스가 시스템을 호출한 직후 제어가 다시 돌아와서 시스템 호출의 종료를 기다리지 않고 다음 처리를한다.

### Synchronous, Asynchronous의 의미

#### Synchronous

작업을 요청한 후 작업의 결과가 나올 때까지 기다린후 처리

#### Asynchronous

프로세스가 작업의 완료를 신경쓰지 않고, 시스템 호출의 종료가 발생하면, 해당하는 작업을 수행한다.

### Synchronous Blocking

가장 기본적인 I/O 모델로 일반적인 read write와 같은 I/O. 시스템 호출

### Synchronous Non-Blocking

시스템 호출은 즉시 반환하여 Blocking이 발생하지 않는다.

하지만 프로세스에서 주기적으로 데이터 상태를 체크한다. 데이터 적재가 완료되면 해당 작업을 수행한다.

### Asynchronous Blocking

시스템 호출은 즉시반환한다. 하지만 프로세스를 블록시킨후, 데이터 적재가 완료되면 해당 신호를 받아 작업을 수행한다.

### Asynchronous Non-Blocking

시스템 호출은 즉시 반환한다. 프로세스는 다른 작업을 수행하고 데이터 적재가 완료되면 콜백 등을 수행하여 해당 업무를 수행한다.