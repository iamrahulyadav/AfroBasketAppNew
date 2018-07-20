package com.afrobaskets.App.bean;

/**
 * Created by HP-PC on 11/26/2017.
 */

public class CategoryListBeans {
   String id;
    String category_name;
    String parent_category_id;
    String category_des;

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
}
