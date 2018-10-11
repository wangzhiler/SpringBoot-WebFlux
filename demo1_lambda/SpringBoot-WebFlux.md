# Springboot-WebFlux

## 一、函数式编程/lambda

```java
public class MinDemo {
    public static void main(String[] args) {
        int[] nums = {33, 55 - 55, 90, -666, 90};
        int min = Integer.MAX_VALUE;
        for (int i : nums) {
            if (i < min) {
                min = i;
            }
        }
        System.out.println(min);
        //jdk8 用流，要最小值就用min(),想要并行就用parallel()
        int min2 = IntStream.of(nums).parallel().min().getAsInt();
        System.out.println(min2);
    }
}
```



```java
public class ThreadDemo {
    public static void main(String[] args) {
        Runnable target = new Runnable() {
            @Override
            public void run() {
                System.out.println("ok");
            }
        };
        new Thread(target).start();

        Object target4 = new Runnable() {
            @Override
            public void run() {
                System.out.println("ok");
            }
        };
        new Thread((Runnable) target4).start();

        //jdk8 lambda
        Runnable target2 = () -> System.out.println("ok");
        Runnable target3 = () -> System.out.println("ok");
        System.out.println(target2==target3); //false

        Object target5 = (Runnable)() -> System.out.println("ok");
        new Thread((Runnable) target5).start();
    }
}
```

有参的lambda

```java
//注解更多的是编译期间的校验, 多写方法会报错
//单一责任制 一个接口一个事情
@FunctionalInterface
interface Interface1{ //接口只有一个要实现的方法
    int doubleNum(int i);
}
public class LambdaDemo1 {
    public static void main(String[] args) {
        Interface1 i1 = (i) -> i * 2;
        //这种是最常见写法
        Interface1 i2 = i -> i * 2;  //参数1个所以括号也可以去掉
        Interface1 i3 = (int i) -> i * 2;
        Interface1 i4 = (int i) -> {  //多行可以这样写
            System.out.println("-----");
            return i * 2;
        };
    }
}
```

默认实现

```java
interface Interface1{ //接口只有一个要实现的方法
    int doubleNum(int i);
    //可以多一个默认实现方法
    default int add(int x, int y) {
        return x + y;
    }
}
public class LambdaDemo1 {
    public static void main(String[] args) {
        Interface1 i1 = (i) -> i * 2;
        System.out.println(i1.add(3, 7)); //10
        System.out.println(i1.doubleNum(9)); //18
    }
}
```

### 1. 函数接口

```java
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
```



| 接口               | 输入参数 | 返回类型 | 说明                         |
| ------------------ | -------- | -------- | ---------------------------- |
| Predicate<T\>      | T        | boolean  | 断言                         |
| Consumer<T\>       | T        | /        | 消费一个数据                 |
| Function<T,R>      | T        | R        | 输入T输出R的函数             |
| Supplier<T\>       | /        | T        | 提供一个数据                 |
| UnaryOperator<T\>  | T        | T        | 一元函数（输出输入类型相同） |
| BiFunction<T,U,R>  | (T,U)    | R        | 2个输入的函数                |
| BinaryOperator<T\> | (T,T)    | T        | 二元函数（输出输入类型相同） |



```java
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
```

### 2. 方法引用

四种引用，分析输入输出，找对应接口

```java
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
```

### 3. 类型推断

```java
@FunctionalInterface
interface IMath{
    int add(int x, int y);
}
@FunctionalInterface
interface IMath2{
    int sub(int x, int y);
}
public class TypeDemo {
    public static void main(String[] args) {
        //变量类型定义
        IMath lambda = (x, y) -> x + y;        
        //数组里
        IMath[] lambdas = {(x, y) -> x + y};        
        //强转
        Object lambda2 = (IMath) (x, y) -> x + y;        
        //通过返回类型
        IMath createLambda = createLambda();
        TypeDemo demo = new TypeDemo();
        //当有二义性的时候，使用强转对应的接口解决
        demo.test((IMath) (x, y) -> x + y);   
    }
    public void test(IMath math) {}
    public void test(IMath2 math) {}
    public static IMath createLambda() {
        return (x, y) -> x + y;
    }
}
```

### 4. 变量引用

