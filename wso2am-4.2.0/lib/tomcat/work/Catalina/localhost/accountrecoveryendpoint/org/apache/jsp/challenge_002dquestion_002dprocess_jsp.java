/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/9.0.70
 * Generated at: 2023-06-17 16:46:25 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.apache.commons.collections.map.HashedMap;
import org.wso2.carbon.identity.mgt.endpoint.util.IdentityManagementEndpointConstants;
import org.wso2.carbon.identity.mgt.endpoint.util.IdentityManagementEndpointUtil;
import org.wso2.carbon.identity.mgt.endpoint.util.IdentityManagementServiceUtil;
import org.wso2.carbon.identity.mgt.endpoint.util.client.ApiException;
import org.wso2.carbon.identity.mgt.endpoint.util.client.api.SecurityQuestionApi;
import org.wso2.carbon.identity.mgt.endpoint.util.client.model.AnswerVerificationRequest;
import org.wso2.carbon.identity.mgt.endpoint.util.client.model.InitiateAllQuestionResponse;
import org.wso2.carbon.identity.mgt.endpoint.util.client.model.InitiateQuestionResponse;
import org.wso2.carbon.identity.mgt.endpoint.util.client.model.Question;
import org.wso2.carbon.identity.mgt.endpoint.util.client.model.RetryError;
import org.wso2.carbon.identity.mgt.endpoint.util.client.model.SecurityAnswer;
import org.wso2.carbon.identity.mgt.endpoint.util.client.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.wso2.carbon.identity.mgt.endpoint.util.EncodedControl;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public final class challenge_002dquestion_002dprocess_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(1);
    _jspx_dependants.put("/includes/localize.jsp", Long.valueOf(1683578762818L));
  }

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.client.model.AnswerVerificationRequest");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.IdentityManagementServiceUtil");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.client.ApiException");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.client.model.RetryError");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.client.model.InitiateAllQuestionResponse");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.client.model.User");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.IdentityManagementEndpointUtil");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.IdentityManagementEndpointConstants");
    _jspx_imports_classes.add("org.apache.commons.collections.map.HashedMap");
    _jspx_imports_classes.add("java.nio.charset.StandardCharsets");
    _jspx_imports_classes.add("java.util.ArrayList");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.client.model.SecurityAnswer");
    _jspx_imports_classes.add("java.util.List");
    _jspx_imports_classes.add("java.util.ResourceBundle");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.EncodedControl");
    _jspx_imports_classes.add("java.util.Map");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.client.model.Question");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.client.model.InitiateQuestionResponse");
    _jspx_imports_classes.add("org.wso2.carbon.identity.mgt.endpoint.util.client.api.SecurityQuestionApi");
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
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

    String BUNDLE = "org.wso2.carbon.identity.mgt.recovery.endpoint.i18n.Resources";
    ResourceBundle recoveryResourceBundle = ResourceBundle.getBundle(BUNDLE, request.getLocale(),
            new EncodedControl(StandardCharsets.UTF_8.toString()));

      out.write('\n');
      out.write('\n');
      out.write('\n');

    String userName = request.getParameter("username");
    String securityQuestionAnswer = request.getParameter("securityQuestionAnswer");
    String sessionDataKey = request.getParameter("sessionDataKey");

    if ( request.getParameter("callback") != null) {
        session.setAttribute("callback", request.getParameter("callback"));
    }
    if (request.getParameter("username") != null) {
        session.setAttribute("username", request.getParameter("username"));
    }
    
    if (request.getParameter("sessionDataKey") != null) {
        session.setAttribute("sessionDataKey", request.getParameter("sessionDataKey"));
    }

    if (userName != null) {
        //Initiate Challenge Question flow with one by one questions

        User user = IdentityManagementServiceUtil.getInstance().getUser(userName);
        session.setAttribute(IdentityManagementEndpointConstants.TENANT_DOMAIN, user.getTenantDomain());

        try {
            Map<String, String> requestHeaders = new HashedMap();
            if (request.getParameter("g-recaptcha-response") != null) {
                requestHeaders.put("g-recaptcha-response", request.getParameter("g-recaptcha-response"));
            }
            
            SecurityQuestionApi securityQuestionApi = new SecurityQuestionApi();
            InitiateQuestionResponse initiateQuestionResponse = securityQuestionApi.securityQuestionGet(
                    user.getUsername(), user.getRealm(), user.getTenantDomain(), requestHeaders);
            IdentityManagementEndpointUtil.addReCaptchaHeaders(request, securityQuestionApi.getApiClient().getResponseHeaders());
            session.setAttribute("initiateChallengeQuestionResponse", initiateQuestionResponse);
            request.getRequestDispatcher("/viewsecurityquestions.do").forward(request, response);
        } catch (ApiException e) {
            if (e.getCode() == 204) {
                request.setAttribute("error", true);
                request.setAttribute("errorMsg", IdentityManagementEndpointUtil.i18n(recoveryResourceBundle,
                        "No.security.questions.found.to.recover.password.contact.system.administrator"));
                request.setAttribute("errorCode", "18017");
                request.setAttribute("username", userName);
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            IdentityManagementEndpointUtil.addReCaptchaHeaders(request, e.getResponseHeaders());
            IdentityManagementEndpointUtil.addErrorInformation(request, e);
            request.setAttribute("username", userName);
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

    } else if (securityQuestionAnswer != null) {

        InitiateQuestionResponse challengeQuestionResponse = (InitiateQuestionResponse)
                session.getAttribute("initiateChallengeQuestionResponse");


        List<SecurityAnswer> securityAnswers = new ArrayList<SecurityAnswer>();
        SecurityAnswer securityAnswer = new SecurityAnswer();
        securityAnswer.setQuestionSetId(challengeQuestionResponse.getQuestion().getQuestionSetId());
        securityAnswer.setAnswer(securityQuestionAnswer);

        securityAnswers.add(securityAnswer);

        AnswerVerificationRequest answerVerificationRequest = new AnswerVerificationRequest();
        answerVerificationRequest.setKey(challengeQuestionResponse.getKey());
        answerVerificationRequest.setAnswers(securityAnswers);

        Map<String, String> requestHeaders = new HashedMap();
        if(request.getParameter("g-recaptcha-response") != null) {
            requestHeaders.put("g-recaptcha-response", request.getParameter("g-recaptcha-response"));
        }

        try {
            SecurityQuestionApi securityQuestionApi = new SecurityQuestionApi();
            InitiateQuestionResponse initiateQuestionResponse =
                    securityQuestionApi.validateAnswerPost(answerVerificationRequest, requestHeaders);

            if ("validate-answer".equalsIgnoreCase(initiateQuestionResponse.getLink().getRel())) {
                session.setAttribute("initiateChallengeQuestionResponse", initiateQuestionResponse);
                request.getRequestDispatcher("/viewsecurityquestions.do").forward(request, response);
            } else if ("set-password".equalsIgnoreCase(initiateQuestionResponse.getLink().getRel())) {
                session.setAttribute("confirmationKey", initiateQuestionResponse.getKey());
                request.setAttribute("callback", session.getAttribute("callback"));
                request.setAttribute("username", session.getAttribute("username"));
                request.setAttribute("sessionDataKey", session.getAttribute("sessionDataKey"));
                session.removeAttribute("callback");
                session.removeAttribute("username");
                session.removeAttribute("sessionDataKey");
                request.getRequestDispatcher("password-reset.jsp").forward(request, response);
            }

        } catch (ApiException e) {
            RetryError retryError = IdentityManagementEndpointUtil.buildRetryError(e);
            if (retryError != null && "20008".equals(retryError.getCode())) {
                IdentityManagementEndpointUtil.addReCaptchaHeaders(request, e.getResponseHeaders());
                request.setAttribute("errorResponse", retryError);
                request.getRequestDispatcher("/viewsecurityquestions.do").forward(request, response);
                return;
            }

            request.setAttribute("error", true);
            if (retryError != null) {
                request.setAttribute("errorMsg", retryError.getDescription());
                request.setAttribute("errorCode", retryError.getCode());
            }

            request.setAttribute("username", userName);
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

    } else if (Boolean.parseBoolean(application.getInitParameter(IdentityManagementEndpointConstants
            .ConfigConstants.PROCESS_ALL_SECURITY_QUESTIONS))) {

        //Process security questions at once

        InitiateAllQuestionResponse initiateAllQuestionResponse =
                (InitiateAllQuestionResponse) session.getAttribute("initiateAllQuestionResponse");
        List<Question> challengeQuestions = initiateAllQuestionResponse.getQuestions();

        List<SecurityAnswer> securityAnswers = new ArrayList<SecurityAnswer>();
        for (int i = 0; i < challengeQuestions.size(); i++) {

            SecurityAnswer userChallengeAnswer = new SecurityAnswer();
            userChallengeAnswer.setQuestionSetId(challengeQuestions.get(i).getQuestionSetId());
            userChallengeAnswer.setAnswer(request.getParameter(challengeQuestions.get(i).getQuestionSetId()));
            securityAnswers.add(userChallengeAnswer);

        }

        Map<String, String> requestHeaders = new HashedMap();
        if(request.getParameter("g-recaptcha-response") != null) {
            requestHeaders.put("g-recaptcha-response", request.getParameter("g-recaptcha-response"));
        }


        AnswerVerificationRequest answerVerificationRequest = new AnswerVerificationRequest();
        answerVerificationRequest.setKey(initiateAllQuestionResponse.getKey());
        answerVerificationRequest.setAnswers(securityAnswers);


        try {
            SecurityQuestionApi securityQuestionApi = new SecurityQuestionApi();
            InitiateQuestionResponse initiateQuestionResponse =
                    securityQuestionApi.validateAnswerPost(answerVerificationRequest, requestHeaders);

            session.setAttribute("confirmationKey", initiateQuestionResponse.getKey());
            request.getRequestDispatcher("password-reset.jsp").forward(request, response);

        } catch (ApiException e) {
            RetryError retryError = IdentityManagementEndpointUtil.buildRetryError(e);
            if (retryError != null && "20008".equals(retryError.getCode())) {
                IdentityManagementEndpointUtil.addReCaptchaHeaders(request, e.getResponseHeaders());
                request.setAttribute("errorResponse", retryError);
                request.getRequestDispatcher("challenge-questions-view-all.jsp").forward(request, response);
                return;
            }

            request.setAttribute("error", true);
            if (retryError != null) {
                request.setAttribute("errorMsg", retryError.getDescription());
                request.setAttribute("errorCode", retryError.getCode());
            }

            request.setAttribute("username", userName);
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
    }


      out.write('\n');
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
