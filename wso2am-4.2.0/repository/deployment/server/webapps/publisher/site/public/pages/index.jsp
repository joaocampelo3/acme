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
<%@page import="org.wso2.carbon.apimgt.ui.publisher.Util" %>

<%@page trimDirectiveWhitespaces="true" %>

<!DOCTYPE html>
<html lang="en">
    <%
        Log log = LogFactory.getLog(this.getClass());
        Map settings = Util.readJsonFile("/site/public/conf/settings.json", request.getServletContext());
        String context = Util.getTenantBasePublisherContext(request, (String) Util.readJsonObj(settings, "app.context"));
    %>
    <head>
        <base href="<%= context%>/" />
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title></title>

        <link rel="shortcut icon" href="<%= context%>/site/public/images/favicon.ico">
        <link href="<%= context%>/site/public/css/main.css" type="text/css" rel="stylesheet" />
        <link href="<%= context%>/site/public/css/draftjs.css" type="text/css" rel="stylesheet" />
    </head>

    <body>

        <div id="react-root">
            <div class="apim-dual-ring"></div>
        </div>

        <script src="<%= context%>/site/public/fonts/iconfont/MaterialIcons.js"></script>
        <script src="<%= context%>/site/public/conf/userThemes.js"></script>
        <script src="<%= context%>/services/settings/settings.js"></script>
        <script src="<%= context%>/site/public/conf/portalSettings.js"></script>
        <script>
            if (typeof module !== 'undefined') {
                module.exports = AppConfig; // For Jest unit tests
            }
        </script>
        <script src="<%= context%>/site/public/dist/index.ebdd6f43ad27f9232620.bundle.js"></script>
        <!-- Swagger worker has being removed until we resolve
        *              https://github.com/wso2/product-apim/issues/10694 issue, need to change webpack config too -->
        <!-- <script src="<%= context%>/"></script> -->
        <link rel="stylesheet" href="<%= context%>/site/public/fonts/iconfont/material-icons.css">
    </body>

</html>
