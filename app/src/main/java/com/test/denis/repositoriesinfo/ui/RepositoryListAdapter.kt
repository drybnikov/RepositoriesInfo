package com.test.denis.repositoriesinfo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.model.Repo
import kotlinx.android.synthetic.main.item_repo.view.*

typealias CallbackArgs = ((Repo, extras: Navigator.Extras) -> Unit)?

class RepositoryListAdapter(
    private val callback: CallbackArgs
) : RecyclerView.Adapter<RepoItemViewHolder>() {

    private val items: ArrayList<Repo> = arrayListOf()

    fun initData(listItems: List<Repo>) {
        items.clear()
        items.addAll(listItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repo, parent, false)

        return RepoItemViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(viewHolder: RepoItemViewHolder, position: Int) {
        val listItemModel = items[position]
        viewHolder.bind(listItemModel, callback)
    }
}

class RepoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageView: ImageView = view.ownerImg
    private val nameText = view.repoName
    private val loginText = view.ownerName
    private val sizeText = view.repoSize

    fun bind(listItemModel: Repo, callback: CallbackArgs) {
        with(listItemModel) {
            Glide
                .with(itemView)
                .load(owner.avatarUrl)
                .into(imageView)
            imageView.transitionName = "${owner.login}_avatar"
            itemView.transitionName = "${owner.login}_item"
            nameText.transitionName = "${owner.login}_repo"
            loginText.transitionName = "${owner.login}_name"

            nameText.text = name
            loginText.text = owner.login
            sizeText.text = size.toString()
            itemView.isActivated = !hasWiki

            val extras = FragmentNavigatorExtras(
                imageView to "${owner.login}_image",
                nameText to "${owner.login}_name",
                itemView to "${owner.login}_card",
                loginText to "${owner.login}"
            )

            itemView.setOnClickListener {
                callback?.invoke(listItemModel, extras)
            }
        }
    }
}