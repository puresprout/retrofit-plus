package com.purestation.retrofitplusapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.purestation.retrofitplus.ErrorPolicyCallback;
import com.purestation.retrofitplus.error.ErrorPolicy;

import java.io.IOException;
import java.util.Date;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    class GreetingResponse {
        public String name;
        public String greeting;
    }

    interface GreetingApi {
        @GET("greeting")
        Call<GreetingResponse> getGreeting();
    }

    MockWebServer mockWebServer;
    Retrofit retrofit;
    GreetingApi api;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mockWebServer = new MockWebServer();

                try {
                    mockWebServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                retrofit = new Retrofit.Builder().baseUrl(mockWebServer.url("/")).addConverterFactory(GsonConverterFactory.create()).build();

                api = retrofit.create(GreetingApi.class);
            }
        }).start();

        ((Button) findViewById(R.id.callButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{\"name\": \"SungHyun Park\", \"greeting\": \"Hello!\"}"));

                requestApi(api);
            }
        });

        ((Button) findViewById(R.id.errorButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // json 형태로 에러 모델을 정의하고 ErrorPolicyCallback.getErrorMessageFrom() 메소드에서 json을 파싱하도록 재정의해도 된다.
                mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("Server Error!"));

                requestApi(api);
            }
        });

        textView = (TextView) findViewById(R.id.greetingTextView);
    }

    private void requestApi(GreetingApi api) {
        //textView.setText("");

        api.getGreeting().enqueue(new ErrorPolicyCallback<GreetingResponse>(
                ErrorPolicy.createDialog(MainActivity.this),
                ErrorPolicy.createDialog(MainActivity.this)
        ) {
            @Override
            public void onSuccessfulStatus(Call<GreetingResponse> call, GreetingResponse response) {
                String greeting = response.greeting + " " + response.name + " at " + new Date();

                textView.setText(greeting);
            }
        });
    }
}
