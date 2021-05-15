## Enum

`Enum`은 변수가 미리 정의된 상수의 집합이 될 수 있도록하는 특수한 데이터 타입이다. 변수는 미리 정의된 값 중하나와 같아야한다.

상수이기 때문에 `Enum` 타입의 필드 이름은 대문자 이다.

자바 프로그래밍 언어의 `Enum`은 클래스 바디에 메서드 및 기타 필드가 포함될 수 있다. 먼저 컴파일러는 `Enum` 형을 만들면 몇 가지 특수한 메서드를 자동으로 추가한다. 예를 들어 `values` 메소드가 있다.

자바에서는 필드 또는 메소드보다 상수를 먼저 정의해야하면, 또한 필드와 메서드가 있는 경우 열거 형 상수 목록은 세미콜론으로 끝나야한다.

```java
public enum Planet {
    MERCURY (3.303e+23, 2.4397e6),
    VENUS   (4.869e+24, 6.0518e6),
    EARTH   (5.976e+24, 6.37814e6),
    MARS    (6.421e+23, 3.3972e6),
    JUPITER (1.9e+27,   7.1492e7),
    SATURN  (5.688e+26, 6.0268e7),
    URANUS  (8.686e+25, 2.5559e7),
    NEPTUNE (1.024e+26, 2.4746e7);

    private final double mass;   // in kilograms
    private final double radius; // in meters
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }
    private double mass() { return mass; }
    private double radius() { return radius; }

    // universal gravitational constant  (m3 kg-1 s-2)
    public static final double G = 6.67300E-11;

    double surfaceGravity() {
        return G * mass / (radius * radius);
    }
    double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity();
    }
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Planet <earth_weight>");
            System.exit(-1);
        }
        double earthWeight = Double.parseDouble(args[0]);
        double mass = earthWeight/EARTH.surfaceGravity();
        for (Planet p : Planet.values())
           System.out.printf("Your weight on %s is %f%n",
                             p, p.surfaceWeight(mass));
    }
}
```

