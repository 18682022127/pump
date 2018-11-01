package com.itouch8.pump.core.util.page;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 分页创建工厂<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public interface IPageFactory {

    
    /**
     * 创建默认分页
     * 
     * @return
     */
    public IPage createPage();

    
    /**
     * 从属性创建
     * 
     * @param pageKey
     * @return
     */
    public IPage createPage(String pageKey);
}
