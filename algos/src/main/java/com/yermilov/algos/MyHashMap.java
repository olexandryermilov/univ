package com.yermilov.algos;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class MyHashMap {
    private ArrayList<LinkedList<Integer>> storage;
    private int p;
    private final static Random RANDOM = new Random(System.currentTimeMillis());
    private final static int a = RANDOM.nextInt(5333);
    private final static int b = RANDOM.nextInt(1235);
    private int min;
    private int max;

    public MyHashMap(int n) {
        storage = new ArrayList<>();
        for (int i = 0; i < n/100; i++) {
            storage.add(new LinkedList<>());
        }
        p = n;
        while (!isPrime(p)) p++;
        min = n;
    }

    private boolean isPrime(int number) {
        if (number == 2) return true;
        if (number % 2 == 0) return false;
        for (int i = 3; 1L * i * i <= number; i += 2) {
            if (number % i == 0) return false;
        }
        return true;
    }

    private int hash(int number) {
        return (int) (((long) a * number + b) % p)%storage.size();
    }

    public void addNumber(int number) {
        storage.get(hash(number)).add(number);
    }

    public void setMinMax() {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).size() > max) max = storage.get(i).size();
            if (storage.get(i).size() < min) min = storage.get(i).size();
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
