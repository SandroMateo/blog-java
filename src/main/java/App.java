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
      model.put("loggedIn", true);
      model.put("createNew", false);
      model.put("users", User.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/user/new", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("createNew", true);
      model.put("users", User.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/login", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String username = request.queryParams("loginUsername");
      String password = request.queryParams("loginPassword");
      if(User.isLoggedIn(username, password)) {
        model.put("user", User.login(username, password));
        request.session().attribute("currentUser", User.login(username, password));
        model.put("loggedIn", User.loggedIn);
        model.put("template", "templates/posts.vtl");
      } else {
        model.put("users", User.all());
        model.put("loggedIn", false);
        model.put("createNew", false);
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
      model.put("user", newUser);
      model.put("loggedIn", User.loggedIn);
      model.put("template", "templates/posts.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/user/:id/post/new", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      User user = User.find(Integer.parseInt(request.params("id")));
      model.put("user",user);
      model.put("loggedIn", User.loggedIn);
      model.put("template", "templates/posts.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/users", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      model.put("users", User.all());
      model.put("template", "templates/users.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/user/:id/posts", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      User user = User.find(Integer.parseInt(request.params("id")));
      model.put("loggedIn", User.loggedIn);
      model.put("user", user);
      model.put("template", "templates/posts.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


    post("/user/:id/post/new", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String title = request.queryParams("title");
      String content = request.queryParams("content");
      User theUser = User.find(Integer.parseInt(request.params(":id")));
      Post thePost = new Post(title, content, theUser.getId());
      thePost.save();
      model.put("user",theUser);
      model.put("post",thePost);
      model.put("loggedIn", User.loggedIn);
      model.put("tags", Tag.all());
      model.put("template", "templates/entry.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/user/:userId/post/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      User user = User.find(Integer.parseInt(request.params("userId")));
      Post post = Post.findPosts(Integer.parseInt(request.params(":id")));
      model.put("user", user);
      model.put("post", post);
      model.put("loggedIn", User.loggedIn);
      model.put("tags", Tag.all());
      model.put("template", "templates/entry.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tags/new", (request, response) -> {
      Map<String, Object> model = new HashMap<>();
      String type = request.queryParams("type");
      Tag tag = new Tag(type);
      User currentUser = request.session().attribute("currentUser");
      model.put("user", currentUser);
      model.put("template", "templates/entry.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // get("/", (request, response) -> {
    //   Map<String, Object> model = new HashMap<>();
    //   model.put("template", "templates/index.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());
  }
}
