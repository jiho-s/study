## Page Replacement Algorithms

### The Not Recently Used Page Replacement Algorithm

R은 페이지가 참조될때마다 표시, 주기적으로 초기화해준다

M은 수정될 때 표시

- Class 0: R 0 M 0
- Class 1: R 0 M 1
- Class 2: R 1 M 0
- Class 3: R 1 M 1

페이지 폴트시 클래스가 작은 것부터 교체

### The First-In, First-Out(FIFO) Page Replace Algorithm

오버헤드가 작다. 가장 많이 사용한 페이지가 삭제될 수 있다.

### The Second-Chance Page Replacement Algorithm

R 비트를 이용해 1이면 맨뒤로 보내 다시한번 기회 부여

### The Clock Page Replacement Algorithm

Second-Chance에서 맨뒤로 보내는게 아닌 포인터를 이용

### The Lest Recently Used(LRU) Page Replacement Algorithm

가장 오랫동안 사용 안한 페이지 교체, 로딩된 순서가 아닌 참조된 순서로 리스트 갱신

### The Working Set Page Replacement Algorithm

현재 프로세스가 사용하는 페이지의 집합을 working set이라고 한다. 멀티 프로그래밍 시스템에서 프로세스 마다 페이지가 다르다. 따라서 프로세스별 사용하는 페이지를 미리 로딩하는것이 필요하다.

###### w(k, t)

현재 시간 t에서 지금까지 참조된 페이지 중에서 가장 최근에 참조된 k개의 페이지 집합

R 0 이고 마지막 사용시간 - 현재시간이 기준보다 크면 교체

### The WSClock Page Replacement Algorithm

페이지 폴트시 화살표가 가르키는 페이지 프레임 검사, R 1 이면 0으로 바꾸고 현재시간 기록, R = 0 이면 기준시간 확인 M = 0 이면 교체 1 이면 스케줄링 하고 다음으로 