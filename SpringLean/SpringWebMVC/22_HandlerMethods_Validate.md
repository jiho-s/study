## @Validated

@Vaild annotation을 그룹을 지정할 수 없지만 @Validated에서는 그룹을 지정 할 수 있다

```java
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class Event {
    interface ValidateID {}
    interface ValidateName {}

    @Min(value = 0, groups = ValidateID.class)
    private Integer id;

    @NotBlank(groups = ValidateName.class)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

@Validated annotation에 Event.ValidateID.class로 설정하면 ValidateID의 그룹으로 설정된 @Min만 적용된다

```java
@Controller
public class SampleController {
    @PostMapping("/events")
    @ResponseBody
    public Event events(@Validated(Event.ValidateID.class) @ModelAttribute Event event, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("==================================");
            bindingResult.getAllErrors().forEach(c -> {
                System.out.println(c.toString());
            });
        }
        return event;
    }
}
```