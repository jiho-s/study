package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Solution solution = new Solution();
        int len = scanner.nextInt();
        for (int i = 0; i < len; i++) {
            int h = scanner.nextInt();
            int w = scanner.nextInt();
            boolean [][] table = new boolean[h][w];
            for (int j = 0; j < h; j++) {
                String next = scanner.next();
                for (int k = 0; k < w; k++) {
                    char c = next.charAt(k);
                    if (c == '#') {
                        continue;
                    }
                    table[j][k] = true;
                }
            }
            System.out.println(solution.solution(table, h, w));
        }
    }

}

class Solution {
    boolean [][] table;
    int maxX;
    int maxY;
    public static final int [][][] types = {
            {{0,0}, {1,0}, {0,1}},
            {{0,0},{0,1},{1,1}},
            {{0,0}, {1,0}, {1,1}},
            {{0,0}, {1,0}, {1,-1}}
    };
    public int solution(boolean [][] table, int h, int w) {
        this.table = table;
        this.maxX = h-1;
        this.maxY = w-1;
        return cover();
    }

    private boolean canSet(int x, int y, int typeNum) {
        return Arrays.stream(types[typeNum]).noneMatch(type -> {
            int nextX = x + type[0];
            int nextY = y + type[1];
            if (nextX > maxX || nextY > maxY || nextX < 0 || nextY < 0) {
                return true;
            }else return !table[nextX][nextY];
        });
    }
    private void set(int x, int y, int typeNum, boolean toSet) {
        Arrays.stream(types[typeNum]).forEach(type -> {
            int nextX = x + type[0];
            int nextY = y + type[1];
            table[nextX][nextY] = toSet;
        });
    }


    private int cover() {
        int x = -1;
        int y = -1;

        loop: for (int i = 0; i <= maxX; i++) {
            for (int j = 0; j <= maxY; j++) {
                if (table[i][j]) {
                    x = i;
                    y = j;
                    break loop;
                }
            }
        }

        if (x == -1) return 1;
        int result = 0;
        for (int i = 0; i < 4; i++) {
            if (canSet(x, y, i)) {
                set(x, y, i, false);
                result += cover();
                set(x, y, i, true);
            }
        }
        return result;
    }
}
