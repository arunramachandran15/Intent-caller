package com.telyutils.retro.interfaces;

import com.telyutils.retro.content.GeneralResponse;
import com.telyutils.retro.content.ReceiveCall;
import com.telyutils.retro.content.StandardCallback;
import com.telyutils.retro.content.StartCall;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ankit on 7/4/2016.
 */
public interface YCallerService {

    String BASE_URL = " http://52.41.160.7:3000/";

    @POST("startcall")
    Call<GeneralResponse> startCall(@Body StartCall startCall);

    @POST("recievecall")
    Call<GeneralResponse> receiveCall(@Body ReceiveCall receiveCall);


    @GET("countdown")
    Call<StandardCallback> getCountDownTime();
}
