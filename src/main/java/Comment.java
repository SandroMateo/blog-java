import org.sql2o.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Comment {
  public String content;
  public Timestamp date;
  public int id;
  public int parentId;

  public Comment (String content, int parentId) {
    this.content = content;
    this.parentId = parentId;
    this.date = new Timestamp(new Date().getTime());
  }

  public String getContent() {
    return this.content;
  }

  public String getDate() {
    return DateFormat.getDateTimeInstance().format(this.date);
  }

  public int getId() {
    return this.id;
  }

  public int getParentId() {
    return this.parentId;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO comments (content, date, parentId) VALUES (:content, now(), :parentId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("content", this.content)
        .addParameter("parentId", this.parentId)
        .executeUpdate()
        .getKey();
    }
  }

  public static Comment find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM comments WHERE id = :id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Comment.class);
    }
  }

  public static List<Comment> allComments() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM comments";
      return con.createQuery(sql).executeAndFetch(Comment.class);
    }
  }

  public void updateContent(String content) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE comments SET content = :content, date = now() WHERE id = :id";
      con.createQuery(sql)
        .addParameter("content", content)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM comments WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  @Override
  public boolean equals(Object otherComment) {
    if(!(otherComment instanceof Comment)) {
      return false;
    } else {
      Comment newComment = (Comment) otherComment;
      return this.getContent().equals(newComment.getContent()) &&
             this.getDate() == newComment.getDate() &&
             this.getParentId()  == newComment.getParentId() &&
             this.getId() == newComment.getId();
    }
  }
}
