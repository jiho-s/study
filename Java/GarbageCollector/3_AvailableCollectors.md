## Available Collectors

Java HotSpot VM은 네가지 다른 종류의 컬렉터가 있다.

### Serial Collector

싱글 스레드를 사용하기 떄문에 스레드간 오버헤드가 없어 상대적으로 효율적이다.

멀티 프로세서를 활용할 수 없기 때문에 단일 프로세서 시스템에 적합

### Parallel Collector

Serial Collector와 유사한 세대별 컬렉터이다. 차이점은 Parallel Collector는 여러개의 스레드를 활용한다.

### Mostly Concurrent Collectors

Concurrent Mark Sweep(CMS) 컬렉터와 Garbage-First(G1) 수집기가 Mostly Concurrent Collector이다. Mostly Concurrent Collectors는 몇 개의 작업을 어플리케이션과 동시에 실행한다.

- #### G1 Garbage Collector

  큰 메모리가 있는 멀티 스레드 시스템에서 유용하다.  메이저 컬렉션시 중지 시간을 최소화 할 수 있다.

- #### CMS Collector

  짧은 컬렉션 중지시간을 위해 사용한다. 

CMS collector 는 JDK 9 부터 deprecated 되었다.

### Z Garbage Collector

Z Garbage Collector는 확장 가능한 저 지연 가비지 컬렉터이다. ZGC는 애플리케이션의 스레드 실행을 중단하지 않고 모든 작업을 동시에 실행한다.

ZGC는 짧은 대기 신간(10ms 이내)와 큰 heap(TB)를 사용하는 애플리케이션 시스템에서 유용한다.

ZGC는 JDK11 부터 실험적으로 사용 할 수 있다.