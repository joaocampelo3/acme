/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/9.0.70
 * Generated at: 2023-06-17 16:47:10 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.services.logout;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.wso2.carbon.apimgt.impl.IDPConfiguration;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.ui.publisher.Util;
import java.util.Map;

public final class logout_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(1);
    _jspx_dependants.put("/services/logout/../constants.jsp", Long.valueOf(1683578756929L));
  }

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("org.wso2.carbon.apimgt.impl.IDPConfiguration");
    _jspx_imports_classes.add("org.wso2.carbon.apimgt.impl.utils.APIUtil");
    _jspx_imports_classes.add("java.util.Map");
    _jspx_imports_classes.add("org.apache.commons.logging.Log");
    _jspx_imports_classes.add("org.apache.commons.logging.LogFactory");
    _jspx_imports_classes.add("org.wso2.carbon.apimgt.ui.publisher.Util");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    if (!javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
        return;
      }
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;


    String SETTINGS_REST_API_URL_SUFFIX = "/api/am/publisher/v4/settings";
    String SERVICE_CATALOG_SETTINGS_REST_API_URL_SUFFIX = "/api/am/service-catalog/v1/settings";
    String DCR_URL_SUFFIX = "/client-registration/v0.17/register";
    String AUTHORIZE_ENDPOINT_SUFFIX = "/oauth2/authorize";
    String OIDC_LOGOUT_ENDPOINT_SUFFIX = "/oidc/logout";
    String TOKEN_URL_SUFFIX = "/oauth2/token";
    String REVOKE_URL_SUFFIX = "/oauth2/revoke";
    String LOGIN_CALLBACK_URL_SUFFIX = "/services/auth/callback/login";
    String LOGOUT_CALLBACK_URL_SUFFIX = "/services/auth/callback/logout";
    String PUBLISHER_CLIENT_APP_NAME_OLD = "admin_publisher";
    String PUBLISHER_CLIENT_APP_NAME = "apim_publisher";
    String SUPER_TENANT_DOMAIN = "carbon.super";

    Log log = LogFactory.getLog(this.getClass());
    log.debug("Logout Request Function");
    Map settings = Util.readJsonFile("/site/public/conf/settings.json", request.getServletContext());
    String appContext = Util.getAppContextForServerUrl((String) Util.readJsonObj(settings, "app.context"), (String) Util.readJsonObj(settings, "app.proxy_context_path"));

    String idTokenP1Cookie = "";
    String idTokenP2Cookie = "";
    Cookie[] cookies = request.getCookies();
    for (int i = 0; i < cookies.length; i++) {
        String cookieName = cookies[i].getName();
        if ("AM_ID_TOKEN_DEFAULT_P1".equals(cookieName)) {
            idTokenP1Cookie = cookies[i].getValue();

        }
        if ("AM_ID_TOKEN_DEFAULT_P2".equals(cookieName)) {
            idTokenP2Cookie = cookies[i].getValue();;
        }
        if (!idTokenP1Cookie.isEmpty() && !idTokenP2Cookie.isEmpty()) {
            break;
        }
    }

    String idToken = "";
    if (!idTokenP1Cookie.isEmpty() && !idTokenP2Cookie.isEmpty()) {
        idToken = idTokenP1Cookie + idTokenP2Cookie;
    }

    String serverUrl = "";
    String forwarded_for = request.getHeader((String) Util.readJsonObj(settings, "app.customUrl.forwardedHeader"));
    boolean customUrlEnabled = (boolean) Util.readJsonObj(settings, "app.customUrl.enabled");
    if (customUrlEnabled && !forwarded_for.isEmpty()) {
        serverUrl = "https://" + forwarded_for;
    } else {
        serverUrl = APIUtil.getServerURL();
        if (serverUrl == null) {
            serverUrl = APIUtil.getServerURL();
        }
    }
    String logoutEndpoint = serverUrl + OIDC_LOGOUT_ENDPOINT_SUFFIX;

    IDPConfiguration idpConfig = APIUtil.getIdentityProviderConfig();
    if (idpConfig != null) {
        logoutEndpoint = idpConfig.getOidcLogoutEndpoint();
    }
    String postLogoutRedirectURI = Util.getTenantBasedLogoutCallBack(request, LOGOUT_CALLBACK_URL_SUFFIX);
    if (postLogoutRedirectURI == null) {
        postLogoutRedirectURI = serverUrl + appContext + LOGOUT_CALLBACK_URL_SUFFIX;
    }
    String idTokenParam = !idToken.isEmpty() ? "?id_token_hint=" + idToken + "&" : "?";
    String url = logoutEndpoint + idTokenParam + "post_logout_redirect_uri=" + postLogoutRedirectURI;

    log.debug("Redirecting to = " + url);
    response.sendRedirect(url);

    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
