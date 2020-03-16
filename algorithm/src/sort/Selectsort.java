package sort;

/**
 *选择排序：每次选取数组中最小的数放在最前面
 * 时间复杂度：o(n^2)
 * 空间复杂度：1
 * 不稳定
 */
public class Selectsort {
    public static void main(String[] args) {
        int[] arrs = {3, 5, 6, 1, 2, 5, 6, 8, 0, 12, 3, 9};
        int[] sort = sort(arrs);
        for (int a : sort) {
            System.out.print(a + " ");
        }
    }

    public static int[] sort(int[] arrs) {
        for (int i = 0; i < arrs.length-1; i++) {
            int min = i;
            for (int j = i; j < arrs.length; j++) {
                if (arrs[j] < arrs[min]) {
                    int temp = arrs[j];
                    arrs[j] = arrs[min];
                    arrs[min] = temp;
                }
            }
        }
        return arrs;
    }
}
