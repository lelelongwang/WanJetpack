package com.longjunhao.wanjetpack.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.databinding.FragmentUserBinding
import com.longjunhao.wanjetpack.util.SharedPrefObject
import com.longjunhao.wanjetpack.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    /**
     * todo:用viewModel在fragment之间共享数据时，此处用activityViewModels()范围有点大了，建议改成navGraphViewModels(id)
     */
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        //设置lifecycleOwner后屏幕才刷新，eg：退出登录时
        binding.lifecycleOwner = this
        binding.userModel = viewModel

        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        binding.setClickListener {
            if (viewModel.isLogin.value == true) {
                Snackbar.make(it, "进入个人信息界面，待实现", Snackbar.LENGTH_LONG).show()
            } else {
                findNavController().navigate(R.id.action_userFragment_to_loginFragment)
            }
        }
        binding.toSettings.setOnClickListener {
            Snackbar.make(it, "进入个人设置界面，待实现", Snackbar.LENGTH_LONG).show()
        }
        binding.logoutBtn.setOnClickListener {
            viewModel.logout().observe(viewLifecycleOwner, Observer {
                if (it.errorCode == 0) {
                    viewModel.username.postValue("")
                    viewModel.password.postValue("")
                    viewModel.isLogin.postValue(false)
                    //SharedPrefObject.logout()
                }
            })
        }
        binding.toCollection.setOnClickListener {
            //todo 如果不通过isLogin判断，直接进入收藏界面，当退出登录后，收藏界面也是能成功获取的，是因为没有清空Cookie，待优化
            if (viewModel.isLogin.value == true) {
                findNavController().navigate(R.id.action_userFragment_to_collectionFragment)
            } else {
                findNavController().navigate(R.id.action_userFragment_to_loginFragment)
            }
        }
    }
}