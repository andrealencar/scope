package org.billthefarmer.scope.Dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import org.billthefarmer.scope.models.Post;
import org.billthefarmer.scope.models.User;
import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM post")
    List<User> getAll();

    @Query("SELECT * FROM post WHERE uid IN (:postIds)")
    List<User> loadAllByIds(int[] postIds);

    @Query("SELECT * FROM post WHERE post_title LIKE :first AND " +
            "post_resumo LIKE :last LIMIT 1")
    Post findByTitle(String first, String last);

    @Query("SELECT * FROM post WHERE uid = :post_id")
    Post findById(int post_id);

    @Insert
    void insertUser(Post post);

    @Delete
    void delete(Post post);
}