<%--
  ~ Copyright (c) 2017, WSO2 LLC (http://www.wso2.org) All Rights Reserved.
  ~
  ~ WSO2 LLC licenses this file to you under the Apache License,
  ~ Version 2.0 (the "License"); you may not use this file except
  ~ in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~    http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~ KIND, either express or implied.  See the License for the
  ~ specific language governing permissions and limitations
  ~ under the License.
--%>

<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@page import="org.wso2.carbon.apimgt.ui.admin.Util"%>
<%@page import="java.util.Map"%>

<%
    Log log = LogFactory.getLog(this.getClass());
    log.debug("Logout Callback Function");
    Map settings = Util.readJsonFile("/site/public/conf/settings.json", request.getServletContext());
    String context = (String) Util.readJsonObj(settings, "app.context");

    Cookie cookie = new Cookie("AM_ACC_TOKEN_DEFAULT_P2", "");
    cookie.setPath(context + "/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(2);
    response.addCookie(cookie);

    cookie = new Cookie("AM_ACC_TOKEN_DEFAULT_P2", "");
    cookie.setPath("/api/am/" + context + "/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(2);
    response.addCookie(cookie);

    cookie = new Cookie("AM_REF_TOKEN_DEFAULT_P2", "");
    cookie.setPath(context + "/");
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setMaxAge(2);
    response.addCookie(cookie);

    cookie = new Cookie("WSO2_AM_REFRESH_TOKEN_1_Default", "");
    cookie.setPath(context + "/");
    cookie.setSecure(true);
    cookie.setMaxAge(2);
    response.addCookie(cookie);

    cookie = new Cookie("WSO2_AM_TOKEN_1_Default", "");
    cookie.setPath(context + "/");
    cookie.setSecure(true);
    cookie.setMaxAge(2);
    response.addCookie(cookie);

    cookie = new Cookie("AM_ID_TOKEN_DEFAULT_P2", "");
    cookie.setPath(context + "/services/logout");
    cookie.setSecure(true);
    cookie.setMaxAge(2);
    response.addCookie(cookie);

    cookie = new Cookie("AM_ID_TOKEN_DEFAULT_P1", "");
    cookie.setPath(context + "/services/logout");
    cookie.setSecure(true);
    cookie.setMaxAge(2);
    response.addCookie(cookie);

    log.debug("redirecting to logout");
    response.sendRedirect(context + "/logout");
%>
