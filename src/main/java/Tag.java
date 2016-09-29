import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Tag{
  private String type;
  private int id;

  public Tag(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public int getId() {
    return this.id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO tags (type) VALUES (:type)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("type", this.type)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Tag> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tags";
      return con.createQuery(sql).executeAndFetch(Tag.class);
    }
  }

  public static Tag find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tags WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Tag.class);
    }
  }

  public void updateType(String type) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tags SET type = :type WHERE id = :id";
      con.createQuery(sql)
        .addParameter("type", type)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql1 = "DELETE FROM tags WHERE id = :id";
      con.createQuery(sql1)
        .addParameter("id", this.id)
        .executeUpdate();
      String sql2 = "DELETE FROM posts_tags WHERE tagid = :id";
      con.createQuery(sql2)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public List<Post> getPosts() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT postid FROM posts_tags WHERE tagid = :id";
      List<Integer> postIds = new ArrayList<Integer>();
      postIds = con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Integer.class);
      List<Post> posts = new ArrayList<Post>();
      String postQuery = "SELECT * FROM posts WHERE id = :postId";
      for (Integer postId : postIds ) {
        Post post = con.createQuery(postQuery)
          .addParameter("postId", postId)
          .executeAndFetchFirst(Post.class);
        posts.add(post);
      }
      return posts;
    }
  }

  @Override
  public boolean equals(Object otherTag) {
    if(!(otherTag instanceof Tag)) {
      return false;
    } else {
      Tag newTag = (Tag) otherTag;
      return this.getType().equals(newTag.getType()) &&
             this.getId() == newTag.getId();
    }
  }
}
