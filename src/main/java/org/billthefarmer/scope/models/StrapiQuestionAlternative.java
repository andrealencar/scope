package org.billthefarmer.scope.models;

import com.google.gson.annotations.SerializedName;


public class StrapiQuestionAlternative {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("question")
    private String question;

    @SerializedName("correct")
    private Boolean correct;

    @SerializedName("active")
    private Boolean active;

    @SerializedName("letra")
    private String letra;


    public StrapiQuestionAlternative(String id,
                                     String title,
                                     String letra,
                                     Boolean active,
                                     Boolean correct) {
        this.id = id;
        this.title = title;
        this.letra = letra;
        this.question = question;
        this.active = active;
        this.correct = correct;
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getLetra() {
        return letra;
    }
    public String getQuestion() {
        return question;
    }
    public Boolean getActive() {
        return active;
    }
    public Boolean getCorrect() {
        return correct;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setLetra(String letra) {
        this.letra = letra;
    }
    public void setQuestion(String question) {
        this.letra = question;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
