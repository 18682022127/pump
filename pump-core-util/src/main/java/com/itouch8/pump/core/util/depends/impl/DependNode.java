package com.itouch8.pump.core.util.depends.impl;

import java.util.ArrayList;
import java.util.List;

import com.itouch8.pump.core.util.depends.IDependNode;


/**
 * Copy Right Information :  <br>
 * Project :  <br>
 * Description : 依赖节点<br>
 * Author : Huangzhong<br>
 * Version : 1.0.0 <br>
 * Since : 1.0.0 <br>
 * Date : 2018-10-30<br>
 */
public class DependNode implements IDependNode {

    private String id;

    private List<String> depends;

    
    public DependNode(String id) {
        this.id = id;
    }
    
    public DependNode(String id, List<?> depends) {
        this.id = id;
        if (null != depends && depends.size() > 0) {
            List<String> dps = new ArrayList<String>();
            for (Object dp : depends) {
                if (null != dp) {
                    String depend = (dp instanceof IDependNode) ? (((IDependNode) dp).getId()) : dp.toString();
                    if (!dps.contains(depend)) {
                        dps.add(depend);
                    }
                }
            }
            this.depends = dps;
        }
    }

    
    public DependNode(String id, Object... depends) {
        this.id = id;
        if (null != depends && depends.length > 0) {
            List<String> dps = new ArrayList<String>();
            for (Object dp : depends) {
                if (null != dp) {
                    String depend = (dp instanceof IDependNode) ? (((IDependNode) dp).getId()) : dp.toString();
                    if (!dps.contains(depend)) {
                        dps.add(depend);
                    }
                }
            }
            this.depends = dps;
        }
    }

    
    public String getId() {
        return id;
    }

    
    public List<String> getDepends() {
        return depends;
    }
}
