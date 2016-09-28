import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      boolean createNew = false;
      model.put("loggedIn", true);
      model.put("createNew", createNew);
      model.put("users", User.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/user/new", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      boolean createNew = true;
      model.put("createNew", createNew);
      model.put("users", User.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/login", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String username = request.queryParams("loginUsername");
      String password = request.queryParams("loginPassword");
      boolean loggedIn = false;
      for(int i = 0; i < User.all().size(); i++) {
        if(username.equals(User.all().get(i).getUsername()) && password.equals(User.all().get(i).getPassword())) {
          loggedIn = true;
          model.put("user", User.all().get(i));
          model.put("template", "templates/posts.vtl");
        }
      }
      if (!loggedIn) {
        model.put("users", User.all());
        model.put("createNew", false);
        model.put("loggedIn", loggedIn);
        model.put("template", "templates/index.vtl");
      }
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/user/new", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String username = request.queryParams("username");
      String password = request.queryParams("password");
      User newUser = new User(username, password);
      newUser.save();
      response.redirect("/posts");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/posts", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("template", "templates/posts.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
    //
    // get("/", (request, response) -> {
    //   Map<String, Object> model = new HashMap<>();
    //   model.put("template", "templates/index.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // get("/", (request, response) -> {
    //   Map<String, Object> model = new HashMap<>();
    //   model.put("template", "templates/index.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // get("/", (request, response) -> {
    //   Map<String, Object> model = new HashMap<>();
    //   model.put("template", "templates/index.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // get("/", (request, response) -> {
    //   Map<String, Object> model = new HashMap<>();
    //   model.put("template", "templates/index.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
    //
    // get("/", (request, response) -> {
    //   Map<String, Object> model = new HashMap<>();
    //   model.put("template", "templates/index.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
  }
}
