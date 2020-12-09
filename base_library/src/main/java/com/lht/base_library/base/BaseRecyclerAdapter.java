package com.lht.base_library.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lht.base_library.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    protected boolean isShowNoData = false;
    public List<T> data;
    public IOnRecyclerClickListener<T> listener;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void refreshData(List<T> list) {
        isShowNoData = false;
        data.clear();
        if (list != null && list.size() > 0) {
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isShowNoData) {
            return new NoDataViewHolder(LayoutInflater.from(context).inflate(R.layout.view_no_data, parent, false));
        } else {
            return new ItemViewHolder(LayoutInflater.from(context).inflate(createViewHolder(viewType), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (holder instanceof ItemViewHolder) {
                itemBindViewHolder((ItemViewHolder) holder, position, payloads);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!isShowNoData && holder instanceof ItemViewHolder) {
            itemBindViewHolder((ItemViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return isShowNoData ? 1 : data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowNoData) {
            return -1;
        } else {
            return itemViewType(position);
        }
    }

    public List<T> getData() {
        return data;
    }

    public void showNoData() {
        isShowNoData = true;
        data.clear();
        notifyDataSetChanged();
    }

    public void setClickListener(IOnRecyclerClickListener<T> listener) {
        this.listener = listener;
    }

    public int itemViewType(int position) {
        return super.getItemViewType(position);
    }

    public abstract int createViewHolder(int position);

    public abstract void itemBindViewHolder(ItemViewHolder holder, int position);

    public void itemBindViewHolder(ItemViewHolder holder, int position, List<Object> payloads) {
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder holder;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            holder = new BaseViewHolder(itemView);
        }

    }

    public static class NoDataViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder holder;

        public NoDataViewHolder(@NonNull View itemView) {
            super(itemView);
            holder = new BaseViewHolder(itemView);
        }

    }

    public interface IOnRecyclerClickListener<T> {

        void onClick(String action, T t, int... position);

    }

}
