package com.algorithm;

import com.util.RandomUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 递归 阶乘实现, 例如：3*2*1
 */
public class RecursionDemo {

    public static void main(String[] args) {
//        int x = 5;
//        int r = doFactorial(x);
//        System.out.println(r);

//        stackFILO();
//        stackFIFO();

//        int ct = 4;
//        sum(ct);

//        int ct = 10;
//        findMax(ct);

//        int ct = 10;
//        binarySearch(ct);

        int ct = 10;
        quickSort(ct);
    }

    /**
     * 快速排序
     * @param ct
     */
    private static void quickSort(int ct) {
        List<Integer> list = RandomUtil.randomList(ct);
        System.out.println(list);
        doQuickSort(list);
    }


    private static int quick_sort_ct = 0;
    private static List<Integer> doQuickSort(List<Integer> list) {
        quick_sort_ct++;
        if (list.size() < 2) {
            return list;
        }

        Integer pivot = list.get(0);
        List<Integer> ltList = list.stream().filter(it -> it < pivot).collect(Collectors.toList());
        List<Integer> gtList = list.stream().filter(it -> it > pivot).collect(Collectors.toList());

        List<Integer> ltSortList = doQuickSort(ltList);
        List<Integer> gtSortList = doQuickSort(gtList);

        List<Integer> rList = new ArrayList<>();
        rList.addAll(ltSortList);
        rList.add(pivot);
        rList.addAll(gtSortList);
        System.out.println(quick_sort_ct + "_" + rList);
        return rList;
    }

    /**
     * 二分查找
     */
    private static int search_ct = 0;
    private static void binarySearch(int ct) {
        List<Integer> list = RandomUtil.randomList(ct);
        list.sort(Comparator.comparing(it -> it));
        System.out.println(list);

        int target = 9;
        int low = 0;
        int hight = list.size() -1;
        int mid = (low + hight) / 2;
        Integer integer = doSearch(list, target, low, hight, mid);
        System.out.println(search_ct +"_"+integer);
    }

    private static Integer doSearch(List<Integer> list, Integer target, int low, int hight, int mid) {
        search_ct ++;
        System.out.println("mid:"+ mid);
        if (low > hight) {
            return null;
        }
        mid = (low + hight) / 2;
        Integer midValue = list.get(mid);
        if (midValue > target) {
            hight = mid -1;
        } else if (midValue < target) {
            low = mid + 1;
        } else {
            return midValue;
        }
        return doSearch(list, target, low, hight, mid);
    }

    /**
     * 找最大值
     * @param ct
     */
    private static void findMax(int ct) {
        List<Integer> list = RandomUtil.randomList(ct);
        System.out.println(list);
        int max = list.get(0);
        max = doFindMax(list, max);
        System.out.println(max);
    }

    private static int doFindMax(List<Integer> list, int max) {
        if(list.isEmpty()) {
            return max;
        }
        if (list.size() == 1) {
            if (list.get(0) > max) {
                return list.get(0);
            } else {
                return max;
            }
        }

        List<Integer> tmp = list.subList(1, list.size());
        int r = doFindMax(tmp, max);
        return list.get(0) > r ? list.get(0) : r;
    }

    /**
     * 求和
     * @param ct
     */
    private static void sum(int ct) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < ct; i++) {
            list.add(RandomUtil.nextInt(ct));
        }

        System.out.println(list);
        int sum = 0;
        sum = doSum(list, sum);

        System.out.println(sum);
    }

    private static int doSum(List<Integer> list, int sum) {
        if (list.isEmpty()) {
            return sum;
        }
        if (list.size() == 1) {
            sum = sum + list.get(0);
            return sum;
        }

        List<Integer> tmp = list.subList(1, list.size());
        sum = list.get(0) + doSum(tmp, sum);
        return sum;
    }

    /**
     * 两个栈实现 先进先出
     * 8
     * 4
     * 10
     * 1
     */
    private static void stackFIFO() {
        Stack stack = new Stack();
        stack.push(8);
        stack.push(4);
        stack.push(10);
        stack.push(1);
        Stack stack2 = new Stack();
        while (!stack.empty()) {
            stack2.push(stack.pop());
        }

        while (!stack2.empty()) {
            System.out.println(stack2.pop());
        }



    }

    /**
     * 先进后出
     * 1
     * 10
     * 4
     * 8
     */
    private static void stackFILO() {
        Stack stack = new Stack();
        stack.push(8);
        stack.push(4);
        stack.push(10);
        stack.push(1);
        while (!stack.empty()) {
            Object pop = stack.pop();
            System.out.println(pop);
        }


    }

    private static int doFactorial(int x) {
        // 基线条件
        if (x == 1) {
            return x;
        }
        // 递归条件
        return x * doFactorial(x -1);
    }
}
