package com.itouch8.pump.util.toolimpl;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;

import com.itouch8.pump.core.util.depends.IDependNode;
import com.itouch8.pump.core.util.depends.impl.DependGraph;


public abstract class CollectionUtilsImpl {

    private static final CollectionUtilsImpl instance = new CollectionUtilsImpl() {};

    private CollectionUtilsImpl() {}

    
    public static CollectionUtilsImpl getInstance() {
        return instance;
    }

    
    public <E extends IDependNode> List<E> sortDependNodes(List<E> nodeList) {
        return new DependGraph<E>(nodeList).sort();
    }

    
    public <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
        Class<?> compType = Object.class;
        if (array != null) {
            compType = array.getClass().getComponentType();
        } else if (obj != null) {
            compType = obj.getClass();
        }
        int newArrLength = (array != null ? array.length + 1 : 1);
        @SuppressWarnings("unchecked")
        A[] newArr = (A[]) Array.newInstance(compType, newArrLength);
        if (array != null) {
            System.arraycopy(array, 0, newArr, 0, array.length);
        }
        newArr[newArr.length - 1] = obj;
        return newArr;
    }

    
    public <T> void copy(List<? super T> dest, List<? extends T> src) {
        Collections.copy(dest, src);
    }
}
