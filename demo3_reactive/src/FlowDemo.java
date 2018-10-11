import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

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
