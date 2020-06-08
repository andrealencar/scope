package org.billthefarmer.scope.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class StrapiQuestion {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("active")
    private Boolean active;

    @SerializedName("alternatives")
    private List<StrapiQuestionAlternative> alternatives;

    public StrapiQuestion(String id, String title, Boolean active, List<StrapiQuestionAlternative> alternatives) {
        this.id = id;
        this.title = title;
        this.active = active;
        this.alternatives = alternatives;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public boolean getActive() {
        return active;
    }
    public List<StrapiQuestionAlternative> getAlternatives() {
        return alternatives;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setAlternatives(List<StrapiQuestionAlternative> alternatives) {
        this.alternatives = alternatives;
    }

}
