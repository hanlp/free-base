package com.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomUtil {
    private static final Random random = new Random();
    private static final Integer bound_multiple = 1;


    public static int nextInt(Integer bound) {
        return random.nextInt(bound);
    }

    public static List<Integer> randomList(int ct) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < ct; i++) {
            list.add(RandomUtil.nextInt(ct * bound_multiple));
        }
        return list;
    }

    /*** @pdai*/
    class MyString{

        private String innerString;
        // ...init & other methods
        // 支持老的方法
        public int length(){
            return innerString.length();
            // 通过innerString调用老的方法
        }
        // 添加新方法
        public String toMyString(){
            //...

        return null;
        }
            }

    }
