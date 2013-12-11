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
<cilla:meta name="robots" content="noindex"/>
<c:choose>
  <c:when test="${not empty result.filter.category}">
    <fmt:message var="catTitle" key="view.pageList.title.category">
      <fmt:param value="${result.filter.category.name}"/>
    </fmt:message>
  </c:when>
  <c:when test="${not empty result.filter.tag}">
    <fmt:message var="catTitle" key="view.pageList.title.tag">
      <fmt:param value="${result.filter.tag.name}"/>
    </fmt:message>
  </c:when>
  <c:when test="${not empty result.filter.creator}">
    <fmt:message var="catTitle" key="view.pageList.title.creator">
      <fmt:param value="${result.filter.creator.name}"/>
    </fmt:message>
  </c:when>
  <c:otherwise>
    <fmt:message var="catTitle" key="view.pageList.title.all"/>
    <cilla:meta name="robots" content="all" replace="true"/>
  </c:otherwise>
</c:choose>
<c:choose>
  <c:when test="${result.filter.date.day gt 0}">
    <fmt:message var="catDate" key="view.pageList.title.dmy">
      <fmt:param value="${result.filter.date.day}"/>
      <fmt:param value="${result.filter.date.month}"/>
      <fmt:param value="${result.filter.date.year}"/>
    </fmt:message>
  </c:when>
  <c:when test="${result.filter.date.month gt 0}">
    <fmt:message var="catDate" key="view.pageList.title.my">
      <fmt:param value="${result.filter.date.month}"/>
      <fmt:param value="${result.filter.date.year}"/>
    </fmt:message>
  </c:when>
  <c:when test="${result.filter.date.year gt 0}">
    <fmt:message var="catDate" key="view.pageList.title.y">
      <fmt:param value="${result.filter.date.year}"/>
    </fmt:message>
  </c:when>
  <c:otherwise>
    <fmt:message var="catDate" key="view.pageList.title.nodate"/>
  </c:otherwise>
</c:choose>
<fmt:message var="headTitle" scope="request" key="view.pageList.title">
  <fmt:param value="${catTitle}"/>
  <fmt:param value="${catDate}"/>
</fmt:message>
<c:import url="/WEB-INF/jsp/design/header.jspf"/>
<c:import url="/WEB-INF/jsp/design/sidebar-left.jsp"/>
<div id="content" class="content">
  <cilla:render fragment="paginator" item="${paginator}" rendered="${paginator.pageCount gt 1}">
    <cilla:param name="filter" value="${result.filter}"/>
  </cilla:render>

  <c:if test="${not empty result.filter.category and (not empty result.filter.category.children or not empty result.filter.category.caption.text)}">
    <cilla:format text="${result.filter.category.caption}"/>
    <c:if test="${not empty result.filter.category.children}">
      <p>
        <fmt:message key="view.pageList.subcategories"/>${" "}
        <c:forEach var="subcat" varStatus="status" items="${result.filter.category.children}">
          <c:if test="${not status.first}">${", "}</c:if>
          <cilla:link category="${subcat}"><c:out value="${subcat.name}"/></cilla:link>
        </c:forEach>
      </p>
    </c:if>
  </c:if>

  <c:if test="${result.count eq 0}">
    <p><fmt:message key="view.pageList.empty"/></p>
  </c:if>

  <c:forEach var="page" items="${result.pages}">
    <cilla:commentThread var="comments" item="${page}"/>
    <h1><cilla:link page="${page}"><c:out value="${page.title}"/></cilla:link></h1>
    <div class="page-header">
      <fmt:formatDate value="${page.publication}" type="both" dateStyle="full"/>
    </div>
    <c:choose>
      <c:when test="${page.restricted}">
        <cilla:link page="${page}" var="btnlink"/><sz:button href="${btnlink}" icon="restricted"><fmt:message key="view.pageList.restricted"/></sz:button>
      </c:when>
      <c:otherwise>
        <cilla:format text="${page.teaser}" page="${page}"/>
        <c:if test="${not empty page.sections}">
          <cilla:link page="${page}" anchor="headline" var="btnlink"/><sz:button href="${btnlink}" icon="more"><fmt:message key="view.pageList.readMore"/></sz:button>
        </c:if>
        <c:forEach var="section" items="${page.sections}">
          <cilla:render fragment="sectionShort" item="${section}"/>
        </c:forEach>
      </c:otherwise>
    </c:choose>

    <p>
      <fmt:message key="view.pageList.author"/>${" "}
      <cilla:link author="${page.creator}"><c:out value="${page.creator.name}"/></cilla:link>
      <c:if test="${not empty page.categories}">
        ${" "}<fmt:message key="view.page.incategory"/>${" "}
        <c:forEach var="category" varStatus="status" items="${page.categories}">
          <c:if test="${not status.first}">${", "}</c:if>
          <cilla:link category="${category}"><c:out value="${category.name}"/></cilla:link>
        </c:forEach>
      </c:if>
      &mdash;
      <cilla:link page="${page}" anchor="comments"><fmt:message key="view.pageList.comments">
        <fmt:param value="${comments.count}"/>
      </fmt:message></cilla:link>
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
  </c:forEach>

  <cilla:render fragment="paginator" item="${paginator}" rendered="${paginator.pageCount gt 1}">
    <cilla:param name="filter" value="${result.filter}"/>
  </cilla:render>
</div>
<c:import url="/WEB-INF/jsp/design/sidebar-right.jsp"/>
<c:import url="/WEB-INF/jsp/design/footer.jspf"/>
