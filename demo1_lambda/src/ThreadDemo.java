/**
 * Created by thinkpad on 2018/10/10.
 */
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
