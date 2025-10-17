package com.xiesx.fastboot.support.taglib.ui;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.xiesx.fastboot.support.taglib.TagUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

public class UITag extends BaseUITag {

    private static final long serialVersionUID = 584494119933433838L;

    private boolean __isCurrentUI;

    /**
     * @return 返回当前线程中的UITag对象
     */
    public UITag currentUI() {
        return (UITag) this.pageContext.getAttribute(UITag.class.getName());
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            if (ObjectUtil.isNull(currentUI())) {
                __isCurrentUI = true;
                this.pageContext.setAttribute(UITag.class.getName(), this);
            }
        } catch (Exception e) {
            throw new JspException(TagUtils.unwrapThrow(e));
        }
        return super.doStartTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        try {
            if (this.bodyContent != null) {
                this.bodyContent.clearBody();
            }
        } catch (Exception e) {
            throw new JspException(TagUtils.unwrapThrow(e));
        }
        return super.doAfterBody();
    }

    @Override
    public int doEndTag() throws JspException {
        if (__isCurrentUI) {
            try {
                /* UI模板文件内容 */
                String __tmplContent = null;
                if (StrUtil.isNotBlank(this.getSrc())) {
                    __tmplContent =
                            TagUtils.includeJSP(
                                    (HttpServletRequest) this.pageContext.getRequest(),
                                    (HttpServletResponse) this.pageContext.getResponse(),
                                    this.buildSrcUrl(),
                                    this.getCharsetEncoding());
                    __tmplContent =
                            this.mergeContent(StrUtil.blankToDefault(__tmplContent, "@{body}"));
                    this.pageContext
                            .getOut()
                            .write(
                                    !isCleanup()
                                            ? __tmplContent
                                            : TagUtils.replaceRegClear(__tmplContent));
                }
            } catch (Exception e) {
                throw new JspException(TagUtils.unwrapThrow(e));
            }
        }
        this.__isCurrentUI = false;
        return super.doEndTag();
    }
}
