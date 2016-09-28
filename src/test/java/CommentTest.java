import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;

public class CommentTest {
  private Comment firstComment;
  private Comment secondComment;

  @Before
  public void initialize() {
    firstComment = new Comment("test content", 1);
    secondComment = new Comment("test contents", 1);
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Comment_instantiatesCorrectly_true() {
    assertEquals(true, firstComment instanceof Comment);
  }


  @Test
  public void getContent_returnsContent_String() {
    assertEquals("test content", firstComment.getContent());
  }

  @Test
  public void getId_returnsId_true() {
    firstComment.save();
    assertTrue(firstComment.getId() > 0);
  }

  @Test
  public void getParentId_returnsParentId_true() {
    firstComment.save();
    assertTrue(firstComment.getParentId() == 1);
  }

  @Test
  public void getDate_returnsDate_true() {
    firstComment.save();
    Timestamp newTime = new Timestamp(new Date().getTime());
    assertEquals(newTime.getDay(), firstComment.getDate().getDay());
  }

  @Test
  public void allComments_returnsAllInstancesOfComment_true() {
    firstComment.save();
    secondComment.save();
    assertTrue(Comment.allComments().get(0).equals((firstComment)));
    assertTrue(Comment.allComments().get(1).equals((secondComment)));
  }

  @Test
  public void find_returnsCommentWithSameId_secondComment() {
    firstComment.save();
    secondComment.save();
    assertEquals(Comment.find(secondComment.getId()), secondComment);
  }

  @Test
  public void equals_returnsTrueIfTitlesAreTheSame() {
    Comment myComment = new Comment("test content", 1);
    assertTrue(firstComment.equals(myComment));
  }

  @Test
  public void save_returnsTrueIfCommentsAreTheSame() {
    firstComment.save();
    assertTrue(Comment.allComments().get(0).equals(firstComment));
  }

  @Test
  public void save_assignsIdToObject() {
    firstComment.save();
    Comment savedComment = Comment.allComments().get(0);
    assertEquals(firstComment.getId(), savedComment.getId());
  }

  @Test
  public void updateContent_updatesContent_true() {
    firstComment.save();
    firstComment.updateContent("content");
    assertEquals("content", Comment.find(firstComment.getId()).getContent());
  }

  @Test
  public void delete_deletesComment_true() {
    firstComment.save();
    int firstCommentId = firstComment.getId();
    firstComment.delete();
    assertEquals(null, Comment.find(firstCommentId));
  }

}
