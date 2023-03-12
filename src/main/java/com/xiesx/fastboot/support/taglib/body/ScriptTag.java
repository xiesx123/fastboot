package com.xiesx.fastboot.support.taglib.body;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.xiesx.fastboot.support.taglib.TagUtils;
import com.xiesx.fastboot.support.taglib.ui.BaseUITag;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ScriptTag extends BodyTagSupport {

    private static final long serialVersionUID = -2774067114541501535L;

    private BaseUITag __ui;

    private String src;

    private String value;

    private String type;

    private String wrapper;

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
                String _propValue = this.bodyContent.getString();
                if (StrUtil.isNotBlank(_propValue)) {
                    this.setValue(_propValue);
                }
                this.bodyContent.clearBody();
            }
        } catch (Exception e) {
            throw new JspException(TagUtils.unwrapThrow(e));
        }
        return super.doAfterBody();
    }

    @Override
    public int doEndTag() throws JspException {
        StringBuilder _scriptTmpl = new StringBuilder("<script");
        if (StrUtil.isNotBlank(this.getId())) {
            _scriptTmpl.append(" id=\"").append(this.getId()).append("\"");
        }
        boolean _isEmpty = true;
        if (StrUtil.isNotBlank(this.getSrc())) {
            _scriptTmpl.append(" src=\"").append(this.getSrc()).append("\"");
            _isEmpty = false;
        }
        _scriptTmpl.append(" type=\"").append(StrUtil.blankToDefault(this.getType(), "text/javascript")).append("\">");
        if (_isEmpty && StrUtil.isNotBlank(this.getValue())) {
            String _wrapper = StrUtil.blankToDefault(this.getWrapper(), "script");
            String _content = StrUtil.strip(this.getValue(), "<" + _wrapper + ">", "</" + _wrapper + ">");
            if (StrUtil.isNotBlank(_content)) {
                this.setValue(_content);
            }
            _scriptTmpl.append(this.getValue()).append("\n");
            _isEmpty = false;
        }
        _scriptTmpl.append("</script>\n");
        if (!_isEmpty) {
            __ui.writerToScriptPart(_scriptTmpl.toString());
        }
        //
        this.__ui = null;
        this.src = null;
        this.value = null;
        this.id = null;
        this.type = null;
        this.wrapper = null;
        return super.doEndTag();
    }
}
