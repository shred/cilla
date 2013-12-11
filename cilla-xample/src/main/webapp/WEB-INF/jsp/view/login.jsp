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
<c:import url="/WEB-INF/jsp/design/header.jspf"/>
<c:import url="/WEB-INF/jsp/design/sidebar-left.jsp"/>
<div id="content" class="content">

  <c:if test="${not empty param.failed}">
    <p>
      <fmt:message key="login.failed"/>
    </p>
  </c:if>

  <p>
    <b><fmt:message key="login.required"/></b>
  </p>

  <form action="j_spring_security_check" method="post">
    <table>
      <tr>
        <td><fmt:message key="login.user"/>:</td>
        <td><input type="text" name="j_username" /></td>
      </tr>
      <tr>
        <td><fmt:message key="login.password"/>:</td>
        <td><input type="password" name="j_password" /></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><input type="checkbox" name="_spring_security_remember_me" /> <fmt:message key="login.rememberme"/></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td><input type="submit" value="<fmt:message key="login.submit"/>" /></td>
      </tr>
    </table>
  </form>

</div>
<c:import url="/WEB-INF/jsp/design/sidebar-right.jsp"/>
<c:import url="/WEB-INF/jsp/design/footer.jspf"/>