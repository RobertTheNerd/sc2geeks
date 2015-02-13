<%@ page import="api.sc2geeks.entity.SearchInput" %>
<%@ page import="com.sc2geeks.front.view.SearchAllNavResult" %>
<%@ page import="com.sc2geeks.front.ui.SearchUrlHelper" %>
<%@ page import="com.sc2geeks.front.ui.ResourceHelper" %>
<%@ page import="api.sc2geeks.entity.Replay" %>
<%@ page import="api.sc2geeks.entity.RefinementField" %>
<%@ page import="api.sc2geeks.entity.SearchResult" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  User: robert
  Date: 5/5/12
  Time: 2:43 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" trimDirectiveWhitespaces="true" %>
<%@ include file="../../base.jsp" %>
<%
	SearchAllNavResult navResult = new SearchAllNavResult(this.<SearchInput>getValue("searchInput"),
		this.<SearchResult<Replay>>getValue("searchResult"),
		this.<SearchUrlHelper>getValue("searchUrlHelper"));

%>
<c:set var="navResult" value="<%= navResult%>"></c:set>
<c:if test="${navResult.totalCount > 0}">
	<div id="navAllTitle">
		<label for="filterName">Please select one <i>${navResult.filterName}</i>. </label><input id="filterName" type="text" /><input id="butFilterList" type="button" value="Filter" />
        <input id="butFilterClear" type="button" value="Show All" />
        <span id="imgFiltering" style="display: none;">Filtering ...</span>
	</div>
	<div class="navigationItemList navListContainer">
		<ul>
			<c:forEach var="i" begin="0" end="${navResult.totalCount - 1}">
			<li>
				<h4><a href="${navResult.links[i]}">${navResult.navigationNodes[i].displayNodeName} (${navResult.navigationNodes[i].count})</a></h4>
			</li>
			</c:forEach>
		</ul>
	</div>
    <script type="text/javascript">
        function onFilterKeyup_SearchAll(e){
            if (e.keyCode == 27 && $('#filterName').val() != '')
            {
                $('#filterName').val('');
                e.stopPropagation();
            }
            filterValues();
        }
        function onFilterKeyup_SearchOnEnter(e){
            if (e.keyCode == 27 && $('#filterName').val() != '')
            {
                $('#filterName').val('');
                e.stopPropagation();
            }
            // only on enter
            if (e.keyCode == 13)
                filterValues();
        }
        function onFilterKeydown(e){
            if (e.keyCode == 27 && $('#filterName').val() != '')
            {
                $('#filterName').val('');
                e.stopPropagation();
            }
        }
        function filterValues() {
            $('#imgFiltering').show();
            var filter = $('#filterName').val().toLowerCase();
            var innerHtml = '';
            for (var i = 0; i < allNames.length; i ++)
            {
                if (allNames[i].indexOf(filter) != -1)
                    innerHtml = innerHtml + " " + allMaps[allNames[i]];
            }
            $('div.navListContainer ul').html(innerHtml);
            $('#imgFiltering').hide();
        }

        var allNames;
        var allMaps;
        $('#imgFiltering').hide();
        allNames = new Array();
        allMaps = new Object();
        $('div.navListContainer ul li').each(function(){
            var li = $(this);
            var name = li.text().trim().toLowerCase();
            allNames.push(name);
            allMaps[name] = '<li>' + li.html().trim() + '</li>';
        });

        // $('#filterName').unbind('keyup', onFilterKeyup_SearchAll);
        $('#filterName').unbind('keyup', onFilterKeyup_SearchOnEnter);

        /*
         if ($('div.navListContainer ul li').length > 50)
         $('#filterName').bind('keyup', onFilterKeyup_SearchOnEnter);
         else
         $('#filterName').bind('keyup', onFilterKeyup_SearchAll);
         */
        $('#filterName').bind('keyup', onFilterKeyup_SearchOnEnter);
        $('#filterName').unbind('keydown', onFilterKeydown);
        $('#filterName').bind('keydown', onFilterKeydown);

        $('#butFilterList').click(function(){
            filterValues();
        });
        $('#butFilterClear').click(function(){
            $('#filterName').val('');
            filterValues();
        });


        $('#filterName').focus();
    </script>
</c:if>
