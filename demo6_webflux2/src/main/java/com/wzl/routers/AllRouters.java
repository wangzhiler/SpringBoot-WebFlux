package com.wzl.routers;

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
