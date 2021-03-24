### @AspectJ Support

@AspectJ는 어노테이션이있는 일반 Java 클래스로 aspect를 선언하는 스타일을 나타낸다. @AspectJ 스타일은 AspectJ5의 일부로 [AspectJ 프로젝트](https://www.eclipse.org/aspectj) 에서 도입 되었다. Spring은 Pointcut  파싱 및 매칭을 위해 AspectJ에서 제공하는 라이브러리를 사용하여 AspectJ5 처럼 주석을 해석한다. AOP 런타임은  Spring AOP이며 AspectJ 컴파일러 또는 위버에 대한 종속성이 없다.

