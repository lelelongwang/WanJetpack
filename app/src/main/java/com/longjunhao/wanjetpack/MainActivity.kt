package com.longjunhao.wanjetpack

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

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
        setStatusBarAndNavigationBar()
    }

    private fun setStatusBarAndNavigationBar() {
        //Kotlin反射：去掉沉侵式后状态栏显示灰色阴影的问题。
        /*try {
            val decorView = window.decorView::class.java
            val field = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                // Android10： StatusBar、NavigationBar都没有灰色阴影
                decorView.getDeclaredField("mSemiTransparentBarColor")
            } else {
                // eg: Android8.1： 只有StatusBar没有灰色阴影。
                decorView.getDeclaredField("mSemiTransparentStatusBarColor")
            }
            field.isAccessible = true
            field.setInt(window.decorView, Color.TRANSPARENT)
        } catch (e: Exception) {
        }*/

        // todo:
        //  1.后期可以把沉侵式统一在themes中设置，或者统一在代码中设置
        //  2.Android6.0上没有SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR属性
        when {
            Build.VERSION.SDK_INT in Build.VERSION_CODES.M..Build.VERSION_CODES.O_MR1 -> {
                //以下代码实现了Android6.0.1 、 Android8.1上导航栏和状态栏的沉侵式，但是要结合themes中的设置
                window.clearFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                )
                //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS )
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                //以下代码实现了Android10上导航栏的沉侵式，在themes中已经设置状态栏的沉侵式
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            else -> Snackbar.make(
                binding.root,
                "VERSION_CODES=${Build.VERSION.SDK_INT} 沉侵式 待适配",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

}