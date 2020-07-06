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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns comments and updates the datastore with comments*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  UserService userService = UserServiceFactory.getUserService();
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int numComments = Integer.parseInt(request.getParameter("numComments"));
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING); // sort by time posted

    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments= new ArrayList<>();
    
    for (Entity entity: results.asIterable()){
        if(comments.size() < numComments){
          long id = entity.getKey().getId();
          String text = (String) entity.getProperty("text");
          long timestamp = (long) entity.getProperty("timestamp");
          String email = (String) entity.getProperty("email");
          String nickname= (String) entity.getProperty("nickname");
          Comment comment = new Comment(id,text,timestamp,email, nickname);
          comments.add(comment);
        }
        else{
            break;
        } 
    }
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = request.getParameter("text");
    long timestamp = System.currentTimeMillis();
    String email = userService.getCurrentUser().getEmail();
    String nickname = getUserNickname(userService.getCurrentUser().getUserId());
 

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text",text);
    commentEntity.setProperty("timestamp", timestamp);
    commentEntity.setProperty("email",email);
    commentEntity.setProperty("nickname",nickname);

    datastore.put(commentEntity);
    response.sendRedirect("/index.html"); 
  }

  /** Returns the nickname of the user with id, or null if the user has not set a nickname. */
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
