package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implemention of the Users API for loggin in and setting a nickname
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  
  /**
   * Checks if user is logged in. If not, redirects user to log in. If yes, checks if user has a nickname
   * If user has no nickname, redirects uder to set nickname
   * Also allows user to loggout if they are logged in and have a nickname set
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      // User has no nickname
      String nickname = GetNickname.getUserNickname(userService.getCurrentUser().getUserId());
      if (nickname == null) {
        response.sendRedirect("/nickname");
        return;
      } 
      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/index.html";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
      response.getWriter().println("<link rel=\"stylesheet\" href=\"style.css\">");
      response.getWriter().println("<body bgcolor=\"#66819c\">");
      response.getWriter().println("<p>Hello " + userEmail + "!</p>");
      response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
      response.getWriter().println("</div></body>");
      response.getWriter().println("</body>");
    } else {
    String urlToRedirectToAfterUserLogsIn = "/login";
    String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
    response.getWriter().println("<link rel=\"stylesheet\" href=\"style.css\">");
    response.getWriter().println("<body bgcolor=\"#66819c\">");
    response.getWriter().println("<p>Hello stranger.</p>");
    response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
    }

  }
}
