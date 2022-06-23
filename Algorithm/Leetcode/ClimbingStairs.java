class Solution {

    int [] table;
    public int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }
        table = new int[n+1];
        table[1] = 1;
        table[2] = 2;
        for (int i = 3; i <= n; i++) {
            table[i] = table[i-1] + table[i-2];
        }
        return table[n];
    }
}