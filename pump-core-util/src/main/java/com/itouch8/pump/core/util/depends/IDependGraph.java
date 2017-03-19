package com.itouch8.pump.core.util.depends;

import java.util.List;


public interface IDependGraph<E extends IDependNode> {

    
    public List<? extends E> sort();
}
