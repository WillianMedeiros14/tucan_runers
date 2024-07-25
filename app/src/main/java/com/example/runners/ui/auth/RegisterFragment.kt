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
import com.example.runners.databinding.FragmentRegisterBinding
import com.example.runners.model.UserCreate
import com.example.runners.ui.adapter.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        initClicks()
    }

    private fun initClicks() {
        binding.btnRegister.setOnClickListener { validateData() }

        binding.btnToLogin.setOnClickListener {
            findNavController().popBackStack();
        }

    }


    private fun validateData() {
        val email = binding.editEmail.text.toString().trim()
        val name = binding.editName.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()
        val confirmPassword = binding.editConfirmPassword.text.toString().trim()

        when {
            email.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe seu e-mail.", Toast.LENGTH_SHORT).show()
            }
            name.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe seu nome.", Toast.LENGTH_SHORT).show()
            }
            password.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe a sua senha.", Toast.LENGTH_SHORT).show()
            }
            confirmPassword.isEmpty() -> {
                Toast.makeText(requireContext(), "Confirme a sua senha.", Toast.LENGTH_SHORT).show()
            }
            password != confirmPassword -> {
                Toast.makeText(requireContext(), "As senhas não correspondem.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                showLoading(true)
                registerUser(email, password, name)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnRegister.isVisible = false
            binding.progressBar.isVisible = true
        } else {
            binding.btnRegister.isVisible = true
            binding.progressBar.isVisible = false
        }
    }

    private fun registerUser(email: String, password: String, name: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val user = UserCreate(email, name, userId)
                        saveUserToDatabase(user)
                    }
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

    private fun saveUserToDatabase(user: UserCreate) {
        val myRef = FirebaseHelper.getDatabase().child("users").child(FirebaseHelper.getIdUser() ?: "")
        myRef.setValue(user).addOnSuccessListener {
            showLoading(false)
//            findNavController().navigate(R.id.)
        }.addOnFailureListener { e ->
            Toast.makeText(
                requireContext(),
                "Erro ao salvar usuário: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            showLoading(false)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}