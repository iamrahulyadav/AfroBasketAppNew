package com.afrobaskets.App.bean;

import java.util.ArrayList;

/**
 * Created by asdfgh on 10/13/2017.
 */

public class CartListBean {

    private String id;
    private String user_id;
    private String guest_user_id;
    private String merchant_inventry_id;
    private String item_name;
    private String number_of_item;
    private String image_root_path;

    public String getImage_root_path() {
        return image_root_path;
    }

    public void setImage_root_path(String image_root_path) {
        this.image_root_path = image_root_path;
    }

    private String created_date;
ArrayList<CartListProductDetailsBean>cartListProductDetailsBeanArrayList;
    ArrayList<CartListProductImage>cartListProductImageArrayList;

    public ArrayList<CartListProductDetailsBean> getCartListProductDetailsBeanArrayList() {
        return cartListProductDetailsBeanArrayList;
    }


    public void setCartListProductDetailsBeanArrayList(ArrayList<CartListProductDetailsBean> cartListProductDetailsBeanArrayList) {
        this.cartListProductDetailsBeanArrayList = cartListProductDetailsBeanArrayList;
    }

    public ArrayList<CartListProductImage> getCartListProductImageArrayList() {
        return cartListProductImageArrayList;
    }

    public void setCartListProductImageArrayList(ArrayList<CartListProductImage> cartListProductImageArrayList) {
        this.cartListProductImageArrayList = cartListProductImageArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getGuest_user_id() {
        return guest_user_id;
    }

    public void setGuest_user_id(String guest_user_id) {
        this.guest_user_id = guest_user_id;
    }

    public String getMerchant_inventry_id() {
        return merchant_inventry_id;
    }

    public void setMerchant_inventry_id(String merchant_inventry_id) {
        this.merchant_inventry_id = merchant_inventry_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getNumber_of_item() {
        return number_of_item;
    }

    public void setNumber_of_item(String number_of_item) {
        this.number_of_item = number_of_item;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}