package com.jetpack.paging3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleCoroutineScope;
import androidx.lifecycle.LifecycleKt;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jetpack.paging3.databinding.ActivityRoomBinding;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RoomActivity extends AppCompatActivity {
    private ActivityRoomBinding binding;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_room);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.list.setLayoutManager(layoutManager);
        adapter = new ListAdapter(new DiffUtil.ItemCallback<User>() {
            @Override
            public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.getName().equals(newItem.getName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                return oldItem.getName().equals(newItem.getName());
            }
        });
        binding.list.setAdapter(adapter.withLoadStateFooter(new PostsLoadStateAdapter(adapter)));
        LoadMoreViewModelForRoom loadMoreViewModel=new ViewModelProvider(this).get(LoadMoreViewModelForRoom.class);
        loadMoreViewModel.getPaging().observe(this, new Observer<PagingData<User>>() {
            @Override
            public void onChanged(PagingData<User> userPagingData) {
                adapter.submitData(getLifecycle(),userPagingData);
            }
        });

        //loadMoreViewModel.getPaging().observe(this,(data)->{adapter.submitData(getLifecycle(),data);});
    }

    public void insert(View view) {
        Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                Log.d("debug_log"," insert   subscribe");
                List<User> list = UserResponse.getUsersForRoom(0, 100);
                User[] array = new User[list.size()];
                list.toArray(array);
                UserDao dao = UserDataBase.getInstance(RoomActivity.this).userDao();
                dao.insertUserAll(array);
                List<User> l = dao.queryRange(26,8);
                Log.d("debug_log"," insert   subscribe 123 l.size = " + new Gson().toJson(dao.top()));

                emitter.onSuccess(true);
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean bv) throws Exception {
                Log.d("debug_log"," insert   accept");
                adapter.refresh();
            }
        });

    }
}