```java
public class VarDemo {
    public static void main(String[] args) {
        // JDK8之前内部类如果引用外部变量，必须声明为final 常量
        // JDK8之后也要，但是没明着写
        List<String> list = new ArrayList<>(); //list不能修改了，其实已经加了final
        //因为外部list和内部list是两个变量，两者传的是值而非引用，即两者都存了ArrayList的地址
        //如果外部修改了值，那就指向别的地方了，而内list依旧存放ArrayList地址，两者就会不一样，所以不能这样写
        //如果传引用的话，那就是让内list指向外list，而JAVA并非如此
        Consumer<String> consumer = s -> System.out.println(s+list);
        consumer.accept("1234");
    }
}
```



### 5. 级联表达式和柯里化

```java
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
```



## 二、Stream流编程

概念：是一个高级的迭代器

```java
public class StreamDemo1 {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        //外部迭代
        int sum = 0;
        for (int i : nums) {
            sum += i;
        }
        System.out.println("结果为：" + sum);

        //使用stream的内部迭代
        //map就是中间操作(返回stream的操作)
        //sum就是终止操作
        int sum2 = IntStream.of(nums).map(StreamDemo1::doubleNum).sum();
        System.out.println(sum2);
        
        System.out.println("惰性求值就是终止没有调用的情况下，中间操作不会执行");
        IntStream.of(nums).map(StreamDemo1::doubleNum);
    }
    public static int doubleNum(int i) {
        System.out.println("执行了x2");
        return i * 2;
    }
}
```

### 1. 创建

|            | 相关方法                               |
| ---------- | -------------------------------------- |
| 集合       | Collection.stream/parallelStream       |
| 数组       | Arrays.stream                          |
| 数字Stream | IntStream/LongStream.range/rangeClosed |
|            | Random.ints/longs/doubles              |
| 自己创建   | Stream.generate/iterate                |

```java
public class StreamDemo2 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        //从集合创建
        list.stream();
        list.parallelStream();
        
        //从数组创建
        Arrays.stream(new int[]{2, 3, 4, 5});
        
        //创建数字流
        IntStream.of(1, 2, 3);
        IntStream.rangeClosed(1, 10);
        
        //使用Radnom创建一个无限流
        new Random().ints().limit(10);

        //自己创建
        Random random = new Random();
        Stream.generate(() -> random.nextInt()).limit(20);
    }
}
```

### 2. 中间操作

无状态操作表示当前操作与其他元素没有依赖关系，有状态表示有所依赖。

eg 排序依赖于所有需要排序的元素，有状态

共同点是返回Stream

|            | 相关方法             |                                |
| ---------- | -------------------- | ------------------------------ |
| 无状态操作 | map/mapToXxx         | 根据某个得到另一个             |
|            | flapMap/flapMapToXxx | A内有B属性，B为集合，得到B列表 |
|            | filter               | 过滤器                         |
|            | peek                 | 入参消费者，类似foreach        |
|            | unordered            | 并行流时用到                   |
| 有状态操作 | distinct             | 去重                           |
|            | sorted               | 排序                           |
|            | limit/skip           | 限流，跳过                     |

```java
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
```



### 3. 终止操作

短路操作：不需要等待所有结果计算完就可以结束的操作

|            | 相关方法                   |                  |
| ---------- | -------------------------- | ---------------- |
| 非短路操作 | forEach/forEachOrdered     |                  |
|            | collect/toArray            |                  |
|            | reduce                     | 将流合成一个数据 |
|            | min/max/count              | 需要比较器       |
| 短路操作   | findFirst/findAny          |                  |
|            | allMatch/anyMatch/nonMatch |                  |

```java
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
```

### 4. 并行流

1) 调用parallel 产生一个并行流

2) 多次调用parallel/sequential，以最后一次调用为准

3) 并行流使用的线程池: ForkJoinPool.commonPool

4) 那么多并行流使用同一个线程池，阻塞，可以用自己写的

