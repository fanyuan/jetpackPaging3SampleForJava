package com.jetpack.paging3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jetpack.paging3.databinding.ActivitySample1Binding

class Sample1Activity : AppCompatActivity() {
    lateinit var binding:ActivitySample1Binding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sample1)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.list.layoutManager = layoutManager
        val adapter = ListAdapter(object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.name == newItem.name
            }
        })
        binding.list.adapter = adapter.withLoadStateFooter(PostsLoadStateAdapter(adapter))
        val loadMoreViewModel = ViewModelProvider(this).get(
            LoadMoreViewModel::class.java
        )
        loadMoreViewModel.paging.observe(this,
            { userPagingData -> adapter.submitData(lifecycle, userPagingData) })
    }
}