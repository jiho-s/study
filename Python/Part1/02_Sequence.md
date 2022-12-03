## Chapter2 시퀀스

리스트부터 문자열 및 파이썬3에 새로 소개된 바이트 자료형에 이르기까지 주로시퀀스에 헤당하는 내용을 설명한다

### 2.1 내장 시퀀시 개요

다음과 같은 시퀀스형을 제공

- 컨테이너시퀀스
  - 서로 다른 자료형의 항목을 담을 수 있는 list, tuple, collections.deque
- 균일 시퀀스
  - 단하나의 자료형만 담을 수 있는 str, bytes, bytearray, memory view, array.arry

컨테이너 시퀀스 객체에 대한 참조를 담고 있음, 균일 시퀀스는 항목의 값을 직접 담고 기본적인 자료형만 저장 가능

- 가변 시퀀스
  - list, bytearray, array.array, collections.deque, memptryview 형
- 불변 시퀀스
  - Tuple, str, bytes 형

### 2.2 지능형 리스트와 제너레이터 표현식

#### 2.2.1 지능형 리스트와 가독성

```python
symbols = '!@#$%'
codes = []
for symbol in symbols:
    codes.append(ord(symbol))
print(codes)
```

```python
symbols = '!@#$%'
codes = [ord(symbol) for symbol in symbols]
print(codes)
```

아래처럼 사용하는 것이 지능형 리스트로 for 문에서 리스트를 만드는 일만 할 경우에 간편하게 만들 수 있다 두줄이상 넘어가면 for 문을 이용해서 작성하는 것이 더 낫다

> 지능형 리스트는 변수를 가리지 않는다
>
> 특정 스코프로 바뀌어 이전에 밖에서 사용했던 변수명을 다시 사용해도 된다

#### 2.2.2 지능형 리스트와 map()/filter() 비교

Map()/filter() 를 조합한 방법이 지능형 리스트보다 빠르지 않다

#### 2.2.3 데카르트 곱

지능형 리스트는 두 개 이상의 반복 가능한 자료형의 데카르트 곱을 나타내는 일련의 리스트를 만들 수 있다.

```python
colors = ['black', 'white']
sizes = ['S', 'M', 'L']
tShirts = [(color, size) for color in colors for size in sizes]
```

#### 2.2.4 제너레이터 표현식

다른 생성자에 전달할 리스트를 통쨰로 만들지 않고 반복자 프로토콜을 이용해 항목을 하나씩 생성하는 제너레이터 표현식을 통해 메모리를 더 적게 사용한다.

제너레이터 표현식은 지능형 리스트와 동일한 구문을 사용하지만, 대괄호 대신 괄호를 사용한다

```python
colors = ['black', 'white']
sizes = ['S', 'M', 'L']
for tShirt in ('%s %s' % (c, s) for c in colors for s in sizes):
    print(tShirt)
```

iterator 이므로 리스트를 안만들고 하나씩 전달한다. 메모리를 더 적게 사용한다. 제너레이터가 작동하는 방식은 14장에서 자세히

### 2.3 튜플은 단순한 불변 리스트가 아니다

튜플은 불변 리스트로도 사용할 수도 있지만 필드명이 없는 레코드로도 사용할 수도 있다.

#### 2.3.1 레코드로서의 튜플

튜플을 필드의 집합으로 사용하는 경우 항목수가 고정되어 있고 항목의 순서가 중요하다.

#### 2.3.2 튜플 언패킹

