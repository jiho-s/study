## FlashAttributes

### FlashAttributes

- Redirect시 데이터를 복합적인 객체로 전달할 때 사용한다
- 데이터가 URI에 노출되지 않는다
- 임의의 객체를 저장할 수 있다
- Redirect전에 데이터를 HTTP Session에 저장하고 Redirect 요청을 처리 한 다음 그 즉시 제거한다
- RedirectAttributes.addFlashAttribute() 메소드를 통해 사용할 수 있다
- 스프링 부트에서 기본적으로 활성화 되어있다

### RedirectAttributes.addAttribute() 메소드와 차이점

- addAttribute()를 통해 넣은 데이터는 쿼리 매개변수를 통해 전달하므로 반드시 문자열로 변환할 수 있어야 한다
- addAttribute() 메소드로는 객체를 전달하지 못한다

### FlashAttributes 구현

addFlashAttribute() 메소드에 전달을 원하는 데이터를 넣어 준다 그후 Get 요청을 처리하는 메소드에서 Model에 값이 들어 있어 model.asMap().get("key") 값으로 데이터를 꺼낼 수 있다

```java
		@PostMapping("/events/form/limit")
    public String eventsFormLimitSubmit(@Validated @ModelAttribute Event event,
                                        BindingResult bindingResult,
                                        SessionStatus sessionStatus,
                                        RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            return "/events/form-limit";
        }
        sessionStatus.setComplete();
        attributes.addFlashAttribute("newEvent", event);
        return "redirect:/events/list";
    }

    @GetMapping("/events/list")
    public String getEvents(@ModelAttribute("newEvent") Event event, Model model,
                            @SessionAttribute LocalDateTime visitTime) {
        System.out.println(visitTime);
        event.setLimit(10);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);

        model.addAttribute(eventList);

        return "/events/list";
    }
```

### Test 코드 작성

```java
		@Test
    public void getEvents() throws Exception {
        Event event = new Event();
        event.setName("dddddd");
        event.setLimit(100);
        mockMvc.perform(get("/events/list")
                .sessionAttr("visitTime", LocalDateTime.now())
                .flashAttr("newEvent", event))
                    .andDo(print())
                    .andExpect(status().isOk());
    }
```