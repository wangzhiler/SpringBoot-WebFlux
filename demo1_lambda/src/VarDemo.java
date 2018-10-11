import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by thinkpad on 2018/10/11.
 */
public class VarDemo {

    public static void main(String[] args) {
        // JDK8之前内部类如果引用外部变量，必须声明为final 常量
        // JDK8之后也要，但是没明着写
//        String str = "hellooo";
        List<String> list = new ArrayList<>(); //list不能修改了，其实已经加了final
        //因为外部list和内部list是两个变量，两者传的是值而非引用，即两者都存了ArrayList的地址
        //如果外部修改了值，那就指向别的地方了，而内list依旧存放ArrayList地址，两者就会不一样，所以不能这样写
        //如果传引用的话，那就是让内list指向外list，而JAVA并非如此
        Consumer<String> consumer = s -> System.out.println(s+list);
        consumer.accept("1234");

    }
}
