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
<fmt:setBundle basename="messages"/>
<fmt:message key="header.list.title" var="headTitle" scope="request"/>
<cilla:meta name="robots" content="noindex"/>
<cilla:mapInit/>
<c:import url="/WEB-INF/jsp/design/header.jspf"/>
<c:import url="/WEB-INF/jsp/design/sidebar-left.jsp"/>
<div id="content" class="content">
  <h1><fmt:message key="header.list.title"/></h1>

  <c:forEach var="head" items="${heads}">
    <h2><c:out value="${head.caption}"/></h2>
    <cilla:link header="${head}"><cilla:image header="${head}" type="preview"/></cilla:link>
    <p>
      <cilla:format text="${head.description}" stripHtml="true" truncate="100"/>
    </p>
  </c:forEach>
</div>
<c:import url="/WEB-INF/jsp/design/sidebar-right.jsp"/>
<c:import url="/WEB-INF/jsp/design/footer.jspf"/>
