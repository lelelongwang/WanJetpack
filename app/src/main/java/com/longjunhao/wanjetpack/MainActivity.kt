package com.longjunhao.wanjetpack

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.longjunhao.wanjetpack.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController = Navigation.findNavController(this, R.id.nav_host)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            runOnUiThread {
                val bottomViewIsShow = destination.id == R.id.homeViewPagerFragment
                        || destination.id == R.id.projectFragment
                        || destination.id == R.id.wechatFragment
                        || destination.id == R.id.userFragment
                //todo 非首次显示隐藏时bottomNavigation需要新增动画，eg：京东的上下动画。微信的左右动画
                if (bottomViewIsShow) {
                    binding.bottomNavigation.visibility = View.VISIBLE
                } else {
                    binding.bottomNavigation.visibility = View.GONE
                }
            }
        }

        //Kotlin反射：去掉沉侵式后状态栏显示灰色阴影的问题。
        try {
            val decorView = window.decorView::class.java
            val field = decorView.getDeclaredField("mSemiTransparentBarColor")
            field.isAccessible = true
            field.setInt(window.decorView, Color.TRANSPARENT)
        } catch (e: Exception) {
        }
    }

}