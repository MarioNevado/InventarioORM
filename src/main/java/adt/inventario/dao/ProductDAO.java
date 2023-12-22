package adt.inventario.dao;

import adt.inventario.exceptions.UsedUnitsExceedException;
import adt.inventario.model.Product;
import org.hibernate.HibernateException;

import java.util.List;

public interface ProductDAO {
    void addProduct(Product product) throws HibernateException;
    void updateProduct(Product product);
    List<Product> list();
    void use(int number, String productName) throws UsedUnitsExceedException;
    void removeProduct(String productName);
    long hasProduct(String productName);
    void getProduct(String productName);
    Object[] getProductByName(String productName);

}
