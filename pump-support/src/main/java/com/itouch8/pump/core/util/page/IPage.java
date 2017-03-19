package com.itouch8.pump.core.util.page;

import java.io.Serializable;


public interface IPage extends Serializable {

    
    public String DEFAULT_PAGE_KEY = "DEFAULT_PAGE_KEY";

    
    public long getTotalRecords();

    
    public boolean isNeedCalTotal();

    
    public void setPageProperty(long totalRecords);

    
    public void setPageProperty(long totalRecords, long currentPage, int pageSize);

    
    public long getTotalPages();

    
    public int getPageSize();

    
    public long getCurrentPage();

    
    public long getStart();

    
    public long getEnd();

    
    public boolean hasPrevPage();

    
    public boolean hasNextPage();

    
    public boolean isFirstPage();

    
    public boolean isLastPage();
}
