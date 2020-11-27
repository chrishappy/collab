package com.themusicians.musiclms.chat;

import com.themusicians.musiclms.Notifications.MyResponse;
import com.themusicians.musiclms.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json", "Authorization:key=AAAAbblwm6U:APA91bGi8tebk4kln4UYWzeeFVfmyipAbV2_Tk2CpZEdK-kYDKHxqKjdZnbOyrZ0_daXsX1pfvwyLLUfEzndaKTx7w3hIxUn8P6Ow_5jZIh5FJRdECoI1QIokYE5spCifeOzbtfY6XD5"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
