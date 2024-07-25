package com.example.runners.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.runners.R
import com.example.runners.databinding.FragmentLoginBinding
import com.example.runners.ui.adapter.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        initClicks()
    }

    private fun initClicks() {

        binding.btnLogin.setOnClickListener { validateData() }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        binding.btnRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                showLoading(true)
                loginUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Informe a sua senha.", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(requireContext(), "Informe seu seu e-mail.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    showLoading(false)
                    findNavController().navigate(R.id.action_global_homeFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        FirebaseHelper.validError(task.exception?.message ?: ""),
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnLogin.isVisible = false
            binding.progressBar.isVisible = true
        } else {
            binding.btnLogin.isVisible = true
            binding.progressBar.isVisible = false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}