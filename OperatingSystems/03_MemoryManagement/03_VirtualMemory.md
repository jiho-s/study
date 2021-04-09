## Virtual Memory

프로그램이 컴푸터의 메모리보다 커지면서, 프로그램이 각각의 주소 공간을 page라는 단위로 나누고 필요한 페이지만 실제 메모리에 적재한다.

### Paging

프로그램 일부만 메모리에 있어도 실행가능, virtual memory를 사용하는 경우 바로 메모리에 접근하는 것이 아닌 MMU(Memory Management Unit)을 통해 접근하게 된다. 또한 vitual memory는 정해진 크기로 나뉘게 되는데, 이를 위해 실제 메모리도 page fram으로 나누어 적재한다.

### Page Tables

virtual address를 실제 address로 바꾸어주는 역할, virtual address를 vitual page number와 offset 두부분으로 나눈다, virtual page number에 해당하는 page tables에 가면 page frame number를 알수 있고 이를 offset과 합치면 실제 주소를 알수 있다.

###### 페이지 테이블 단점

- 내부 단편화가 있다.

  offset을 줄이면 페이지테이블의 크기가 커진다.

- 메모리에 두번 접근해야한다.

  페이지테이블에 먼저 접근후, 그 다음 실제 메모리에 접근해야한다.

### Speeding Up Paging

#### Translation Lookaside Buffers

하드웨어로 페이지테이블의 일부를 저장하고 있는 버퍼를 만든다. TLB에 먼저 접근하고 없으면, 페이지 테이블 접근

### Page Tables for Large Memories

페이지 테이블이 너무 크다는 문제가 여전히 남아있다.

#### Mulitilevel Page Tables

페이지 테이블을 여러 레벨로 만들어 실제 사용하고 있는 페이지 테이블만 만든다. 프로그램은 가운데가 비어있고 stack과 힙의 크기 증가에 따라 가운데 메모리가 채워진다. 페이지 테이블을 전체를 사용하는 경우 페이지 테이블은 증가하지만, 중간부분은 사용하지 않기 때문에 페이지 테이블의 크기를 줄일 수 있다.

#### Inverted page Tables

페이지 테이블의 크기를 실제 메모리의 크기 만큼 생성, 프로세스 전체당 하나의 페이지 테이블을 가진다. 매핑시 0번부터 순서데로 pid가 일치할때까지 찾은후 index번호가 실제 메모리 주소이다.