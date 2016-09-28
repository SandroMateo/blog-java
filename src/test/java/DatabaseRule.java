import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource {

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/blog_test", null, null);
  }

  @Override
  protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deletePostsQuery = "DELETE FROM posts *;";
      String deleteCommentsQuery = "DELETE FROM comments *;";
      String deleteUsersQuery = "DELETE FROM users *;";
      String deleteTagsQuery = "DELETE FROM tags *;";
      String deletePostsTagsQuery = "DELETE FROM posts_tags *;";
      con.createQuery(deletePostsQuery).executeUpdate();
      con.createQuery(deleteCommentsQuery).executeUpdate();
      con.createQuery(deleteUsersQuery).executeUpdate();
      con.createQuery(deleteTagsQuery).executeUpdate();
      con.createQuery(deletePostsTagsQuery).executeUpdate();
    }
 }

}
