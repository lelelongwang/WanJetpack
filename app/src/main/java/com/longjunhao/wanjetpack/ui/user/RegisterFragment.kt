package com.longjunhao.wanjetpack.ui.user

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.databinding.FragmentRegisterBinding
import com.longjunhao.wanjetpack.util.API_RESPONSE_NO_NET
import com.longjunhao.wanjetpack.util.SharedPrefObject
import com.longjunhao.wanjetpack.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.userModel = viewModel

        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        binding.closeLogin.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.setClickListener {
            viewModel.register().observe(viewLifecycleOwner, Observer { response ->
                if (response.errorCode == 0) {
                    viewModel.isLogin.postValue(true)
                    SharedPrefObject.put(SharedPrefObject.KEY_IS_LOGIN, true)
                    response.data?.let {
                        val name = if (TextUtils.isEmpty(it.nickname)) it.username else it.nickname
                        SharedPrefObject.put(SharedPrefObject.KEY_LOGIN_NAME, name)
                    }
                    //返回到上一个fragment，注意与popBackStack()的区别
                    //findNavController().navigateUp()

                    //清空导航栈到指定的fragment，且在nav_gragh中设置popUpTo、popUpToInclusive
                    //findNavController().navigate(R.id.action_registerFragment_to_userFragment)

                    //popBackStack()是弹出当前fragment，而popBackStack(@IdRes int destinationId, boolean inclusive)是
                    //可以清空当前fragment到destinationId之间的fragment。inclusive为true的话，destinationId也会弹出。
                    //todo 界面退出动画需要优化
                    findNavController().popBackStack(R.id.loginFragment,true)

                } else if (response.errorCode == API_RESPONSE_NO_NET) {
                    Snackbar.make(binding.root, getString(R.string.no_net), Snackbar.LENGTH_LONG).show()
                } else {
                    viewModel.isLogin.postValue(false)
                    SharedPrefObject.put(SharedPrefObject.KEY_IS_LOGIN, false)
                    binding.repassword.error = getString(R.string.register_fail)
                }
            })
        }
    }
}