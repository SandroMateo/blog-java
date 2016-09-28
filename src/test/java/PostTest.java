import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class PostTest {
  private Post firstPost;
  private Post secondPost;

  @Before
  public void initialize() {
    firstPost = new Post("test title", "test content", 1);
    secondPost = new Post("test titles", "test contents", 1);
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Post_instantiatesCorrectly_true() {
    assertEquals(true, firstPost instanceof Post);
  }

  @Test
  public void getTitle_returnsTitle_String() {
    assertEquals("test title", firstPost.getTitle());
  }

  @Test
  public void getContent_returnsContent_String() {
    assertEquals("test content", firstPost.getContent());
  }

  @Test
  public void getId_returnsId_true() {
    firstPost.save();
    assertTrue(firstPost.getId() > 0);
  }

  @Test
  public void all_returnsAllInstancesOfPost_true() {
    firstPost.save();
    secondPost.save();
    assertTrue(Post.all().get(0).equals((firstPost)));
    assertTrue(Post.all().get(1).equals((secondPost)));
  }

  @Test
  public void findPosts_returnsPostWithSameId_secondPost() {
    firstPost.save();
    secondPost.save();
    assertEquals(Post.findPosts(secondPost.getId()), secondPost);
  }

  @Test
  public void equals_returnsTrueIfTitlesAreTheSame() {
    Post myPost = new Post("test title", "test content", 1);
    assertTrue(firstPost.equals(myPost));
  }

  @Test
  public void save_returnsTrueIfPostsAreTheSame() {
    firstPost.save();
    assertTrue(Post.all().get(0).equals(firstPost));
  }

  @Test
  public void save_assignsIdToObject() {
    firstPost.save();
    Post savedPost = Post.all().get(0);
    assertEquals(firstPost.getId(), savedPost.getId());
  }

  @Test
  public void updateTitle_updatesTitle_true() {
    firstPost.save();
    firstPost.updateTitle("Title");
    assertEquals("Title", Post.findPosts(firstPost.getId()).getTitle());
  }

  @Test
  public void updateContent_updatesContent_true() {
    firstPost.save();
    firstPost.updateContent("content");
    assertEquals("content", Post.findPosts(firstPost.getId()).getContent());
  }

  @Test
  public void delete_deletesPost_true() {
    firstPost.save();
    int firstPostId = firstPost.getId();
    firstPost.delete();
    assertEquals(null, Post.findPosts(firstPostId));
  }

  @Test
  public void getComments_returnsAllComments_true() {
    firstPost.save();
    Comment comment1 = new Comment("test content", firstPost.getId());
    comment1.save();
    Comment comment2 = new Comment("test content", firstPost.getId());
    comment2.save();
    assertTrue(firstPost.getComments().get(0).equals(comment1));
    assertTrue(firstPost.getComments().get(1).equals(comment2));
  }
}
