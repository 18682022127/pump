package com.itouch8.pump.core.util.page;

import java.io.Serializable;


/**
 * Copy Right Information :  <br>
 * Project :  分页接口<br>
 * Description : <br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
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
