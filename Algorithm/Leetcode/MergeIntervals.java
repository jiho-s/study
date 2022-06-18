import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(array -> array[0]));
        List<int[]> result = new ArrayList<>(intervals.length);
        for (int[] interval : intervals) {
            if (result.isEmpty()) {
                result.add(interval);
                continue;
            }
            int[] last = result.get(result.size() - 1);
            if (last[1] >= interval[0]) {
                last[1] = Integer.max(last[1], interval[1]);
            } else {
                result.add(interval);
            }
        }
        return result.toArray(new int[0][]);
    }
}