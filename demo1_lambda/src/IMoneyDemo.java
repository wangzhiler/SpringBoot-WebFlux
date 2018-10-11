import java.text.DecimalFormat;
import java.util.function.Function;

/**
 * Created by thinkpad on 2018/10/10.
 */

//lambda只需要知道输入是什么，输出是什么
//所以可以删掉接口
//interface IMoneyFormat {
//    String format(int i);
//}

class MyMoney{
    private final int money;

    public MyMoney(int money) {
        this.money = money;
    }

//    public void printMoney(IMoneyFormat moneyFormat) {
//        System.out.println("我的存款：" + moneyFormat.format(this.money));
//    }

    //直接用Function 函数定义
    public void printMoney(Function<Integer, String> moneyFormat) {
        System.out.println("我的存款：" + moneyFormat.apply(this.money));
    }
}

public class IMoneyDemo {

    public static void main(String[] args) {
        MyMoney me = new MyMoney(99999999);

        //输入字符串，返回字符串，链式操作
        Function<Integer, String> moneyFormat = i -> new DecimalFormat("#,###").format(i);
//        me.printMoney(moneyFormat);
        me.printMoney(moneyFormat.andThen(s -> "人民币" + s));
    }
}
