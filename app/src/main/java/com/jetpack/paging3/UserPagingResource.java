package com.jetpack.paging3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import com.google.common.base.Function;

public class UserPagingResource extends ListenableFuturePagingSource<Integer,User> {
    /**
     * 需要用到线程池
     */
    private ListeningExecutorService executorService= MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, User> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, User>> loadFuture(@NonNull LoadParams<Integer> loadParams) {
        Integer nextIndex = loadParams.getKey();
        if(nextIndex == null){
            nextIndex = 0;
        }
        Integer finalNextIndex = nextIndex;
        ListenableFuture<LoadResult<Integer,User>> pageFuture = Futures
                .transform(executorService.submit(new Callable<List<User>>() {
                    @Override
                    public List<User> call() throws Exception {
                        return UserResponse.getUsers(finalNextIndex,10);
                    }
                }),new Function<List<User>,LoadResult.Page<Integer,User>>(){
                    @Override
                    public LoadResult.Page<Integer, User> apply(List<User> input) {
                        //这里传入的三个参数中,刚才请求的数据,第二个参数为请求的上一页的页数,当为null时不再加载上一页,第三个参数则是下一页,后两个参数不介绍,自行了解
                        return new LoadResult.Page<>(input,finalNextIndex==0?null:finalNextIndex-1,input.isEmpty()?null:finalNextIndex+1);
                    }
                },executorService);

        ListenableFuture<LoadResult<Integer,User>> partialLoadResultFuture = Futures.catching(
                pageFuture,
                Exception.class,
                LoadResult.Error::new,
                executorService);

        return Futures.catching(partialLoadResultFuture,
                Exception.class, LoadResult.Error::new, executorService);
    }
}
