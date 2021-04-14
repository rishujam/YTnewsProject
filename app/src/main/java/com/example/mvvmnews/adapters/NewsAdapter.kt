package com.example.mvvmnews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmnews.R
import com.example.mvvmnews.models.Article

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        lateinit var title :TextView
        lateinit var description:TextView
        lateinit var publish:TextView
        lateinit var src:TextView
        lateinit var image:ImageView
        init {
            title =itemView.findViewById(R.id.tvTitle)
            description=itemView.findViewById(R.id.tvDescription)
            publish=itemView.findViewById(R.id.tvPublishedAt)
            src =itemView.findViewById(R.id.tvSource)
            image=itemView.findViewById(R.id.ivArticleImage)
        }
    }
    private val differCallback= object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return  oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_article_preview,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article =differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.image)
            setOnClickListener {
                onItemClickListener?.let {it(article)  }
            }
        }
        holder.title.text=article.title
        holder.description.text=article.description
        holder.publish.text=article.publishedAt
        holder.src.text=article.source.name

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener:((Article) -> Unit)? =null

    fun setOnItemClickListener(listener:(Article) ->Unit){
        onItemClickListener=listener
    }
}