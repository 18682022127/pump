package com.itouch8.pump.core.dao.mybatis.page;

import org.apache.ibatis.session.RowBounds;

import com.itouch8.pump.core.dao.jndi.IJndi;
import com.itouch8.pump.core.util.page.IPage;


public class PageAdapter extends RowBounds {

    private IPage page;

    private IJndi jndi;

    public PageAdapter(IPage page) {
        super();
        this.page = page;
    }

    public PageAdapter(IPage page, IJndi jndi) {
        super();
        this.page = page;
        this.jndi = jndi;
    }

    public IPage getPage() {
        return page;
    }

    public void setPage(IPage page) {
        this.page = page;
    }

    public IJndi getJndi() {
        return jndi;
    }

    public void setJndi(IJndi jndi) {
        this.jndi = jndi;
    }
}
