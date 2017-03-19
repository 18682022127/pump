package com.itouch8.pump.util.param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itouch8.pump.util.param.IParam;
import com.itouch8.pump.util.param.IParamService;
import com.itouch8.pump.util.param.IParamServiceApi;
import com.itouch8.pump.util.param.enums.IEnumParam;
import com.itouch8.pump.util.param.enums.IEnumParamItem;
import com.itouch8.pump.util.param.single.ISingleParam;
import com.itouch8.pump.util.param.single.ISingleParamService;
import com.itouch8.pump.util.param.tree.ITreeParam;


public abstract class AbstractParamService implements IParamService {

    private ISingleParamService service;

    private IParamServiceApi<IEnumParam> enumService;

    private IParamServiceApi<ITreeParam> treeService;

    public void setService(ISingleParamService service) {
        this.service = service;
    }

    public void setEnumService(IParamServiceApi<IEnumParam> enumService) {
        this.enumService = enumService;
    }

    public void setTreeService(IParamServiceApi<ITreeParam> treeService) {
        this.treeService = treeService;
    }

    @Override
    public String getParameter(String name) {
        return null == service ? null : service.get(name);
    }

    @Override
    public <E> E getParameter(String name, Class<E> cls) {
        return null == service ? null : service.get(name, cls);
    }

    @Override
    public <E> E getParameter(String name, E defaultValue, Class<E> cls) {
        return null == service ? null : service.get(name, defaultValue, cls);
    }

    @Override
    public void refreshParameters() {
        if (null != service) {
            service.refresh();
        }
    }

    @Override
    public void clearSingleParameters() {
        if (null != service) {
            service.clear();
        }
    }

    @Override
    public ISingleParam getSingleParameter(String name) {
        return null == service ? null : service.getParam(name);
    }

    @Override
    public Map<String, ISingleParam> getSingleParameters(List<String> names) {
        return null == service ? null : service.getParams(names);
    }

    @Override
    public IEnumParam getEnumParameter(String name) {
        return null == enumService ? null : enumService.getParam(name);
    }

    @Override
    public Map<String, IEnumParam> getEnumParameters(List<String> names) {
        return null == enumService ? null : enumService.getParams(names);
    }

    @Override
    public List<IEnumParamItem> getEnumParameterItems(String name) {
        IEnumParam param = getEnumParameter(name);
        return null == param ? null : param.getItems();
    }

    @Override
    public void removeEnumParameter(List<String> names) {
        if (null != enumService) {
            enumService.removeParams(names);
        }
    }

    @Override
    public void clearEnumParameters() {
        if (null != enumService) {
            enumService.clear();
        }
    }

    @Override
    public ITreeParam getTreeParameter(String name) {
        return null == treeService ? null : treeService.getParam(name);
    }

    @Override
    public Map<String, ITreeParam> getTreeParameters(List<String> names) {
        return null == treeService ? null : treeService.getParams(names);
    }

    @Override
    public void removeTreeParameter(List<String> names) {
        if (null != treeService) {
            treeService.removeParams(names);
        }
    }

    @Override
    public void clearTreeParameters() {
        if (null != treeService) {
            treeService.clear();
        }
    }

    @Override
    public List<IParam> getParameters(List<String> names) {
        if (null != names && !names.isEmpty()) {
            List<String> singles = new ArrayList<String>();
            List<String> enums = new ArrayList<String>();
            List<String> trees = new ArrayList<String>();

            Map<String, IParam> params = new HashMap<String, IParam>();
            for (String name : names) {
                if (enumService.isLoaded(name)) {
                    params.put(name, enumService.getParam(name));
                } else if (treeService.isLoaded(name)) {
                    params.put(name, treeService.getParam(name));
                } else {
                    enums.add(name);
                }
            }
            if (!enums.isEmpty()) {
                Map<String, IEnumParam> ps = enumService.getParams(enums);
                if (null != ps && !ps.isEmpty()) {
                    for (String name : enums) {
                        if (ps.containsKey(name)) {
                            params.put(name, ps.get(name));
                        } else {
                            trees.add(name);
                        }
                    }
                } else {
                    trees.addAll(enums);
                }
            }
            if (!trees.isEmpty()) {
                Map<String, ITreeParam> ps = treeService.getParams(trees);
                if (null != ps && !ps.isEmpty()) {
                    for (String name : trees) {
                        if (ps.containsKey(name)) {
                            params.put(name, ps.get(name));
                        } else if (service.isLoaded(name)) {
                            params.put(name, service.getParam(name));
                        } else {
                            singles.add(name);
                        }
                    }
                } else {
                    singles.addAll(trees);
                }
            }

            if (!singles.isEmpty()) {
                Map<String, ISingleParam> ps = service.getParams(singles);
                if (null != ps && !ps.isEmpty()) {
                    for (String name : singles) {
                        if (ps.containsKey(name)) {
                            params.put(name, ps.get(name));
                        }
                    }
                }
            }
            return new ArrayList<IParam>(params.values());
        }
        return null;
    }
}
