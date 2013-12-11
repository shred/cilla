<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
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
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="messages"/>

<div class="sidebar-item">
<h1><fmt:message key="sidebar.login.title"/></h1>
<sec:authentication property="principal" var="user" scope="request"/>
<c:choose>
  <c:when test="${!empty user && user != 'anonymousUser'}">
    <fmt:message key="sidebar.login.login">
      <fmt:param value="${user.username}"/>
    </fmt:message><br />
    <a href="<c:url value="/logout"/>"><fmt:message key="sidebar.login.logout"/></a>
  </c:when>
  <c:otherwise>
    <cilla:link view="login"><fmt:message key="sidebar.login.login"/></cilla:link>
  </c:otherwise>
</c:choose>
</div>