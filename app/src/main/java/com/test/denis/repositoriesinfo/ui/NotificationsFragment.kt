package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.test.denis.repositoriesinfo.R
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notifications, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        smallItem.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                smallItem to "cardTransitionNameEnd",
                notificationName2 to "titleTransitionNameEnd"
            )

            navController().navigate(NotificationsFragmentDirections.openNotification("My Notification"), extras)
        }
    }

    fun navController() = findNavController()
}