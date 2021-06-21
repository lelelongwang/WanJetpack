WanJetpack
==========

### 参考demo

- [Sunflower](https://github.com/android/sunflower)

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
    - [Safe Args 导航](https://developer.android.google.cn/guide/navigation/navigation-conditional)

- [Preference]
    - [Jetpack Preference库](https://developer.android.google.cn/jetpack/androidx/releases/preference?hl=zh_cn)
    - [Preference指南](https://developer.android.google.cn/guide/topics/ui/settings?hl=zh_cn)
    - 参考官方demo：[PreferencesKotlin](https://github.com/android/user-interface-samples/tree/master/PreferencesKotlin)

- [ViewPager2](https://github.com/android/views-widgets-samples/tree/main/ViewPager2)

- [TabLayout]()

- [BottomNavigationView]()

- [RecyclerView]()
    - 默认的adapter：RecyclerView.Adapter：[认识 RecyclerView](https://zhuanlan.zhihu.com/p/363343211)
    - **ListAdapter**：继承RecyclerView.Adapter：[在 RecyclerView 中使用 ListAdapter](https://www.jianshu.com/p/16b364e20ee7)
    - ConcatAdapter：https://mp.weixin.qq.com/s/QTaz45aLucX9mivVMbCZPQ
    - PagingDataAdapter：继承RecyclerView.Adapter

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