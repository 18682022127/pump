package com.itouch8.pump.web.page;

import javax.servlet.http.HttpServletRequest;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.page.IPage;
import com.itouch8.pump.core.util.page.IPageFactory;
import com.itouch8.pump.core.util.page.impl.BasePage;
import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.servlet.ServletHelp;


public class WebPageFactory implements IPageFactory {

    
    @Override
    public IPage createPage() {
        return createPage(IPage.DEFAULT_PAGE_KEY);
    }

    protected BasePage createBasePage(HttpServletRequest request, String pageKey) {
        return new BasePage();
    }

    @Override
    public IPage createPage(String pageKey) {
        HttpServletRequest request = ServletHelp.getRequest();
        BasePage page = createBasePage(request, pageKey);
        if (null == page) {
            page = new BasePage();
        }
        // 获取当前页数
        try {
            page.setCurrentPage(getIntParamValue(request, WebPumpConfig.getCurrentPageParamName()));
        } catch (Exception e) {
            page.setCurrentPage(1);
        }

        // 获取分页大小
        int pageSize = WebPumpConfig.getDefaultPageSize();
        try {
            String pageSizeParamName = WebPumpConfig.getPageSizeParamName();
            if (!CoreUtils.isBlank(pageSizeParamName)) {
                pageSize = getIntParamValue(request, pageSizeParamName);
            }
        } catch (Exception e) {
        }
        page.setPageSize(pageSize);

        // 获取总记录数
        try {
            page.setPageProperty(getIntParamValue(request, WebPumpConfig.getTotalRecordsParamName()));
        } catch (Exception e) {
            page.setNeedCalTotal(true);
        }
        return page;
    }

    
    private int getIntParamValue(HttpServletRequest request, String paramName) {
        return Integer.parseInt(request.getParameter(paramName));
    }
}