```java
public class StreamDemo5 {
    public static void main(String[] args) {
        // 1) 调用parallel 产生一个并行流
//        IntStream.range(1, 100).parallel().peek(StreamDemo5::debug).count();

        //现在要实现一个这样的效果：先并行，再串行
        // 2) 多次调用parallel/sequential，以最后一次调用为准
        /*
        IntStream.range(1, 100)
                //调用parallel产生并行流
                .parallel().peek(StreamDemo5::debug)
                //调用sequential产生串行流
                .sequential().peek(StreamDemo5::debug2)
                .count();
        */

        // 3) 并行流使用的线程池: ForkJoinPool.commonPool
        //默认线程数是当前CPU个数
        //使用这个属性可以修改默认的线程数
//        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
//        IntStream.range(1, 100).parallel().peek(StreamDemo5::debug).count();

        // 4) 那么多并行流使用同一个线程池，阻塞
        //所以重要的场合用自己的线程池，不受制于默认的线程池比较好
        ForkJoinPool pool = new ForkJoinPool(20);
        pool.submit(() -> IntStream.range(1, 100).parallel()
                .peek(StreamDemo5::debug).count());
        //要记得关闭
        pool.shutdown();

        synchronized (pool) {
            try {
                pool.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void debug(int i) {
        System.out.println(Thread.currentThread().getName() + "debug" + i);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void debug2(int i) {
        System.err.println("debug" + i);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### 5. 收集器

```java
//得到所有学生的年龄列表
        //s -> s.getAge() --> Student::getAge, 不会生成一个类似lambda@()这样的函数
        List<Integer> ages = students.stream().map(Student::getAge)
                .collect(Collectors.toList());
        Set<Integer> ages2 = students.stream().map(Student::getAge)
                .collect(Collectors.toSet()); //默认是HashSet，可以指定Set类型
        Set<Integer> ages3 = students.stream().map(Student::getAge)
                .collect(Collectors.toCollection(TreeSet::new)); //定义成TreeSet
        System.out.println("所有学生的年龄：" + ages);
        System.out.println("所有学生的年龄去重：" + ages2);

        //统计汇总信息
        IntSummaryStatistics intSummaryStatistics = students.stream()
                .collect(Collectors.summarizingInt(Student::getAge));
        System.out.println("年龄汇总信息："+intSummaryStatistics);
        //年龄汇总信息：IntSummaryStatistics{count=8, sum=115, min=11, average=14.375000, max=18}

        //分块
        Map<Boolean, List<Student>> genders = students.stream()
                .collect(Collectors.partitioningBy(s -> s.getGender() == Gender.MALE));
        System.out.println("男女学生列表：" + genders);
        //男女学生列表：{false=[Student@dde6e5, Student@177ecd, Student@80bfe8]
        // , true=[Student@a29884, Student@169b07b, Student@c34f4d, Student@1a7cec2, Student@1b3120a]}

        //分组
        Map<Grade, List<Student>> grades = students.stream()
                .collect(Collectors.groupingBy(Student::getGrade));
        System.out.println("学生班级列表：" + grades);
        //学生班级列表：{THREE=[Student@80bfe8], TWO=[Student@169b07b], ONE=[Student@a29884, Student@dde6e5
        // , Student@c34f4d, Student@1a7cec2, Student@1b3120a], FOUR=[Student@177ecd]}

        //得到所有班级学生的个数
        Map<Grade, Long> gradesCount = students.stream()
                .collect(Collectors.groupingBy(Student::getGrade,Collectors.counting()));
        System.out.println("学生班级列表：" + gradesCount);
        //学生班级列表：{THREE=1, TWO=1, ONE=5, FOUR=1}
```



### 6. Stream运行机制

```java
/**
 * 验证stream运行机制
 * 1. 所有操作是链式调用，一个元素只迭代一次
 * 2. 每一个中间操作返回一个新的stream
 *  流里面有一个属性sourceStage执行同一个地方，就是Head
 * 3. Head->nextStage->nextStage->...->null
 * 4. 有状态操作会把无状态操作截断，单独处理
 * 5. 并行环境下，有状态的中间操作不一定能并行操作
 * 
 * 6. parallel/ sequential 这两个也是中间操作（也是返回stream）
 *      但是他们不创建流，只修改Head的并行标志
 *
 */
public class RunStream {
    public static void main(String[] args) {
        Random random = new Random();
        //随机产生数据
        Stream<Integer> stream = Stream.generate(() -> random.nextInt())
                //产生500个(无限流需要短路操作)
                .limit(500)
                //第一个无状态操作
                .peek(s -> print("peek:" + s))
                //第二个无状态操作
                .filter(s -> {
                    print("filter:" + s);
                    return s > 1000000;
                })
                //有状态操作
                .sorted((i1, i2) -> {
                    print("排序：" + i1 + "," + i2);
                    return i1.compareTo(i2);
                })
                //有一个无状态操作
                .peek(s -> {
                    print("peek2:" + s);
                }).parallel();
        //终止操作
        stream.count();
    }

