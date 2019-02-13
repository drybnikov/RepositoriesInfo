package com.test.denis.repositoriesinfo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.model.Repo
import kotlinx.android.synthetic.main.item_repo.view.*

class RepositoryListAdapter(
    private val repoClickCallback: ((Repo) -> Unit)?
) : RecyclerView.Adapter<RepoItemViewHolder>() {

    private val items: ArrayList<Repo> = arrayListOf()

    fun initData(listItems: List<Repo>) {
        items.clear()
        items.addAll(listItems)
        notifyDataSetChanged()
    }

    fun addData(listItems: List<Repo>) {
        var size = this.items.size
        items.addAll(listItems)
        var sizeNew = items.size
        notifyItemRangeChanged(size, sizeNew)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repo, parent, false)

        return RepoItemViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: RepoItemViewHolder, position: Int) {
        val listItemModel = items[position]
        viewHolder.bind(listItemModel)
        viewHolder.itemView.setOnClickListener {
            repoClickCallback?.invoke(listItemModel)
        }
    }
}

class RepoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageView = view.ownerImg
    private val nameText = view.repoName
    private val loginText = view.ownerName
    private val sizeText = view.repoSize

    fun bind(listItemModel: Repo) {
        with(listItemModel) {
            Glide
                .with(itemView)
                .load(owner.avatarUrl)
                .into(imageView)

            nameText.text = name
            loginText.text = owner.login
            sizeText.text = size.toString()
            itemView.isActivated = !hasWiki
        }
    }
}