## 04 유스케이스 구현하기

### 도메인 모델 구현하기

```java
public record Account(AccountId id, Money baselineBalance, ActivityWindow activityWindow) {

  /**
   * Creates an {@link Account} entity without an ID. Use to create a new entity that is not yet
   * persisted.
   */
  public static Account withoutId(Money baselineBalance, ActivityWindow activityWindow) {
    return new Account(null, baselineBalance, activityWindow);
  }

  /**
   * Creates an {@link Account} entity with an ID. Use to reconstitute a persisted entity.
   */
  public static Account withId(AccountId accountId, Money baselineBalance,
      ActivityWindow activityWindow) {
    return new Account(accountId, baselineBalance, activityWindow);
  }

  public Optional<AccountId> getId() {
    return Optional.ofNullable(this.id);
  }

  /**
   * Calculates the total balance of the account by adding the activity values to the baseline
   * balance.
   */
  public Money calculateBalance() {
    return Money.add(this.baselineBalance, this.activityWindow.calculateBalance(this.id));
  }

  /**
   * Tries to withdraw a certain amount of money from this account. If successful, creates a new
   * activity with a negative value.
   *
   * @return true if the withdrawal was successful, false if not.
   */
  public boolean withdraw(Money money, AccountId targetAccountId) {

    if (!mayWithdraw(money)) {
      return false;
    }

    Activity withdrawal = new Activity(this.id, this.id, targetAccountId, LocalDateTime.now(),
        money);
    this.activityWindow.addActivity(withdrawal);
    return true;
  }

  private boolean mayWithdraw(Money money) {
    return Money.add(this.calculateBalance(), money.negate()).isPositiveOrZero();
  }

  /**
   * Tries to deposit a certain amount of money to this account. If sucessful, creates a new
   * activity with a positive value.
   *
   * @return true if the deposit was successful, false if not.
   */
  public boolean deposit(Money money, AccountId sourceAccountId) {
    Activity deposit = new Activity(this.id, sourceAccountId, this.id, LocalDateTime.now(), money);
    this.activityWindow.addActivity(deposit);
    return true;
  }

  public record AccountId(Long value) {

  }
}
```

### 유스케이스 둘러보기

일반적으로 유스케이스는 다음과 같은 단계를 따른다

1. 입력을 받는다
2. 비즈니스 규칙을 검증한다
3. 모델 상태를 조작한다
4. 출력을 반환한다

유스케이스 코드가 도메인 로직에만 신경 써야 하고 입력 유효성 검증으로 오염되면 안된다

그러나 유스케이스는 비즈니스 규칙을 검증할 책임이 있다

### 입력 유효성 검증

유효성 검증은 유스케이스 클래스의 책임이 아니라고 이야기하긴 했지만 애플리케이션 계층의 책임에 해당한다

입력 모델이 이문제를 다루도록 한다 생성자 내에서 입력 유효성을 검증할 것이다

```java
public abstract class SelfValidating<T> {

  private final Validator validator;
  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

  public SelfValidating() {
    this.validator = factory.getValidator();
  }

  @SuppressWarnings("unchecked")
  protected void validateSelf() {
    Set<ConstraintViolation<T>> violations = validator.validate((T) this);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
```

별로 인거 같음 validation 을 위해 구지 상속을 유틸로 충분히 할수 있다고 생각함

```java
public class ValidateUtils {

  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

  private static final Validator validator = factory.getValidator();

  public static  <T> void validate(T clazz) {
    Set<ConstraintViolation<T>> violations = validator.validate(clazz);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
```



### 생성자의 힘

빌더를 사용하는 방법도 있지만 필드를 새로 추가하는 상황에서 빌더를 호출하는 코드에 새로운 필드를 추가하는 것을 잊을수 도 있다

### 유스케이스마다 다른 입력 모델

각 유스케이스 전용 입력 모델은 유스케이스를 훨씬 명확하게 만들고 다른 유스케이스와의 결합도 제거해서 불필요한 부수효과가 발생하지 않게 한다

### 비즈니스 규칙 검증하기

입력 유효성을 검증하는 것은 구문상의 유효성을 검증하는 것이라고도 할수 있다 반면 비즈니스 규칙은 유스케이스의 맥락 속에서 의미적인 유효성을 검증하는 일이라고 할 수 있다

### 풍부한 도메인 모델 vs 빈약한 도메인 모델

풍부한 도메인 모델의 경우 엔티티에 비즈니스 규칙이 있고 빈약한 도메인 모델에서는 유스케이스 클래스에 구현되어 있고

각갖의 필요에 맞는 스타일을 자유롭게 택해서 사용하면 된다.

### 유스케이스마다 다른 출력 모델

입력과 비슷하게 출력도 가능하면 각 유스케이스에 맞게 구체적일수록 좋다

### 읽기 전용 유스케이스는 어떨까?

쿠리를 위한 인커밍 전용 포트를 만들고 이를 쿼리 서비스 에 구현하는것

### 유지보수 가능한 소프트웨어를 만드는 데 어떻게 도움이 될까?

꼼꼼한 입력 유효성 검증, 유스케이스별 입출력 모델은 지속 가능한 코드를 만드는 데 큰 도움이 된다