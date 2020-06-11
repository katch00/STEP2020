// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


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
import com.google.gson.Gson;
import com.google.sps.servlets.Comment;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/comments")
public class DataServlet extends HttpServlet {

  // Gets comments from datastore and puts them into an array list, to be used by getMessage() funtion
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("comment").addSort("timestamp", SortDirection.DESCENDING);
   
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    //List<Entity> results = getComments(); 

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String mssg = (String) entity.getProperty("comment");
      long timestamp = (long) entity.getProperty("timestamp");
      String userEmail = (String) entity.getProperty("user");
      String fullComment = userEmail + ": " + mssg;
      
      Comment comment = new Comment(id, fullComment, timestamp);
      comments.add(comment);
    }

    String json = convertToJson(comments);

    response.setContentType("application/json;");
    response.getWriter().println(json); 
  }

  public List<Entity> getComments() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("comment").addSort("timestamp", SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);
    return results.asList(FetchOptions.Builder.withLimit(5));
  }
  
  public String convertToJson(List<Comment> arr) {
    Gson gson = new Gson();
    String json = gson.toJson(arr);
    return json;
  }

  // Allows users to create comments to be stored in datastore, if they are logged in
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (!userService.isUserLoggedIn()) {
        response.sendRedirect("/login");
        return;
    }
    
    String comment = getParameter(request, "text-input", "");
    long timestamp = System.currentTimeMillis();
    String userName = getUserNickname(userService.getCurrentUser().getUserId());

    Entity mssgEntity = new Entity("comment");
    mssgEntity.setProperty("comment", comment);
    mssgEntity.setProperty("timestamp", timestamp);
    mssgEntity.setProperty("user", userName);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(mssgEntity);

    response.sendRedirect("/index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  private String getUserNickname(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
      new Query("UserInfo")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return null;
    }
    String nickname = (String) entity.getProperty("nickname");
    return nickname;
  }
}
