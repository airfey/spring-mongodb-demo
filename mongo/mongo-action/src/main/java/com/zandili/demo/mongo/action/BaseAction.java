package com.zandili.demo.mongo.action;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * 
 * @author airfey
 * 
 */
public class BaseAction extends ActionSupport implements Preparable {

	private static final long serialVersionUID = -4429486312479870462L;
	private static final String CHARSET = "utf-8";

	protected String title;

	@Override
	public void prepare() throws Exception {
		this.title = "mongodb-demo";

	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	protected String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	protected HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	protected void write(String val) {
		this.writes(val);
	}

	private void writes(String context) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding(CHARSET);
		Writer writer = null;
		try {
			writer = response.getWriter();
			assert writer != null;
			writer.write(context);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
