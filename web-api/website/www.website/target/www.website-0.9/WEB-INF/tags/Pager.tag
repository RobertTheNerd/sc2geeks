<%@ tag import="com.sc2geeks.front.ui.PageUrlHelper" %>
<%@ tag import="java.util.List" %>
<%@ tag import="java.util.ArrayList" %>
<%@ tag import="com.sc2geeks.front.ui.QueryStringManager" %>
<%@ tag import="java.util.HashMap" %>
<%@ attribute name="pageUrlHelper" type="com.sc2geeks.front.ui.PageUrlHelper" required="true" %>
<%@ attribute name="totalPages" type="java.lang.Integer" required="true" %>
<%@ attribute name="currentPage" type="java.lang.Integer" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%!
	public class PagerParam
	{
		int minPage;
		int maxPage;
		String firstPageLink;
		String prevPageLink;
		String nextPageLink;
		String lastPageLink;
		HashMap<Integer, String> urlList;

		public PagerParam(int currentPage, int totalPages, PageUrlHelper pageUrlHelper)
		{
			if (totalPages > 1)
			{
				minPage = Math.max(1, currentPage - 4);
				maxPage = Math.min(totalPages, currentPage + 4);
				if (minPage == 1)
				{
					maxPage = Math.min(totalPages, 9);
				}
				else if (maxPage == totalPages)
				{
					minPage = Math.max(1, totalPages - 9);
				}
				urlList = new HashMap<Integer, String>(maxPage - minPage + 1);
				for (int i = minPage; i <= maxPage; i ++)
				{
					urlList.put(i, pageUrlHelper.setQueryStringParameter(QueryStringManager.Param_Page, Integer.toString(i)));
				}
				firstPageLink = pageUrlHelper.setQueryStringParameter(QueryStringManager.Param_Page, "1");
				prevPageLink = pageUrlHelper.setQueryStringParameter(QueryStringManager.Param_Page, Integer.toString(currentPage - 1));
				nextPageLink = pageUrlHelper.setQueryStringParameter(QueryStringManager.Param_Page, Integer.toString(currentPage + 1));
				lastPageLink = pageUrlHelper.setQueryStringParameter(QueryStringManager.Param_Page, Integer.toString(totalPages));
			}
		}

		public int getMinPage()
		{
			return minPage;
		}

		public int getMaxPage()
		{
			return maxPage;
		}

		public HashMap<Integer, String> getUrlList()
		{
			return urlList;
		}

		public String getFirstPageLink()
		{
			return firstPageLink;
		}

		public String getPrevPageLink()
		{
			return prevPageLink;
		}

		public String getNextPageLink()
		{
			return nextPageLink;
		}

		public String getLastPageLink()
		{
			return lastPageLink;
		}
	}
%>

<%@ include file="base.tag" %>
<%
	PagerParam pagerParam = new PagerParam(currentPage, totalPages, pageUrlHelper);
%>
<c:if test="${totalPages > 1}">
	<c:set var="pagerParam" value="<%= pagerParam %>" />

	<div class="pageNav">
		<ul id="pagination">
		<%-- first & prev page --%>
		<c:if test="${totalPages > 10}">
			<c:choose>
				<c:when test="${currentPage == 1}">
					<li class="disabled">&lt;&lt; First</li>
					<li class="disabled">&lt; Prev</li>
				</c:when>
				<c:otherwise>
					<li><a href="${pagerParam.firstPageLink}">&lt;&lt; First</a></li>
					<li><a href="${pagerParam.prevPageLink}">&lt; Prev</a></li>
				</c:otherwise>
			</c:choose>
		</c:if>

		<%-- pages with number --%>
		<c:forEach var="i" begin="${pagerParam.minPage}" end="${pagerParam.maxPage}">
			<c:choose>
				<c:when test="${i == currentPage}">
					<li class="active">${i}</li>
				</c:when>
				<c:otherwise>
					<li><a href="${pagerParam.urlList[i]}">${i}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<%-- next & last page --%>
		<c:if test="${totalPages > 10}">
			<c:choose>
				<c:when test="${currentPage == totalPages}">
					<li class="disabled">Next &gt;</li>
					<li class="disabled">Last &gt;&gt;</li>
				</c:when>
				<c:otherwise>
					<li><a href="${pagerParam.nextPageLink}">Next &gt;</a></li>
					<li><a href="${pagerParam.lastPageLink}">Last &gt;&gt;</a></li>
				</c:otherwise>
			</c:choose>
		</c:if>
		</ul>
	</div>
</c:if>