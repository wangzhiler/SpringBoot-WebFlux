/**
 * Created by thinkpad on 2018/10/10.
 */

//注解更多的是编译期间的校验, 多写方法会报错
//单一责任制 一个接口一个事情
@FunctionalInterface
interface Interface1{ //接口只有一个要实现的方法
    int doubleNum(int i);
    //可以多一个默认实现方法
    default int add(int x, int y) {
        return x + y;
    }
}

@FunctionalInterface
interface Interface2{
    int doubleNum(int i);
    default int add(int x, int y) {
        return x + y;
    }
}

@FunctionalInterface
interface Interface3 extends Interface2, Interface1{
    @Override
    default int add(int x, int y) {
        return Interface1.super.add(x, y);
    }
}


public class LambdaDemo1 {

    public static void main(String[] args) {
        Interface1 i1 = (i) -> i * 2;
        System.out.println(i1.add(3, 7));
        System.out.println(i1.doubleNum(9));

        //这种是最常见写法
        Interface1 i2 = i -> i * 2;  //参数1个所以括号也可以去掉

        Interface1 i3 = (int i) -> i * 2;

        Interface1 i4 = (int i) -> {  //多行可以这样写
            System.out.println("-----");
            return i * 2;
        };
    }
}
