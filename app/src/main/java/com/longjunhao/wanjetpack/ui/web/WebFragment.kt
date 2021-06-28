package com.longjunhao.wanjetpack.ui.web

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.navigation.findNavController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.longjunhao.wanjetpack.databinding.FragmentWebBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * todo：
 *  1. 没有用webViewClient，没有仔细研究webViewClient的作用
 *  2. 深色主题下，加载WebFragment页面的过程中，界面是白色的，待优化适配，且需要加上加载进度条
 *  3. 某些文章的界面显示内容后瞬间内容有消失，不知什么原因，待分析。eg：link：https://www.jianshu.com/p/ded52111cc31 title：Android 四大组件通信核心
 *  4. 本页面需要新增FloatingActionButton，实现收藏功能
 *  5. WebFragment 打开、关闭动画没有实现
 *  6. WebFragment暂时没有对应的ViewModel，不确定后期还需不需要加上
 */
@AndroidEntryPoint
class WebFragment : Fragment() {

    private lateinit var binding: FragmentWebBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebBinding.inflate(inflater,container,false)
        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        binding.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        binding.toolbarText.text = arguments?.getString("title")

        configureWebView(arguments?.getString("link") ?: "")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView(link: String) {

        // Configuring Dark Theme
        // *NOTE* : The force dark setting is not persistent. You must call the static
        // method every time your app process is started.
        // *NOTE* : The change from day<->night mode is a
        // configuration change so by default the activity will be restarted
        // (and pickup the new values to apply the theme). Take care when overriding this
        //  default behavior to ensure this method is still called when changes are made.
        val nightModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        // Check if the system is set to light or dark mode
        if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
            // Switch WebView to dark mode; uses default dark theme
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(
                    binding.webView.settings,
                    WebSettingsCompat.FORCE_DARK_ON
                )
            }

            /* Set how WebView content should be darkened. There are three options for how to darken
             * a WebView.
             * PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING- checks for the "color-scheme" <meta> tag.
             * If present, it uses media queries. If absent, it applies user-agent (automatic)
             * darkening DARK_STRATEGY_WEB_THEME_DARKENING_ONLY - uses media queries always, even
             * if there's no "color-scheme" <meta> tag present.
             * DARK_STRATEGY_USER_AGENT_DARKENING_ONLY - it ignores web page theme and always
             * applies user-agent (automatic) darkening.
             * More information about Force Dark Strategy can be found here:
             * https://developer.android.com/reference/androidx/webkit/WebSettingsCompat#setForceDarkStrategy(android.webkit.WebSettings,%20int)
             */
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
                WebSettingsCompat.setForceDarkStrategy(
                    binding.webView.settings,
                    WebSettingsCompat.DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING
                )
            }
        }

        // Setup debugging; See https://developers.google.com/web/tools/chrome-devtools/remote-debugging/webviews for reference
        /*if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            WebView.setWebContentsDebuggingEnabled(true)
        }*/
        WebView.setWebContentsDebuggingEnabled(true)

        // Enable Javascript
        binding.webView.settings.javaScriptEnabled = true

        binding.webView.loadUrl(link)

    }


    // Creating the custom WebView Client Class
    /*private class MyWebViewClient(private val assetLoader: WebViewAssetLoader) :
        WebViewClientCompat() {
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(request.url)
        }
    }*/
}