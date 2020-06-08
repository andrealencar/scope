package org.billthefarmer.scope.models;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Image {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "post_remote_id")
    public int id;

    @ColumnInfo(name = "image_name")
    public String name;

    @ColumnInfo(name = "image_alternativeText")
    public String alternativeText;

    @ColumnInfo(name = "image_caption")
    public String caption;

    @ColumnInfo(name = "image_hash")
    public String hash;

    @ColumnInfo(name = "image_ext")
    public String ext;

    @ColumnInfo(name = "image_mime")
    public String mime;

    @ColumnInfo(name = "image_url")
    public String url;

    public Image(Integer uid,
                 Integer remote_id,
                 String name,
                 String alternativeText,
                 String caption,
                 String hash,
                 String ext,
                 String mime,
                 String url) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.alternativeText = alternativeText;
        this.caption = caption;
        this.hash = hash;
        this.caption = caption;
        this.caption = ext;
        this.caption = mime;
        this.caption = url;
    }

}
