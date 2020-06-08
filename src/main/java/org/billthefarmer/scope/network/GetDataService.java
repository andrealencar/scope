package org.billthefarmer.scope.network;
import org.billthefarmer.scope.models.Alternative;
import org.billthefarmer.scope.models.Post;
import org.billthefarmer.scope.models.StrapiPost;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("posts")
    Call<List<StrapiPost>> ListPosts();

    @GET("questions")
    Call<List<Alternative>> ListQuestions();

}
