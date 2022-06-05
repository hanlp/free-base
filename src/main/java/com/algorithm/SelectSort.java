package com.algorithm;

import com.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择排序
 */
public class SelectSort {


    public static void main(String[] args) {
        int ct = 100;
        List<Integer> list = getList(ct);
        System.out.println("list：" + list);
        List<Integer> sortList = doSort(list);
        System.out.println(sortList);
    }

    private static List<Integer> doSort(List<Integer> list) {
        List<Integer> tmpList = new ArrayList<>(list);

        List<Integer> sortList = new ArrayList<>();
        for (Integer integer : list) {
            Integer min = findMin(tmpList);
            sortList.add(min);
            tmpList.remove(min);
        }
        return sortList;
    }

    private static Integer findMin(List<Integer> tmpList) {
        Integer min = tmpList.get(0);
        for (int i = 0; i < tmpList.size(); i++) {
            if (tmpList.get(i) < min) {
                min = tmpList.get(i);
            }
        }
        return min;
    }

    private static List<Integer> getList(Integer ct) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < ct; i++) {
            list.add(RandomUtil.nextInt(ct));
        }
        return list;
    }
}
