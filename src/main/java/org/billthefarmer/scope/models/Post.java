package org.billthefarmer.scope.models;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity
public class Post {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "post_remote_id")
    public int id;

    @ColumnInfo(name = "post_title")
    public String title;

    @ColumnInfo(name = "post_resumo")
    public String resumo;

    @ColumnInfo(name = "post_text")
    public String text;

//    @ColumnInfo(name = "post_images")
//    public List<Image> images;


}
