import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by thinkpad on 2018/10/11.
 */
public class StreamDemo3 {
    public static void main(String[] args) {
        String str = "my name is 007";

        //把每个单词的长度打印出来
        Stream.of(str.split(" "))
                .filter(s -> s.length() > 2) //Predicate
                .map(s -> s.length())
                .forEach(System.out::println);

        //flapMap A->B 属性(是个集合) 最终得到所有的A元素里面的所有B属性集合
        // intStream/longStream 并不是Stream的子类，用boxed进行装箱
        // Stream<Integer> boxed();
        Stream.of(str.split(" ")).flatMap(s -> s.chars().boxed())
                .forEach(
                        i -> System.out.print((char) i.intValue())
                );

        //peek 用于debug 是个中间操作，类似foreach，但foreach是终止操作
        System.out.println("------------------------");
        Stream.of(str.split(" ")).peek(System.out::println).forEach(System.out::println);

        //limit使用，主要用于无限流
        new Random().ints().filter(i->i>100 && i<1000).limit(10)
                .forEach(System.out::println);

    }
}
