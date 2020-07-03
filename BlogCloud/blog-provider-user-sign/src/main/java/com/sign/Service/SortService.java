package com.sign.Service;

import org.springframework.stereotype.Service;

@Service
public interface SortService {

    //快速排序算法
    void quickSort(long[] array,int low,int high);

    //二分查找的算法
    int binarySearch(long[] arrays,long searchValue);
}
