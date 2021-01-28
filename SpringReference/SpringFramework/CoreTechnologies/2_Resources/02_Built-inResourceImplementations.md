## 기본 `Resource` 구현체

-  [UrlResource](#urlresource)
-  [ClassPathResource](#classpathresource)
-  [FileSystemResource](#filesystemresource)
-  [ServletContextResource](#servletcontextresource)
-  [InputStreamResource](#inputstreamresource)
-  [ByteArrayResource](#bytearrayresource)

### `UrlResource`

`java.net.URL`을 래핑 한 `UrlResource`는 파일, HTTP 타겟, FTP 타겟 같은 URL로 접근 가능한 모든 객체에 접근하는데 사용할 수 있다. prefix가 url의 타입을 나타낸다. `file:`은  파일시스템 경로를 나타내고 `http:`는 HTTP 프로토콜을 통한 리소스 접근, `ftp:`는 FTP를 통한 리소스 접근을 나타낸다.

### `ClassPathResource`

classpath를 통해 리소스를 얻어야 하는 것을 나타낸다.

`ClassPathReousrce`는 클래스 패스 리소스가 파일 시스템안에 있고 jar 파일에 있지않고 파일 시스템으로 확장되지 않는 경우 `java.io.File`로 매칭을 지원한다.

### `FileSystemResource`

이 구현체는 `java.io.File` 과 `java.nio.file.Path`를 다룬다.

### `ServletContextResource`

이 구현체는 웹 어플리케이션의 루트 디렉토리에서 상대적인 경로를 해석하는 `ServletContext` 리소스에대한 구현체이다.

### `InputStreamResource`

`InputStreamResource`는 `InputStream`에 대한 구현체이다.  특정 `Resource` 구현이 적용되지 않은 경우 사용해야 한다. 

다른 구현체와 달리 이미 열린 리소스에 대한 객체이다. `isOpen()`의 값은 항상 참이다.

### `ByteArrayResource`

주어진 바이트 배열에 대한 구현체로 주어진 바이트 배열로 부터 `ByteArrayInputStream `을 만든다.

`InputStremaResource`의 단일 사용을 사용하지 않고 주어진 바이트 배열에서 콘텐츠를 로드하는대 유용하다.