package com.janardan.application.coding;

/**
 * Created for janardan on 16/10/20
 */
public class Problem1 {

    public static String getMinString(String str) {
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder(str);
        int n = chars.length;
        int i = 0;
        int j = n - 1;
        while (i < n) {
            char c = chars[i];
            while (i < j && chars[i] == c) {
                sb.deleteCharAt(i);
                i++;
            }
            while (i > j && chars[j] == c) {
                sb.deleteCharAt(j);
                j--;
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getMinString("aabcccabba"));
    }
}
