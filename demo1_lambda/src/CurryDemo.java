import java.util.function.Function;

/**
 * 级联表达式和柯里化
 */
public class CurryDemo {
    public static void main(String[] args) {
        //级联表达式就是有多个箭头的lambda表达式
        // ->左边是输入右边是输出，所以输入x，输出函数。函数输入y，输出x+y
        // 本身需要输入两个参数，现在用两个函数，每次输入一个参数
        // 柯里化：把多个参数的函数转换为只有一个参数的函数
        // 柯里化的目的：函数标准化
        // 高阶函数：返回函数的函数
        Function<Integer, Function<Integer, Integer>> fun = x -> y -> x + y;
        System.out.println(fun.apply(2).apply(5));

        Function<Integer, Function<Integer, Function<Integer, Integer>>> fun2 = x -> y -> z -> x + y + z;

        int[] nums = {2, 3, 4};
        Function f = fun2;
        for(int i=0; i<nums.length; i++) {
            if (f instanceof Function) { //类似批量处理
                Object object = f.apply(nums[i]);
                if (object instanceof Function) {
                    f = (Function) object;
                } else {
                    System.out.println("调用结束：" + object);
                }
            }
        }
    }
}
