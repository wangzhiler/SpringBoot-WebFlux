import java.util.function.*;

/**
 * Created by thinkpad on 2018/10/11.
 */
public class FunctionDemo {
    public static void main(String[] args) {
        //断言函数接口
        Predicate<Integer> predicate = i -> i > 0;
        System.out.println(predicate.test(-9));
        //如果是基本类型的话jdk自带了, 优先使用基本类型的，就不用写泛型了
        //IntPredicate、DoublePredicate...

        //消费函数接口 也有基本类型的 IntConsumer...
        Consumer<String> consumer = s -> System.out.println(s);
        consumer.accept("abc");
    }
}
