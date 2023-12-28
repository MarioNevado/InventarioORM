package adt.inventario.dao;

import adt.inventario.exceptions.NegativeGettedUnitsException;
import adt.inventario.exceptions.UsedUnitsExceedException;
import adt.inventario.model.Product;
import org.hibernate.HibernateException;

import java.util.List;

public interface ProductDAO {
    void addProduct(Product product) throws HibernateException;

    void updateProduct(Product product) throws HibernateException;

    List<Product> list() throws HibernateException;

    void use(int number, String productName) throws UsedUnitsExceedException;

    void removeProduct(Product product) throws HibernateException;

    List<Product> hasProduct(String productName) throws HibernateException;

    boolean isAdded(String productName) throws HibernateException;

    void getProduct(String productName, int units) throws NegativeGettedUnitsException;

    Product getProductByName(String productName) throws HibernateException;

    int getId(Product product) throws HibernateException;

}
