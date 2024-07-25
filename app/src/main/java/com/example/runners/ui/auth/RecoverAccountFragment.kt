package com.example.runners.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.runners.databinding.FragmentRecoverAccountBinding
import com.example.runners.ui.adapter.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RecoverAccountFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        initClicks()
    }

    private fun initClicks() {

        binding.btnSend.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString().trim()

        if (email.isNotEmpty()) {
            showLoading(true)
            recoverAccountUser(email)

        } else {
            Toast.makeText(requireContext(), "Informe seu seu -email.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSend.isVisible = false
            binding.progressBar.isVisible = true
        } else {
            binding.btnSend.isVisible = true
            binding.progressBar.isVisible = false
        }
    }

    private fun recoverAccountUser(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Pronto, acabamos de enviar um link para seu email.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        FirebaseHelper.validError(task.exception?.message ?: ""),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                showLoading(false)
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}