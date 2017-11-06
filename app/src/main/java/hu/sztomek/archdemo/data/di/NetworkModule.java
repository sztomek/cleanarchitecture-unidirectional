package hu.sztomek.archdemo.data.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class NetworkModule {

    public static final String NAMED_BASEURL = "baseUrl";

    @Singleton
    @Named(NAMED_BASEURL)
    @Provides
    String provideBaseUrl() {
        return "https://XXX.firebaseio.com";
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor.Logger provideHttpLogger() {
        return message -> Timber.d(message);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor.Logger logger) {
        return new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(logger))
                .build();
    }

    @Singleton
    @Provides
    Converter.Factory provideGsonConverter() {
        return GsonConverterFactory.create();
    }

    @Singleton
    @Provides
    CallAdapter.Factory provideRxJava2CallFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Converter.Factory converter, CallAdapter.Factory callAdapter, OkHttpClient client, @Named(NAMED_BASEURL) String baseUrl) {
        return new Retrofit.Builder()
                .addConverterFactory(converter)
                .addCallAdapterFactory(callAdapter)
                .client(client)
                .baseUrl(baseUrl)
                .build();
    }

}
