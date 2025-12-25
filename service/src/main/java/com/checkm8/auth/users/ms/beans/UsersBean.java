package com.checkm8.auth.users.ms.beans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import com.checkm8.auth.users.ms.entities.User;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class UsersBean {

    @PersistenceContext
    private EntityManager em;
    private Logger log = Logger.getLogger(UsersBean.class.getName());

    @PostConstruct
    private void init() {
        log.info("Bean initialized " + UsersBean.class.getSimpleName());
    }
    @PreDestroy
    private void destroy() {
        log.info("Bean destroyed " + UsersBean.class.getSimpleName());
    }

    public List<User> getAll() {
        return em.createNamedQuery("User.getAll", User.class).getResultList();
    }

    public User get(String sub) {
        return em.find(User.class, sub);
    }

    @Transactional
    public User create(String sub) {

        User existing = this.get(sub);
        if (existing != null) return existing;

        User user = User.createStarterUser(sub);
        em.persist(user);
        return user;
    }

    @Transactional
    public User update(String sub, User user) {

        if (user == null || sub == null) throw new IllegalArgumentException();

        User oldUser = this.get(sub);
        if (oldUser == null) return null;

        if (user.getElo() != null) oldUser.setElo(user.getElo());

        return oldUser;
    }

    @Transactional
    public boolean delete(String sub) {

        User user = this.get(sub);
        if (user != null) {
            em.remove(user);
            return true;
        }
        return false;
    }
}
