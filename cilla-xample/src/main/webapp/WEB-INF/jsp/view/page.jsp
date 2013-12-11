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
<cilla:commentThread var="comments" item="${page}"/>
<c:import url="/WEB-INF/jsp/design/header.jspf"/>
<c:import url="/WEB-INF/jsp/design/sidebar-left.jsp"/>

<div id="content" class="content">
  <c:if test="${not page.hidden}"><sz:pageturner/></c:if>

  <div <c:if test="${not page.published}">class="page-preview"</c:if>>
    <h1 id="headline"><c:out value="${page.title}"/></h1>
    <div class="page-header">
      <fmt:formatDate value="${page.publication}" type="both" dateStyle="full"/>
    </div>

    <cilla:format text="${page.teaser}" page="${page}"/>

    <c:if test="${not empty page.sections}">
      <div id="more">
        <c:forEach var="section" items="${page.sections}">
          <cilla:render fragment="section" item="${section}"/>
        </c:forEach>
      </div>
    </c:if>

    <c:if test="${not empty sameSubject}">
      <div id="subject">
        <fmt:message key="view.page.subject"/>
        <ul>
          <c:forEach var="sp" items="${sameSubject}">
            <c:if test="${sp == page}"><li><c:out value="${sp.title}"/></li></c:if>
            <c:if test="${sp != page}"><li><cilla:link page="${sp}"><c:out value="${sp.title}"/></cilla:link></li></c:if>
          </c:forEach>
        </ul>
      </div>
    </c:if>

    <p>
      <fmt:message key="view.page.author"/>${" "}
      <cilla:link author="${page.creator}"><c:out value="${page.creator.name}"/></cilla:link>

      <c:if test="${not empty page.categories}">
        ${" "}<fmt:message key="view.page.incategory"/>${" "}
        <c:forEach var="category" varStatus="status" items="${page.categories}">
          <c:if test="${not status.first}">${", "}</c:if>
          <cilla:link category="${category}"><c:out value="${category.name}"/></cilla:link>
        </c:forEach>
      </c:if>

      <c:if test="${page.thread.commentable or comments.count gt 0}">
        &mdash;
        <cilla:link page="${page}" anchor="comments"><fmt:message key="view.page.comments">
          <fmt:param value="${comments.count}"/>
        </fmt:message></cilla:link>
      </c:if>
    </p>

    <c:if test="${not empty page.tags}">
      <p>
        <fmt:message key="view.page.tags"/>${" "}
        <c:forEach var="tag" varStatus="status" items="${page.tags}">
          <c:if test="${not status.first}">${", "}</c:if>
          <cilla:link tag="${tag}"><c:out value="${tag.name}"/></cilla:link>
        </c:forEach>
      </p>
    </c:if>
  </div>

  <cilla:render fragment="comment" item="${page}"/>

</div>
<c:import url="/WEB-INF/jsp/design/sidebar-right.jsp"/>
<c:import url="/WEB-INF/jsp/design/footer.jspf"/>
