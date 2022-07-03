# Chapter 01 도메인 모델 시작하기

## 1.1 도메인

소프트웨어로 해결하고자 하는 문제 영역을 도메인 이라하고 하고 한 도메인은 다시 하위 도메인으로 나눌수 있다.

특정 도메인을 위한 소프트웨어라고 해서 도메인이 제공해야 할 모든 기능을 직접 구현하는 것은 아니다

## 1.2 도메인 전문가와 개발자 간 지식 공유

개발자는 요구사항을 분석하고 설계하여 코드를 작성하며 테스트하고 배포한다. 코딩에 앞서 요구사항을 올바르게 이해하는 것이 중요하다.

## 1.3 도메인 모델

특정 도메인을 개념적으로 표현한것 클래스 다이어그램이나 상태 다이어그램과 같은 UML, 그래프, 수학 공식 등을 활용해 도메인 모델을 만들수 있다.

## 1.4 도메인 모델 패턴

| 영역                        | 설명                                                         |
| --------------------------- | ------------------------------------------------------------ |
| 사용자 인터페이스 또는 표현 | 사용자 요청을 처리하고 사용자에게 정보를 보여줌              |
| 응용                        | 사용자가 요청한 기능을 실행                                  |
| 도메인                      | 시스템이 제공할 도메인 규칙을 구현                           |
| 인프라스트럭쳐              | 데이터베이스나 메시징 시스템과 같은 외부 시스템과의 연동을 처리한다 |

도메인 모델과 관련된 중요 업무는 도메인 모델에만 위치시켜야 규칙이 바뀌거나 규칙을 확장할때 다른 코드에 영향을 덜 주고 변경 내역을 모델에 반영할 수 있다

## 1.5 도메인 모델 도출

도메인을 모델링 할 때 기본이 되는 작업은 모델을 구성하는 핵심 구성요소, 규칙, 기능을 찾는 것이다. 이 과정은 요구사항에서 출발한다

> - 최소 한 종류 이상의 상품을 주문해야 한다
> - 한 상품을 한 개 이상 주문할 수 있다.
> - 총 주문 금액은 각 상품의 구매 가격 합을 모두 더한 금액이다
> - 각 상품의 구매 가격 합은 상품 가격에 구매개수를 곱한 값이다
> - 주문할 때 배송지 정보를 반드시 지정해야한다.
> - 배송지 정보는 받는 사람 이름, 전화번호, 주소로 구성된다
> - 출고를 하면 배송지를 변경할 수 없다
> - 출고 전에 주문을 취소할 수 있다
> - 고객이 결제를 완료하기 전에는 상품을 준비하지 않는다

