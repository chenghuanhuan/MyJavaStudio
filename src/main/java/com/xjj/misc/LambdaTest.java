package com.xjj.misc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by XuJijun on 2018-11-16.
 */
public class LambdaTest {
    public static void main(String[] args){
        Set<String> ss = new HashSet<>();
        ss.add("aaa");
        ss.add("bbb");
        ss.add("ccc");

        Optional<String> reduce = ss.stream().reduce((s1, s2) -> s1 + "#" + s2);
        reduce.ifPresent(System.out::println);
    }
}
