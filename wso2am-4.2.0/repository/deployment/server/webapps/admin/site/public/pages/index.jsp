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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="org.wso2.carbon.apimgt.ui.admin.Util" %>

<%@page trimDirectiveWhitespaces="true" %>

<!DOCTYPE html>
<html lang="en">
    <%
        Map settings = Util.readJsonFile("/site/public/conf/settings.json", request.getServletContext());
        String context = (String) Util.readJsonObj(settings, "app.context");
    %>

    <head>
        <base href="<%= context%>/" />
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=1024, initial-scale=1" />
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>[Admin Portal]WSO2 APIM</title>

        <link href="<%= context%>/site/public/css/main.css" type="text/css" rel="stylesheet" />
        <link href="<%= context%>/site/public/css/draftjs.css" type="text/css" rel="stylesheet" />
        <link rel="shortcut icon" href="<%= context%>/site/public/images/favicon.png">
    </head>

    <body>

        <div id="react-root">
            <div class="apim-dual-ring"></div>
        </div>

        <script src="<%= context%>/site/public/fonts/iconfont/MaterialIcons.js"></script>
        <script src="<%= context%>/site/public/conf/userTheme.js"></script>
        <script src="<%= context%>/services/settings/settings.js"></script>
        <script src="<%= context%>/site/public/dist/index.953096645d67a0e9e956.bundle.js"></script>
        <link rel="stylesheet" href="<%= context%>/site/public/fonts/iconfont/material-icons.css">
    </body>

</html>
