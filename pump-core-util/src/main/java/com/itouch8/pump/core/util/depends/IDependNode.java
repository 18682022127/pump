package com.itouch8.pump.core.util.depends;

import java.util.List;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 依赖节点<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public interface IDependNode {

    public String getId();

    
    public List<String> getDepends();
}
