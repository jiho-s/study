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