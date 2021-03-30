### An AOP Example

동시성 문제로 인해 비즈니스 서비스 실행이 실패 할 수 있다. 만약, 작업이 재 시도되면 다음 시도에서 성공할 가능성이 높다. 이러한 조건에서 재 시도하는 것이 적절한 비즈니스 서비스 (충돌 해결을 위해 사용자에게 다시 돌아갈 필요가없는 연산)의 경우 클라이언트가 `PessimisticLockingFailureException` 보는 대신 투명하게 다시 시도하는것을 원한다. 이는 서비스 계층의 여러 서비스를 명확하게 구분하는 요구 사항이므로 Aspect를 통해 구현하는 데 이상적이다.

작업을 다시 시도하고 싶기 때문에 `proceed`여러 번 호출 할 수 있도록 around advice를 사용해야 한다.

```java
@Aspect
public class ConcurrentOperationExecutor implements Ordered {

    private static final int DEFAULT_MAX_RETRIES = 2;

    private int maxRetries = DEFAULT_MAX_RETRIES;
    private int order = 1;

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Around("com.xyz.myapp.CommonPointcuts.businessService()")
    public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
        int numAttempts = 0;
        PessimisticLockingFailureException lockFailureException;
        do {
            numAttempts++;
            try {
                return pjp.proceed();
            }
            catch(PessimisticLockingFailureException ex) {
                lockFailureException = ex;
            }
        } while(numAttempts <= this.maxRetries);
        throw lockFailureException;
    }
}
```

`Ordered` 구현은 트랜잭션 advice보다 더 높은 우선 순위를 설정할 수 있도록  한다 (재 시도 할 때마다 새로운 트랜잭션 생성). `maxRetries`및 `order`속성은 모두 Spring에 의해 구성된다. 주요 작업은 `doConcurrentOperation` around advice에서 발생한다 . 현재 재시도 논리를 각 `businessService()`. 진행을 시도하고으로 실패하면 `PessimisticLockingFailureException`모든 재시도 시도가 소진되지 않는 한 다시 시도합니다.

