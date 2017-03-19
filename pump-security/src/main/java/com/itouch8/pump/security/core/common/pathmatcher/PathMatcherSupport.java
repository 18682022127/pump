package com.itouch8.pump.security.core.common.pathmatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


public class PathMatcherSupport {

    private PathMatcher pathMatcher = new AntPathMatcher();

    
    private List<String> urlPatterns;

    
    private boolean matcherWhenUrlPatternsIsEmpty;

    
    protected String getMatcherPattern(String url) {
        List<String> urlPatterns = getUrlPatterns();
        if (null != urlPatterns && !urlPatterns.isEmpty()) {
            List<String> matchingPatterns = new ArrayList<String>();
            for (String pattern : urlPatterns) {
                if (pathMatcher.match(pattern, url)) {
                    matchingPatterns.add(pattern);
                }
            }
            if (!matchingPatterns.isEmpty()) {
                Comparator<String> patternComparator = pathMatcher.getPatternComparator(url);
                Collections.sort(matchingPatterns, patternComparator);
                String pattern = matchingPatterns.get(0);
                return pattern;
            }
        }
        return null;
    }

    
    protected boolean isMatcher(String url) {
        List<String> urlPatterns = getUrlPatterns();
        if (null != urlPatterns && !urlPatterns.isEmpty()) {
            for (String pattern : urlPatterns) {
                if (pathMatcher.match(pattern, url)) {
                    return true;
                }
            }
            return false;
        } else {
            return this.isMatcherWhenUrlPatternsIsEmpty();
        }
    }

    
    protected Map<String, String> extractUriTemplateVariables(String pattern, String url) {
        return pathMatcher.extractUriTemplateVariables(pattern, url);
    }

    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<String> urlPatterns) {
        if (null != urlPatterns && !urlPatterns.isEmpty()) {
            for (String urlPattern : urlPatterns) {
                setUrlPattern(urlPattern);
            }
        }
    }

    public void setUrlPattern(String urlPattern) {
        if (null != urlPattern) {
            if (null == this.urlPatterns) {
                this.urlPatterns = new ArrayList<String>();
            }
            this.urlPatterns.addAll(Arrays.asList(urlPattern.split("\\s*,\\s*")));
        }
    }

    public boolean isMatcherWhenUrlPatternsIsEmpty() {
        return matcherWhenUrlPatternsIsEmpty;
    }

    public void setMatcherWhenUrlPatternsIsEmpty(boolean matcherWhenUrlPatternsIsEmpty) {
        this.matcherWhenUrlPatternsIsEmpty = matcherWhenUrlPatternsIsEmpty;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }
}
