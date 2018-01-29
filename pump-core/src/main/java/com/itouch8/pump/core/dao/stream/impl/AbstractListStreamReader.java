package com.itouch8.pump.core.dao.stream.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.itouch8.pump.core.dao.exception.DaoExceptionCodes;
import com.itouch8.pump.core.dao.stream.IListStreamReader;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.core.util.page.IPage;
import com.itouch8.pump.core.util.page.impl.BasePage;


public abstract class AbstractListStreamReader<T> implements IListStreamReader<T> {

    
    private static final int defaultFetchSize = 1000;

    
    private static final int maxFetchSize = 10000;

    
    private final int fetchSize;

    
    private final IPage page;

    
    private final ListStreamIterator iterator = new ListStreamIterator();

    
    private final AtomicBoolean finish = new AtomicBoolean(false);

    
    public AbstractListStreamReader() {
        this(defaultFetchSize);
    }

    
    public AbstractListStreamReader(int fetchSize) {
        if (fetchSize <= 0) {
            fetchSize = defaultFetchSize;
        } else if (fetchSize > maxFetchSize) {
            Throw.throwRuntimeException(DaoExceptionCodes.YT020012, fetchSize, "(0, " + maxFetchSize + "]");
        }
        this.fetchSize = fetchSize;
        BasePage page = new BasePage();
        page.setPageSize(fetchSize);
        this.page = page;
    }

    
    @Override
    public List<T> read() {
        if (!finish.get()) {
            synchronized (finish) {
                if (!finish.get()) {
                    List<T> rs = doRead(page);// 查询当前页数据
                    if (null != rs) {
                        iterator.inner = rs.iterator();
                    }
                    if (page.hasNextPage()) {// 有下一页，游标指向下一页
                        page.setPageProperty(page.getTotalRecords(), page.getCurrentPage() + 1, fetchSize);
                    } else {// 没有下一页，完成
                        finish.set(true);
                    }
                    return rs;
                }
            }
        }
        return null;
    }

    
    abstract protected List<T> doRead(IPage page);

    
    @Override
    public synchronized void reset() {
        this.finish.set(false);
        this.page.setPageProperty(this.page.getTotalRecords(), 1, fetchSize);
        this.iterator.inner = null;
    }

    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

    @Override
    public int size() {
        return page == null ? -1 : (int) page.getTotalRecords();
    }

    @Override
    public List<T> readAll() {
        List<T> list = new ArrayList<T>();
        for (Iterator<T> iterator = iterator(); iterator.hasNext();) {
            list.add(iterator.next());
        }
        return list;
    }

    private class ListStreamIterator implements Iterator<T> {

        private Iterator<T> inner;

        @Override
        public boolean hasNext() {
            if (null == inner || !inner.hasNext()) {
                read();
            }
            return null != inner && inner.hasNext();
        }

        @Override
        public T next() {
            return null == inner ? null : inner.next();
        }

        @Override
        public void remove() {
            if (null != inner) {
                inner.remove();
            }
        }
    }
}
