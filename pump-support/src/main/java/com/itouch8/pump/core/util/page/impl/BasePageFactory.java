package com.itouch8.pump.core.util.page.impl;

import com.itouch8.pump.core.util.page.IPage;
import com.itouch8.pump.core.util.page.IPageFactory;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 默认请求工厂<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public class BasePageFactory implements IPageFactory {

    
    public IPage createPage() {
        return createPage(IPage.DEFAULT_PAGE_KEY);
    }

    
    public IPage createPage(String pageKey) {
        BasePage page = this.createBasePage(pageKey);
        this.setPageProperties(page, pageKey);
        return page;
    }

    
    protected BasePage createBasePage(String pageKey) {
        return new BasePage();
    }

    
    protected void setPageProperties(BasePage page, String pageKey) {

    }

}
