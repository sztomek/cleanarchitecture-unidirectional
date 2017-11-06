package hu.sztomek.archdemo.data.datasource;

import java.util.Map;

import hu.sztomek.archdemo.data.model.BaseResponse;
import hu.sztomek.archdemo.data.model.PostResponse;
import hu.sztomek.archdemo.data.model.Profile;
import hu.sztomek.archdemo.data.model.Timezone;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestService {

    @PUT("users/{uid}.json")
    Observable<Profile> saveUser(@Body Profile profile, @Path("uid") String uid, @Query("auth") String token);

    @GET("users/{uid}.json")
    Observable<Profile> getUser(@Path("uid") String uid, @Query("auth") String token);

    @POST("timezones/{uid}.json")
    Observable<PostResponse> saveTimezone(@Path("uid") String uid, @Body Timezone timezone, @Query("auth") String token);

    @PUT("timezones/{uid}/{tzid}.json")
    Observable<Timezone> updateTimezone(@Path("uid") String uid, @Path("tzid") String tzid, @Body Timezone timezone, @Query("auth") String token);

    @DELETE("timezones/{uid}/{tz}.json")
    Observable<BaseResponse> deleteTimezone(@Path("uid") String uid, @Path("tz") String tzid, @Query("auth") String token);

    @GET("timezones/{uid}.json")
    Observable<Map<String, Timezone>> getTimezonesForUser(@Path("uid") String uid, @Query("auth") String token, @Query("orderBy") String orderBy, @Query("limitToFirst") int limitToFirst, @Query("startAt") String startAt);

    @GET("timezones/{uid}/{tzid}.json")
    Observable<Timezone> getTimezoneForUser(@Path("uid") String uid, @Path("tzid") String timezoneId, @Query("auth") String token);
}
