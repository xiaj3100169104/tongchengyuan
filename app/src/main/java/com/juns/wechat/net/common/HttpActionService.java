package com.juns.wechat.net.common;

import com.juns.wechat.net.response.LoginBean;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by xiajun on 2017/3/25.
 */


public interface HttpActionService {

    @POST("addUser") @FormUrlEncoded
    Call<String> register(@Field("userName")  String userName, @Field("passWord") String password);

    @POST("login") @FormUrlEncoded
    Call<String> login(@Field("userName")  String userName, @Field("passWord") String password);

    @POST("newToken") @FormUrlEncoded
    Call<String> refreshToken(@Field("token")  String oldToken);

    @POST("updateUser") @FormUrlEncoded
    Call<String> updateUser(@FieldMap Map<String, Object> map);

    @POST("uploadAvatar")
    Call<String> uploadAvatar(@Body MultipartBody requestBody);

    @POST("searchUser") @FormUrlEncoded
    Call<String> searchUser(@FieldMap Map<String, Object> map);

    @POST("syncUserData") @FormUrlEncoded
    Call<String> syncUserData(@FieldMap Map<String, Object> map);

    @POST("queryUser") @FormUrlEncoded
    Call<String> queryUserData(@FieldMap Map<String, Object> map);

    @POST("queryUser") @FormUrlEncoded
    Call<String> queryPhone(@FieldMap Map<String, Object> map);

    @POST("getUsersByNames") @FormUrlEncoded
    Call<String> getUsersByNames(@FieldMap Map<String, Object> map);

    @POST("syncFriendData") @FormUrlEncoded
    Call<String> syncFriendData(@FieldMap Map<String, Object> map);

    @POST("addFriend") @FormUrlEncoded
    Call<String> addFriend(@FieldMap Map<String, Object> map);

    @POST("uploadAvatar") @FormUrlEncoded
    Call<String> uploadAvatar(@FieldMap Map<String, Object> map);

    @POST("addDynamic")
    Call<String> addDynamic(@Body RequestBody requestBody);

    @POST("addComment") @FormUrlEncoded
    Call<String> addComment2Dynamic(@FieldMap Map<String, Object> map);

    @POST("getDynamicsByPage") @FormUrlEncoded
    Call<String> getFriendCircleDynamic(@FieldMap Map<String, Object> map);



}
