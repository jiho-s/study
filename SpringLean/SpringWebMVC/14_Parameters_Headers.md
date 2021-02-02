### 헤더와 파라미터 맵핑

#### 특정한 헤더가 있는 요청을 처리하고 싶은 경우: @RequestMapping(header = "key")

```java
@Controller
public class SampleController {

    @RequestMapping(value = "/hello", headers = HttpHeaders.FROM) 
    // "From"으로 작성해도 됨
    @ResponseBody // 없으면 이름에 해당하는 뷰로 돌아간다, 있으면 문자열을 응답으로 보냄
    public String hello() {
        return "hello";
    }

}
```

```java
@ExtendWith(SpringExtension.class)
@WebMvcTest //mock을 주입해준다
class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void helloTest() throws Exception{
        mockMvc.perform(get("/hello")
                    .header(HttpHeaders.FROM,"localhost")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }
}
```

#### 특정한 헤더가 없는 요청을 처리하고 싶은 경우: @RequestMapping(header = "!key")

```java
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

    @RequestMapping(value = "/hello", headers = "!" + HttpHeaders.FROM)
    // "From"으로 작성해도 됨
    @ResponseBody // 없으면 이름에 해당하는 뷰로 돌아간다, 있으면 문자열을 응답으로 보냄
    public String hello() {
        return "hello";
    }

}
```

#### 특정한 헤더 키/값이 있는 요청을 처리하고 싶은 경우: @RequestMapping(header = "key=value")

```java
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

    @RequestMapping(value = "/hello", headers = HttpHeaders.FROM +"=localhost")
    // "From"으로 작성해도 됨
    @ResponseBody // 없으면 이름에 해당하는 뷰로 돌아간다, 있으면 문자열을 응답으로 보냄
    public String hello() {
        return "hello";
    }

}
```

```java
@ExtendWith(SpringExtension.class)
@WebMvcTest //mock을 주입해준다
class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void helloTest() throws Exception{
        mockMvc.perform(get("/hello")
                    .header(HttpHeaders.FROM,"localhost")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }
}
```

#### 특정한 요청 매개변수 키를 가지고 있는 요청을 처리하고 싶은경우: @RequestMapping(params="a")

parmas에 해당하는 매개변수 key가 있어야한다(/hello?name=jiho)

```java
@Controller
public class SampleController {

    @RequestMapping(value = "/hello", params = "name")
    // "From"으로 작성해도 됨
    @ResponseBody // 없으면 이름에 해당하는 뷰로 돌아간다, 있으면 문자열을 응답으로 보냄
    public String hello() {
        return "hello";
    }

}
```

```java
@ExtendWith(SpringExtension.class)
@WebMvcTest //mock을 주입해준다
class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void helloTest() throws Exception{
        mockMvc.perform(get("/hello")
                .param("name", "hongik")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hello"));
    }
}
```

#### 특정한 요청 매개변수가 없는 요청을 처리하고 싶은 경우: @RequestMapping(params="!a")

#### 특정한 요청 매개변수 키/값을 가지고 있는 요청을 처리하고 싶은 경우: @RequestMapping(params="a=b")

```java
@Controller
public class SampleController {

    @RequestMapping(value = "/hello", params = "name=hongik")
    // "From"으로 작성해도 됨
    @ResponseBody // 없으면 이름에 해당하는 뷰로 돌아간다, 있으면 문자열을 응답으로 보냄
    public String hello() {
        return "hello";
    }

}
```