package org.billthefarmer.scope.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Post {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    public int uid;

    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "order")
    public Integer order;

    @ColumnInfo(name = "post_title")
    public String title;

    @ColumnInfo(name = "post_resumo")
    public String resumo;

    @ColumnInfo(name = "post_text")
    public String text;

//    @ColumnInfo(name = "post_images")
//    public List<Image> images;

}
