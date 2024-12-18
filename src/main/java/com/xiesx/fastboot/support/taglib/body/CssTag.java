package com.xiesx.fastboot.support.taglib.body;

import com.xiesx.fastboot.support.taglib.TagUtils;
import com.xiesx.fastboot.support.taglib.ui.BaseUITag;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.BodyTagSupport;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CssTag extends BodyTagSupport {

    private static final long serialVersionUID = -1975747968925711584L;

    private BaseUITag __ui;

    private String href;

    private String rel;

    private String media;

    private String type;

    @Override
    public int doStartTag() throws JspException {
        __ui = (BaseUITag) this.getParent();
        if (ObjectUtil.isNull(__ui)) {
            throw new JspException("Parent UITag or LayoutTag not found.");
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
        StringBuilder _metaTmpl = new StringBuilder("<link");
        boolean _isEmpty = true;
        if (StrUtil.isNotBlank(this.getHref())) {
            _metaTmpl.append(" href=\"").append(this.getHref()).append("\"");
            _isEmpty = false;
        }
        if (!_isEmpty) {
            if (StrUtil.isEmpty(this.getRel())) {
                this.setRel("stylesheet");
            }
            _metaTmpl.append(" rel=\"").append(this.getRel()).append("\"");
            //
            if (StrUtil.isNotBlank(this.getType())) {
                _metaTmpl.append(" type=\"").append(this.getType()).append("\"");
            }
            //
            if (StrUtil.isNotBlank(this.getMedia())) {
                _metaTmpl.append(" media=\"").append(this.getMedia()).append("\"");
            }
            _metaTmpl.append(">\n");
            __ui.writerToCssPart(_metaTmpl.toString());
        }
        //
        this.__ui = null;
        this.href = null;
        this.rel = null;
        this.media = null;
        this.type = null;
        return super.doEndTag();
    }
}
