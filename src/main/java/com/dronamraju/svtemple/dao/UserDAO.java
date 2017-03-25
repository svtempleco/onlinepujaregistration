package com.dronamraju.svtemple.dao;

import com.dronamraju.svtemple.model.Product;
import com.dronamraju.svtemple.model.User;
import com.dronamraju.svtemple.model.UserProduct;
import com.dronamraju.svtemple.util.EncryptDecryptStringWithDES;
import com.dronamraju.svtemple.util.EntityManagerUtil;
import com.dronamraju.svtemple.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;


public class UserDAO {
	private static Log log = LogFactory.getLog(UserDAO.class);

	EntityManager entityManager = EntityManagerUtil.getEntityManager();

	public void saveCat(Product cat){
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			log.info("Saving cat: " + cat);
			entityTransaction.begin();
			entityManager.persist(cat);
			entityTransaction.commit();
		} catch (Exception e) {
			entityTransaction.rollback();
			throw new RuntimeException(e);
		}
	}

	public User saveUser(User user){
		log.info("Saving user: " + user);
		log.info("entityManager: " + entityManager);
		user.setPassword(user.getPassword());
		User savedUser = null;
		EntityTransaction entityTransaction = entityManager.getTransaction();
		try {
			entityTransaction.begin();
			savedUser = entityManager.merge(user);
			entityTransaction.commit();
		} catch (Exception e) {
			if (entityTransaction.isActive()) {
				entityTransaction.rollback();
			}
			throw new RuntimeException(e);
		}
		//savedUser.setPassword(EncryptDecryptStringWithDES.decrypt(savedUser.getPassword()));
		//savedUser.setRePassword(EncryptDecryptStringWithDES.decrypt(savedUser.getPassword()));
		log.info("savedUser: " + savedUser);
		return savedUser;
	}

	public User findUser(Long userId){
		log.info("findUser..");
		User user = entityManager.find(User.class, userId);
		//user.setPassword(EncryptDecryptStringWithDES.decrypt(user.getPassword()));
		//user.setRePassword(EncryptDecryptStringWithDES.decrypt(user.getPassword()));
		return user;
	}

	public User findUser(String email, String password) {
		Query query = entityManager.createQuery("SELECT user FROM User user WHERE email = :email and password = :password", User.class);
		query.setParameter("email", email);
		query.setParameter("password", password);
		List<User> users = query.getResultList();
		if (users == null || users.size() < 1) {
			return null;
		}
		User user = users.get(0);
		//user.setPassword(EncryptDecryptStringWithDES.decrypt(user.getPassword()));
		//user.setRePassword(EncryptDecryptStringWithDES.decrypt(user.getPassword()));
		log.info("findUser - user: " + user);
		return user;
	}

	public List<UserProduct> findUserProducts(String orderNumber) {
		Query query = entityManager.createQuery("SELECT userProduct FROM UserProduct userProduct WHERE orderNumber = :orderNumber", UserProduct.class);
		query.setParameter("orderNumber", orderNumber);
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

	public String findValue(String name) {
		log.info("findValue: " + name);
		Query query = entityManager.createQuery("SELECT value FROM configuration_table configuration WHERE configuration_name = :name", String.class);
		query.setParameter("name", name);
		List<String> values = query.getResultList();
		log.info("values: " + values);
		if (values == null || values.size() < 1) {
			return values.get(0);
		}
		return null;
	}

	public boolean orderNumberExists(String orderNumber) {
		log.info("orderNumberExists: " + orderNumber);
		Query query = entityManager.createQuery("SELECT orderNumber FROM UserProduct userProduct WHERE orderNumber = :orderNumber", String.class);
		query.setParameter("orderNumber", orderNumber);
		List<String> orderNumbers = query.getResultList();
		log.info("orderNumbers: " + orderNumbers);
		if (orderNumbers == null || orderNumbers.size() < 1) {
			return false;
		}
		return true;
	}

	public Product findProduct(Long productId){
		log.info("findProduct..");
		return entityManager.find(Product.class, productId);
	}
}
