package com.sihhackathon.e_omfed.Model;

public class Products
{

    private String Category ,Product_name,Product_Description,Product_price,Image_URL,pid;

    public Products()
    {

    }


    public Products(String category, String product_name, String product_Description, String product_price, String image_URL, String pid) {
        Category = category;
        Product_name = product_name;
        Product_Description = product_Description;
        Product_price = product_price;
        Image_URL = image_URL;
        this.pid = pid;
    }


    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getProduct_name() {
        return Product_name;
    }

    public void setProduct_name(String product_name) {
        Product_name = product_name;
    }

    public String getProduct_Description() {
        return Product_Description;
    }

    public void setProduct_Description(String product_Description) {
        Product_Description = product_Description;
    }

    public String getProduct_price() {
        return Product_price;
    }

    public void setProduct_price(String product_price) {
        Product_price = product_price;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
