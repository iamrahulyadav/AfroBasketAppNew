package com.afrobaskets.App.bean;

/**
 * Created by asdfgh on 10/17/2017.
 */

public class OrderBean {

    private Integer order_id;
    private Integer order_date;
    private String order_address;
    private Integer delivery_date;

    public OrderBean(){

    }

    public OrderBean(Integer order_id, Integer order_date, String order_address, Integer delivery_date) {
        this.order_id = order_id;
        this.order_date = order_date;
        this.order_address = order_address;
        this.delivery_date = delivery_date;
    }



    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Integer order_date) {
        this.order_date = order_date;
    }

    public String getOrder_address() {
        return order_address;
    }

    public void setOrder_address(String order_address) {
        this.order_address = order_address;
    }

    public Integer getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Integer delivery_date) {
        this.delivery_date = delivery_date;
    }



}