    public static void print(String s) {
        System.out.println(Thread.currentThread().getName() + ">" + s);
        try {
            TimeUnit.MILLISECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### 7. 小结

1. 惰性求值

之间操作：有状态 / 无状态

终止操作：短路操作

parallel/ sequential 不创建流，只修改Head里的并行标志

2. 收集器 - 分组
3. 运行机制

链式， Head->nextStage

并行 fork/join 阻塞



## 三、JDK9 Reactive Stream

背压 back Press

多了发布订阅之间的交流

原来发布者关自己发布，不论发布多少，订阅者都会收到。

现在订阅者可以主动请求数据，起到调节发布者速率的作用

关键在于subscrpition.request() cancel()

submit是一个block方法，如果订阅时缓冲池满了就会被阻塞

默认256，所以一次生产256条，然后消费者消费一条之后就再生产一条

```java
public class FlowDemo {
    public static void main(String[] args) throws InterruptedException {
        //1. 定义发布者，发布的数据类型是Integer
        //直接用jdk自带的SubmissionPublisher, 它实现了Publisher接口
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        //2. 定义订阅者
        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {
            private Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                //保存订阅关系，需要用它来给发布者响应
                this.subscription = subscription;
                //请求一个数据
                this.subscription.request(1);
            }
            @Override
            public void onNext(Integer item) {
                //接受到一个数据，处理
                System.out.println("接受导数据:" + item);
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //可以起到调节发布者发布速率作用
                //处理完调用request再请求一个数据
                this.subscription.request(1);
                //或者 已经达到了目标，调用cancel告诉发布者不再接受数据了
//                this.subscription.cancel();
            }
            @Override
            public void onError(Throwable throwable) {
                //出现了异常(例如处理数据的时候产生了异常)
                throwable.printStackTrace();
                //我们可以告诉发布者，后面不接受数据了
                this.subscription.cancel();
            }
            @Override
            public void onComplete() {
                //全部数据处理完了(发布者关闭了)
                System.out.println("处理完了！");
            }
        };
        //3. 发布者和订阅者建立订阅关系
        publisher.subscribe(subscriber);
        //4. 生产数据，并发布
        // 这里忽略数据生产过程
        int data = 111;
        for (int i = 0; i < 1000; i++) {
            System.out.println("生产数据：" + i);
            //submit是一个block方法，如果订阅时缓冲池满了就会被阻塞
            //默认256，所以一次生产256条，然后消费者消费一条之后就再生产一条
            publisher.submit(i);
        }
//        publisher.submit(data);
//        publisher.submit(111);
//        publisher.submit(222);
//        publisher.submit(333);
//        publisher.submit(444);
        //5. 结束后 关闭发布者
        //正式环境 应该放finally或者try-resource确保关闭
        publisher.close();
        //主线程延迟停止，否则数据没有消费就退出
        Thread.currentThread().join(1000);
    }
}
```

### Processor

中转站，接收数据，处理数据，发布数据

Publisher -> Processor -> Subscriber

```java
/* Processor需要继承SubmissionPublisher并实现Processor接口
 * 输入源数据integer，过滤掉小于0的，然后转换成字符串发布出去
 */
class MyProcessor extends SubmissionPublisher<String>
        implements Flow.Processor<Integer, String> {
    private Flow.Subscription subscription;
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        //保存订阅关系，需要用它来给发布者响应
        this.subscription = subscription;
        this.subscription.request(1);
    }
    @Override
    public void onNext(Integer item) {
        //接收到一个数据，处理
        System.out.println("处理器接受到数据:" + item);
        //过滤掉小于0的，然后发布出去
        if (item > 0) {
            this.submit("转换后的数据:" + item);
        }
        //处理完调用request再请求一个数据
        this.subscription.request(1);
        //或者 已经达到了目标，调用cancel告诉发布者不再接受数据了
        //this.subscription.cancel();
    }
    @Override
    public void onError(Throwable throwable) {
        //出现了异常(例如处理数据的时候产生了异常)
        throwable.printStackTrace();
        //我们可以告诉发布者，后面不接受数据了
        this.subscription.cancel();
    }
    @Override
    public void onComplete() {
        //全部数据处理完了(发布者关闭了)
        System.out.println("处理完了！");
        //关闭发布者
        this.close();
    }
}
public class FlowDemo2 {
    public static void main(String[] args) throws InterruptedException {
        //1. 定义发布者，发布的数据类型是Integer
        //直接用jdk自带的SubmissionPublisher, 它实现了Publisher接口
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        //2.定义Processor，对数据进行过滤，并转换为String类型
        MyProcessor processor = new MyProcessor();
        //3. 发布者和处理器建立订阅关系
        publisher.subscribe(processor);
        //4. 定义最终订阅者，消费String类型数据
        Flow.Subscriber<String> subscriber = new Flow.Subscriber<String>() {
            private Flow.Subscription subscription;
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                //保存订阅关系，需要用它来给发布者响应
                this.subscription = subscription;
                //请求一个数据
                this.subscription.request(1);
            }
            @Override
            public void onNext(String item) {
                //接受到一个数据，处理
                System.out.println("接受导数据:" + item);
                //可以起到调节发布者发布速率作用
                //处理完调用request再请求一个数据
                this.subscription.request(1);
                //或者 已经达到了目标，调用cancel告诉发布者不再接受数据了
//                this.subscription.cancel();
            }
            @Override
            public void onError(Throwable throwable) {
                //出现了异常(例如处理数据的时候产生了异常)
                throwable.printStackTrace();
                //我们可以告诉发布者，后面不接受数据了
                this.subscription.cancel();
            }
            @Override
            public void onComplete() {
                //全部数据处理完了(发布者关闭了)
                System.out.println("处理完了！");
            }
        };
        //5. 处理器和最终订阅者建立订阅
        processor.subscribe(subscriber);
        //6. 生产数据，并发布
        publisher.submit(-111);
        publisher.submit(111);
        //7. 结束后，关闭发布者
        //正式环境，应该放finally或者使用try-resource确保关闭
        publisher.close();
        //主线程延迟停止，否则数据没有消费就退出
        Thread.currentThread().join(1000);
    }
}
```

## 四、Spring WebFlux

WebFlux 非阻塞

MVC 同步

优势：支持高并发量

### 1. 异步Servlet

1) 为什么要使用异步Servlet? 同步Servlet阻塞了什么?

2) 异步Servlet是怎么样工作？

对于服务器后台，才由异步概念。对于浏览器，都是同步。

servlet容器里面，每处理一个请求会占用一个线程，同步servlet里面，业务代码处理多久，servlet容器的线程就会等多久，而servlet容器的线程是有上限的，当请求多了的时候servlet容器线程就会全部用光，就无法再处理请求（这个时候请求可能排队也可能丢弃，看如何配置），就会限制了应用的吞吐量

而异步servlet里面，servlet容器的线程不会傻等业务代码处理完毕，而是直接返回（继续处理其他请求），给业务代码一个回调函数（asynContext.complete()），业务代码处理完了再通知。可以使用少量的线程处理更加高的请求，从而实现高吞吐量

```java
@WebServlet(urlPatterns = {"/AsyncServlet"}, asyncSupported = true)
public class AsyncServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long t1 = System.currentTimeMillis();
        //开启异步
        AsyncContext asyncContext = request.startAsync();
        //执行业务代码,从request获取信息，返回给response
        //异步操作可以把耗时放入线程池
        //方便起见
        CompletableFuture.runAsync(() ->
        //异步处理是在另一个线程里，处理完要主动告诉asynContext自己结束了，所以也要传入
                doSomeThing(asyncContext,asyncContext.getRequest(), asyncContext.getResponse()));
        System.out.println("耗时：" + (System.currentTimeMillis() - t1));
    }
    private void doSomeThing(AsyncContext asyncContext, ServletRequest request, ServletResponse response){
        try {
            //模拟耗时操作
            TimeUnit.SECONDS.sleep(5);
            response.getWriter().append("done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //业务代码处理完毕，通知结束
        asyncContext.complete();
    }
}
```

上面的代码，业务花了5秒，但servlet容器的线程几乎没有任何耗时。如果同步servlet，线程就会等5秒



### 2. webflux开发

Mono 、Flux

```java
public class ReactorDemo {
    public static void main(String[] args) {
        // reactor => jdk8 stream + jdk9 reactive stream
        // Mono 0~1个元素
        // Flux 0-n个元素
        String[] strs = {"1", "2", "3"};
        //2. 定义订阅者
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {...};
        // 这里是jdk8的stream, 也是个发布者, map中间操作做个数据转换
        Flux.fromArray(strs).map(s -> Integer.parseInt(s))
                // 最终操作是订阅
                // 这就是jdk9的reactive stream
                .subscribe(subscriber);
    }
}
```



```java
@RestController
@Slf4j
public class TestController {
    //老写法 createStr()占了5秒，所以此Controller线程占了5秒
    @GetMapping("/1")
    public String get1() {
        log.info("get1 start");
        String str = createStr();
        log.info("get1 end");
        return str;
    }
//2018-10-11 23:07:40.792  INFO 4520 --- [ctor-http-nio-2] com.wzl.TestController : get1 start
//2018-10-11 23:07:45.794  INFO 4520 --- [ctor-http-nio-2] com.wzl.TestController : get1 end

    //WebFlux写法 Mono 或者 Flux 线程占时很短很短
    //返回Mono 其实返回的是流，在Controller内没有调流操作，所以不会阻塞Controller线程
    @GetMapping("/2")
    public Mono<String> get2() {
        log.info("get2 start");
        Mono<String> result = Mono.fromSupplier(() -> createStr());
        log.info("get2 end");
        return result;
    }
//2018-10-11 23:07:53.922  INFO 4520 --- [ctor-http-nio-2] com.wzl.TestController : get2 start
//2018-10-11 23:07:53.926  INFO 4520 --- [ctor-http-nio-2] com.wzl.TestController : get2 end

    //Flux 返回0-n个元素. 需要告诉生产者的类型
    //produces = "text/event-stream" / produces = MediaType.TEXT_EVENT_STREAM_VALUE
    @GetMapping(value = "/3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> get3() {
        log.info("get2 start");
        // 用jdk8的流创建个数据，再用Flux包一下
        Flux<String> result = Flux.fromStream(IntStream.range(1, 5).mapToObj(i -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "flux data--" + i;
        }));
        log.info("get2 end");
        return result;
    }
    
	//模拟一个耗时操作
    private String createStr() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "some string";
    }
}
```

### 3. SSE（Server-Sent Events）

Flux可以返回多次数据，但是Http协议是基于一问一答的形式，那么如何做到多次返回？

用的就是H5的SSE

SSE由于一次多个返回，所以可以用于服务器往前台推送数据的场景，比如聊天室，而且会自动重连

```java
@WebServlet("/SSE")
public class SSEServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 这两个头一定要设置
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("utf-8");

        for (int i = 0; i < 5; i++) {
            //还可以指定事件标识，起个名字
            response.getWriter().write("event:yooo\n");
            //格式： data:+数据+2个回车
            response.getWriter().write("data:" + i + "\n\n");
            //每次都要flush()一下
            response.getWriter().flush();

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
```

```html
<script type="text/javascript">
    //初始化，参数url
    //依赖H5
    var sse = new EventSource("SSE");
    sse.onmessage=function (evt) {
        console.log("message", evt.data, evt);
    }
    sse.addEventListener("yooo", function (evt) {
        console.log("message yooo--", evt.data);
        if (evt.data == 3) {
            sse.close(); //不关闭会自动重连。
        }
    })
</script>
```

### 4. 第一种开发模式MVC

连接上响应式数据库MongoDB

1） 添加依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
</dependency>
```

2) @EnableReactiveMongoRepositories

3) 定义对象

​	@Document(collection="user")

​	@Data

4)  Repository

````java
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
````

5) properties

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/webflux
```

#### CRUD

6) UserController.java 	

