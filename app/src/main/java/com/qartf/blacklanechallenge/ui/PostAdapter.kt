package com.qartf.blacklanechallenge.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.qartf.blacklanechallenge.R
import com.qartf.blacklanechallenge.data.model.Post

class PostAdapter(private val clickListener: OnClickListener) : ListAdapter<Post,
        PostAdapter.ViewHolder>(GridViewDiffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(clickListener, product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_post_item, parent, false)
        )
    }

    class ViewHolder constructor(val binding: View) :
        RecyclerView.ViewHolder(binding) {

        fun bind(clickListener: OnClickListener, item: Post) {
            val title: TextView = binding.findViewById(R.id.title)
            val body: TextView = binding.findViewById(R.id.body)
            title.text = item.title
            body.text = item.body
        }
    }

    companion object GridViewDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    interface OnClickListener {
        fun onClick(product: Post)
    }
}