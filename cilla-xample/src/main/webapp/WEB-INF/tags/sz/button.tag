<%@ tag body-content="scriptless" pageEncoding="utf-8" trimDirectiveWhitespaces="true" %>
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
<%@ attribute name="href" rtexprvalue="true" %>
<%@ attribute name="icon" required="true" rtexprvalue="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cilla" uri="http://cilla.shredzone.org/taglib/cilla" %>
<c:if test="${not empty href}"><a href="${href}" class="button"><img src="<c:url value="/img/icon/${icon}.png"/>" width="16" height="16" alt="${icon}" /><jsp:doBody/></a></c:if><c:if test="${empty href}"><span class="button button-disabled"><img src="<c:url value="/img/icon/${icon}.png"/>" width="16" height="16" alt="${icon}" /><jsp:doBody/></span></c:if>