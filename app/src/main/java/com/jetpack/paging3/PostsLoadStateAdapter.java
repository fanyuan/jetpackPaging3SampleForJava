package com.jetpack.paging3;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class PostsLoadStateAdapter extends LoadStateAdapter<NetworkStateItemViewHolder> {
    private ListAdapter adapter;

    public PostsLoadStateAdapter(ListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkStateItemViewHolder networkStateItemViewHolder, @NonNull LoadState loadState) {
        Log.d("debug_log","PostsLoadStateAdapter   onBindViewHolder");
        networkStateItemViewHolder.bindTo(loadState);
    }

    @NonNull
    @Override
    public NetworkStateItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        Runnable callback = () -> {adapter.retry();};
        return new NetworkStateItemViewHolder(viewGroup,callback);
    }
}
