package adt.inventario.dao;

import adt.inventario.model.Product;
import org.hibernate.HibernateException;

import java.util.List;

public interface ProductDAO {
    void addProduct(Product product) throws HibernateException;
    void updateProduct(Product product);
    List<Product> list();
    void use(int number);
    void removeProduct();
    List<Product> hasProduct(String productName);

    void exit();

}
