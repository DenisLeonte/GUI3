package com.denistechs.carrentalgui3.domain;

import java.io.Serializable;


public class Entity<ID> implements Identifiable<ID>, Serializable {
    protected ID id;

    /***
     * Default constructor for Entity
     */
    public Entity() {
        id = null;
    }

    /***
     * Constructor for Entity
     * @param id The id of the entity
     */
    public Entity(ID id)
    {
        this.id = id;
    }

    @Override
    public ID getID() {
        return id;
    }

    @Override
    public void setID(ID id) {
        this.id = id;
    }
}
