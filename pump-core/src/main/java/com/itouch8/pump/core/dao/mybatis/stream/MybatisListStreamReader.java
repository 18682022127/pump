package com.itouch8.pump.core.dao.mybatis.stream;

import java.util.List;

import com.itouch8.pump.core.dao.IDaoTemplate;
import com.itouch8.pump.core.dao.stream.impl.AbstractListStreamReader;
import com.itouch8.pump.core.util.page.IPage;


public class MybatisListStreamReader<T> extends AbstractListStreamReader<T> {

    
    private final IDaoTemplate daoTemplate;
    
    private final String statement;

    
    private final Object parameter;

    
    public MybatisListStreamReader(IDaoTemplate daoTemplate, String statement, Object parameter) {
        super();
        this.daoTemplate = daoTemplate;
        this.statement = statement;
        this.parameter = parameter;
    }

    
    public MybatisListStreamReader(IDaoTemplate daoTemplate, String statement, Object parameter, int fetchSize) {
        super(fetchSize);
        this.daoTemplate = daoTemplate;
        this.statement = statement;
        this.parameter = parameter;
    }

    
    @Override
    protected List<T> doRead(IPage page) {
        return daoTemplate.selectList(statement, parameter, page);
    }
}
