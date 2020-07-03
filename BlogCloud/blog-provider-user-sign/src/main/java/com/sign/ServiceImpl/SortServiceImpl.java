package com.sign.ServiceImpl;

import com.sign.Service.SortService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SortServiceImpl implements SortService {



    //快速排序的算法
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public void quickSort(long[] array,int low,int high) {
        if (array.length==0){
            return;
        }
        if (low>high){
            return;
        }
        int i=low;
        int j=high;
        //基准值
        long threshold=array[low];
        //从表的两端交替向中间扫描
        while (i<j) {
            //从右往左找到第一个小于threshold的数的位置
            while (i < j && array[j] > threshold) {
                j--;
            }
            if (i < j) {
                array[i++] = array[j];//用比threshold小的数替换低位
            }
            //从左往右找到第一个大于threshold的数的位置
            while (i < j && array[i] < threshold) {
                i++;
            }
            if (i < j) {
                array[j--] = array[i];//用比threshold大的数替换高位
            }
        }
        array[i]=threshold;//将threshold替换回array[i]
        //对threshold左边的数快排
        quickSort(array,low,i-1);
        //对threshold右边的数快排
        quickSort(array,i+1,high);
    }

    //二分查找的算法
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int binarySearch(long[] arrays, long searchValue) {
        //1.先初始化最低位 low=0，最高位high=scores.length-1
        int low=0;
        int high=arrays.length-1;
        int mid=0;
        while (low<=high){
            //2.求出中间数mid=（low+high）/2的值scores[mid]
            mid=(low+high)/2;
            //3.将中间位scores[mid]与Value比较
            //如果scores[mid]==value说明找到了当前索引mid，
            if (arrays[mid]==searchValue){
                return mid;
            }
            else if (arrays[mid]<searchValue){
                //如果小于成立，则说明要找的Value在mid+1和high之间
                low=mid+1;
            }
            else if (arrays[mid]>searchValue){
                //如果大于成立，则说明要找到的Values在low和mid-1之间
                high=mid-1;
            }
        }
        return -1;
    }
}
