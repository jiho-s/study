import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static java.lang.Math.max;
import static java.lang.Math.min;


class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        Solution solution = new Solution();
        while (n-- > 0) {
            int size = Integer.parseInt(br.readLine());
            int [] list = new int[size];
            StringTokenizer tokenizer = new StringTokenizer(br.readLine());
            for (int i = 0; i < list.length; i++) {
                list[i] = Integer.parseInt(tokenizer.nextToken());
            }
            System.out.println(solution.solution(0, list.length-1, list));
        }
    }
}

class Solution {
    int solution(int start, int end, int [] list) {
        if (start == end) {
            return list[start];
        }
        int mid = (start + end) / 2;
        int result = max(solution(start, mid, list), solution(mid + 1, end, list));
        int mStart = mid;
        int mEnd = mid+1;
        int height = min(list[mStart], list[mEnd]);
        result = max(result, height * 2);
        while (mStart > start || mEnd < end) {
            if (mEnd < end && (mStart == start || list[mStart-1] < list[mEnd+1])) {
                mEnd++;
                height = min(height, list[mEnd]);
            } else {
                mStart--;
                height = min(height, list[mStart]);
            }
            result = max(result, height * (mEnd - mStart + 1));
        }
        return result;
    }
}