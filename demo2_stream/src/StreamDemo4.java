import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by thinkpad on 2018/10/11.
 */
public class StreamDemo4 {
    public static void main(String[] args) {
        String str = "my name is 008";

        //使用并行流，出来顺序是乱的
        str.chars().parallel().forEach(i -> System.out.print((char) i));
        System.out.println();

        //forEachOrdered 顺序的
        str.chars().parallel().forEachOrdered(i -> System.out.print((char) i));


        //收集到list
        List<String> list = Stream.of(str.split(" ")).collect(Collectors.toList());
        System.out.println(list);

        //使用reduce拼接字符串
        Optional<String> letters = Stream.of(str.split(" ")).reduce((s1, s2) -> s1 + "|" + s2);
        System.out.println(letters.orElse(""));  //做一个空判断

        //带初始化值的reduce 不用做空判断
        String reduce = Stream.of(str.split(" ")).reduce("", (s1, s2) -> s1 + "|" + s2);
        System.out.println(reduce);

        //计算所有单词总长度
        Integer length = Stream.of(str.split(" ")).map(s -> s.length())
                .reduce(0, (s1, s2) -> s1 + s2);
        System.out.println(length);

        //max的使用，传入比较器
        Optional<String> max = Stream.of(str.split(" ")).max((s1, s2) -> s1.length() - s2.length());
        System.out.println(max.get());

        // 使用findFist短路操作
        OptionalInt first = new Random().ints().findFirst();
        System.out.println(first.getAsInt());

    }
}
