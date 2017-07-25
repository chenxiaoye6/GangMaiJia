package com.example.administrator.gangmaijia.Util;

import com.example.administrator.gangmaijia.Model.PurchaseWrite;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/2/7.
 */

public class ComparatorOrder implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        PurchaseWrite purchaseWrite0 = (PurchaseWrite) o1;
        PurchaseWrite purchaseWrite1 = (PurchaseWrite) o2;
        int flag = purchaseWrite0.getOrder_num().compareTo(purchaseWrite1.getOrder_num());
        return flag;
    }
}
