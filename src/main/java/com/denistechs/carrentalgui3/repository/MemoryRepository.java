package com.denistechs.carrentalgui3.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.denistechs.carrentalgui3.domain.Car;
import com.denistechs.carrentalgui3.domain.CarRental;
import com.denistechs.carrentalgui3.domain.Entity;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import com.denistechs.carrentalgui3.service.ExceptionCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Car.class, CarRental.class})
public class MemoryRepository<ID,T_ELEM extends Entity> implements repository<ID,T_ELEM>, Serializable {
    @JsonProperty
    protected Map<ID, T_ELEM> repo;
    public MemoryRepository()
    {
        this.repo = new HashMap<ID, T_ELEM>();
    }

    public MemoryRepository(HashMap<ID,T_ELEM> map)
    {
        this.repo = map;
    }

    @Override
    public void add(ID id, T_ELEM e) throws ExceptionCode {
        if(repo.containsKey(id)) throw new ExceptionCode(2);
        this.repo.put(id,e);
    }

    @Override
    public void remove(ID id, T_ELEM e) throws ExceptionCode {
        if(!this.repo.containsKey(id)) throw new ExceptionCode(3);
        this.repo.remove(id);
    }

    @Override
    public void modify(ID id, T_ELEM o, T_ELEM n) throws ExceptionCode {
        if(!this.repo.containsKey(id)) throw new ExceptionCode(3);
        this.repo.replace(id,o,n);
    }

    @Override
    public T_ELEM get(ID id) throws ExceptionCode {
        if(!this.repo.containsKey(id)) throw new ExceptionCode(4);
        return this.repo.get(id);
    }

    @Override
    public Iterable<T_ELEM> getValues() {
        return this.repo.values();
    }

    public HashMap<ID, T_ELEM> getMap() {
        return (HashMap) this.repo;
    }

    public Integer getGreatestID() {
        Integer m_ID = null;
        for(T_ELEM t: this.repo.values()){
            if(m_ID == null){
                m_ID = (Integer)t.getID();
            } else if (m_ID < (Integer)t.getID()) {
                m_ID = (Integer) t.getID();
            }
        }
        return m_ID;
    }
}
