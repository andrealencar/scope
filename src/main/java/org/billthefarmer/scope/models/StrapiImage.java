package org.billthefarmer.scope.models;
import com.google.gson.annotations.SerializedName;

public class StrapiImage {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String name;

    @SerializedName("alternativeText")
    private String alternativeText;

    @SerializedName("url")
    private String url;

    public StrapiImage(String id, String title, String alternativeText,String url) {
        this.id = id;
        this.name = title;
        this.alternativeText = alternativeText;
        this.url = url;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAlternativeText() {
        return alternativeText;
    }
    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.name = name;
    }
    public void setUrl(String url) {
        this.alternativeText = url;
    }

}
