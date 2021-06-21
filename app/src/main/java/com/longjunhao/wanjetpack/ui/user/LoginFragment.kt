package com.longjunhao.wanjetpack.ui.user

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.longjunhao.wanjetpack.R
import com.longjunhao.wanjetpack.databinding.FragmentLoginBinding
import com.longjunhao.wanjetpack.util.SharedPrefObject
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
            viewModel.login().observe(viewLifecycleOwner, Observer { response ->
                if (response.errorCode == 0) {
                    viewModel.isLogin.postValue(true)
                    SharedPrefObject.put(SharedPrefObject.KEY_IS_LOGIN, true)
                    response.data?.let {
                        val name = if (TextUtils.isEmpty(it.nickname)) it.username else it.nickname
                        SharedPrefObject.put(SharedPrefObject.KEY_LOGIN_NAME, name)
                    }
                    findNavController().navigateUp()
                } else {
                    viewModel.isLogin.postValue(false)
                    SharedPrefObject.put(SharedPrefObject.KEY_IS_LOGIN, false)
                    binding.password.error = getString(R.string.login_fail)
                }
            })
        }
    }
}