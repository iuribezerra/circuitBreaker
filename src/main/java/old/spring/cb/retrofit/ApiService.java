package old.spring.cb.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("status/{code}")
    Call<ResponseBody> getStatus(@Path("code") int code);

    @GET("delay/{delay}")
    Call<ResponseBody> getDelay(@Path("delay") int delay);
}
