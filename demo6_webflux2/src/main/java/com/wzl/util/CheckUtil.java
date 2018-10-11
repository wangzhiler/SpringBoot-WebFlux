package com.wzl.util;

import com.wzl.exceptions.CheckException;

import java.util.stream.Stream;

public class CheckUtil {

    private static final String[] INVALID_NAMES = {"admin", "guanliyuan"};

    /**
     * 校验名字，不成功则抛出校验异常
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
