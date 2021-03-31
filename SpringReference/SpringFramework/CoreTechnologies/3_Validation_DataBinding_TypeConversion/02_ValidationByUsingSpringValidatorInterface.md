## Validation by Using Spring’s Validator Interface

Spring은 객체의 유효성을 검사하는 데 사용할 수 있는 `Validator`인터페이스를 제공한다.  `Validator`인터페이스는  `Errors` 객체를 사용하여 검증하는동안, `validator` 는 `Errors` 객체에 검증 실패를 보고할수 있다.

먼저, 다음과 같은 간단한 데이터 객체가 있다.

```java
public class Person {

    private String name;
    private int age;

    // the usual getters and setters...
}
```

다음 예제는 `org.springframework.validation.Validator` 인터페이스의 다음 두 메서드를 구현하여 `Person` 클래스에 대한 유효성 검증 과정을 보여준다.

- `supports(Class)`: `Validator `가 해당 `Class`의 인스턴스의 유효성을 검사 할 수 있는지
- `validate(Object, org.springframework.validation.Errors)`: 주어진 객체의 유효성을 검사하고 유효성 검사 오류가 발생한 경우 해당 오류를 지정된  `Errors` 객체에 등록한다.

`Validator` 구현은 Spring Framework가 제공 하는 도우미 클래스  `ValidationUtils`를 사용하면 매우 간단하다 . 다음 예제는 `Person`인스턴스에 대한 `Validator`를 구현한다.

```java
public class PersonValidator implements Validator {

    /**
     * This Validator validates only Person instances
     */
    public boolean supports(Class clazz) {
        return Person.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        Person p = (Person) obj;
        if (p.getAge() < 0) {
            e.rejectValue("age", "negativevalue");
        } else if (p.getAge() > 110) {
            e.rejectValue("age", "too.darn.old");
        }
    }
}
```

`ValidationUtils` 클래스 의 `static` `rejectIfEmpty(..)`메서 는 `name`속성이 `null` 이거나 빈 문자열 인 경우 `name`  속성 을 거부하는 데 사용된다.

단일 `Validator` 클래스를 구현하여 객체의 각 중첩 객체를 검증 하는 것이 가능하지만 중첩 된 객체 클래스 각각에 대한 `Validator` 구현으로 검증 로직을 캡슐화하는 것이 더 좋다 . `Customer` 는 두 개의 `String` 속성 (첫 번째 이름과 두 번째 이름)과 복잡한 `Address` 객체로 구성된 객체이다 . `Address` 객체는 `Customer`객체와 독립적으로 사용될 수 있으므로 별개의 `AddressValidator` 가 구현되었다.  `CustomerValidator`가 복사-붙여넣기 의지 없이  `AddressValidator`에 포함 된 로직을 재사용 하기 위해서는, `CustomerValidator` 내에 `AddressValidator`를 의존성 주입 또는 인스턴스화 하여 사용할 수 있다.

```java
public class CustomerValidator implements Validator {

    private final Validator addressValidator;

    public CustomerValidator(Validator addressValidator) {
        if (addressValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is " +
                "required and must not be null.");
        }
        if (!addressValidator.supports(Address.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must " +
                "support the validation of [Address] instances.");
        }
        this.addressValidator = addressValidator;
    }

    /**
     * This Validator validates Customer instances, and any subclasses of Customer too
     */
    public boolean supports(Class clazz) {
        return Customer.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "field.required");
        Customer customer = (Customer) target;
        try {
            errors.pushNestedPath("address");
            ValidationUtils.invokeValidator(this.addressValidator, customer.getAddress(), errors);
        } finally {
            errors.popNestedPath();
        }
    }
}
```