```kotlin
package order

class Order private constructor(
    // 최소 한 종류 이상의 상품을 주문해야 한다.
    private val orderLines: List<OrderLine>,
    // 주문할 때 배송지 정보를 반드시 지정해야한다
    private var shippingInfo: ShippingInfo,

    private var status: Status
) {

    constructor(orderLines: List<OrderLine>, shippingInfo: ShippingInfo) : this(
        orderLines,
        shippingInfo,
        Status.PAYMENT_WAITING
    )

    init {
        // 최소 한 종류 이상의 상품을 주문해야 한다
        require(orderLines.isNotEmpty()) {
            "최소 한 종류 이상의 상품을 주문해야 한다"
        }
    }
    
    fun editShippingInfo(shippingInfo: ShippingInfo) {
        require(status.isBeforeShiping()) {
            " 출고를 하면 배송지를 변경할 수 없다"
        }
        this.shippingInfo = shippingInfo
    }
    
    fun cancel() {
        require(status.isBeforeShiping()) {
            "출고 전에 주문을 취소할 수 있다"
        }
        this.status = Status.CANCELED
    }
    
    


    // 총 주문 금액은 각 상품의 구매 가격 합을 모두 더한 금액이다
    val totalOrderPrice: Long by lazy { orderLines.sumOf { it.orderLinePrice } }

    enum class Status {
        PAYMENT_WAITING, PREPARING, SHIPPED, DELIVERING, COMPLETED, CANCELED;

        fun paymentCompleted(): Boolean {
            return this != PAYMENT_WAITING;
        }

        fun isBeforeShiping(): Boolean {
            return when (this) {
                PAYMENT_WAITING, PREPARING -> true
                SHIPPED, DELIVERING, CANCELED, COMPLETED -> false
            }
        }
    }


}

data class OrderLine(
    val product: Product,
    // 한 상품을 한 개 이상 주문 할 수 있다
    val count: Int,
) {
    init {
        require(count > 0) {
            "상품을 한 개 이상 주문할 수 있다"
        }
    }

    val orderPrice: Long get() = TODO()

    // 각 상품의 구매 가격 합은 상품 가격에 구매개수를 곱한 값이다
    val orderLinePrice: Long by lazy { orderPrice * count }
}

data class Product(
    val id: Long,
    val price: Long,
)

// 배송지 정보는 받는 사람 이름, 전화번호, 주소로 구성된다
data class ShippingInfo(
    val name: String,
    val phoneNumber: String,
    val address: Any,
)
```

## 1.6 엔티티와 밸류

도출한 모델은 크게 엔티티와 밸류로 구분할 수 있다

### 1.6.1 엔티티

식별자를 가진다

### 1.6.2 엔티티의 식별자 생성

- 특정 규칙에 따라 생성
- UUID Nano ID 같은 고유 식별자 생성기
- 값을 직접 입력
- 일련번호 사용

### 1.6.3 밸류 타입

밸류 타입은 개념적으로 완전한 하나를 표현할 때 사용한다

```kotlin
data class ShippingInfo(
    val name: String,
    val phoneNumber: String,
    val address: String,
    val shippingZipcode: String
)
```

위에서 Receiver 와 Address 로 묶을 수 있다

```kotlin
data class Address(
  val address: String,
  val shippingZipcode: String,
)
```

```kotlin
data class Receiver(
  val name: String,
  val phoneNumber: String,
)
```

밸류 타입이 꼭 두개 이상의 데이터를 가져야 하는 것은 아니다. 의미를 명확하게 표현하기 위해 밸류 타입을 사용하는 경우도 있다

```kotlin
@JvmInline
value class Money(
    val value: Long
) {

    operator fun plus(other: Money): Money {
        return Money(this.value + other.value)
    }

    operator fun times(count: Int): Money {
        return Money(this.value * count)
    }
}
```

의미를 명확하게 표현하기 위해서 사용할 수 도 있다

```kotlin
data class OrderLine(
    val product: Product,
    // 한 상품을 한 개 이상 주문 할 수 있다
    val count: Int,
) {
    init {
        require(count > 0) {
            "상품을 한 개 이상 주문할 수 있다"
        }
    }

    val orderPrice: Money get() = TODO()

    // 각 상품의 구매 가격 합은 상품 가격에 구매개수를 곱한 값이다
    val orderLinePrice: Money by lazy { orderPrice * count }
}
```

Money 처럼 데이터 변경 기능을 제공하지 않는 타입을 불변이라고 표현한다.

### 1.6.5 도메인 모델에 set 메서드 넣지 않기

불변 밸류 타입을 사용하면 자연스럽게 밸류 타입에는 set 메서드를 구현하지 않는다

### 1.7 도메인 용어와 유비쿼터스 언어

도메인에서 사용하는 용어를 코드에 반영하지 않으면 그 코드는 개발자에게 코드의 의미를 해석해야 하는 부담을 준다

```kotlin
enum class Status {
  STEP1, STEP2, STEP3;
}
```

알맞은 영단어를 찾는 것은 쉽지 않은 일이지만 시간을 들여 찾는 노력을 해야한다