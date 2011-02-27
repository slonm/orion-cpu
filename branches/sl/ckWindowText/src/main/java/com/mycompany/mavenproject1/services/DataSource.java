package com.mycompany.mavenproject1.services;

import java.util.*;

/**
 *
 * @author slobodyanuk
 */
public class DataSource {

    private List<Entity> list = new ArrayList<Entity>();
    private int nextId = 1;

    {
        list.add(new Entity(nextId++, "Entity 1", "Entity 1Entity 1Entity 1Entity 1Entity 1Entity 1Entity 1Entity 1Entity 1Entity 1Entity 1"));
        list.add(new Entity(nextId++, "Entity 2", "Entity 2Entity 2Entity 2Entity 2Entity 2Entity 2Entity 2Entity 2Entity 2Entity 2Entity 2"));
        list.add(new Entity(nextId++, "Entity 3", "Entity 3Entity 3Entity 3Entity 3Entity 3Entity 3Entity 3Entity 3Entity 3Entity 3Entity 3Entity 3"));
        list.add(new Entity(nextId++, "Entity 4", "Entity 4Entity 4Entity 4Entity 4Entity 4Entity 4Entity 4Entity 4Entity 4Entity 4Entity 4Entity 4Entity 4"));
        list.add(new Entity(nextId++, "Entity 5", "Entity 5Entity 5Entity 5Entity 5Entity 5Entity 5Entity 5Entity 5Entity 5Entity 5Entity 5Entity 5Entity 5"));
        list.add(new Entity(nextId++, "Entity 6", "Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6Entity 6"));
    }

    public Entity newEntity() {
        return new Entity();
    }

    public void insert(Entity e) {
        e.setId(nextId++);
        list.add(e);
    }

    public void update(Entity e) {
        //no operations
    }

    public void delete(Integer id) {
        Iterator<Entity> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().getId() == id) {
                it.remove();
                return;
            }
        }
    }

    public List<Entity> findAll() {
        return Collections.unmodifiableList(list);
    }

    public Entity findById(Integer id) {
        for (Entity e : list) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }
}
