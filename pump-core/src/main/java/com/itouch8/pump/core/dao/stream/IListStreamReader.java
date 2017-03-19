package com.itouch8.pump.core.dao.stream;

import java.util.List;


public interface IListStreamReader<T> extends Iterable<T> {

    
    public List<T> read();

    
    public void reset();

    
    public int size();

    
    public List<T> readAll();
}
