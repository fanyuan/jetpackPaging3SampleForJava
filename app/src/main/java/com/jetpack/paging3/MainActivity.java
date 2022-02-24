package com.jetpack.paging3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.jetpack.paging3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.list.setLayoutManager(layoutManager);
        ListAdapter adapter = new ListAdapter(new DiffUtil.ItemCallback<User>() {
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
        LoadMoreViewModel loadMoreViewModel=new ViewModelProvider(this).get(LoadMoreViewModel.class);
        loadMoreViewModel.getPaging().observe(this, new Observer<PagingData<User>>() {
            @Override
            public void onChanged(PagingData<User> userPagingData) {
                adapter.submitData(getLifecycle(),userPagingData);
            }
        });

        //loadMoreViewModel.getPaging().observe(this,(data)->{adapter.submitData(getLifecycle(),data);});
    }
}