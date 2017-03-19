package com.itouch8.pump.util.param.tree;

import com.itouch8.pump.util.param.IParam;
import com.itouch8.pump.util.tree.ITree;


public interface ITreeParam extends ITree<ITreeParamNode>, IParam {

    
    public void build();
}
