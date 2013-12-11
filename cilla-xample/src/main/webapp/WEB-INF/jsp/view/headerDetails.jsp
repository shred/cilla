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
<c:set var="headTitle" value="${headerImage.caption}" scope="request"/>
<cilla:mapInit/>
<c:import url="/WEB-INF/jsp/design/header.jspf"/>
<c:import url="/WEB-INF/jsp/design/sidebar-left.jsp"/>
<div id="content" class="content">

  <div class="page-toolbar">
    <cilla:link view="headerList" var="btnlink"/><sz:button href="${btnlink}" icon="pictures"><fmt:message key="header.details.catalog"/></sz:button>
  </div>

  <h1><c:out value="${headerImage.caption}"/></h1>

  <div class="page-header">
    <fmt:message key="header.details.author"/>${" "}
    <cilla:link author="${headerImage.creator}"><c:out value="${headerImage.creator.name}"/></cilla:link>${" "}
    <fmt:message key="header.details.on"/>${" "}
    <fmt:formatDate value="${headerImage.creation}" type="both" dateStyle="full"/>
  </div>

  <cilla:image header="${headerImage}" uncropped="true"/>
  <c:if test="${not empty headerImage.description.text}">
    <cilla:format text="${headerImage.description}"/>
  </c:if>

  <div id="mapinfo"><fmt:message key="header.details.showmap" /></div>

  <cilla:render fragment="comment" item="${headerImage}"/>
</div>
<c:if test="${not empty headerImage.location && not empty headerImage.location.longitude && not empty headerImage.location.latitude}">
  <cilla:link view="header.map" header="${headerImage}" var="maplink"/>
  <script type="text/javascript">//<![CDATA[
    gallery.setupMap('<c:url value="${maplink}"/>', '<c:out value="${headerImage.caption}"/>');
  //]]></script>
</c:if>
<c:import url="/WEB-INF/jsp/design/sidebar-right.jsp"/>
<c:import url="/WEB-INF/jsp/design/footer.jspf"/>