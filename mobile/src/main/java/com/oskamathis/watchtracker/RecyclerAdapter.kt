package com.oskamathis.watchtracker

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(context: Context, private val data: List<String>) : RecyclerView.Adapter<ViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ここでViewHolderを作る
        return ViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        // データの要素数を返す
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // ViewHolderを通してデータをViewに設定する
        holder.textView.text = data[position]
    }
}
