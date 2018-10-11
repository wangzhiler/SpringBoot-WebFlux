package com.wzl.handlers;

import com.wzl.domain.User;
import com.wzl.repository.UserRepository;
import com.wzl.util.CheckUtil;
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
        //获得的是Mono, 要先转换成User
        Mono<User> user = request.bodyToMono(User.class);

        return user.flatMap(u -> {
            //校验代码放这里,不直接处理，aop思想，交给切面统一处理
            CheckUtil.checkName(u.getName());
            return ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(this.repository.save(u), User.class);
        });

//        return ok().contentType(MediaType.APPLICATION_JSON_UTF8)
//                .body(this.repository.saveAll(user), User.class);
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
