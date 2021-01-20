package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static int countPairing(ArrayList<Integer>[] table, boolean [] mark, int n) {
        int small = -1;
        for (int i = 0; i < n; i++) {
            if (!mark[i]) {
                small = i;
                break;
            }
        }
        if (small == -1) {
            return 1;
        }
        int result = 0;
        mark[small] = true;
        for (Integer target : table[small]) {
            if (!mark[target]) {
                mark[target] = true;
                result += countPairing(table, mark, n);
                mark[target] = false;
            }
        }
        mark[small] = false;
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
                System.out.println(countPairing(table, mark, n));
            }

        }
    }

}