```java
import com.wzl.domain.User;
import com.wzl.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {

    //    @Autowired
//    private UserRepository userRepository;

    //官方现在推荐用构造的写法，与Spring耦合度更低
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    // *** 任何时候用Flux都写两个，一个普通返回一个指定product
    // 这样获取数据可以选择老的模式获取数据，也可以用新的一条一条获取

    /**
     * 以数组形式一次性返回数据
     * @return
     */
    @GetMapping("/")
    public Flux<User> getAll() {
        return repository.findAll();
    }

    /**
     * 以SSE形式多次返回数据
     * @return
     */
    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamGetAll() {
        return repository.findAll();
    }

    /**
     * 新增
     * @param user
     * @return
     */
    @PostMapping("/")
    public Mono<User> createUser(@RequestBody User user) {
        // spring data jpa里面，新增和修改都是save，有id是修改，id为空是新增
        // 根据实际情况是否置空id
        user.setId(null); //每次都新增
        return this.repository.save(user);
    }

    /**
     * 根据id删除用户，存在返回200，不存在返回404
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") String id) {
        //这个deleteById返回值Mono<Void>就是没有返回值，不能判断数据是否存在
        //this.repository.deleteById(id);

        //所以先找出id
        return this.repository.findById(id)
                //当你要操作数据，并返回一个Mono，这个时候使用flatMap
                //如果不操作数据，只是转换数据，使用map
                .flatMap(user -> this.repository.delete(user)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 修改数据
     * 存在的时候返回200和修改后的数据
     * 不存在时返回404
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(
            @PathVariable("id") String id,
            @RequestBody User user
    ) {
        return this.repository.findById(id)
                //flapMap操作数据
                .flatMap(u -> {
                    u.setAge(user.getAge());
                    u.setName(user.getName());
                    return this.repository.save(u);
                })
                //map转换数据
                .map(u -> new ResponseEntity<User>(HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据ID查找用户，存在返回用户信息，不存在返回404
     * @return
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findUserById(
            @PathVariable("id") String id
    ) {
        return this.repository.findById(id)
                .map(u -> new ResponseEntity<User>(HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
```



