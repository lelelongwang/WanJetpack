package com.longjunhao.wanjetpack.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.databinding.FragmentRegisterBinding
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
            val username = binding.username1.text.toString()
            val password = binding.password1.text.toString()
            val repassword = binding.repassword1.text.toString()
            //viewModel.register.observe(viewLifecycleOwner, Observer {
            //todo 此处传参数是个不好的方案，应该不需要传参数。
            viewModel.register(username, password, repassword).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    viewModel.name.postValue(it.username)
                    viewModel.user.postValue(it)
                    viewModel.isLogin.postValue(true)
                    //findNavController().navigateUp()
                    findNavController().navigate(R.id.action_registerFragment_to_userFragment)
                } else {
                    viewModel.isLogin.postValue(false)
                    binding.repassword.error = getString(R.string.register_fail)
                }
            })
        }
    }
}