package com.simonsw.common.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.simonsw.common.util.DateUtils;

/**
 * @author Simon Lv
 * @since 2012-8-9
 */
@SuppressWarnings("serial")
public class DateFormat extends TagSupport {
	
	private String value;
	private String pattern = DateUtils.PATTERN;

	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			if (StringUtils.isNotBlank(value)) {
				out.print(DateUtils.toString(value, pattern));
				return EVAL_PAGE;
			}
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
