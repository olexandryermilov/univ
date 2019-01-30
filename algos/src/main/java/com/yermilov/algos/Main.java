package com.yermilov.algos;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int n = 1000;
        for (int p = 3; p <= 6; p++) {
            Random random = new Random(System.currentTimeMillis());
            MyHashMap hashMap = new MyHashMap(n);
            for (int i = 0; i < n; i++) {
                hashMap.addNumber(Math.abs(random.nextInt()));
            }
            hashMap.setMinMax();
            System.out.println("n = " + n + ", min = " + hashMap.getMin() + ", max = " + hashMap.getMax());
            n*=10;
        }
    }
}
