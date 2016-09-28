import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class UserTest {
  private User firstUser;
  private User secondUser;

  @Before
  public void initialize() {
    firstUser = new User("sandromateo", "qwerty");
    secondUser = new User("joannaAndErson", "asdfg");
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void User_instantiatesCorrectly_true() {
    assertEquals(true, firstUser instanceof User);
  }

  @Test
  public void getUsername_returnsUsername_String() {
    assertEquals("sandromateo", firstUser.getUsername());
  }

  @Test
  public void getPassword_returnsPassword_String() {
    assertEquals("qwerty", firstUser.getPassword());
  }

  @Test
  public void getId_returnsId_true() {
    firstUser.save();
    assertTrue(firstUser.getId() > 0);
  }

  @Test
  public void all_returnsAllInstancesOfUser_true() {
    firstUser.save();
    secondUser.save();
    assertTrue(User.all().get(0).equals(firstUser));
    assertTrue(User.all().get(1).equals(secondUser));
  }

  @Test
  public void find_returnsUserWithSameId_secondUser() {
    firstUser.save();
    secondUser.save();
    assertEquals(User.find(secondUser.getId()), secondUser);
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    User myUser = new User("sandromateo", "qwerty");
    assertTrue(firstUser.equals(myUser));
  }

  @Test
  public void save_returnsTrueIfNamesAreTheSame() {
    firstUser.save();
    assertTrue(User.all().get(0).equals(firstUser));
  }

  @Test
  public void save_assignsIdToObject() {
    firstUser.save();
    User savedUser = User.all().get(0);
    assertEquals(firstUser.getId(), savedUser.getId());
  }

  @Test
  public void updateUsername_updatesUsername_true() {
    firstUser.save();
    firstUser.updateUsername("mateo");
    assertEquals("mateo", User.find(firstUser.getId()).getUsername());
  }

  @Test
  public void updatePassword_updatesPassword_true() {
    firstUser.save();
    firstUser.updatePassword("asdf");
    assertEquals("asdf", User.find(firstUser.getId()).getPassword());
  }

  @Test
  public void delete_deletesUser_true() {
    firstUser.save();
    int firstUserId = firstUser.getId();
    firstUser.delete();
    assertEquals(null, User.find(firstUserId));
  }

  @Test
  public void getPosts_returnsAllPosts_true() {
    firstUser.save();
    Post post1 = new Post("test", "test content", firstUser.getId());
    post1.save();
    Post post2 = new Post("test", "test content", firstUser.getId());
    post2.save();
    assertTrue(firstUser.getPosts().get(0).equals(post1));
    assertTrue(firstUser.getPosts().get(1).equals(post2));
  }
}
