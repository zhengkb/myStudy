package sort;

/**
 * 冒泡排序
 * 每次将最大的数排在末尾
 * 时间复杂度：o(n^2)
 * 空间复杂度：1
 * 稳定
 */
public class BubbleSort {
    public static void main(String[] args) {
        int[] arr = {3, 4, 1, 2, 5, 67, 8, 3, 42, 55, 6, 7, 9};
        int[] sort = sort(arr);
        for (int a : sort) {
            System.out.print(a + " ");
        }
    }

    public static int[] sort(int[] arrs) {
        for (int i = 0; i < arrs.length; i++) {
            for (int j = 0; j < arrs.length - i - 1; j++) {
                if (arrs[j] > arrs[j + 1]) {
                    int temp = arrs[j];
                    arrs[j] = arrs[j + 1];
                    arrs[j + 1] = temp;
                }
            }
        }
        return arrs;
    }
}
