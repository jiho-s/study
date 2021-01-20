package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {


    public static int countPairing(ArrayList<Integer>[] table, boolean [] mark, int current, int toPick) {

        if (toPick == 0) {
            return 1;
        }
        int result = 0;
        for (Integer target : table[current]) {
            if (!mark[target]) {
                mark[target] = true;
                result += countPairing(table, mark, target, toPick-1);
                mark[target] = false;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int len = scanner.nextInt();
        for (int i = 0; i < len; i++) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            ArrayList<Integer>[] table = new ArrayList[n];
            boolean [] mark = new boolean[n];
            for (int j = 0; j < m; j++) {
                int front = scanner.nextInt();
                int last = scanner.nextInt();
                if (table[front] == null) {
                    table[front] = new ArrayList<>();
                }
                if (table[last] == null) {
                    table[last] = new ArrayList<>();
                }
                table[front].add(last);
                table[last].add(front);
            }
            mark[0] = true;
            System.out.println(countPairing(table, mark, 0, n-1));

        }
    }

}
