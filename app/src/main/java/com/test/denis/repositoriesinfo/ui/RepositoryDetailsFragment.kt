package com.test.denis.repositoriesinfo.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.test.denis.repositoriesinfo.R
import kotlinx.android.synthetic.main.fragment_repository_details.*

class RepositoryDetailsFragment : Fragment() {
    private var handler = Handler(Looper.getMainLooper())
    private val imageRequestListener = object: RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
            startPostponedEnterTransition()
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            startPostponedEnterTransition()
            return false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds()
        //sharedElementReturnTransition = ChangeBounds()

        /*sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }*/
        /*sharedElementReturnTransition = ChangeBounds().apply {
            duration = 750
        }*/

        handler.postDelayed({
            startPostponedEnterTransition()
        }, 1000)
        postponeEnterTransition()

        return inflater.inflate(R.layout.fragment_repository_details, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            RepositoryDetailsFragmentArgs.fromBundle(it).repository.apply {
                ownerName.text = name
                ownerFullName.text = fullName
                Glide
                    .with(this@RepositoryDetailsFragment)
                    .load(owner.avatarUrl)
                    .listener(imageRequestListener)
                    .into(ownerImg)


                ownerImg.transitionName = "${owner.login}_image"
                ownerName.transitionName = "${owner.login}_name"
                repoCard.transitionName = "${owner.login}_card"
            }
        }
    }
}