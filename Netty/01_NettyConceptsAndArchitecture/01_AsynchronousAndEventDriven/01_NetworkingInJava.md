## 1.1 자바의 네트워킹

최초의 자바 네트워킹 API(java.net)은 네이티브 시스템 소켓 라이브러리가 제공하는 블로킹 함수만 지원했다.

```java
ServerSocket serverSocket = new ServerSocket(80); // 새로운 'ServerSocket'이 지정된 포트에서 연결 요청을 수신한다.
Socket clientSocket = serverSocket.accept(); // accept() 호출은 연결될 때까지 진행을 블록킹한다.
BufferedReader in = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream())
); // 소켓으로부터 스트림객체를 파생한다.
PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
String request, response;
while ((request = in.readLine()) != null) { // 처리 루프를 시작한다.
    if ("Done".equals(request)) break;
    response = "response";
    out.println(response); // 서버의 응답이 클라이언트로 전달됬다
}
```

이 코드는 한 번에 한 연결만 처리한다. 다수의 클아이언트를 동시에 관리하려면 새로운 클라이언트 `Socket` 마다 새로운 `Thread`를 할당해야 한다.

##### 단점

- 여러 스레드가 입력이나 출력 데이터가 들어오기를 기다리며 무한정 대기 상태로 유지될수 있다
- 각 스레드가 스택 메모리를 할당해야하는데 64KM에서 1MB까지 차지할 수 있다.
-  JVM이 물리적으로 아주 많은 수의 스레드를 지원할 수 있지만, 동시 접속이 한계에 이르기 훨씬 전부터 컨텍스트 전환에 따른 오버헤드가 심각한 문제가 될 수 있다.

10만 이상의 동시 연결을 지원해야 할 때는 적합하지 않다.

### 1.1.1 자바 NIO

네이티브 소켓 라이브러리에는 오래 전 부터 네트워크 리소스 사용률을 세부적으로 제어할 수 있는 논블록킹 호출이 포함돼 있다.

- `setsockopt()`를 이용해, 진행을 블로킹할 상황에서 읽기/쓰기 호출이 즉시 반환하도록 소켓을 구성할 수 있다.
- 이벤트 통지 API를 이용해 논블록킹 소켓의 집합을 등록하면 읽거나 기록할 데이터가 준비됐는지 여부를 알 수 있다.

논블로킹 입출력을 위한 자바 지원 기능은 JDK 1.4 패키지의 java.nio와 함께 도입됬다.

### 1.1.2 셀렉터

`java.nio.channels.Selector` 클래스는 논블로킹 `Socket`의 집합에서 입출력이 가능한 항목을 지정하기 위해 이벤트 통지 API를 이용한다. 언제든지 읽기나 쓰기 작업의 완료 상태를 확인할 수 있으므로 한 스레드로 동시에 여러 연결을 처리할 수 있다.

- 적은 수의 스레드로 더 많은 연결을 처리할 수 있으므로 메모리 관리와 컨텍스트 전환에 따르는 오버헤드 감소
- 입출력을 처리하지 않을 때는 스레드를 다른 작업에 활용할 수 있다.

