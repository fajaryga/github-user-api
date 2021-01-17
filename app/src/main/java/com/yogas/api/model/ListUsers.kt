package com.yogas.api.model

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yogas.api.DetailActivity
import com.yogas.api.R
import com.yogas.api.databinding.ItemMainBinding

class ListUsers : RecyclerView.Adapter<ListUsers.ListViewHolder>() {

    private val mData = ArrayList<Users>()
    fun setData(items: ArrayList<Users>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_main, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemMainBinding.bind(itemView)
        fun bind(user: Users) {
            binding.itemLogin.text = user.login
            binding.itemDescription.text = user.html_url
            Glide.with(itemView.context)
                .load(user.avatar_url)
                .apply(
                    RequestOptions()
                        .error(R.drawable.ic_launcher_background)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .override(55, 55)
                )
                .into(binding.itemAvatar)

            itemView.setOnClickListener {
                val setObjectIntent = Intent(it.context, DetailActivity::class.java)
                setObjectIntent.putExtra(DetailActivity.DETAIL_USER, user)
                it.context.startActivity(setObjectIntent)
            }
        }
    }
}