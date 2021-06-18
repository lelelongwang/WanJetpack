package com.longjunhao.wanjetpack.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.databinding.FragmentLoginBinding
import com.longjunhao.wanjetpack.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.userModel = viewModel

        Log.d("LoginFragment", "onCreateView: ljh isLogin="+viewModel.isLogin.value+"  username="+viewModel.username.value)
        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {
        binding.closeLogin.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.setClickListener {
            val username = binding.username1.text.toString()
            val password = binding.password1.text.toString()
            //viewModel.login.observe(viewLifecycleOwner, Observer {
            //todo 此处传参数是个不好的方案，应该不需要传参数。
            viewModel.login(username, password).observe(viewLifecycleOwner, Observer {
                if (it.errorCode == 0) {
                    viewModel.name.postValue(it.data?.username)
                    viewModel.user.postValue(it.data)
                    viewModel.isLogin.postValue(true)
                    findNavController().navigateUp()
                } else {
                    viewModel.isLogin.postValue(false)
                    binding.password.error = getString(R.string.login_fail)
                }
            })
        }
    }
}