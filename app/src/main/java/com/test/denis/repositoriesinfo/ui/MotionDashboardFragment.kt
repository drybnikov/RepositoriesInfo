package com.test.denis.repositoriesinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.di.Injectable

class MotionDashboardFragment : Fragment(), Injectable {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.motion_01_basic, null)

    fun navController() = findNavController()
}