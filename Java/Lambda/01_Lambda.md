## Lambda Expressions

> #### 참고자료
>
> [람다 표현식 레퍼런스](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
>
> [람다 지역변수 제한](https://jeong-pro.tistory.com/211)

### Lambda Expressions

익명클래스에서 클래스 구현을 이름을 정하지 않고 구현할수 있다. 이름이 지정된 클래스보다 간결하지만 메서드가 하나 뿐인 클래스의 경우 익명 클래스도 번거롭다. 람다 표현식을 사용하면 단일 메서드 클래스의 인스턴스를 보다 간결하게 표현할 수 있다.

### 람다 식의 구문

람다식은 다음으로 구성된다

- 매개 변수 목록
- 화살 토큰 `->`
- 본문

#### 매개 변수 목록

괄호로 묶인 쉼표로 구분, 람다식에서 매개 변수의 데이터 유형을 생략할 수 있고 또한 매개변수가 하나만 있는 경우 괄호를 생략 할 수 있다.

```java
p-> p.getGender () == Person.Sex.MALE 
    && p.getAge ()> = 18
    && p.getAge () <= 25
```

#### 화살토큰

`->`

#### 본문

단일 표현식 또는 명령문 블록으로 구성된다. 단일 표현식을 지정하면 런타임시 표현식을 실행한후 해당 값을 반환한다. 또는 return문을 사용할 수 있다.

```java
p-> {
    return p.getGender () == Person.Sex.MALE
        && p.getAge ()> = 18
        && p.getAge () <= 25;
}
```

### 바깥 영역의 로컬 변수 접근

람다 식은 변수를 캡쳐할수 있다. 따라서 바깥 영역의 로컬 변수에 접근할 수 있다. 하지만 람다식은 새로운 수준의 범위 지정을 의미하지 않고 단지 둘러쌰인 환경을 만든다는 것을 의미한다.

```java
class FirstLevel {

        public int x = 1;

        void methodInFirstLevel(int x) {
            
            // The following statement causes the compiler to generate
            // the error "local variables referenced from a lambda expression
            // must be final or effectively final" in statement A:
            //
            // x = 99;
            
            Consumer<Integer> myConsumer = (y) -> 
            {
                System.out.println("x = " + x); // Statement A
                System.out.println("y = " + y);
                System.out.println("this.x = " + this.x);
                System.out.println("LambdaScopeTest.this.x = " +
                    LambdaScopeTest.this.x);
            };

            myConsumer.accept(x);

        }
    }

    public static void main(String... args) {
        LambdaScopeTest st = new LambdaScopeTest();
        LambdaScopeTest.FirstLevel fl = st.new FirstLevel();
        fl.methodInFirstLevel(23);
    }
}
```

람다식에서 `methodFirstLevel()`의 x에 직접 접근하고 바깥 클래스의 변수에 접근하려면 `this` 키워드를 이용해 `FirstLevel.x`에 접근할 수 있다.

익명 클래스와 마찬가지로 람다 표현식은 `final`로 선언되거나 수정되지 않는 변수 및  매개변수에만 접근할 수 있다. 

```java
void methodInFirstLevel (int x) {
     x = 99;
    // ...
}

```

`x`의 값이 변경되는 코드가 있는 경우 오류가 발생하게 된다.

### 타겟 타입

람다식의 타입은 자바 컴파일러가 람다식이 발견된 상황 또 상황의 대상 타입을 사용한다. 자바 컴파일러가 대상 타겟을 결정할 수 있는 상황에서만 람다 표현식을 사용할 수 있다.

### 람다 식에서 final 처럼 쓰이고 있는 로컬 변수에만 접근할 수 있는 이유

람다는 로컬 변수를 사용시 자유 변수의 복사본을 만들어 접근을 허용하도록 하고 이를 람다 캡쳐링이라고 한다.

람다 캡쳐링에 의해 복사된 참조 값을 변경하는 코드는 람다 실행 시점에 따라 복사된 참조 값이 어떤 값인지 예측할 수 없기 때문에 final 또는 effectively final인 로컬 변수만 접근할 수 있다.