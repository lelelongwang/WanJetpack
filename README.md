WanJetpack
==========

### 参考demo

- [Sunflower](https://github.com/android/sunflower)
- [architecture-components-samples](https://github.com/android/architecture-components-samples)
- [views-widgets-samples](https://github.com/android/views-widgets-samples)
- [user-interface-samples](https://github.com/android/user-interface-samples)
- [architecture-samples](https://github.com/android/architecture-samples)
- [compose-samples](https://github.com/android/compose-samples)

### 相关知识点

- [LiveData文档](https://developer.android.google.cn/topic/libraries/architecture/livedata)
    - [LiveDataSample](https://github.com/android/architecture-components-samples/tree/main/LiveDataSample):
    - 持有可被观察的类类似于**EventBus**或者**RxJava**。LiveData是一种可感知生命周期的组件
    - LiveData与MutableLiveData区别
    - [LiveData使用](https://www.jianshu.com/p/c69a7db3299a)
    - [理解协程、LiveData 和 Flow](https://mp.weixin.qq.com/s/p5H51RC6QfyyoAcQ1aGRLg)
    - [Google 推荐在 MVVM 架构中使用 Kotlin Flow](https://juejin.cn/post/6854573211930066951)
    - 关于Retrofit和LiveData相关参考demo：GithubBrowserSample[]

- [ViewModel文档](https://developer.android.google.cn/topic/libraries/architecture/viewmodel)
    - [ViewModel 四种集成方式](https://mp.weixin.qq.com/s/Hl8Yuf2bkDlVlgdB4M-wrw),即：
        - ViewModel 中的 Saved State —— 后台进程重启时，ViewModel 的数据恢复；
        - 在 NavGraph 中使用 ViewModel —— ViewModel 与导航 (Navigation) 组件库的集成；
        - ViewModel 配合数据绑定 (data-binding) —— 通过使用 ViewModel 和 LiveData 简化数据绑定；
        - viewModelScope —— Kotlin 协程与 ViewModel 的集成。
    - [在Activity或者Fragment中**如何处理ViewModel的三种方式**（没太懂）](https://juejin.cn/post/6854573211930066951)

- [ViewBinding文档](https://developer.android.google.cn/topic/libraries/view-binding)

- [DataBinding文档](https://developer.android.google.cn/topic/libraries/data-binding)
    - 取代findviewbyId，类似于**Butterknife**。

- [coroutines]()
    - [理解协程、LiveData 和 Flow](https://mp.weixin.qq.com/s/p5H51RC6QfyyoAcQ1aGRLg)
        - liveData 协程构造方法提供了一个协程代码块，这个块就是 LiveData 的作用域，**当 LiveData 被观察的时候，里面的操作就会被执行，当 LiveData 不再被使用时，里面的操作就会取消。** 而且该协程构造方法产生的是一个不可变的LiveData，可以直接暴露给对应的视图使用。而 emit() 方法则用来更新 LiveData 的数据。
        - 一个常见用例，比如当用户在 UI 中选中一些元素，然后将这些选中的内容显示出来。一个常见的做法是，把被选中的项目的 ID 保存在一个 MutableLiveData 里，然后运行 switchMap。现在在 switchMap 里，您也可以使用协程构造方法:
        ```kotlin
            private val itemId = MutableLiveData<String>()
            val result = itemId.switchMap {
                liveData { emit(fetchItem(it)) }
            }
        ```
    - [Google 推荐在 MVVM 架构中使用 Kotlin Flow](https://juejin.cn/post/6854573211930066951)
    - [图解协程原理](https://juejin.cn/post/6883652600462327821)

- [Hilt]()
    - hilt 和 **Koin**

- [Paging文档](https://developer.android.google.cn/topic/libraries/architecture/paging/v3-overview)
    - [PagingWithNetworkSample](https://github.com/android/architecture-components-samples/tree/main/PagingWithNetworkSample)

- [Room]()

- [DataStore]()
    - [使用 Jetpack DataStore 进行数据存储](https://mp.weixin.qq.com/s/26Uxotf3-oceKUbrujqX3w)

- [App Startup]()

- [WorkManager]()

- [compose]()

- [Navigation文档](https://developer.android.google.cn/guide/navigation)
    - [NavigationAdvancedSample](https://github.com/android/architecture-components-samples/tree/main/NavigationAdvancedSample)
    - 和路由框架**ARouter**的区别：ARouter主要是用于Activity路由的框架，采用的是APT技术，可用于组件化改造。而Navigation主要是用于Fragment路由导航的框架。
    - [Jetpack 之 Navigation 全面剖析](https://mp.weixin.qq.com/s/qgNbxgB-6qrFzJflqaBUdg)
    - [底部导航栏1](https://www.jianshu.com/p/8ec9f0185777)
    - [底部导航栏2](https://www.jianshu.com/p/729375b932fe)
    - **用户登录场景**
        - [条件导航](https://developer.android.google.cn/guide/navigation/navigation-conditional)
        - [Fragment 间用 activityViewModels() 共享数据](https://developer.android.google.cn/topic/libraries/architecture/viewmodel)
        - [在 NavGraph 中使用 ViewModel 共享数据](https://mp.weixin.qq.com/s/Hl8Yuf2bkDlVlgdB4M-wrw)
    - [Safe Args 导航](https://developer.android.google.cn/guide/navigation/navigation-pass-data)

- [Preference 库](https://developer.android.google.cn/jetpack/androidx/releases/preference?hl=zh_cn)
    - [官方指南](https://developer.android.google.cn/guide/topics/ui/settings?hl=zh_cn)
    - 官方demo：[PreferencesKotlin](https://github.com/android/user-interface-samples/tree/master/PreferencesKotlin)

- [RecyclerView 库](https://developer.android.google.cn/jetpack/androidx/releases/recyclerview?hl=zh_cn)
    - [官方文档](https://developer.android.google.cn/guide/topics/ui/layout/recyclerview?hl=zh_cn)
    - [官方demo](https://github.com/android/views-widgets-samples/tree/main/RecyclerView)
        - RecyclerView： java版本普通demo
        - RecyclerViewAnimations： 新增、删除、更新 item的demo
        - RecyclerViewKotlin： ConcatAdapter的demo
            - 实现了ConcatAdapter，**能规避嵌套滑动**
            - 实现了点击item的方案，WanJetpack建议参考
            - 实现了新增、删除item的方案及更新item动画
        - RecyclerViewSimple： kotlin版本普通demo
    - 默认的adapter：**RecyclerView.Adapter**：[认识 RecyclerView](https://zhuanlan.zhihu.com/p/363343211)
    - **ListAdapter**：继承RecyclerView.Adapter：[在 RecyclerView 中使用 ListAdapter](https://www.jianshu.com/p/16b364e20ee7)
    - **ConcatAdapter**：https://mp.weixin.qq.com/s/QTaz45aLucX9mivVMbCZPQ
    - **PagingDataAdapter**：继承RecyclerView.Adapter

- [ViewPager2 库](https://developer.android.google.cn/jetpack/androidx/releases/viewpager2)
    - [官方文档](https://developer.android.google.cn/guide/navigation/navigation-swipe-view-2)
    - [官方demo](https://github.com/android/views-widgets-samples/tree/main/ViewPager2)
        - 官方demo中的ViewPager2 with a Preview of Next/Prev Page 相当于Banner中类似的场景
        - 官方demo中的ViewPager2 with a Nested RecyclerViews 场景很好，**提供了解决嵌套滑动的方案**
    - ViewPager2 **底层使用 RecycleView 实现的**，所以这里不再使用 PagerAdapter 而是使用了 **RecyclerView.Adapter**
    - 对应的fragment用的是 **FragmentStateAdapter**，而不是 FragmentStatePagerAdapter、FragmentPagerAdapter之类的


- Banner：其实就是 ViewPager 的应用
    - 三方库：
        - [banner](https://github.com/youth5201314/banner)
        - [开发一款商业级Banner控件](https://mp.weixin.qq.com/s/X615qrAXzVXVlsYtmXTj1w)
        - [打造一个灵活易用的Banner组件](https://mp.weixin.qq.com/s/b2_NB8ue0gvKZ2lvHITbNA)
        - [ViewPager2：打造Banner控件](https://juejin.cn/post/6844904066011643911)
    - 自己实现方案：
        - 让Banner和RecyclerView分开： 通过NestedScrollView里包裹ViewPager2和RecyclerView的话，会有滑动卡顿的问题，即使加上android:nestedScrollingEnabled="false"属性，除非再加上setHasFixedSize(true)，但是还会有其他的问题：加上setHasFixedSize(true)后，界面的数据只显示一页了。故此方案暂时行不通了。本方案相关代码
            ```kotlin
                binding.articleList.setHasFixedSize(true)
                binding.articleList.isNestedScrollingEnabled = false
            ```
        - 让Banner成为RecyclerView的一部分：
            - 如果Banner在顶部：banner在顶部的话，就做header
            - 如果Banner在中间：在中间的话，就type，或者对adapter做一个扩展，做一个可以在中间插入的类似header。毕竟type的话，写起来也蛮麻烦的
        - 通过 ConcatAdapter 实现：
            - 本demo就是用的该方案，demo中通过HomeFirstAdapter添加RecyclerView的ConcatAdapter中，通过HomeBannerAdapter实现ViewPager2的adapter。
            - 通过上述的方式加上ViewPager2之后，ViewPager2没有影响RecyclerView的功能，RecyclerView上下滑动流畅；但是ViewPager2不能滑动，因为事件被RecyclerView拦截了。故需新增自定义布局 NestedFrameLayout 嵌套在ViewPager2之上，在 NestedFrameLayout 去处理父类的事件分发，即当左右滑动 NestedFrameLayout 时，执行 NestedFrameLayout 的parent.requestDisallowInterceptTouchEvent(true)方法，让ViewPager2消费事件。
        - 通过 MultiTypeAdapter 实现：暂时没有验证
        - **工行融e购**实现方案：首页除了viewpager功能都放在AppBarLayout里面，但是这样TabLayout可能就要和融e购一样放在下面了，不是想要的。用ConcatAdapter也可以实现工行融e购的首页效果。
        - **京东首页**实现方案：自定义控件实现。用ConcatAdapter也能实现京东首页效果
- NestedScrollView
    - 直接在 NestedScrollView 中放入 ViewPager2 和 RecyclerView 时，会出现滑动卡顿。[解决方案参考](https://www.jianshu.com/p/8dd1e902b7cd)
    - [NestedScrollView](https://www.jianshu.com/p/f55abc60a879)
    - 事件冲突的原因：Android 的事件分发机制中，只要有一个控件消费了事件，其他控件就没办法再接收到这个事件了。因此，当有嵌套滑动场景时，我们都需要自己手动解决事件冲突。而在 Android 5.0 Lollipop 之后，Google 官方通过 嵌套滑动机制 解决了传统 Android 事件分发无法共享事件这个问题。
    - 嵌套滑动机制：嵌套滑动机制 的基本原理可以认为是事件共享，即当子控件接收到滑动事件，准备要滑动时，会先通知父控件(startNestedScroll）；然后在滑动之前，会先询问父控件是否要滑动（dispatchNestedPreScroll)；如果父控件响应该事件进行了滑动，那么就会通知子控件它具体消耗了多少滑动距离；然后交由子控件处理剩余的滑动距离；最后子控件滑动结束后，如果滑动距离还有剩余，就会再问一下父控件是否需要在继续滑动剩下的距离（dispatchNestedScroll)...


- [TabLayout]()
    - 和ViewPager2、Fragment应用

- [BottomNavigationView]()
    - 和Navigation、Fragment、ViewPager2应用

- [Constraint Layout]()
    [Constraint Layout 2.0 用法详解](https://mp.weixin.qq.com/s/7wEr6okR-CAAoNDYB4gHig)

- [CoordinatorLayout、NestedScrollView、CollapsingToolbarLayout、AppBarLayout、MaterialToolbart]()

- 待优化场景，搜索场景：[search](https://developer.android.google.cn/guide/topics/search)在 Android 系统的协助下使用**搜索对话框**或**搜索微件**传递搜索查询
    - 搜索对话框
    - 搜索微件

- Glide
    - 和Coil对比，建议换成Coil加载图片

- Cookie
    - [CookieManager](https://developer.android.google.cn/reference/kotlin/android/webkit/CookieManager)
    - 本demo中，和收藏相关都需要登录操作，建议登录将返回的cookie（其中包含账号、密码）持久化到本地即可。

- [Webview 库](https://developer.android.google.cn/jetpack/androidx/releases/webkit)
    - [官方文档](https://developer.android.google.cn/guide/webapps/webview)
    - [官方demo](https://github.com/android/views-widgets-samples/tree/main/WebView)
    - 本demo中跳转到WebFragment是通过 Bundle 传递参数，**没有**用通过 Navigation 的 Safe Args 导航实现
    - 本demo中的WebView适配了深色主题。
