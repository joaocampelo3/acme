/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/9.0.70
 * Generated at: 2023-06-17 16:47:10 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.services.login;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import org.wso2.carbon.apimgt.impl.dto.SystemApplicationDTO;
import org.wso2.carbon.apimgt.impl.dao.SystemApplicationDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URI;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import org.wso2.carbon.apimgt.impl.IDPConfiguration;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.ui.publisher.Util;
import java.util.Map;

public final class idp_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(1);
    _jspx_dependants.put("/services/login/../constants.jsp", Long.valueOf(1683578756929L));
  }

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("org.wso2.carbon.apimgt.impl.dto.SystemApplicationDTO");
    _jspx_imports_classes.add("java.util.Arrays");
    _jspx_imports_classes.add("org.wso2.carbon.apimgt.impl.IDPConfiguration");
    _jspx_imports_classes.add("java.util.concurrent.Semaphore");
    _jspx_imports_classes.add("org.wso2.carbon.apimgt.impl.utils.APIUtil");
    _jspx_imports_classes.add("java.util.HashMap");
    _jspx_imports_classes.add("com.google.gson.GsonBuilder");
    _jspx_imports_classes.add("java.util.ArrayList");
    _jspx_imports_classes.add("java.net.http.HttpRequest");
    _jspx_imports_classes.add("java.net.URLEncoder");
    _jspx_imports_classes.add("java.util.List");
    _jspx_imports_classes.add("org.wso2.carbon.apimgt.impl.dao.SystemApplicationDAO");
    _jspx_imports_classes.add("com.google.gson.Gson");
    _jspx_imports_classes.add("java.util.Map");
    _jspx_imports_classes.add("org.apache.commons.logging.Log");
    _jspx_imports_classes.add("java.net.http.HttpClient");
    _jspx_imports_classes.add("java.util.regex.Pattern");
    _jspx_imports_classes.add("org.apache.commons.logging.LogFactory");
    _jspx_imports_classes.add("java.net.URI");
    _jspx_imports_classes.add("org.wso2.carbon.apimgt.ui.publisher.Util");
    _jspx_imports_classes.add("java.net.http.HttpResponse");
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
    log.debug("Services login DCR request");
    Map settings = Util.readJsonFile("/site/public/conf/settings.json", request.getServletContext());
    String appContext = Util.getAppContextForServerUrl((String) Util.readJsonObj(settings, "app.context"), (String) Util.readJsonObj(settings, "app.proxy_context_path"));
    String serverUrl = "";
    String forwarded_for = request.getHeader((String) Util.readJsonObj(settings, "app.customUrl.forwardedHeader"));
    boolean customUrlEnabled = (boolean) Util.readJsonObj(settings, "app.customUrl.enabled");
    if (customUrlEnabled && !forwarded_for.isEmpty()) {
        serverUrl = "https://" + forwarded_for;
    } else {
        serverUrl = APIUtil.getServerURL();
    }
    String authorizeEndpoint = serverUrl + AUTHORIZE_ENDPOINT_SUFFIX;

    IDPConfiguration idpConfig = APIUtil.getIdentityProviderConfig();
    if (idpConfig != null) {
        authorizeEndpoint = idpConfig.getAuthorizeEndpoint();
    }
    String host = (String) Util.readJsonObj(settings, "app.origin.host");
    String settingsAPIUrl = Util.getLoopbackOrigin(host) + SETTINGS_REST_API_URL_SUFFIX;
    String serviceCatalogSettingsAPIUrl = Util.getLoopbackOrigin(host) + SERVICE_CATALOG_SETTINGS_REST_API_URL_SUFFIX;

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest getSettingsReq = HttpRequest.newBuilder()
            .uri(URI.create(settingsAPIUrl))
            .build();
    HttpResponse<String> settingsResult = client.send(getSettingsReq, HttpResponse.BodyHandlers.ofString());

    HttpRequest getCatalogReq = HttpRequest.newBuilder()
            .uri(URI.create(serviceCatalogSettingsAPIUrl))
            .build();
    HttpResponse<String> serviceCatalogResult = client.send(getCatalogReq, HttpResponse.BodyHandlers.ofString());

    boolean responseFailed = false;
    String errorLogin = serverUrl + appContext + "/error-pages?code=";

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Map settingsResponse = gson.fromJson(settingsResult.body(), Map.class);
    Map serviceCatalogSettingsResponse = gson.fromJson(serviceCatalogResult.body(), Map.class);

    String dcrUrl = Util.getLoopbackOrigin(host) + DCR_URL_SUFFIX;
    String loginCallbackUrl = Util.getTenantBasedLoginCallBack(request, LOGIN_CALLBACK_URL_SUFFIX);
    if (loginCallbackUrl == null) {
        loginCallbackUrl = serverUrl + appContext + LOGIN_CALLBACK_URL_SUFFIX;
    }
    String logoutCallbackUrl = Util.getTenantBasedLogoutCallBack(request, LOGOUT_CALLBACK_URL_SUFFIX);
    if (logoutCallbackUrl == null) {
        logoutCallbackUrl = serverUrl + appContext + LOGOUT_CALLBACK_URL_SUFFIX;
    }
    String callbackUrl = "regexp=(" + loginCallbackUrl + "|" + logoutCallbackUrl + ")";
    String scopes = "";
    if (settingsResponse != null && serviceCatalogSettingsResponse != null) {
        List<String> scopesArray = (ArrayList<String>) Util.readJsonObj(settingsResponse, "scopes");
        String[] scopesList = scopesArray.toArray(new String[0]);
        scopes = String.join(" ", scopesList);
        List<String> scopesCatalogArray = (ArrayList<String>) Util.readJsonObj(serviceCatalogSettingsResponse, "scopes");
        String[] scopesCatalogList = scopesCatalogArray.toArray(new String[0]);
        String catalogScopes = String.join(" ", scopesCatalogList);
        scopes = scopes + " " + catalogScopes;
    } else {
        response.sendRedirect(errorLogin + "500");
        responseFailed = true;
    }

    String referer = request.getHeader("Referer");
    String state = "";
    // Get the pathname from query param 'referrer'
    if (referer != null) {
        String contextRef = !appContext.isEmpty() && appContext.charAt(0) == '/' ? appContext.substring(1) : appContext;
        String hostnamePattern = "(https?:\\/\\/.*):?(\\d*)\\/?(" + contextRef + ")";
        Pattern regPattern = Pattern.compile(hostnamePattern);
        String replaced = regPattern.matcher(referer).replaceAll("");
        state = URLEncoder.encode(replaced, "UTF-8");
    }

    String authorizationHeader = "Basic " + APIUtil.getBase64EncodedAdminCredentials();

    SystemApplicationDAO systemApplicationDAO = new SystemApplicationDAO();
    String clientId = "";
    String serviceProviderTenantDomain = Util.getServiceProviderTenantDomain(request);

    // this is to support migration from admin_store to admin_devportal
    SystemApplicationDTO systemApplicationDTO = systemApplicationDAO.getClientCredentialsForApplication(PUBLISHER_CLIENT_APP_NAME, serviceProviderTenantDomain);
    if (systemApplicationDTO == null) {
        systemApplicationDTO = systemApplicationDAO.getClientCredentialsForApplication(PUBLISHER_CLIENT_APP_NAME_OLD, serviceProviderTenantDomain);
    }

    Semaphore lock = SystemApplicationDAO.getLock();
    if (systemApplicationDTO != null) {
        clientId = systemApplicationDTO.getConsumerKey();
    } else {
        try {
            lock.acquire();
            systemApplicationDTO = systemApplicationDAO.getClientCredentialsForApplication(PUBLISHER_CLIENT_APP_NAME, serviceProviderTenantDomain);
            if (systemApplicationDTO == null) {
                systemApplicationDTO = systemApplicationDAO.getClientCredentialsForApplication(PUBLISHER_CLIENT_APP_NAME_OLD, serviceProviderTenantDomain);
            }
            if (systemApplicationDTO == null) {
                HashMap<String, Object> dcrRequestData = new HashMap();
                dcrRequestData.put("callbackUrl", callbackUrl);
                dcrRequestData.put("clientName", PUBLISHER_CLIENT_APP_NAME);
                dcrRequestData.put("owner", (String) APIUtil.getTenantAdminUserName(serviceProviderTenantDomain));
                dcrRequestData.put("grantType", "authorization_code refresh_token");
                dcrRequestData.put("saasApp", true);

                HttpRequest postReq = HttpRequest.newBuilder()
                        .uri(URI.create(dcrUrl))
                        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(dcrRequestData)))
                        .header("Authorization", authorizationHeader)
                        .header("Content-Type", "application/json")
                        .build();
                HttpResponse<String> dcrResult = client.send(postReq, HttpResponse.BodyHandlers.ofString());
                Map dcrResponse = gson.fromJson(dcrResult.body(), Map.class);
                clientId = (String) dcrResponse.get("clientId");
                String clientSecret = (String) dcrResponse.get("clientSecret");

                log.debug("Client ID = " + clientId);
                boolean addApplicationKey = systemApplicationDAO.addApplicationKey(PUBLISHER_CLIENT_APP_NAME, clientId, clientSecret, serviceProviderTenantDomain);
                if (!addApplicationKey) {
                    log.error("Error while persisting application information in system application DB table!!");
                    log.error("Client ID = " + clientId);
                }
            }
        } finally {
            lock.release();
        }
    }

    String authRequestParams = "?response_type=code&client_id=" + clientId + "&scope=" + scopes + "&state=" + state + "&redirect_uri=" + loginCallbackUrl;
    String queryString = request.getQueryString();
    if (queryString != null && queryString.equals("not-Login")) {
        authRequestParams += "&prompt=none";
    }
    log.debug("Redirecting to = " + authorizeEndpoint + authRequestParams);
    Cookie cookie = new Cookie("CLIENT_ID", clientId);
    cookie.setPath(appContext + "/");
    cookie.setSecure(true);
    cookie.setMaxAge(-1);
    if (!responseFailed) {
        response.addCookie(cookie);
        response.sendRedirect(authorizeEndpoint + authRequestParams);
    }


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
