import org.sql2o.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Post extends Comment{
  private String title;

  public Post(String title, String content, int parentId) {
    super(content, parentId);
    this.title = title;
    this.date = new Timestamp(new Date().getTime());
  }

  public String getTitle() {
    return this.title;
  }

  public List<Tag> getTags() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT tagid FROM posts_tags WHERE postid = :postId";
      List<Integer> tagIds = con.createQuery(sql)
        .addParameter("postId", this.id)
        .executeAndFetch(Integer.class);

      List<Tag> tags = new ArrayList<Tag>();
      for (Integer tagId : tagIds) {
        String sqlQuery = "SELECT * FROM tags WHERE id = :tagId";
        Tag tag = con.createQuery(sqlQuery)
          .addParameter("tagId", tagId)
          .executeAndFetchFirst(Tag.class);
          tags.add(tag);
      }
      return tags;
    }
  }

  public List<Comment> getComments() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM comments WHERE parentid = :id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Comment.class);
    }
  }

  public void updateTitle(String title) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE posts SET title = :title, date = now() WHERE id = :id";
      con.createQuery(sql)
        .addParameter("title", title)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  @Override
  public void updateContent(String content) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE posts SET content = :content, date = now() WHERE id = :id";
      con.createQuery(sql)
        .addParameter("content", content)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  @Override
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO posts (title, content, date, parentId) VALUES (:title, :content, now(), :parentId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", this.title)
        .addParameter("content", this.content)
        .addParameter("parentId", this.parentId)
        .executeUpdate()
        .getKey();
    }
  }

  public void addTag (int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO posts_tags (postId, tagId) VALUES(:postId, :tagId)";
      con.createQuery(sql)
        .addParameter("postId", this.id)
        .addParameter("tagId", id)
        .executeUpdate();
    }
  }

  public static List<Post> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM posts";
      return con.createQuery(sql).executeAndFetch(Post.class);
    }
  }

  public static Post findPosts(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM posts WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Post.class);
    }
  }

  @Override
  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql1 = "DELETE FROM posts WHERE id = :id";
      con.createQuery(sql1)
        .addParameter("id", this.id)
        .executeUpdate();
      String sql2 = "DELETE FROM posts_tags WHERE postid = :id";
      con.createQuery(sql2)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  @Override
  public boolean equals(Object otherPost) {
    if(!(otherPost instanceof Post)) {
      return false;
    } else {
      Post newPost = (Post) otherPost;
      return this.getContent().equals(newPost.getContent()) &&
      this.getDate().getDay() == newPost.getDate().getDay() &&
      this.getTitle().equals(newPost.getTitle()) &&
      this.getParentId()  == newPost.getParentId();
    }
  }
}
