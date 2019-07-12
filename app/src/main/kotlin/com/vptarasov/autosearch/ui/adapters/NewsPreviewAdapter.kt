package com.vptarasov.autosearch.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.interfaces.NewsInteractionListener
import com.vptarasov.autosearch.model.News
import kotlinx.android.synthetic.main.item_news.view.*
import java.util.*


class NewsPreviewAdapter(private val previews: ArrayList<News>) :
    RecyclerView.Adapter<NewsPreviewAdapter.ViewHolderItem>(), View.OnClickListener {
    private var listener: NewsInteractionListener? = null

    override fun getItemCount(): Int {
        return previews.size
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): ViewHolderItem {
        val contactView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolderItem(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        val news = previews[position]
        holder.textInfo?.text = news.text
        holder.textTitle?.text = news.title

        Picasso.get()
            .load(news.photo)
            .placeholder(R.drawable.placeholder)
            .into(holder.photo)

        if (listener != null) {
            holder.itemView.tag = news
            holder.itemView.setOnClickListener(this@NewsPreviewAdapter)

        }

    }

    fun setListener(listener: NewsInteractionListener) {
        this.listener = listener
    }


    override fun onClick(view: View) {
        val news = view.tag as News
        listener?.onItemClick(news)
    }


    class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textInfo: TextView? = itemView.bodyNews
        var textTitle: TextView? = itemView.titleNews
        var photo: ImageView? = itemView.photoNews

    }
}
