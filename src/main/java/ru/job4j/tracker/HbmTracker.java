package ru.job4j.tracker;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Collections;
import java.util.List;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.persist(item);
            session.getTransaction().commit();
            return item;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            sf.close();
        }
        return item;
    }

    @Override
    public boolean replace(Integer id, Item item) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE Item SET name = :itemName WHERE id = :id",
                            Item.class)
                    .setParameter("itemName", item.getName())
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            sf.close();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE FROM Item WHERE id = :id", Item.class)
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            sf.close();
        }
        return false;
    }

    @Override
    public List<Item> findAll() {
        var session = sf.openSession();
        try {
            var items = session.createQuery("FROM Item", Item.class).list();
            session.getTransaction().commit();
            return items;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            sf.close();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Item> findByName(String key) {
        var session = sf.openSession();
        try {
            var items = session.createQuery("FROM Item item where item.name = :name", Item.class)
                    .setParameter("name", key)
                    .list();
            session.getTransaction().commit();
            return items;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            sf.close();
        }
        return Collections.emptyList();
    }

    @Override
    public Item findById(Integer id) {
        var session = sf.openSession();
        try {
            var item = session.createQuery("FROM Item item where item.id = :id", Item.class)
                    .setParameter("id", id).uniqueResult();
            session.getTransaction().commit();
            return item;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            sf.close();
        }
        return new Item();
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
