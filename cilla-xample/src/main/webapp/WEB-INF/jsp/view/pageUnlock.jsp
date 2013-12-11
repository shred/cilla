<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<%@ taglib prefix="sz" tagdir="/WEB-INF/tags/sz" %>
<fmt:setBundle basename="messages"/>
<c:set var="headTitle" value="${page.title}" scope="request"/>
<cilla:meta name="robots" content="noindex, nofollow"/>
<c:import url="/WEB-INF/jsp/design/header.jspf"/>
<c:import url="/WEB-INF/jsp/design/sidebar-left.jsp"/>

<div id="content" class="content">
  <c:if test="${not page.hidden}"><sz:pageturner/></c:if>

  <div <c:if test="${not page.published}"> class="page-preview"</c:if>>
    <h1><c:out value="${page.title}"/></h1>

    <div class="page-header">
      <fmt:formatDate value="${page.publication}" type="both" dateStyle="full"/>
    </div>

    <c:choose>
      <c:when test="${badRestrictionResponse}">
        <p><fmt:message key="view.unlock.failed"/></p>
      </c:when>
      <c:otherwise>
        <p><fmt:message key="view.unlock.query"/></p>
      </c:otherwise>
    </c:choose>

    <p>
      <fmt:message key="view.unlock.question"/>${" "}
      <strong><c:out value="${page.challenge}"/></strong>
    </p>

    <form action="<c:url value="${selfUri}"/>" method="post">
      <p>
        <fmt:message key="view.unlock.answer"/>${" "}
        <input type="text" name="restrictionresponse" />
        <input type="submit" value="<fmt:message key="view.unlock.send"/>" />
      </p>
    </form>
  </div>

</div>
<c:import url="/WEB-INF/jsp/design/sidebar-right.jsp"/>
<c:import url="/WEB-INF/jsp/design/footer.jspf"/>
