package com.itouch8.pump.web.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.itouch8.pump.web.WebPumpConfig;
import com.itouch8.pump.web.path.IPathResolver;


public class PathTag extends TagSupport {

    
    private static final long serialVersionUID = 8150237386260219530L;

    private String url; // 支持{root}、{locale}、{theme}、{debug}、{min}五个变量

    public int doStartTag() throws JspException {
        JspWriter out = null;
        try {
            out = pageContext.getOut();
            IPathResolver pathResolver = WebPumpConfig.getPathResolver();
            if (null != pathResolver && null != url) {
                HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
                out.print(pathResolver.resolver(request, url));
            } else {
                out.print(url);
            }
        } catch (Exception e) {
            throw new JspException(e);
        }
        return super.doStartTag();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