#### JPA

MongoDB: query语法

```cmd
> use webflux
switched to db webflux
> show tables
user
> db.user.find()
{ "_id" : ObjectId("5bbf8984ea7c982bc872abf5"), "name" : "hello", "age" : 35, "_class" : "com.wzl.domain.User" }
{ "_id" : ObjectId("5bbf8ed8ea7c9805b4077892"), "name" : "sss", "age" : 30, "_class" : "com.wzl.domain.User" }
{ "_id" : ObjectId("5bbf8ee0ea7c9805b4077893"), "name" : "sss", "age" : 22, "_class" : "com.wzl.domain.User" }
{ "_id" : ObjectId("5bbf8ee8ea7c9805b4077894"), "name" : "sss", "age" : 25, "_class" : "com.wzl.domain.User" }
{ "_id" : ObjectId("5bbf8eefea7c9805b4077895"), "name" : "sss", "age" : 27, "_class" : "com.wzl.domain.User" }
> db.user.find({age:{$gte:20, $lte:30}})
{ "_id" : ObjectId("5bbf8ed8ea7c9805b4077892"), "name" : "sss", "age" : 30, "_class" : "com.wzl.domain.User" }
{ "_id" : ObjectId("5bbf8ee0ea7c9805b4077893"), "name" : "sss", "age" : 22, "_class" : "com.wzl.domain.User" }
{ "_id" : ObjectId("5bbf8ee8ea7c9805b4077894"), "name" : "sss", "age" : 25, "_class" : "com.wzl.domain.User" }
{ "_id" : ObjectId("5bbf8eefea7c9805b4077895"), "name" : "sss", "age" : 27, "_class" : "com.wzl.domain.User" }
```

