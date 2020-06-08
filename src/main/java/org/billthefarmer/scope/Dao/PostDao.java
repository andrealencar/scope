package org.billthefarmer.scope.Dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.billthefarmer.scope.models.Post;
import org.billthefarmer.scope.models.User;
import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM post")
    List<Post> getAll();

    @Query("SELECT * FROM post WHERE post_title LIKE :first AND " +
            "post_resumo LIKE :last LIMIT 1")
    Post findByTitle(String first, String last);

    @Query("SELECT * FROM post WHERE id = :post_id")
    Post findById(String post_id);

    @Update()
    void updatePost(Post post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(Post post);

    @Delete
    void delete(Post post);

}