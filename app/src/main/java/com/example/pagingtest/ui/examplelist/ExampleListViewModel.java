package com.example.pagingtest.ui.examplelist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.lifecycle.AndroidViewModel;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.pagingtest.clients.UsersClient;
import com.example.pagingtest.db.ExampleDb;
import com.example.pagingtest.db.UserEntity;
import com.example.pagingtest.util.GsonFactory;
import com.google.gson.Gson;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@OptIn(markerClass = ExperimentalPagingApi.class)
public class ExampleListViewModel extends AndroidViewModel {

    private Flowable<PagingData<UserEntity>> items;

    public ExampleListViewModel(@NonNull Application application) {
        super(application);
    }

    public Flowable<PagingData<UserEntity>> loadItems() {

        if (items == null) {
            initializeItems();
        }

        return items;
    }

    private synchronized void initializeItems() {

        if (items == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            Gson gson = GsonFactory.createCommonGson();

            PagingConfig pagingConfig = new PagingConfig(ExampleListRemoteMediator.PAGE_SIZE,
                    ExampleListRemoteMediator.PREFETCH_DISTANCE, true);

            UsersClient usersClient = new Retrofit.Builder()
                    .baseUrl("http://jsonplaceholder.typicode.com/")
                    .client(httpClient.build())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(UsersClient.class);

            ExampleDb exampleDb = ExampleDb.getInstance(getApplication()
                    .getApplicationContext());

            Pager<Integer, UserEntity> pager = new Pager<>(pagingConfig, 0,
                    new ExampleListRemoteMediator(exampleDb, usersClient),
                    () -> exampleDb.userDao().selectAll());

            this.items = PagingRx.getFlowable(pager);
        }
    }
}
