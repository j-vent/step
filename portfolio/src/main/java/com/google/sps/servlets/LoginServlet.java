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

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Status;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{

    private final UserService userService = UserServiceFactory.getUserService();
    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    private static final String URL_TO_REDIRECT_TO_AFTER_LOGIN = "/nickname";
    private static final String URL_TO_REDIRECT_TO_AFTER_LOGOUT = "/index.html";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
      boolean isLoggedIn;
      String  url = "";

      if(!userService.isUserLoggedIn()){
        isLoggedIn = false;
        url = userService.createLoginURL(URL_TO_REDIRECT_TO_AFTER_LOGIN);
      }
      else{
        isLoggedIn =true;
        url = userService.createLogoutURL(URL_TO_REDIRECT_TO_AFTER_LOGOUT);
      }
      Status status = new Status(userService.isUserLoggedIn(),url);
      Gson gson = new Gson();
      response.setContentType("application/json;");
      response.getWriter().println(gson.toJson(status));
    }

     /** Returns the nickname of the user with id, or null if the user has not set a nickname. */
    private String getUserNickname(String id) {
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