* 考虑场景：根据年龄段查找用户

```java
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Flux<User> findByAgeBetween(int start, int end);

    @Query("{'age':{'$gte':20, '$lte':30}}")
    Flux<User> age20to30();
}

```

```java
    /* 根据年龄查找用户
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/age/{start}/{end}")
    public Flux<User> findByAge(@PathVariable("start") int start
            , @PathVariable("end") int end) {
        return repository.findByAgeBetween(start, end);
    }
    @GetMapping(value = "/stream/age/{start}/{end}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamFindByAge(@PathVariable("start") int start
            , @PathVariable("end") int end) {
        return repository.findByAgeBetween(start, end);
    }

    /**
     * 得到20~30用户，用的是MongoDB的query
     * @return
     */
    @GetMapping("/age20")
    public Flux<User> age20to30() {
        return repository.age20to30();
    }
    @GetMapping(value = "/stream/age20",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamAge20to30() {
        return repository.age20to30();
    }
```



#### 参数校验

一种是用Hibernate的注解校验

```java
@NotBlank
private String name;

@Range(min = 10, max = 100)
private int age;

//增加修改加一个@Valid
public Mono<User> createUser(@Valid @RequestBody User user) 
public Mono<ResponseEntity<User>> updateUser(
            @PathVariable("id") String id,
            @Valid @RequestBody User user
    )
```

然后用切面处理校验异常

```java
/**
 * 异常处理切面
 */
@ControllerAdvice
public class CheckAdvice {

    //处理的异常为 WebExchangeBindException
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity handlerBindException(WebExchangeBindException e) {
        return new ResponseEntity<String>(toStr(e), HttpStatus.BAD_REQUEST);
    }

    /**
     * 把校验异常转换成字符串
     * @param ex
     * @return
     */
    private String toStr(WebExchangeBindException ex) {
        return ex.getFieldErrors().stream().map(e -> e.getField() + ":" + e.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 + "\n" + s2);
    }
}
```



