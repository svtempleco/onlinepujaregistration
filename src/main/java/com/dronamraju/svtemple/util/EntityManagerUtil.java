package com.dronamraju.svtemple.util;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by mdronamr on 2/28/17.
 */
public class EntityManagerUtil {
    private static EntityManager entityManager;

    private EntityManagerUtil() {
    }

    public static EntityManager getEntityManager() {
        if(entityManager==null){
            EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("jpa-example");
            return emFactory.createEntityManager();
        }
        return entityManager;
    }
}
