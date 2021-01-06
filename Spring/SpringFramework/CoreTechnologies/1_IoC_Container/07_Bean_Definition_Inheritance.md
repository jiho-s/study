## Bean Definition Inheritance

Bean definition은 설정 정보, 생성자 인수, 프로퍼티 값, 컨테이너 틍정 정보를 포함한 많은 구성정보가 포함될 수 있다. child 빈은 이러한 설정 정보를 상속 받을 수 있다. child definition은 상속 받은 값 재정의 및 또는 필요한 다른 값을 추가 할 수 있다.

XML 기반 구성을 사용 할 때, child bean definition을 `parent` 속성을 사용해 나타낼 수 있다.

```xml
<bean id="inheritedTestBean" abstract="true"
        class="org.springframework.beans.TestBean">
    <property name="name" value="parent"/>
    <property name="age" value="1"/>
</bean>

<bean id="inheritsWithDifferentClass"
        class="org.springframework.beans.DerivedTestBean"
        parent="inheritedTestBean" init-method="initialize">  
    <property name="name" value="override"/>
    <!-- the age property value of 1 will be inherited from parent -->
</bean>
```

`abstact` 속성을 이용해 명시적으로 parent bean의 정의를 추상화 시켰다