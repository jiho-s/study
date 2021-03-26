## Interprocess Communication

##### 프로세스간 통신의 문제점

1. 한 프로세스에서 다른 프로세스로 정보를 어떻게 보낼것인지
2. 여러개의 프로세스가 같은 정보를 다르게 취급하고 있지는 않은지
3. 서로 의존성이 있지는 않은지

### Race Conditions

두 개 또는 그 이상의 프로세스가 공유된 데이터를 읽거나 쓸때 실행 결과를 예측할 수 없는 경우

### Critical Regions

#### Mutual Exclusion

한 프로세스가 공유된 자원을 사용중일 때 다른 프로세스는 접근할 수 없는 것

#### Critical Regions

공유된 메모리든 파일이든 Race를 야기할수 있는 모든 것

#### Critical Regions의 4가지 원칙

1. Critical Region에는 한개의 스레드만
2. CPU의 개수나 속도에는 어떠한 가정도 하지 않는다
3. Critical Region 외부에 있는 프로세스가 내부에 있는 프로세스를 블록시킬수는 없다
4. 무한이 기다리는 프로세스는 없어야 한다.

### Mutual Exclusion with Bush Waiting

#### Disabling Interrupts

프로세스가 중단되지 않는다면, 공유된 메모리가 다른 프로세스 개입 없이 읽고 수정할수 있다.

하지만 시스템이 멀티프로세스인 경우 소용없다.

#### Lock Variables

전역 변수에 다른 프로세스가 사용중인 경우 상태를 바꾼다.

전역 변수 가 사용중이지 않은것을 확인하고 Context Switch가 일어날수 있어 Mutal Exclusion을 보장할 수 없다.

#### Strict Alternation

##### Busy waiting

상태가 원하는 값이 나올때까지 계속 테스트를 한는 방법, 이때 사용하는 변수를 **Spin Lock**이라고 한다

3번째 룰을 위반한다. Critical Region안에 있는 프로세스가 상태를 바꾼후 끝나지 않은 상태에서 다른 프로세스가 현재 프로세스를 블록시킬수 있다.

#### Peterson's Solution

모두 만족하지만 오버헤드가 크다

#### The TSL Instruction

TSL은 Lock을 읽고 수정하는 작업중 다른 프로세스가 Lock에 접근하는 것을 하드웨어적으로 막아준다.

#### Busy Waiting의 문제점

- CPU 시간 낭비
- 우선순위가 있는 경우 우선순위가 높은 프로세스가 실행중이지만, 낮은 프로세스가 Critical Region에 있는 경우

### Sleep and Wakeup

#### The Producer-Consummer Problem

두 개의 프로세스가 자원을 공유하는 경우 하나의 프로세스는 버퍼에 넣어주고 다른 하나는 버퍼에서 소비한다.

- Critical Region이 설정되지 않아 insert와 remove가 동시에 될수 있다
- sleep하기전에 contextSwitch가 일어날 수 있다.

### Semaphores

#### atomic action

semaphore 작업이 시작되면 다른 프로세스가 세마포어 작업이 끝나거나 블록될때까지 세마포어에 접근을 못하도록 막는다

#### Solving the Producer-Consummer Problem Using Semaphores

atomic action을 이용해 Critical Region에 한개의 프로세스만 접근가능하게 한다

```swift
var mutex: Semaphore = 1
var empty: Semaphore = n
var full: Semaphore = 0

func producer() {
    var item: Int
    while true {
        item = produceItem()
        down(empty)
        down(mutex)
        items.append(item)
        up(mutex)
        up(full)
    }
}

func consumer() {
    var item: Int
    while true {
        down(full)
        down(mutex)
        item = items.removeLast()
        up(mutex)
        up(empty)
        consumeItem(item)
    }
    
}
```

##### 세마포어의 종류

- Binary semaphore

  상태를 1또는 0만 가지고 있는 경우

- Counting semaphore

  상태를 n개 가지고 있는 경우

#### Semaphore의 문제점

위의 `producer()`에서 `down()` 의 순서가 뒤바뀐 경우 Deadlock이 발생할 수 있다. 세마포어를 다룰때는 코드를 잘 구현해야 한다.

### Mutexes

1 또는 0의 상태를 가지고 있는 변수를 공유해 Mutual Exclusion을 보장

### Monitor

한개의 프로세스만 모니터에만 활동 가능하다.  컴파일러에서 지원해 주어야한다. 세마포어의 문제를 해결

### Message Passing

메모리를 공유하는 것이 아닌 메시지를 전달하는 방식으로 프로세스간 통신을 한다.

#### The Producer-Consumer Problem with Message Passing

Prodcer의 속도가 빨라 메시지가 가득 차는 경우, Consumer의 속도가 빨라 메시지가 없는 경우가 있다

##### Mailbox

메일 박스가 가득 찬 경우 메일박스에 빈공간이 생길 때 까지 중지시킨다.