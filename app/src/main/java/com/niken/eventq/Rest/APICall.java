package com.niken.eventq.Rest;

import com.niken.eventq.Controller.AppConfig;
import com.niken.eventq.Model.Event.EventModel;
import com.niken.eventq.Model.EventParticipant.EventParticipant;
import com.niken.eventq.Model.EventParticipant.EventParticipantModel;
import com.niken.eventq.Model.Region.RegencyModel;
import com.niken.eventq.Model.User.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APICall {

    @POST("login")
    Call<UserModel> postLogin (
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("register")
    Call<UserModel> postRegister (
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password
    );


    @POST("update/{id}")
    @Multipart
    Call<UserModel> postUpdatePhoto (
            @Path("id") String id,
            @Query("name") String name,
            @Query("email") String email,
            @Query("address") String address,
            @Query("gender") String gender,
            @Query("regency_id") String regency_id,
            @Part MultipartBody.Part photo,
            @Part("filename") RequestBody filename
            );

    @POST("update/{id}")
    Call<UserModel> postUpdate (
            @Path("id") String id,
            @Query("name") String name,
            @Query("email") String email,
            @Query("address") String address,
            @Query("gender") String gender,
            @Query("regency_id") String regency_id
    );

    @POST("updatePass/{id}")
    Call<UserModel> postPass(
            @Path("id") String id,
            @Query("oldpassword") String oldpassword,
            @Query("newpassword") String newpassword
    );

    @POST("create_eventpart")
    Call<EventParticipantModel> postEventPart (
            @Query("user_id") String user_id,
            @Query("event_id") String event_id
    );

    @GET("show_event")
    Call<EventModel> getEventAll ();

    @GET("detailuser/{id}")
    Call<UserModel>getDetailUser(
            @Path("id") String id
    );

    @GET("show_latest_event")
    Call<EventModel> getNewEvent ();

    @GET("show_done_event")
    Call<EventModel> getHistory();

    @GET("show_eventpart")
    Call<EventParticipantModel> getEventPart();

    @GET("show_eventpart/{id}")
    Call<EventParticipant> getEventPartby (
            @Path("user_id") String user_id
    );

    @GET("regencies_id")
    Call<RegencyModel> getRegency();

    @GET("search")
    Call<EventModel> getSearch(
            @Query("keyword") String keyword
    );
}
