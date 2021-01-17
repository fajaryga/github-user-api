package com.yogas.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yogas.api.R
import com.yogas.api.data.User
import kotlinx.android.synthetic.main.item_main.view.*

class MainAdapter(val list: List<User>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var onItemClickListener: ((User) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User) {
            with(itemView) {
                Glide.with(this).load(user.avatarUrl).into(itemAvatar)
                itemLogin.text = user.login
                itemDescription.text = user.htmlUrl
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}