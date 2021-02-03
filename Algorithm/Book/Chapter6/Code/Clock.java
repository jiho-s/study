package com.company;

import java.util.List;

import static java.lang.Math.min;

public class Main {

    public static void main(String[] args) {

    }

}

class Solution {
    int switchNum;
    final String [] table = {
            "xxx.............",
            "...x...x.x.x....",
            "x...xxxx........",
            "......xxx.x.x...",
            "x.x...........xx",
            "...x..........xx",
            "....xx.x......xx",
            ".xxxxx..........",
            "...xxx...x...x.."
    };

    boolean isOk(List<Integer> clocks) {
        return clocks.stream().noneMatch(clock -> clock!=12);
    }

    void pushSwitch(List<Integer> clocks, int currentSwitch) {
        for (int i = 0; i < table.length; i++) {
            if (table[i].charAt(i) == 'x') {
                Integer temp = clocks.get(i);
                temp += 3;
                clocks.set(i, temp == 15 ? 0 : temp);
            }
        }
    }
    int solution(List<Integer> clocks, int currentSwitch) {
        if (currentSwitch == table.length) {
            return isOk(clocks) ? 0 : Integer.MAX_VALUE;
        }

        int answer = Integer.MAX_VALUE;
        for (int cnt = 0; cnt < 4; cnt++) {
            answer = min(answer, cnt + solution(clocks, currentSwitch+1));
            pushSwitch(clocks, currentSwitch);
        }
        return answer;
    }
}
