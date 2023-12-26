package adt.inventario.dao;

import adt.inventario.exceptions.UsedUnitsExceedException;
import adt.inventario.model.Product;
import org.hibernate.HibernateException;

import java.util.List;

public interface ProductDAO {
    void addProduct(Product product) throws HibernateException;

    void updateProduct(Product product) throws HibernateException;

    List<Product> list() throws HibernateException;

    void use(int number, String productName) throws UsedUnitsExceedException;

    void removeProduct(String productName) throws HibernateException;

    long hasProduct(Product product) throws HibernateException;

    void getProduct(String productName);

    Object[] getProductByName(String productName) throws HibernateException;

    int getId(Product product) throws HibernateException;

}
