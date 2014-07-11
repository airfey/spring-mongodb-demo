package com.zandili.demo.mongo.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class NotFoundAction extends BaseAction {
 
	private static final long serialVersionUID = 1690442226178955767L;

	public String execute() throws Exception {
		return "success";
	}
}
