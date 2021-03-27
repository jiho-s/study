## Scheduling

### Introduction to Scheduling

#### Proceess Behavior

CPU의 증가 속도가 disk의의 증가 속도보다 빨라 CPU 점유시간보다 I/O점유시간이 증가하고 있다

#### When to Schedule

스캐줄을 결정해야할 여러 상황들

- 새로운 프로세스가 생성될때 어떤것을 실행할지
- 프로세스가 종료될때 어떤것을 실행할지
- 프로세스가 I/O, semaphore로 블럭될시 어떤 것을 실행할지
- I/O interrupt가 발생시

##### 비선점 스케줄링

끝나거나 블럭될때까지 강제로 뺏지 않는다

##### 선점 스케줄링

강제적으로 전환시킬수 있다

#### Categories of Scheduling Algorithms

##### Batch

컴퓨터가 가능한 많은일을 하게, 비선점 알고리즘이나, 시간이 긴 선점 알고리즘

##### Interactive

사용자와 상호작용, 선점 알고리즘

##### Real-time

데드라인을 맞추는게 중요, 작업과 블록이 빠르게 전환

### Scheduling in Batch Systems

#### First-Come, First-Served

먼저 들어온 작업 먼저 처리, Response Time이 클수 있다.

#### Shortest Job First

짧은 작업을 먼저 처리, 작업이 긴 작업은 starvation이 발생할 수 있다

CPU 시간이 얼마나 걸리는지 정확히 알아햐 한다.

#### Shortest Remaining Time Next

Shoretest Job First의 선점 버전, 한 Unit 돌릴때 마다 스케줄링

스케줄링 오버헤드가 크다

