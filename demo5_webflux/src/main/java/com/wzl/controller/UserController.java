package com.wzl.controller;


import com.wzl.domain.User;
import com.wzl.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.wzl.util.CheckUtil.checkName;

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
    public Mono<User> createUser(@Valid @RequestBody User user) {
        // spring data jpa里面，新增和修改都是save，有id是修改，id为空是新增
        // 根据实际情况是否置空id
        user.setId(null); //每次都新增
        checkName(user.getName());
        return this.repository.save(user);
    }

    /**
     * 根据id删除用户，存在返回200，不存在返回404
     *
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
            @Valid @RequestBody User user
    ) {
        checkName(user.getName());
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

    //考虑场景：根据年龄段查找用户
    /**
     * 根据年龄查找用户
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
}
