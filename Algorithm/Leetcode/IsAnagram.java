class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        var sortedS = s.chars().sorted().toArray();
        var sortedT = t.chars().sorted().toArray();
        for (int i = 0; i < sortedS.length; i++) {
            if (sortedS[i] != sortedT[i]) return false;
        }
        return true;
    }
}