package com.itouch8.pump.util.param;

import java.util.List;
import java.util.Map;

import com.itouch8.pump.util.param.enums.IEnumParam;
import com.itouch8.pump.util.param.enums.IEnumParamItem;
import com.itouch8.pump.util.param.single.ISingleParam;
import com.itouch8.pump.util.param.tree.ITreeParam;

public interface IParamService {

    public String getParameter(String name);

    public <E> E getParameter(String name, Class<E> cls);

    public <E> E getParameter(String name, E defaultValue, Class<E> cls);

    public void refreshParameters();

    public ISingleParam getSingleParameter(String name);

    public Map<String, ISingleParam> getSingleParameters(List<String> names);

    public void clearSingleParameters();

    public IEnumParam getEnumParameter(String name);

    public Map<String, IEnumParam> getEnumParameters(List<String> names);

    public List<IEnumParamItem> getEnumParameterItems(String name);

    public void removeEnumParameter(List<String> names);

    public void clearEnumParameters();

    public ITreeParam getTreeParameter(String name);

    public Map<String, ITreeParam> getTreeParameters(List<String> names);

    public void removeTreeParameter(List<String> names);

    public void clearTreeParameters();

    public List<IParam> getParameters(List<String> names);

    public List<Map<String, String>> sGetTableParameters(String sourceId, String code, String text, String where, String order);
}
