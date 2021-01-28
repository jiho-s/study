## Resource Interface

Spring의 `Resource` 인터페이스는 low-level 리소스에 접근하기 위한 더 나은 인터페이스이다.

```java
public interface Resource extends InputStreamSource {

    boolean exists();

    boolean isOpen();

    URL getURL() throws IOException;

    File getFile() throws IOException;

    Resource createRelative(String relativePath) throws IOException;

    String getFilename();

    String getDescription();
}
```

`Resource`는 `InputStreamSource`를 확장시킨 인터페이스이다.

```java
public interface InputStreamSource {

    InputStream getInputStream() throws IOException;
}
```

- `getInputStream()`

  리소스를 찾아서 열고,  리소스에서 읽기 위한 `InputStream`을 반환한다.

- `exists()`

  리소스가 실제 존재하는지 나타낸다.

- `isOpen()`

  리소스가 열려 있는지 확인

- `getDescription()`

  리소스에 대한 설명을 리턴한다. 보통 파일 이름이나 리소스의 실제 파일 경로를 리턴한다.

스프링은 `Resource` 추상화를 사용해 리소스가 필요할때 매개변수 타입으로 사용한다. 