有时自带的标注不能满足需求，可以自定义

先自定义异常跟check

```java
import lombok.Data;
@Data
public class CheckException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    //出错字段的名字
    private String fieldName;
    //出错字段的值
    private String fieldValue;
    public CheckException(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    public CheckException() {}
    public CheckException(String message) {super(message);}
    public CheckException(String message, Throwable cause) {super(message, cause);}
    public CheckException(Throwable cause) {super(cause);}
    public CheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
```

```java
import com.wzl.exceptions.CheckException;
import java.util.stream.Stream;
public class CheckUtil {
    private static final String[] INVALID_NAMES = {"admin", "guanliyuan"};
    /* 校验名字，不成功则抛出校验异常
     * @param value
     */
    public static void checkName(String value) {
        Stream.of(INVALID_NAMES).filter(name -> name.equalsIgnoreCase(value))
                //找到任何错误就退出
                .findAny().ifPresent(name->{
            throw new CheckException("name", value);
        });
    }
}
```

切面

```java
@ExceptionHandler(CheckException.class)
public ResponseEntity handlerCheckException(CheckException e) {
    return new ResponseEntity<String>(toStr2(e), HttpStatus.BAD_REQUEST);
}
private String toStr2(CheckException e) {
    return e.getFieldName() + ":错误的值" + e.getFieldValue();
}
```

然后就可以在创建修改中调用checkName(user.getName());



### 5. 第二种开发模式 Router Functions

HandlerFunction (输入ServerRequest返回ServerResponse)

-> RouterFunction(请求URL和HandlerFunction对应起来)

-> HttpHandler

->Server处理

```java
import com.wzl.domain.User;
import com.wzl.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class UserHandler {

    private final UserRepository repository;

    public UserHandler(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * 得到所有用户
     * @param request
     * @return
     */
    public Mono<ServerResponse> getAllUser(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(this.repository.findAll(), User.class);
    }

    /**
     * 创建用户
     * @param request
     * @return
     */
    public Mono<ServerResponse> createUser(ServerRequest request) {
        Mono<User> user = request.bodyToMono(User.class);

        return ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(this.repository.saveAll(user), User.class);
    }

    /**
     * 根据id删除用户
     * @param request
     * @return
     */
    public Mono<ServerResponse> deleteUserById(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.repository.findById(id)
                .flatMap(user -> this.repository.delete(user).then(ok().build()))
                .switchIfEmpty(notFound().build());
    }
}
```

```java
import com.wzl.handlers.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

//类似SpringMVC的dispatcher
@Configuration
public class AllRouters {
    @Bean
    RouterFunction<ServerResponse> userRouter(UserHandler handler) {
        return nest(
                //相当于类上 @RequestMapping("/user")
                path("/user"),
                //相当于类里面方法上的 @GetMapping("/")
                //得到所有用户
                route(GET("/"), handler::getAllUser)
                        //创建用户
                        .andRoute(POST("/")
                                        .and(accept(MediaType.APPLICATION_JSON_UTF8))
                                , handler::createUser)
                        //删除用户
                        .andRoute(DELETE("/{id}")
                                , handler::deleteUserById)
        );
    }
}
```

#### 参数校验

原来在校验时已经直接拿到了对象，现在拿到的是Mono

```java
public Mono<ServerResponse> createUser(ServerRequest request) {
        //获得的是Mono, 要先转换成User
        Mono<User> user = request.bodyToMono(User.class);
        return user.flatMap(u -> {
            //校验代码放这里,不直接处理，aop思想，交给切面统一处理
            CheckUtil.checkName(u.getName());
            return ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(this.repository.save(u), User.class);
        });
}
```

```java
import com.wzl.exceptions.CheckException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
@Component
@Order(-2)  //数值越小，优先级越高
public class ExceptionHandler implements WebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        //设置响应头400
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        //设置返回类型
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        //异常信息
        String errorMsg = toStr(ex);
        DataBuffer db = response.bufferFactory().wrap(errorMsg.getBytes());
        return response.writeWith(Mono.just(db));
    }
    private String toStr(Throwable ex) {
        //已知异常
        if (ex instanceof CheckException) {
            CheckException e = (CheckException) ex;
            return e.getFieldName() + ":invalid value：" + e.getFieldValue();
        }
        //未知异常,需要打印堆栈，方便定位
        else {
            ex.printStackTrace();
            return ex.toString();
        }
    }
}
```

