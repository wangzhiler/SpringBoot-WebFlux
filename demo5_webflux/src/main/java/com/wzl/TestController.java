package com.wzl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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

    //WebFlux写法 线程占时很短很短
    //返回Mono 或者Flux， 其实返回的是流，在Controller内没有调流操作，所以不会阻塞Controller线程
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
