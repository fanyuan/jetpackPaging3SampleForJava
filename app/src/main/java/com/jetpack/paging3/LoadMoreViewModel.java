package com.jetpack.paging3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import kotlinx.coroutines.CoroutineScope;

public class LoadMoreViewModel extends ViewModel {
	MutableLiveData<PagingData<User>> dataMutableLiveData=new MutableLiveData<>();
	//初始化配置,可以定义最大加载的数据量
	PagingConfig pagingConfig=new PagingConfig(10,10,false,10);

	public LiveData<PagingData<User>> getPaging(){
		CoroutineScope viewmodelScope= ViewModelKt.getViewModelScope(this);
		//构造函数根据自己的需要来调整
		Pager<Integer, User> pager = new Pager<Integer, User>(pagingConfig, ()->new UserPagingResource());
		return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager),viewmodelScope);
	}
}
