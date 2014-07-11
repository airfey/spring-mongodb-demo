package com.zandili.demo.mongo.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.zandili.demo.mongo.common.page.PageWithData;
import com.zandili.demo.mongo.core.domain.User;
import com.zandili.demo.mongo.core.service.UserService;

@Controller
@Scope("prototype")
public class IndexAction extends BaseAction {

	private static final long serialVersionUID = -6940072469596850791L;
	@Resource
	private UserService userService;

	public String execute() throws Exception {
		if (null == page || "".equals(page)) {
			this.page = 1;
		}
		pageWithData = userService.queryAdvertByPage(null, page, 10);
		if (this.page > pageWithData.getTotalPage()) {
			this.page = pageWithData.getTotalPage();
		}
		users = pageWithData.getData();
		// 分页导航
		pageString = pageWithData
				.pageNavigation(pageWithData.getTotalResults(), 10, this.page,
						"index.do?page=");
		return SUCCESS;
	}

	private List<User> users;
	/**
	 * 分页导航
	 */
	private String pageString;
	// 接收传过来的nowPage
	private Integer page;
	private PageWithData<User> pageWithData;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getPageString() {
		return pageString;
	}

	public void setPageString(String pageString) {
		this.pageString = pageString;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public PageWithData<User> getPageWithData() {
		return pageWithData;
	}

	public void setPageWithData(PageWithData<User> pageWithData) {
		this.pageWithData = pageWithData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
