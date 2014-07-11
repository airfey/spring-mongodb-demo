package com.zandili.demo.mongo.common.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 * 
 * @ClassName: PageWithData
 * @author: airfey 2013-11-8 下午12:19:54
 * @param <E>
 * @version V1.0
 * 
 */
public class PageWithData<E extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1153863153214796311L;
	/**
	 * 当前页数据list
	 */
	private List<E> data;
	/**
	 * 当前页码
	 */
	private int currentPage;
	/**
	 * 首条记录
	 */
	private int firstResult;
	/**
	 * 总记录条数
	 */
	private int totalResults;
	/**
	 * 每页条数
	 */
	private int onePageSize;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 下一页页码
	 */
	private int nextPage;
	/**
	 * 上一页页码
	 */
	private int previousPage;

	public PageWithData(int currentPage, int onePageSize) {
		if (currentPage > 1)
			this.currentPage = currentPage;
		else
			this.currentPage = 1;
		this.onePageSize = onePageSize;
		this.firstResult = (this.currentPage - 1) * this.onePageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		if (currentPage <= 0)
			this.currentPage = 1;
		if (currentPage > this.totalPage)
			this.currentPage = totalPage;
		this.firstResult = (this.currentPage - 1) * this.onePageSize;

	}

	public int getOnePageSize() {
		return onePageSize;
	}

	public void setOnePageSize(int onePageSize) {
		this.onePageSize = onePageSize;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
		if (totalResults % this.onePageSize == 0) {
			this.totalPage = totalResults / this.onePageSize;
		} else {
			this.totalPage = (int) Math.floor(totalResults / this.onePageSize) + 1;
		}

		if (this.totalPage == 0) {
			this.totalPage = 1;
		}
		if (this.currentPage > totalPage) {
			this.currentPage = totalPage;
			this.firstResult = (this.currentPage - 1) * this.onePageSize;

		}
		if (this.currentPage > 1) {
			this.previousPage = this.currentPage - 1;
		} else {
			this.previousPage = 1;
		}
	}

	public int getFirstResult() {
		return firstResult;
	}

	public int getNextPage() {
		if (this.currentPage < this.totalPage) {
			this.nextPage = this.currentPage + 1;
		} else {
			this.nextPage = this.totalPage;
		}
		return nextPage;
	}

	public int getPreviousPage() {
		return previousPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public List<E> getData() {
		return data;
	}

	public void setData(List<E> data) {
		this.data = data;
	}

	/**
	 * 前台分页导航
	 * 
	 * @param total
	 *            总记录数
	 * @param pageSize
	 *            一页显示长度
	 * @param currentPage
	 *            当前页码
	 * @param query_string
	 *            链接字符串
	 * @return Writer:airfey
	 */
	public String pageNavigation(int total, int pageSize, int currentPage,
			String query_string) {

		boolean flag = false;
		if (query_string.indexOf("%s") > -1) {
			flag = true;
		}

		int allpage = 0; // 总页数
		int next = 0; // 下一页
		int pre = 0; // 上一页
		int startcount = 0; // 开始数
		int endcount = 0; // 结束数

		if (currentPage < 1) {
			currentPage = 1;
		}
		if (pageSize != 0) {
			allpage = (total / pageSize);
			allpage = ((total % pageSize) != 0 ? allpage + 1 : allpage);
			allpage = (allpage == 0 ? 1 : allpage);
		}
		next = currentPage + 1;
		pre = currentPage - 1;

		startcount = currentPage - 2;
		endcount = currentPage + 2;
		// 校正，如果页数小于5则显示全部、大于5最多显示5条
		if (startcount < 1) {
			startcount = 1;
			if (allpage >= 5) {
				endcount = 5;
			} else {
				endcount = allpage;
			}
		}
		// 如果当前页为最后一页，总页数大于5则起始页为总页数-4，否则起始页为第一页
		if (allpage < endcount) {
			endcount = allpage;
			if (allpage >= 5) {
				startcount = allpage - 4;
			} else {
				startcount = 1;
			}
		}
		// 判断是否大于最后一页
		if (currentPage > allpage) {
			currentPage = allpage;
		}

		StringBuilder sb = new StringBuilder();
		if (currentPage > 1) {
			sb.append("<a href=\"")
					.append(getQueryUrl(query_string, "" + pre, flag))
					.append("\" class=\"pre-page\" >上一页</a> ");
		} else {
			sb.append(" <span class=\"pre-page disabled\">上一页</span> ");
		}
		// 中间页处理，这个增加时间复杂度，减小空间复杂度
		for (int i = startcount; i <= endcount; i++) {
			if (currentPage == i) {
				sb.append("<a class=\"page-p on\">").append(i).append("</a> ");
			} else {
				sb.append(" <a class=\"page-p\" href=\"")
						.append(getQueryUrl(query_string, "" + i, flag))
						.append("\">").append(i).append("</a> ");
			}
		}
		if (currentPage != allpage) {
			sb.append(" <a  href=\"")
					.append(getQueryUrl(query_string, "" + next, flag))
					.append("\" class=\"next-page\">下一页</a> ");
		} else {
			sb.append("<span class=\"next-page disabled\">下一页</span> ");
		}
		sb.append(" <span class=\"ml10\">&nbsp;共").append(allpage)
				.append("页&nbsp;</span>");

		// 跳转到第几页
		sb.append(" 跳转至第<input type='text' value='")
				.append(currentPage)
				.append("'id='jumpPageBox' size='2' style='width:24px;height:20px;' onblur='checkCurrentPage(document.getElementById(\"jumpPageBox\").value,")
				.append(allpage)
				.append(")'/>页 <input class='jump' style='cursor:pointer;'  type='button'  value='&nbsp;跳转&nbsp;' onclick='document.getElementById(\"pages\").value=document.getElementById(\"jumpPageBox\").value;window.location.href=\"")
				.append(getQueryUrl(query_string,
						"\"+document.getElementById(\"jumpPageBox\").value",
						flag)).append(";'/>");

		sb.append("<input type='hidden' value='" + currentPage
				+ "' name='currentPage' id='pages' />");

		return sb.toString();
	}

	private String getQueryUrl(String query, String pageNo, boolean flag) {
		String valueString = "";
		if (flag) {
			valueString = String.format(query, pageNo);
		} else {
			valueString = query + pageNo;
		}

		return valueString;

	}
}
