package com.leonino.vneocherediandroid.service;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leonino.vneocherediandroid.forms.LoginForm;
import com.leonino.vneocherediandroid.forms.UserForm;
import com.leonino.vneocherediandroid.models.User;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserService {
    private String httpResponse;
    private final Gson gson;

    public UserService() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    public String login(LoginForm form) {
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), gson.toJson(form));

        getHttpResponse("POST","https://vne-ocheredi.herokuapp.com/loginAndroid", body);

        while (httpResponse == null) {}

        return httpResponse;
    }

    public String register(UserForm form) {
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), gson.toJson(form));

        getHttpResponse("POST","https://vne-ocheredi.herokuapp.com/registerAndroid", body);

        while (httpResponse == null) {}

        return httpResponse;
    }

    public String getUsername(String token) {
        getHttpResponse("GET","https://vne-ocheredi.herokuapp.com/login?token=" + token, null);

        while (httpResponse == null) {}

        return httpResponse;
    }

    public User getUser(String token) {
        getHttpResponse("GET","https://vne-ocheredi.herokuapp.com/user?token=" + token, null);

        while (httpResponse == null) {}

        return new Gson().fromJson(httpResponse, User.class);
    }

    private void getHttpResponse(String method, String url, RequestBody body) {
        httpResponse = null;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method(method, body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            // если запрос неудачный
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("*************");
                System.out.println(e.getMessage());
                System.out.println("*************");
            }

            // В случае успешного запроса
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                httpResponse = response.body().string();
            }
        });
    }
}
