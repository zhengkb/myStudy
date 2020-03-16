package sort;

import java.util.Arrays;

/**
 * 希尔排序
 */
public class ShellSort {
    public static void main(String[] args) {

    }

    public static int[] sort(int[] arrs) {
        int[] narr = Arrays.copyOf(arrs, arrs.length);

        int gap = 1;
        while (gap < narr.length) {
            gap = gap * 3 + 1;
        }

        while (gap > 0) {
            for (int i = gap; i < narr.length; i++) {
                int temp = narr[i];
                int j = i - gap;
                while (j >= 0 && narr[j] > temp) {
                    narr[j + gap] = narr[j];
                    j -= gap;
                }
                narr[j + gap] = temp;
            }
            gap = (int) Math.floor(gap / 3);
        }
        return narr;

    }
}
