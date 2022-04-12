package com.fairhr.module_support.baseadapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 */
public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private OnItemClickListener<T> mOnItemClickListener;
    public Context mContext;
    public List<T> mDatas;
    private OnItemLongClickListener<T> mLongClickListener;

    public BaseAdapter(List<T> datas) {
        mDatas = datas;
    }

    public BaseAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return mLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        mLongClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        onBindViewToData(holder, position, mDatas.get(position));
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                if (mDatas != null && position < mDatas.size())
                    mOnItemClickListener.onItemClick(position, mDatas.get(position), v);
            });
        }

        if(mLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(position, mDatas.get(position), v);
                    return true;
                }
            });
        }
    }

    public abstract void onBindViewToData(VH holder, int position, T item);

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data, View view);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(int position, T data, View view);
    }
}
