## Deadlock Detection And Recovery

데드락이 발생한는것을 막지 못하지만, 데드락이 발생시 복구하는 전략을 취한다.

### Deadlock Detection with One Resource of Each Type

자원이 하나인 경우, 사이클이 만들어지면 데드락이 발생한다.

### Deadlock Detection with Multiple Resources of Each Type

자원이 여러개인 경우, 현재 할당된 행렬, 요청 행렬을 만들고 전체 리소스 백터, 이용가능한 리소스 백터를 만든다. 이를 이용해 데드락이 발생했는지 알 수 있다.

### Recovery from Deadlock

#### Recovery through Preemption

자원을 강제로 해제한다. 자원의 속성에 따라 할수 없을수도 있다.

#### Recovery through Rollback

check point로 이동하여 될때까지 다시 실행한다.

#### Recovery through Killing Processes

그냥 강제 종료한다.

