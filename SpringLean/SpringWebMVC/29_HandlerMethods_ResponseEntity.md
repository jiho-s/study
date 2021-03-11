## ResponseEntity

### ResponseEntity

- 응답 상태 코드, 응답 헤더, 응답 본문을 모두 설정할 수 있다

### ResponseEntity를 이용하여 파일 다운로드 구현

#### 파일 다운로드 응답 헤더에 설정할 내용

- Content-Disposition: 사용자가 해당 파일을 받을 때 사용할 파일 이름
- Content-Type: 어떤 파일인가
- Content-Length: 얼마나 큰 파일인가

#### Tika

파일의 Content-Type을 알아내는 라이브러리

```xml
<dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-core</artifactId>
    <version>1.24.1</version>
</dependency>
```

#### 파일 다운로드 구현

resource 안에 파일을 하나 넣어준다  @Autowired로 ResourceLoader를 주입받는다

```java
@Controller
public class FileController {

    @Autowired
    private ResourceLoader resourceLoader;

		//...

}
```

ResourceLoader를 이용해 파일을 받아오고 tika를 이용해 파일의 Content-Type을 가져왔다 ResponseEntity를 응답으로 설정하여 상태코드, 헤더, 바디를 설정 하였다

```java
@Controller
public class FileController {

    @Autowired
    private ResourceLoader resourceLoader;

		//...

		@GetMapping("/file/{filename}")
    public ResponseEntity<Resource> fileDownload(@PathVariable String filename) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + filename);
        File file = resource.getFile();

        Tika tika = new Tika();
        String mediaType = tika.detect(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\\"" + resource.getFilename() +"\\"")
                .header(HttpHeaders.CONTENT_TYPE, mediaType)
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                .body(resource);
    }
}
```