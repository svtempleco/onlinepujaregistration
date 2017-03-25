package com.dronamraju.svtemple.dao;


import com.dronamraju.svtemple.model.Product;
import com.dronamraju.svtemple.model.User;
import com.dronamraju.svtemple.model.UserProduct;
import com.dronamraju.svtemple.util.EntityManagerUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdronamr on 2/23/17.
 */
public class ProductDAO {

    private static Log log = LogFactory.getLog(ProductDAO.class);

    EntityManager entityManager = EntityManagerUtil.getEntityManager();

    public Product findProduct(Long productId){
        log.info("findProduct..");
        return entityManager.find(Product.class, productId);
    }

    public List getProducts() {
        Query query = entityManager.createQuery("SELECT product FROM Product product", Product.class);
        List results = query.getResultList();
        List<Product> products = query.getResultList();
        log.info("ProductDAO - Products: " + products);
        return products;
    }

    public List getProducts(Long userId) {
        Query query = entityManager.createQuery("SELECT product FROM Product product WHERE productId = :userId", Product.class);
        query.setParameter("userId", userId);
        List<Product> products = query.getResultList();
        log.info("ProductDAO - Products: " + products);
        return products;
    }

    public void saveUserProduct(UserProduct userProduct){
        log.info("Saving userProduct: " + userProduct);
        log.info("entityManager: " + entityManager);
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.merge(userProduct);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public void save(Product product) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            log.info("Saving product: " + product);
            entityTransaction.begin();
            entityManager.persist(product);
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            throw new RuntimeException(e);
        }
    }

    public void save(UserProduct userProduct){
        log.info("Saving userProduct: " + userProduct);
        entityManager.getTransaction().begin();
        entityManager.persist(userProduct);
        entityManager.getTransaction().commit();
    }

    public void updateProduct(Product selectedProduct) {
        entityManager.getTransaction().begin();
        entityManager.persist(selectedProduct);
        entityManager.getTransaction().commit();
    }

    public void removeProduct(Product selectedProduct) {
        entityManager.getTransaction().begin();
        entityManager.remove(selectedProduct);
        entityManager.getTransaction().commit();
    }

    public Product find(Long id) {
        return entityManager.find(Product.class, id);
    }

    public User findUser(Long userId){
        log.info("findUser..");
        return entityManager.find(User.class, userId);
    }

    public List<UserProduct> findAllUserProducts() {
        Query query = entityManager.createQuery("SELECT userProduct FROM UserProduct userProduct", UserProduct.class);
        List<UserProduct> userProducts = query.getResultList();
        for (UserProduct userProduct : userProducts) {
            userProduct.setUser(findUser(userProduct.getUserId()));
            userProduct.setProduct(findProduct(userProduct.getProductId()));
        }
        log.info("userProducts: " + userProducts.size());
        if (userProducts == null || userProducts.size() < 1) {
            return null;
        }
        return userProducts;
    }
}