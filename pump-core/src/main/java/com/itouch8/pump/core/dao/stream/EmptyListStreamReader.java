package com.itouch8.pump.core.dao.stream;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class EmptyListStreamReader<T> implements IListStreamReader<T> {

    @Override
    public void reset() {}

    @Override
    public List<T> read() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public List<T> readAll() {
        return null;
    }
}
