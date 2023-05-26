package ru.job4j.tracker;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
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
            session.save(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return item;
    }

    @Override
    public boolean replace(Integer id, Item item) {
        var session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "UPDATE Item SET name = :itemName WHERE id = :id")
                    .setParameter("itemName", item.getName())
                    .setParameter("id", id)
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public boolean delete(Integer id) {
        var session = sf.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "DELETE FROM Item WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Item> findAll() {
        var session = sf.openSession();
        List<Item> result = new ArrayList<>();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Item", Item.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        var session = sf.openSession();
        List<Item> result = new ArrayList<>();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Item WHERE name = :name", Item.class)
                    .setParameter("name", key)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public Item findById(Integer id) {
        var session = sf.openSession();
        Item item = new Item();
        try {
            session.beginTransaction();
            item = session.createQuery("FROM Item WHERE id = :id", Item.class)
                    .setParameter("id", id).getSingleResult();
            session.getTransaction().commit();
        } catch (Exception exception) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return item;
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
