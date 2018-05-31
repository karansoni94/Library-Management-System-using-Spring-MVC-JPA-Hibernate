package org.libmgmt.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.libmgmt.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDao {

	EntityManagerFactory entityManagerFactory;

	@PersistenceContext
	EntityManager entityManager;

	public void setEntityManager(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
		entityManager = entityManagerFactory.createEntityManager();
	}

	@Transactional
	public User addUser(User user) {
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.getTransaction().commit();
		// entityManager.flush();
		return user;
	}

	@Transactional
	public User updateUser(User user) {
		entityManager.getTransaction().begin();
		user = entityManager.merge(user);
		entityManager.getTransaction().commit();
		return user;
	}

	@Transactional
	public Integer deleteUser(Integer userId) {
		entityManager.getTransaction().begin();
		entityManager.remove(entityManager.find(User.class, userId));
		entityManager.getTransaction().commit();
		return userId;
	}

	public User getUser(Integer id) {
		return entityManager.find(User.class, id);
	}

	public List<User> listUsers() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = cb.createQuery(User.class);
		query.from(User.class);
		return entityManager.createQuery(query).getResultList();
	}

	public List<User> searchUser(String propertyName, String value) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = cb.createQuery(User.class);
		Root<User> root = query.from(User.class);
		Path path = root.get(propertyName);
		Predicate predicate = cb.like(cb.lower(path), "%" + value + "%");
		query.where(predicate);
		return entityManager.createQuery(query).getResultList();
	}

}
