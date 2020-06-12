package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * Servlet used to get user vote, also outs votes into json to be used in a graph
 */
@WebServlet("/game-data")
public class GameDataServlet extends HttpServlet {

  private Map<String, Integer> gameVotes = new HashMap<>();

  /**
   * Gets votes and converts them to json formatting
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(gameVotes);
    response.getWriter().println(json);
  }

  /**
   * Takes user input and adds 1 to the game that was chosen to be displayed in the graph
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String game = request.getParameter("game");
    int currentVotes = gameVotes.getOrDefault(game, 0);
    gameVotes.put(game, currentVotes + 1);

    response.sendRedirect("/charts.html");
  }
}
