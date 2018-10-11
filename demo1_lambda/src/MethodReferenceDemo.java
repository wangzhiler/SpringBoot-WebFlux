import java.util.function.*;

/**
 * Created by thinkpad on 2018/10/11.
 */


class Dog{
    private String name = "wangwang";
    private int food = 10;

    //静态方法，输入是Dog实例，输出为空，Conumer
    public static void bark(Dog dog) { //狗叫
        System.out.println(dog + "叫了");
    }

    //非静态方法, 输入int，输出int
    public int eat(int num) { //吃狗粮
        System.out.println("吃了" + num + "斤");
        this.food -= num;
        return food;
    }

    //静态方法与成员方法区别：成员方法可以访问当前实例，有this指针
    //JDK默认会把当前实例传入到非静态方法，参数名为this，位置在第一个
    //public int eat(int num) == public int eat(Dog this, int num)
    //所以其实有两个输入

    public Dog() {
    }

    //带参数的构造函数，输入String，输出实例
    public Dog(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

public class MethodReferenceDemo {

    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.eat(1);

        Consumer<String> consumer = s -> System.out.println(s);
        // 当执行体只有一个函数调用，且参数与箭头左边一样，就可以缩写成
        //方法引用
        Consumer<String> consumer1=System.out::println;
        consumer1.accept("123");

        // 1) 对静态方法做方法引用 类名+静态方法名
        Consumer<Dog> consumer2 = Dog::bark;
        consumer2.accept(new Dog());

        // 2) 非静态方法，使用对象实例的方法引用 dog::eat
        UnaryOperator<Integer> unaryOperator = dog::eat;
        IntUnaryOperator intUnaryOperator = dog::eat;
        System.out.println("还剩" + unaryOperator.apply(3) + "斤");
        System.out.println("还剩" + intUnaryOperator.applyAsInt(3) + "斤");

        // 3) 使用类名来引用方法 Dog::eat
        // Dog::eat  public int eat(Dog this, int num) 有两个入参
        BiFunction<Dog, Integer, Integer> biFunction = Dog::eat;
        System.out.println("还剩" + biFunction.apply(dog, 2) + "斤");

        // 4) 构造函数方法引用
        // 默认构造 没有输入，输出为新的实例
        Supplier<Dog> supplier=Dog::new;
        System.out.println("创建新对象：" + supplier.get());

        // 有参构造
        Function<String, Dog> function = Dog::new;
        System.out.println("创建了新对象："+function.apply("qwertyu"));
    }
}
