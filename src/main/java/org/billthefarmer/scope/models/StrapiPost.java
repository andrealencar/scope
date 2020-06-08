package org.billthefarmer.scope.models;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StrapiPost {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("images")
    private List<StrapiImage> images;

    public StrapiPost(String id, String title, String text,List<StrapiImage> images) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.images = images;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getText() {
        return text;
    }
    public List<StrapiImage> getImages() {
        return images;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setImages(List<StrapiImage> images) {
        this.images = images;
    }

}
