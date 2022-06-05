package com.algorithm;

import java.util.*;

/**
 * 二分查找
 * 时间复杂度 O(log n)
 */
public class BinarySearch {



    public static void main(String[] args) {
        List<Integer> list = getSortedList(100);

        int target = 50;
        Integer r = search(list, target);
        System.out.println(r);
    }

    private static Integer search(List<Integer> list, int target) {
        int ct = 0;

        int low = 0;
        int hight = list.size() -1;
        while (low <= hight) {
            ct++;
            int mid = (low + hight) / 2;
            System.out.println("mid:" +mid);
            Integer temp = list.get(mid);
            if (temp == target) {
                System.out.println("ct:" + ct);
                return temp;
            } else if (temp > target) {
                hight = mid -1;
            } else {
                low = mid + 1;
            }
        }
        System.out.println("ct:" + ct);
        return null;
    }


    private static List<Integer> getSortedList(int size) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            int nextInt = new Random().nextInt(size);
            set.add(nextInt);
        }

        List<Integer> list = new ArrayList<>();
        list.addAll(set);
        list.sort(Comparator.naturalOrder());
        System.out.println("list:" + list);
        System.out.println("list.size:" + list.size());
        return list;
    }
}
