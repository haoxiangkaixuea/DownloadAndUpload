package cn.edu.scujcc.downloadandupload.http;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Administrator
 */
public class RetrofitClient {
    private static Retrofit INSTANCE = null;

    public static Retrofit getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Retrofit.Builder()
        //Retrofit要求base url必须以"/"结尾
                    .baseUrl("http://172.32.12.243:20527/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTANCE;
    }
}
