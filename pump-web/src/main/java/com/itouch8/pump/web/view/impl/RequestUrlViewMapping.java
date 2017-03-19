package com.itouch8.pump.web.view.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.AntPathMatcher;

import com.itouch8.pump.core.service.request.IRequestInfo;
import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.web.view.IViewMapping;


public class RequestUrlViewMapping implements IViewMapping {

    private static final AntPathMatcher antMatcher = new AntPathMatcher();

    /**
     * url及其视图的映射
     */
    private Map<String, String> urlMapping;

    /**
     * 获取默认视图名称
     */
    @Override
    public String getViewname(IRequestInfo requestInfo) {
        Map<String, String> mapping = getUrlMapping();
        if (null != mapping) {
            String view = parseViewname(mapping, requestInfo.getRequestUrl());
            if (!CoreUtils.isBlank(view)) {
                return view;
            }
        }
        return null;
    }

    /**
     * 不覆盖用户返回视图
     */
    @Override
    public boolean overrideUserView() {
        return false;
    }

    private String parseViewname(Map<String, String> mapping, String url) {
        List<String> matchingPatterns = new ArrayList<String>();
        for (String pattern : mapping.keySet()) {
            if (antMatcher.match(pattern, url)) {
                matchingPatterns.add(pattern);
            }
        }
        if (!matchingPatterns.isEmpty()) {
            Comparator<String> patternComparator = antMatcher.getPatternComparator(url);
            Collections.sort(matchingPatterns, patternComparator);
            for (String pattern : matchingPatterns) {
                String view = mapping.get(pattern);
                // 对路径变量做处理
                Map<String, String> vs = antMatcher.extractUriTemplateVariables(pattern, url);
                if (null != vs && !vs.isEmpty()) {
                    for (String vn : vs.keySet()) {
                        String v = vs.get(vn);
                        view = view.replaceAll("\\Q{" + vn + "}\\E", v);
                        pattern = pattern.replaceAll("\\Q{" + vn + "}\\E", v);
                    }
                }

                if (pattern.startsWith("**")) {
                    // 对 **/path 的格式做特殊处理，目前只支持这种简单格式
                    int vi = view.indexOf("**");
                    if (-1 != vi) {
                        String vp = view.substring(0, vi);
                        String s = antMatcher.extractPathWithinPattern(pattern, url);
                        int si = s.indexOf(pattern.substring(2));
                        view = s.substring(0, si) + view.substring(vi + 2);
                        if (vp.endsWith("/") && view.startsWith("/")) {
                            view = vp + view.substring(1);
                        } else if (!vp.endsWith("/") && !view.startsWith("/") && !"".endsWith(vp)) {
                            view = vp + "/" + view;
                        }
                        if (view.startsWith("/")) {
                            view = view.substring(1);
                        }
                    }
                } else if (pattern.endsWith("**")) {
                    // 对 path/** 的格式做特殊处理，目前只支持这种简单格式
                    int vi = view.indexOf("**");
                    if (-1 != vi) {
                        String s = antMatcher.extractPathWithinPattern(pattern, url);
                        if (!CoreUtils.isBlank(s)) {
                            view = view.replaceAll("\\Q**\\E", s);
                        }
                        if (view.startsWith("/")) {
                            view = view.substring(1);
                        }
                    }
                }
                return view;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        RequestUrlViewMapping uvm = new RequestUrlViewMapping();
        Map<String, String> mapping = new HashMap<String, String>();
        mapping.put("demo/**", "demo/add/**/aa");
        mapping.put("**/{a}/search{suffix}", "**/aaa/{a}/list{suffix}");
        mapping.put("**/update", "success");

        String view = null;
        view = uvm.parseViewname(mapping, "demo/demo1");
        System.out.println(view);
        view = uvm.parseViewname(mapping, "demo/demo1/listView");
        System.out.println(view);

        view = uvm.parseViewname(mapping, "system/bbbb/aaa/menu/search.do");
        System.out.println(view);
        view = uvm.parseViewname(mapping, "system/role/search.do");
        System.out.println(view);
        view = uvm.parseViewname(mapping, "system/menu/searchRln.do");
        System.out.println(view);
        view = uvm.parseViewname(mapping, "system/menu/update");
        System.out.println(view);
        view = uvm.parseViewname(mapping, "system/menu/list");
        System.out.println(view);
    }

    public Map<String, String> getUrlMapping() {
        return urlMapping;
    }

    public void setUrlMapping(Map<String, String> urlMapping) {
        this.urlMapping = urlMapping;
    }
}
