package adt.inventario.pojo;

import adt.inventario.dao.ProductDAO;
import adt.inventario.exceptions.UsedUnitsExceedException;
import adt.inventario.model.Product;
import adt.inventario.utils.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ProductPojo implements ProductDAO {
    @Override
    public void addProduct(Product product) throws HibernateException {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (product != null) {
                tx = session.beginTransaction();
                session.persist(product);
                tx.commit();
            } else throw new HibernateException("El producto está vacío");
        } catch (HibernateException h) {
            if (tx != null) {
                tx.rollback();
            }
            throw new HibernateException("Error intentando añadir el producto " + product);
        }


    }

    @Override
    public void updateProduct(Product product) throws HibernateException{
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(product);
            tx.commit();
        } catch (HibernateException h) {
            if (tx != null) {
                tx.rollback();
            }
            throw new HibernateException("Error actualizando");
        }
    }

    @Override
    public List<Product> list() throws HibernateException{
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery("FROM products", Product.class);
            return query.list();
        } catch (HibernateException h) {
            throw h;
        }
    }

    @Override
    public void use(int number, String productName) throws UsedUnitsExceedException {
        Product product;
        product = new Product((String) getProductByName(productName)[0], (int) getProductByName(productName)[1]);
        product.setAmount(product.getAmount() - number);
        if (product.getAmount() < number) throw new UsedUnitsExceedException("No hay tantas unidades disponibles");
        else if (product.getAmount() == number) removeProduct(productName);
        else updateProduct(product);

    }

    @Override
    public void removeProduct(String productName) throws HibernateException{
        Transaction tx = null;
        Product product;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            product = session.get(Product.class, productName);
            if (product != null) {
                session.remove(product);
            }
            tx.commit();
        } catch (HibernateException h) {
            if (tx != null) {
                tx.rollback();
            }
            throw h;
        }
    }

    @Override
    public long hasProduct(String productName) throws HibernateException{
        productName = productName.toLowerCase();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder(); //constructor
            CriteriaQuery<Long> cQuery = cb.createQuery(Long.class); //query que indica que devolverá long
            Root<Product> root = cQuery.from(Product.class); // referencia la clase de origen de la consulta
            cQuery.select(cb.count(root)).where(cb.like(root.get("name"), "%" + productName + "%")).orderBy(cb.asc(root.get("name")));
            Query<Long> query = session.createQuery(cQuery);
            return query.getSingleResult();
        } catch (HibernateException h) {
            throw h;
        }
    }

    @Override
    public void getProduct(String productName) {
        Product product;
        if (hasProduct(productName) != 0) {
            product = new Product((String) getProductByName(productName)[0], (Integer) getProductByName(productName)[1]);
            product.setAmount(product.getAmount() + 1);
            updateProduct(product);
        }

    }

    @Override
    public Object[] getProductByName(String productName) throws HibernateException{
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> cQuery = cb.createQuery(Object[].class);
            Root<Product> root = cQuery.from(Product.class);
            cQuery.multiselect(root.get("name"), root.get("amount")).where(cb.equal(root.get("name"), productName));
            Query<Object[]> query = session.createQuery(cQuery);
            return query.getSingleResult();
        } catch (HibernateException h) {
            throw h;
        }
    }


}
