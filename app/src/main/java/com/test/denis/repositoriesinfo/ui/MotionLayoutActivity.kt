package com.test.denis.repositoriesinfo.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.test.denis.repositoriesinfo.R
import com.test.denis.repositoriesinfo.di.Injectable
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.coordinatorlayout_header.*
import kotlinx.android.synthetic.main.motion_01_basic.*
import kotlinx.android.synthetic.main.motion_drawerlayout.*
import kotlinx.android.synthetic.main.motion_drawerlayout_content.*
import javax.inject.Inject

class MotionLayoutActivity : AppCompatActivity(), Injectable, HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.motion_drawerlayout)

        drawerLayout.setScrimColor(Color.TRANSPARENT)

        toggleButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawers()
            else
                drawerLayout.openDrawer(GravityCompat.START)
        }

        navigation.setupWithNavController(navController())
        //setupActionBarWithNavController(navController())

        initSearchInputListener()
        searchMotion.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                if (currentId == R.id.endSearch) {
                    inputSearch.requestFocus()
                }
            }
        })

        //searchViewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    private fun initSearchInputListener() {
        inputSearch?.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        inputSearch?.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val query = inputSearch.text.toString()

        dismissKeyboard(v.windowToken)
        searchViewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onBackPressed() {
        if (motionLayout.currentState == R.id.end) {
            //motionLayout.transitionToStart()
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    fun navController() = findNavController(R.id.contentContainer)
}