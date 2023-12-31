package adt.inventario.dao;

import adt.inventario.exceptions.IncorrectAcquiredUnitsException;
import adt.inventario.exceptions.UsedUnitsExceedException;
import adt.inventario.model.Product;
import adt.inventario.utils.CSVReader;
import org.hibernate.HibernateException;

import java.io.IOException;
import java.util.List;

public interface ProductDAO {
    void addProduct(Product product) throws HibernateException;

    void updateProduct(Product product) throws HibernateException;

    List<Product> list() throws HibernateException;

    void use(int number, Product product) throws UsedUnitsExceedException;

    void removeProduct(Product product) throws HibernateException;

    List<Product> hasProduct(String productName) throws HibernateException;

    boolean isNew(Product product) throws HibernateException;

    void getProduct(String productName, int units) throws IncorrectAcquiredUnitsException;

    Product getProductByName(String productName) throws HibernateException;

    int getId(Product product) throws HibernateException;

    void restart(CSVReader csv) throws IOException;

}
