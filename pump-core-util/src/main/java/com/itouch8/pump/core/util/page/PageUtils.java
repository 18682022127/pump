package com.itouch8.pump.core.util.page;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.config.BaseConfig;
import com.itouch8.pump.core.util.page.IPage;
import com.itouch8.pump.core.util.page.impl.BasePage;


public class PageUtils {

    
    private final static String PAGE_ATTR_NAME = PageUtils.class.getName() + ".PAGE_ATTR_NAME";

    
    public static IPage createPage() {
        return createPage(IPage.DEFAULT_PAGE_KEY);
    }

    
    public static IPage createPage(String pageKey) {
        IPage page = getPage(pageKey);
        if (null == page) {
            synchronized (PAGE_ATTR_NAME) {
                page = getPage(pageKey);
                if (null == page) {
                    page = BaseConfig.getPageFactory().createPage(pageKey);
                    CoreUtils.putThreadCache(PAGE_ATTR_NAME + "." + pageKey, page);
                }
            }
        }
        return page;
    }

    
    public static IPage getPage() {
        return getPage(IPage.DEFAULT_PAGE_KEY);
    }

    
    public static IPage getPage(String pageKey) {
        return CoreUtils.getThreadCache(PAGE_ATTR_NAME + "." + pageKey, IPage.class);
    }

    
    public static void setPageSize(int size) {
        setPageSize(IPage.DEFAULT_PAGE_KEY, size);
    }

    
    public static void setPageSize(String pageKey, int size) {
        IPage page = createPage(pageKey);
        if (page instanceof BasePage) {
            ((BasePage) page).setPageSize(size);
        }
    }
}
