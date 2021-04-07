## A Memory Abstraction: Address Spcaces

##### 물리 메모리 노출의 문제점

- operating system에 접근해서 쉽게 바꿀수 있다.
- 여러 프로그램을 동시에 실행시키는데에 어려움이 있다.

### The Notion of an Address Space

##### Address Space

프로세스별 사용할 수 있는 추상적인 메모리 공간.

#### Base and Limit Registers

**dynamic relocation**

base 와 limit 레지스터를 이용해 로드시는 location이 없고 실행시 base 레지스터의 값을 더해주어 올바른 메모리에 접근할수 있게 한다.

###  Swapping

실행시 모두 메모리에 올라가야 한다. 메모리가 가득차면, 프로그램 하나 전체를 다시 디스크로 보내 시간이 많이 걸리고, 또한, segmantation이 발생한다. 또한 프로그램이 실행하면서, 메모리가 점점 증가하는 경우를 처리할 수 없다.

### Managing Free Memory

#### Memory Management with Bitmaps

메모리를 비트맵으로 관리, allocation unit이 커지면, 내부 단편화, 작아지면 비트맵이 커진다.

#### Memory Management with Linked Lists

allocation unit에 따른 내부 단편화가 없다.

###### 메모리 할당 전략

first fit, next fit, best fit, worst fit, quick fit

- first fit

  내부 단편화가 발생하지만 가장 효율적

- quick fit

  같은 크기의 공간끼리 묶어논다.