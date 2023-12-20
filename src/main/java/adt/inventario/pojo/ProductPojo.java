package adt.inventario.pojo;

import adt.inventario.dao.ProductDAO;
import adt.inventario.model.Product;
import adt.inventario.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProductPojo implements ProductDAO {
    @Override
    public void addProduct(Product product) throws HibernateException{
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (product != null) {
                tx = session.beginTransaction();
                session.persist(product);
                tx.commit();
            }else throw new HibernateException("El producto está vacío");
        } catch (HibernateException h) {
            if (tx != null){
                tx.rollback();
            }
            throw new HibernateException("Error intentando añadir el producto " + product);
        }


    }
    @Override
    public void updateProduct(Product product) {
        Transaction tx = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            tx = session.beginTransaction();
            session.merge(product);
            tx.commit();
        }catch(HibernateException h){
            if (tx != null){
                tx.rollback();
            }
            throw new HibernateException("Error actualizando");
        }
    }

    @Override
    public List<Product> list() {
        return null;
    }

    @Override
    public void use(int number) {

    }

    @Override
    public void removeProduct() {

    }

    @Override
    public List<Product> hasProduct(String productName) {
        return null;
    }
    @Override
    public void exit() {

    }
}
