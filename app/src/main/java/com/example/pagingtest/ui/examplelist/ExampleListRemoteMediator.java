package com.example.pagingtest.ui.examplelist;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.paging.ExperimentalPagingApi;
import androidx.paging.LoadType;
import androidx.paging.PagingState;
import androidx.paging.RemoteMediator;
import androidx.paging.rxjava3.RxRemoteMediator;

import com.example.pagingtest.clients.UsersClient;
import com.example.pagingtest.db.ExampleDb;
import com.example.pagingtest.db.UserEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;

@OptIn(markerClass = ExperimentalPagingApi.class)
public class ExampleListRemoteMediator extends RxRemoteMediator<Integer, UserEntity> {

    public static final int PAGE_SIZE = 20;
    public static final int PREFETCH_DISTANCE = 5;
    private static final int INVALID_PAGE = -1;

    private final ExampleDb database;
    private final UsersClient usersClient;

    public ExampleListRemoteMediator(ExampleDb database,
                                     UsersClient usersClient) {
        this.database = database;
        this.usersClient = usersClient;
    }

    @NonNull
    @Override
    public Single<RemoteMediator.InitializeAction> initializeSingle() {
        return Single.just(InitializeAction.LAUNCH_INITIAL_REFRESH);
    }

    @NonNull
    @Override
    public Single<RemoteMediator.MediatorResult> loadSingle(@NonNull LoadType loadType,
                                                            @NonNull PagingState<Integer, UserEntity> pagingState) {

        Log.w("TMP", "loadSingle: " + loadType);
        int nextKey = INVALID_PAGE;
        switch (loadType) {

            case REFRESH:
                nextKey = Optional.ofNullable(pagingState.getAnchorPosition())
                        .map(pagingState::closestItemToPosition)
                        .filter(Objects::nonNull)
                        .map(UserEntity::getPagingPrevKey)
                        .filter(Objects::nonNull)
                        .orElse(0);
                break;
            case PREPEND:
                nextKey = pagingState.getPages().stream().findFirst()
                        .filter(Objects::nonNull)
                        .filter(p -> !p.getData().isEmpty())
                        .flatMap(p -> p.getData().stream().findFirst())
                        .map(UserEntity::getPagingPrevKey)
                        .orElse(INVALID_PAGE);
                break;
            case APPEND:
                nextKey = Optional.of(pagingState.getPages())
                        .filter(p -> !p.isEmpty())
                        .map(p -> p.get(p.size() - 1))
                        .filter(Objects::nonNull)
                        .filter(p -> !p.getData().isEmpty())
                        .flatMap(p -> Optional.of(p.getData()).filter(d -> !d.isEmpty()).map(d -> d.get(d.size() - 1)))
                        .map(UserEntity::getPagingNextKey)
                        .orElse(INVALID_PAGE);
                break;
        }

        if (nextKey == INVALID_PAGE) {
            return Single.just(new RemoteMediator.MediatorResult.Success(true));
        }

        int finalNextKey = nextKey;
        return this.usersClient.getUsers(Math.max(finalNextKey * PAGE_SIZE, 0), PAGE_SIZE)
                .map(userDtos -> userDtos.stream().map(userDto -> {
                    UserEntity entity = new UserEntity();
                    entity.setId(userDto.getId());
                    entity.setUser(userDto);
                    return entity;
                }).collect(Collectors.toList()))
                .map(entityDtos -> insertToDb(finalNextKey, loadType, entityDtos))
                .map(e -> (RemoteMediator.MediatorResult) new RemoteMediator.MediatorResult.Success(isEndOfPage(e)))
                .onErrorReturn(RemoteMediator.MediatorResult.Error::new);
    }

    private List<UserEntity> insertToDb(Integer pageKey, LoadType loadType,
                                        List<UserEntity> entities) {

        database.runInTransaction(() -> {
            if (loadType == LoadType.REFRESH) {
                database.userDao().clearAll();
            }

            Integer prevKey = (pageKey <= 0) ? null : pageKey - 1;
            Integer nextKey = (isEndOfPage(entities)) ? null : pageKey + 1;

            entities.forEach(e -> {
                e.setPagingPrevKey(prevKey);
                e.setPagingNextKey(nextKey);
            });

            database.userDao().insertAll(entities);
        });

        return entities;
    }

    private boolean isEndOfPage(List<UserEntity> entities) {
        return entities.isEmpty();
    }
}
