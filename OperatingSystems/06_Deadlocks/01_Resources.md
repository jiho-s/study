## Resources

Resource는 하드웨어 디바이스, 데이터베이스에 저장된 정보 등이 될수 있다.

### Preemptable and Nonpreemptable Resources

- Preemptable Resource

  프로세스가 사용중에 다른 프로세스가 가져가는 경우 실패가 없는경우(메모리 등)

- Nonpreemptable Resource

  프로세스가 사용중에 다른 프로세스가 가져가는 경우 실패할 수 있는 자원

Deadlock은 nonpreemptable 자원에서 발생한다.

프로세스는 자원 요청이 실패하면 자동으로 블록되고, 다시 사용가능하면 깨어난다.