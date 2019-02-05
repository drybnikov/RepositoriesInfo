package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import com.test.denis.repositoriesinfo.R
import kotlinx.android.synthetic.main.fragment_notifications_details.*

class NotificationDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition = ChangeBounds()
        sharedElementReturnTransition = ChangeBounds()

        /*sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 750
        }*/

        return inflater.inflate(R.layout.fragment_notifications_details, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            notificationName.text = NotificationDetailsFragmentArgs.fromBundle(it).notificationId
        }
    }
}