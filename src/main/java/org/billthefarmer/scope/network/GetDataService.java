package org.billthefarmer.scope.network;

import org.billthefarmer.scope.models.StrapiPost;
import org.billthefarmer.scope.models.StrapiQuestion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("posts")
    Call<List<StrapiPost>> ListPosts();

    @GET("questions")
    Call<List<StrapiQuestion>> ListQuestions();

}
