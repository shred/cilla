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
<div class="sidebar-item">
<h1><fmt:message key="sidebar.menu.title"/></h1>
<div class="menu">
  <ul>
    <li><cilla:link><fmt:message key="sidebar.menu.home"/></cilla:link></li>
    <c:forEach var="cat" items="${rootCategories}">
      <li><cilla:link category="${cat}"><c:out value="${cat.name}"/></cilla:link></li>
    </c:forEach>
    <li><cilla:link><cilla:param name="#pagename" value="${'contact'}"/><fmt:message key="sidebar.menu.contact"/></cilla:link></li>
  </ul>
</div>
</div>
