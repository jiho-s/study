## Parallel Collector

Seral Collector와 유사한 세대별 수집기로, 가장 큰 차이점은 Parallel Collector에는 가비지 컬렉션 속도를 높이기 위해 여러 스레드를 사용한다.

Parallel Collector는 `-XX:+UseParallelGC`로 활성화 한다.

### Paraller Collector의 스레드 개수

총 스레드의 개수가 8개보다 많으면 Paraller Collector는 고정된 비율의 스레드를 가비지 컬렉터 스레드로 사용한다.

사용하는 비율은 5/8이다. 

가비지 컬렉터 스레드는 `-XX:ParallelGCThreads=<N>`으로 제어할 수 있다. 

여러개의 스레드가 마이너 컬렉션에 참여하고 있기 때문에, young generation에서 old generation으로 이동시 단편화가 발생할 수 있다. 각각의 가비지 컬렉션 스레드는 마이너 컬렉션중에 old generation의 일부 영역을 나눠서 사용하고 이떄 단편화가 발생 할 수 있다.

### Parallel Collector의 세대

