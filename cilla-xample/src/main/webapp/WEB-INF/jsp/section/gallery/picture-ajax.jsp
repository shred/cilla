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
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="sz" tagdir="/WEB-INF/tags/sz" %>
<fmt:setBundle basename="messages"/>

<div class="page-toolbar">
  <cilla:link page="${section.page}" anchor="headline" var="btnlink"/><sz:button href="${btnlink}" icon="pictures"><fmt:message key="section.gallery.picture.back"/></sz:button>
  <c:if test="${info.first}">
    <sz:button icon="first"><fmt:message key="section.gallery.picture.first"/></sz:button>
    <sz:button icon="previous"><fmt:message key="section.gallery.picture.previous"/></sz:button>
  </c:if>
  <c:if test="${not info.first}">
    <cilla:link section="${section}" picture="${info.firstPicture}" var="btnlink"/>
    <sz:button href="${btnlink}" icon="first"><fmt:message key="section.gallery.picture.first"/></sz:button>
    <cilla:link section="${section}" picture="${info.previousPicture}" var="btnlink"/>
    <sz:button href="${btnlink}" icon="previous"><fmt:message key="section.gallery.picture.previous"/></sz:button>
  </c:if>
  <c:if test="${info.last}">
    <sz:button icon="next"><fmt:message key="section.gallery.picture.next"/></sz:button>
    <sz:button icon="last"><fmt:message key="section.gallery.picture.last"/></sz:button>
  </c:if>
  <c:if test="${not info.last}">
    <cilla:link section="${section}" picture="${info.nextPicture}" var="btnlink"/>
    <sz:button href="${btnlink}" icon="next"><fmt:message key="section.gallery.picture.next"/></sz:button>
    <cilla:link section="${section}" picture="${info.lastPicture}" var="btnlink"/>
    <sz:button href="${btnlink}" icon="last"><fmt:message key="section.gallery.picture.last"/></sz:button>
  </c:if>
</div>

<div class="page-header">
  <span style="float: left">
    <fmt:message key="section.gallery.picture.number">
      <fmt:param value="${info.count}"/>
      <fmt:param value="${info.numberOfPictures}"/>
    </fmt:message>
    ${"&nbsp;&mdash;&nbsp;"}<c:out value="${section.page.title}"/>
  </span>
  <c:set var="ctp" value="${empty picture.createTimeDefinition ? section.defaultTimePrecision : picture.createTimeDefinition}"/>
  <c:if test="${ctp ne 'NONE'}">
    <fmt:message key="section.gallery.picture.precision.${ctp}" var="pattern"/>
    <fmt:formatDate value="${picture.createDate}" pattern="${pattern}" timeZone="${empty picture.createTimeZone ? section.defaultTimeZone : picture.createTimeZone}"/>
  </c:if>
</div>

<cilla:image picture="${picture}" alt="${info.count}"/>
<c:if test="${not empty picture.caption.text}">
  <cilla:format text="${picture.caption}" page="${section.page}"/>
</c:if>

<c:if test="${not empty picture.exifData}">
  <ul>
    <c:if test="${not empty picture.exifData.cameraModel}">
      <li>
        <fmt:message key="tag.exif.cameraModel"/>${' '}
        <c:out value="${picture.exifData.cameraModel}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.program}">
      <li>
        <fmt:message key="tag.exif.program"/>${' '}
        <c:out value="${picture.exifData.program}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.shutter}">
      <li>
        <fmt:message key="tag.exif.shutter"/>${' '}
        <c:out value="${picture.exifData.shutter}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.aperture}">
      <li>
        <fmt:message key="tag.exif.aperture"/>${' '}
        <c:out value="${picture.exifData.aperture}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.focalLength}">
      <li>
        <fmt:message key="tag.exif.focalLength"/>${' '}
        <c:out value="${picture.exifData.focalLength}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.iso}">
      <li>
        <fmt:message key="tag.exif.iso"/>${' '}
        <c:out value="${picture.exifData.iso}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.whiteBalance}">
      <li>
        <fmt:message key="tag.exif.whiteBalance"/>${' '}
        <c:out value="${picture.exifData.whiteBalance}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.exposureBias}">
      <li>
        <fmt:message key="tag.exif.exposureBias"/>${' '}
        <c:out value="${picture.exifData.exposureBias}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.flash}">
      <li>
        <fmt:message key="tag.exif.flash"/>${' '}
        <c:out value="${picture.exifData.flash}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.focusMode}">
      <li>
        <fmt:message key="tag.exif.focusMode"/>${' '}
        <c:out value="${picture.exifData.focusMode}"/>
      </li>
    </c:if>

    <c:if test="${not empty picture.exifData.meteringMode}">
      <li>
        <fmt:message key="tag.exif.meteringMode"/>${' '}
        <c:out value="${picture.exifData.meteringMode}"/>
      </li>
    </c:if>
  </ul>
</c:if>

<div id="mapinfo"><fmt:message key="section.gallery.picture.showmap" /></div>

<c:if test="${not empty picture.tags}">
  <p>
    <fmt:message key="section.gallery.picture.tags"/>${" "}
    <c:forEach var="tag" varStatus="status" items="${picture.tags}">
      <c:if test="${not status.first}">${", "}</c:if>
      <cilla:link tag="${tag}"><c:out value="${tag.name}"/></cilla:link>
    </c:forEach>
  </p>
</c:if>

<cilla:render fragment="comment" item="${picture}">
  <cilla:param name="enabled" value="${section.commentable}"/>
</cilla:render>

<c:if test="${not empty picture.location && not empty picture.location.longitude && not empty picture.location.latitude}">
  <cilla:link view="gallery.map" section="${section}" picture="${picture}" var="maplink"/>
  <script type="text/javascript">//<![CDATA[
    gallery.setupMap('<c:url value="${maplink}"/>', '<c:out value="${section.page.title}"/>');
  //]]></script>
</c:if>
