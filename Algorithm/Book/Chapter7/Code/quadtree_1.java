package com.company;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n).forEach(i -> {
            String s = scanner.next();
            Solution solution = new Solution(s);
            System.out.println(solution.reverse());
        });
    }

}

class Solution {

    String s;

    public Solution(String s) {
        this.s = s;
    }

    public String reverse() {
        char head = s.charAt(0);
        s = s.substring(1);
        if (head == 'b' || head == 'w') {
            return String.valueOf(head);
        }
        String upperLeft = reverse();
        String upperRight = reverse();
        String lowerLeft = reverse();
        String lowerRight = reverse();
        return "x" + lowerLeft + lowerRight + upperLeft + upperRight;
    }
    
}

