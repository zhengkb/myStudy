package sort;

/**
 * 插入排序:在已知有序的集合中插入新的数字
 * 最好时间复杂度：o(n)
 * 最坏时间复杂度：o(n^2)
 * 平均时间复杂度：o(n^2)
 * 空间复杂度：o(1)
 * 稳定
 */
public class InsertSort {
    public static void main(String[] args) {
        int[] arrs = {3, 5, 6, 1, 2, 5, 6, 8, 0, 12, 3, 9};
        int[] sort = sort(arrs);
        for(int a:sort){
            System.out.print(a+" ");
        }
    }

    public static int[] sort(int[] arrs) {
        for (int i = 1; i < arrs.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arrs[j] < arrs[j - 1]) {
                    int temp = arrs[j - 1];
                    arrs[j - 1] = arrs[j];
                    arrs[j] = temp;
                }
            }
        }
        return arrs;
    }
}
