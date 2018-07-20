package com.afrobaskets.App.interfaces;

import com.afrobaskets.App.bean.CartListBean;

import java.util.ArrayList;

/**
 * Created by HP-PC on 12/12/2017.
 */

public interface CartCallback {
    void onSuccess(ArrayList<CartListBean>cartListBeanArrayList,int result);
}
