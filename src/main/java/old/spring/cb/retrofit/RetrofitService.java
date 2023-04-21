package old.spring.cb.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import old.spring.cb.circuitBreaker.CircuitBreakerInterceptor;
import old.spring.cb.circuitBreaker.CustomCircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class RetrofitService {

    @Autowired
    private CustomCircuitBreaker customCircuitBreaker;

    public ResponseEntity<?> callStatus(int code) throws IOException {
        ApiService service = configRetro();
        // Execução da requisição GET com Retrofit
        Call<ResponseBody> call = service.getStatus(code);
        Response<ResponseBody> response = call.execute();

        return handleResponse(response);
    }

    public ResponseEntity<?> callDelay(int delay) throws IOException {
        ApiService service = configRetro();
        // Execução da requisição GET com Retrofit
        Call<ResponseBody> call = service.getDelay(delay);
        Response<ResponseBody> response = call.execute();

        return handleResponse(response);
    }

    private ApiService configRetro(){
        // Configuração do cliente HTTP com OkHttp
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new CircuitBreakerInterceptor(customCircuitBreaker.getCircuit()))
                .readTimeout(2, TimeUnit.SECONDS)
                .build();

        // Criação do objeto Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:3000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Criação da interface de serviço
        return retrofit.create(ApiService.class);
    }

    private ResponseEntity<?> handleResponse(Response<ResponseBody> response) throws IOException {
        if (response.isSuccessful()){
            return ResponseEntity.status(response.code()).body(response.body().string());
        }

        return ResponseEntity.status(response.code()).body(response.errorBody().string());
    }

}
