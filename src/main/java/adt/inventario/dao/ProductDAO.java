package adt.inventario.dao;

import adt.inventario.model.Product;

import java.util.List;

public interface ProductDAO {
    List<Product> list();
    void use();
    void hasProduct(String productName);
    Product getProduct();
    void exit();

}
