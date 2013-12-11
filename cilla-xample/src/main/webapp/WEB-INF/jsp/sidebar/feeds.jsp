<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
<%--
 -
 -  cilla - Blog Management System
 -
 -  Copyright (C) 2013 Richard "Shred" Körber
 -    http://cilla.shredzone.org
 -
 -  This program is free software: you can redistribute it and/or modify
 -  it under the terms of the GNU Affero General Public License as published
 -  by the Free Software Foundation, either version 3 of the License, or
 -  (at your option) any later version.
 -
 -  This program is distributed in the hope that it will be useful,
 -  but WITHOUT ANY WARRANTY; without even the implied warranty of
 -  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 -  GNU General Public License for more details.
 -
 -  You should have received a copy of the GNU Affero General Public License
 -  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 -
 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cilla" uri="http://cilla.shredzone.org/taglib/cilla" %>
<fmt:setBundle basename="messages"/>
<cilla:feed ref="${result.filter}" type="ATOM" var="atomUrl"/>
<cilla:feed ref="${result.filter}" type="RSS2" var="rss2Url"/>
<cilla:feed ref="${result.filter}" type="RSS" var="rssUrl"/>

<div class="sidebar-item">
<h1><fmt:message key="sidebar.feeds.title"/></h1>
<a href="<c:url value="${atomUrl}"/>">ATOM 1.0</a><br />
<a href="<c:url value="${rss2Url}"/>">RSS 2.0</a><br />
<a href="<c:url value="${rssUrl}"/>">RSS 1.0</a><br />
</div>
