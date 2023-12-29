package adt.inventario.pojo;

import adt.inventario.dao.ProductDAO;
import adt.inventario.exceptions.IncorrectAcquiredUnitsException;
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
    public void updateProduct(Product product) throws HibernateException {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            product.setId(getId(product));
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
    //TODO preguntar que es eso de buscar por id
    public List<Product> list() throws HibernateException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Product> query = session.createQuery("FROM Product", Product.class);
            return query.list();
        } catch (HibernateException h) {
            throw h;
        }
    }

    @Override
    public void use(int number, Product product) throws UsedUnitsExceedException {
        if (product.getAmount() < number) throw new UsedUnitsExceedException("No hay tantas unidades disponibles");
        else if (product.getAmount() == number) removeProduct(product);
        else {
            product.setAmount(product.getAmount() - number);
            updateProduct(product);
        }
    }

    @Override
    public void removeProduct(Product product) throws HibernateException {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            product.setId(getId(product));
            product = session.get(Product.class, product.getId());
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
    public List<Product> hasProduct(String productName) throws HibernateException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder(); //constructor
            CriteriaQuery<Product> cQuery = cb.createQuery(Product.class); //query que indica que devolverá long
            Root<Product> root = cQuery.from(Product.class); // referencia la clase de origen de la consulta
            cQuery.multiselect(root.get("name"), root.get("amount")).where(cb.like(root.get("name"), "%" + productName + "%")).orderBy(cb.asc(root.get("name")));
            Query<Product> query = session.createQuery(cQuery);
            return query.list();
        } catch (HibernateException h) {
            throw h;
        }
    }

    @Override
    public boolean isNew(String productName) throws HibernateException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder(); //constructor
            CriteriaQuery<Product> cQuery = cb.createQuery(Product.class); //query que indica que devolverá long
            Root<Product> root = cQuery.from(Product.class); // referencia la clase de origen de la consulta
            cQuery.multiselect(root.get("name"), root.get("amount")).where(cb.equal(root.get("name"), productName)).orderBy(cb.asc(root.get("name")));
            Query<Product> query = session.createQuery(cQuery);
            return query.list().isEmpty();
        } catch (HibernateException h) {
            throw h;
        }
    }
    @Override
    public void getProduct(String productName, int units) throws IncorrectAcquiredUnitsException {
        Product product;
        if (!hasProduct((productName)).isEmpty()) {
            if (units < 0)throw new IncorrectAcquiredUnitsException("La base de datos no puede adquirir unidades negativas");
            else if (units == 0) throw new IncorrectAcquiredUnitsException("La base de datos no adquiere nada");
            else {
                product = getProductByName(productName);
                product.setAmount(product.getAmount() + units);
                updateProduct(product);
            }
        } else {
            addProduct(new Product(productName, units));
        }
    }
    @Override
    public Product getProductByName(String productName) throws HibernateException {
        Product product;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> cQuery = cb.createQuery(Object[].class);
            Root<Product> root = cQuery.from(Product.class);
            cQuery.multiselect(root.get("id"), root.get("name"), root.get("amount")).where(cb.equal(root.get("name"), productName));
            Query<Object[]> query = session.createQuery(cQuery);
            product = new Product((String) query.getSingleResult()[1], (Integer) query.getSingleResult()[2]);
            product.setId((Integer) query.getSingleResult()[0]);
            return product;
        } catch (HibernateException h) {
            throw h;
        }
    }

    @Override
    public int getId(Product product) throws HibernateException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> criteriaQuery = cb.createQuery(Integer.class);
            Root<Product> root = criteriaQuery.from(Product.class);
            criteriaQuery.select(root.get("id")).where(cb.equal(root.get("name"), product.getName()));
            Query<Integer> query = session.createQuery(criteriaQuery);
            return query.getSingleResult();
        } catch (HibernateException h) {
            throw h;
        }
    }
}
