package com.jetpack.paging3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.RecyclerView;

public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {
    private ProgressBar progressBar;
    private TextView errorMsg;
    private Button retry;

    private Runnable retryCallback;

    public NetworkStateItemViewHolder(@NonNull ViewGroup parent, Runnable retryCallback) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_network_state,parent,false));
        this.retryCallback = retryCallback;
        progressBar = itemView.findViewById(R.id.progress);
        errorMsg = itemView.findViewById(R.id.textview);
        retry = itemView.findViewById(R.id.button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retryCallback.run();
            }
        });
    }

    public void bindTo(LoadState loadState){
        Log.d("debug_log","NetworkStateItemViewHolder    bindTo");
        progressBar.setVisibility(loadState instanceof LoadState.Loading?View.VISIBLE:View.INVISIBLE);
        retry.setVisibility(loadState instanceof LoadState.Error?View.VISIBLE:View.INVISIBLE);
        boolean isError = loadState instanceof LoadState.Error && !((LoadState.Error)loadState).getError().getMessage().isEmpty();
        errorMsg.setVisibility(isError?View.VISIBLE:View.INVISIBLE);
        if(isError){
            errorMsg.setText(((LoadState.Error)loadState).getError().getMessage());
        }

    }
}
