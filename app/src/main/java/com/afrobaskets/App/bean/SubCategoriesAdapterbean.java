package com.afrobaskets.App.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by HP-PC on 11/26/2017.
 */

public class SubCategoriesAdapterbean
{
    String product_id;
    String product_name;
    String product_desc;
    String category_id;
    String image_id;
    String image_name;
    String image_root_path;
    String brand_name;
    String nutrition_name;
    String nutrition_image;
    ArrayList<SubCategoriesAdapterAttributesBean>subCategoriesAdapterAttributesBeanArrayList;

    public String getNutrition_name()
    {
        return nutrition_name;
    }

    public void setNutrition_name(String nutrition_name)
    {
        this.nutrition_name = nutrition_name;
    }

    public String getNutrition_image()
    {
        return nutrition_image;
    }

    public void setNutrition_image(String nutrition_image)
    {
        this.nutrition_image = nutrition_image;
    }

    public String getBrand_name()
    {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }
String bullet_desc;

    public String getBullet_desc() {
        return bullet_desc;
    }

    public void setBullet_desc(String bullet_desc) {
        this.bullet_desc = bullet_desc;
    }

    LinkedHashMap<String,String>description;

    public LinkedHashMap<String, String> getDescription() {
        return description;
    }

    public void setDescription(LinkedHashMap<String, String> description) {
        this.description = description;
    }

    public String getImage_root_path() {
        return image_root_path;
    }

    public void setImage_root_path(String image_root_path) {
        this.image_root_path = image_root_path;
    }

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



    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public ArrayList<SubCategoriesAdapterAttributesBean> getSubCategoriesAdapterAttributesBeanArrayList() {
        return subCategoriesAdapterAttributesBeanArrayList;
    }

    public void setSubCategoriesAdapterAttributesBeanArrayList(ArrayList<SubCategoriesAdapterAttributesBean>
                                                                       subCategoriesAdapterAttributesBeanArrayList) {
        this.subCategoriesAdapterAttributesBeanArrayList = subCategoriesAdapterAttributesBeanArrayList;
    }
}
