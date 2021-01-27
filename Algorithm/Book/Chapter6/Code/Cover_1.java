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
    int mayY;
    public static final int [][][] types = {
            {{0,0}, {1,0}, {0,1}},
            {{0,0},{0,1},{1,1}},
            {{0,0}, {1,0}, {1,1}},
            {{0,0}, {1,0}, {1,-1}}
    };
    public int solution(boolean [][] table, int h, int w) {
        this.table = table;
        this.maxX = h-1;
        this.mayY = w-1;
        return cover(0,0);
    }

    private int cover(int i, int j) {
        if (i == maxX && j == mayY) {
            return 1;
        } else if (j > mayY) {
            return cover(i+1, 0);
        }
        if (!table[i][j]) {
            return cover(i, j+1);
        }
        int answer = 0;
        for (int k = 0; k < types.length; k++) {
            if (!canSet(i, j, k)) {
                continue;
            }
            set(i, j, k, true);
            answer += cover(i, j + 1);
            set(i, j, k, false);
        }
        return answer;
    }
    private boolean canSet(int i, int j, int type) {
        return Arrays.stream(types[type]).noneMatch(coordinate -> {
            int x = i + coordinate[0];
            int y = j + coordinate[1];
            if (x < 0 || x > maxX) {
                return true;
            } else if (y < 0 || y > mayY) {
                return true;
            } else return !table[x][y];
        });
    }
    private void set(int i, int j, int type, boolean to) {
        Arrays.stream(types[type]).forEach(coordinate -> {
            int x = i + coordinate[0];
            int y = j + coordinate[1];
            table[x][y] = to;
        });


    }
}
