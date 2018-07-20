package com.afrobaskets.App.bean;

/**
 * Created by HP-PC on 11/25/2017.
 */

public class ShopByCategoriesFragmentsBean {

    String id;
    String category_name;
    String parent_category_id;
    String category_des;
    String image_root_path;

    public String getImage_root_path() {
        return image_root_path;
    }

    public void setImage_root_path(String image_root_path) {
        this.image_root_path = image_root_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getParent_category_id() {
        return parent_category_id;
    }

    public void setParent_category_id(String parent_category_id) {
        this.parent_category_id = parent_category_id;
    }

    public String getCategory_des() {
        return category_des;
    }

    public void setCategory_des(String category_des) {
        this.category_des = category_des;
    }
    String image_id,image_name;
    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
