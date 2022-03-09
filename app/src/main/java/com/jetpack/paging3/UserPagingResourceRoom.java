package com.jetpack.paging3;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class UserPagingResourceRoom extends ListenableFuturePagingSource<Integer,User> {
    private static final int PAGE_SIZE = 10;
    /**
     * 需要用到线程池
     */
    private ListeningExecutorService executorService;
    {
        try{
            int count = Runtime.getRuntime().availableProcessors();
            executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(count));
        }catch (Exception e){
            executorService = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        }
        Log.d("debug_log","UserPagingResourceRoom-----executorService is inited-----");
    }
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, User> pagingState) {
        return null;
    }
    Integer finalNextIndex = 0;
    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, User>> loadFuture(@NonNull LoadParams<Integer> loadParams) {
        UserDao dao = UserDataBase.getInstance(MyApplication.instance).userDao();
        Integer nextIndex = loadParams.getKey();
        if(nextIndex == null){
            nextIndex = null;
        }
        finalNextIndex = nextIndex;
        ListenableFuture<LoadResult<Integer, User>> pageFuture = Futures
                .transform(

                        executorService.submit(new Callable<List<User>>() {
                            @Override
                            public List<User> call() throws Exception {


                                Log.d("debug_log","UserPagingResourceRoom  nextIndex = " + finalNextIndex);



                                if(finalNextIndex == null){
                                    finalNextIndex = dao.top().id;
                                    Log.d("debug_log"," 开始进行初始加载 ");
                                }

                                List<User> list = dao.queryRange(finalNextIndex, PAGE_SIZE);

                                Log.d("debug_log", "UserPagingResourceRoom   start = " + list.get(0).getName() + "  end = " + list.get(list.size() - 1).getName());
                                return list;
                            }
                        }),
                        new Function<List<User>, LoadResult.Page<Integer, User>>() {
                            @Override
                            public LoadResult.Page<Integer, User> apply(List<User> input) {
                                Log.d("debug_log", "UserPagingResourceRoom   loadFuture apply  input == null --->" + (input == null) + "  input.size --->" + input.size() + " finalNextIndex = " + finalNextIndex);

                                Integer moreIndex = input.isEmpty() ? null : finalNextIndex - input.size();
                                Integer preIndex = Integer.MAX_VALUE;
                                int length = input == null ? 0 : input.size();

                                preIndex = finalNextIndex + length;

                                if(preIndex > dao.top().id){
                                    preIndex = null;
                                }

                                Log.d("debug_log", "UserPagingResourceRoom   loadFuture apply  preIndex --->" + preIndex + "  moreIndex --->" + moreIndex + " finalNextIndex = " + finalNextIndex);

                                //这里传入的三个参数中,刚才请求的数据,第二个参数为请求的上一页的页数,当为null时不再加载上一页,第三个参数则是下一页
                                return new LoadResult.Page<>(input, preIndex, moreIndex);
                            }
                        },
                        executorService
                );

        ListenableFuture<LoadResult<Integer,User>> partialLoadResultFuture = Futures.catching(
                pageFuture,
                Exception.class,
                LoadResult.Error::new,
                executorService);

        return Futures.catching(partialLoadResultFuture,
                Exception.class, LoadResult.Error::new, executorService);
    }
}
