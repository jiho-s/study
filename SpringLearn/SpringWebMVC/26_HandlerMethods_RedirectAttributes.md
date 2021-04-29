## RedirectAttributes

### Redirect시 Model의 값 전달

- 스프링 MVC에서는 Redirect 할 때 기본적으로 Model에 들어있는 primitive type 데이터(String, Integer)는 URI 쿼리 매개변수("/events/list?name=hihi&limit=10")에 추가된다

- 스프링 부트에서는 이 기능이 기본적으로 비활성화 되어있다

  Ignore-default-model-on-redirect 프로퍼티를 사용해서 활성화 할 수 있다

  [application.propertie](http://application.properties)s에 다음 값을 추가한다

  ```
  spring.mvc.ignore-default-model-on-redirect=false
  ```

  Redirect시 URI는 "/event/list?name=aaa&limit=111" 형태가 된다

  ```java
  @PostMapping("/events/form/limit")
      public String eventsFormLimitSubmit(@Validated @ModelAttribute Event event,
                                         BindingResult bindingResult,
                                         SessionStatus sessionStatus,
                                          Model model) {
          if (bindingResult.hasErrors()) {
              return "/events/form-limit";
          }
          model.addAttribute("name", event.getName());
          model.addAttribute("limit", event.getLimit());
          sessionStatus.setComplete();
          return "redirect:/events/list";
      }
  ```

### RedirectAttributes

- 모델에 있는 모든 데이터가 아닌 원하는 데이터만 전달하고 싶을때 사용한다
- Redirect 요청을 처리하는 곳에서는 쿼리 매개변수를 @RequestParam 또는 @ModelAttribute로 받을 수 있다

아규먼트로 RedirectAttributes를 받아와 addAttribute 메소드로 값을 넣는다

```java
		@PostMapping("/events/form/limit")
    public String eventsFormLimitSubmit(@Validated @ModelAttribute Event event,
                                        BindingResult bindingResult,
                                        SessionStatus sessionStatus,
                                        RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "/events/form-limit";
        }
        attributes.addAttribute("name", event.getName());
        sessionStatus.setComplete();
        return "redirect:/events/list";
    }
```

쿼리 매개변수를 @RequestParam으로 받아 왔다

```java
		@GetMapping("/events/list")
    public String getEvents(@RequestParam String name, Model model,
                            @SessionAttribute LocalDateTime visitTime) {
        System.out.println(visitTime);
        Event event = new Event();
        event.setName(name);
        event.setLimit(10);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);

        model.addAttribute(eventList);

        return "/events/list";
    }
```

@ModelAttribute로 값을 가져오는 경우 @SessionAttributes의 key값과 달라야 한다

```java
@Controller
@SessionAttributes("event")
public class SampleController {
		//...
		@GetMapping("/events/list")
    public String getEvents(@ModelAttribute("newEvent") Event event, Model model,
                            @SessionAttribute LocalDateTime visitTime) {
        System.out.println(visitTime);
        Event event = new Event();
        event.setName(name);
        event.setLimit(10);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);

        model.addAttribute(eventList);

        return "/events/list";
    }
}
```