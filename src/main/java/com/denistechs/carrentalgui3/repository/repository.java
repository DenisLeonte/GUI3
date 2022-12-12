package com.denistechs.carrentalgui3.repository;

import com.denistechs.carrentalgui3.service.ExceptionCode;

public interface repository<ID, T_ELEM> {
    public void add(ID id, T_ELEM e) throws ExceptionCode;
    public void remove(ID id, T_ELEM e) throws ExceptionCode;
    public void modify(ID id,T_ELEM o, T_ELEM n) throws ExceptionCode;
    public T_ELEM get(ID id) throws ExceptionCode;
    public Iterable<T_ELEM> getValues();
}
