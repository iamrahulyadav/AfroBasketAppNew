package com.afrobaskets.App.bean;

import java.util.ArrayList;

/**
 * Created by HP-PC on 12/11/2017.
 */

public class ViewAndTrackOrderBeans {
   String id;
    String user_id;
    String order_id;
    String parent_order_id;
    String merchant_id;
    String shipping_address_id;
    String amount;
    String payable_amount;
    String discount_amount;
    String commission_amount;
    String tax_amount;
    String payment_status;
    String order_status;
    String created_date;
    String updated_date;
    String number_of_item;
    String imageRootPath;
    String shipping_charge;
    String time_slots;
String  delivery_date;

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTime_slots() {
        return time_slots;
    }

    public void setTime_slots(String time_slots) {
        this.time_slots = time_slots;
    }

    public String getImageRootPath() {
        return imageRootPath;
    }

    public void setImageRootPath(String imageRootPath) {
        this.imageRootPath = imageRootPath;
    }

    public String getShipping_charge() {
        return shipping_charge;
    }

    public void setShipping_charge(String shipping_charge) {
        this.shipping_charge = shipping_charge;
    }

    public String getNumber_of_item() {
        return number_of_item;
    }

    public void setNumber_of_item(String number_of_item) {
        this.number_of_item = number_of_item;
    }

    ArrayList<ViewAndTrackItemOrderBean>viewAndTrackItemOrderBeanArrayList;

    public ArrayList<ViewAndTrackItemOrderBean> getViewAndTrackItemOrderBeanArrayList() {
        return viewAndTrackItemOrderBeanArrayList;
    }

    public void setViewAndTrackItemOrderBeanArrayList(ArrayList<ViewAndTrackItemOrderBean> viewAndTrackItemOrderBeanArrayList) {
        this.viewAndTrackItemOrderBeanArrayList = viewAndTrackItemOrderBeanArrayList;
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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getParent_order_id() {
        return parent_order_id;
    }

    public void setParent_order_id(String parent_order_id) {
        this.parent_order_id = parent_order_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getShipping_address_id() {
        return shipping_address_id;
    }

    public void setShipping_address_id(String shipping_address_id) {
        this.shipping_address_id = shipping_address_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getCommission_amount() {
        return commission_amount;
    }

    public void setCommission_amount(String commission_amount) {
        this.commission_amount = commission_amount;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
}
