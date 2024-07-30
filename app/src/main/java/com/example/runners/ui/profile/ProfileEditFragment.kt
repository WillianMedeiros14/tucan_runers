package com.example.runners.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.runners.R
import com.example.runners.databinding.FragmentProfileEditBinding
import com.example.runners.databinding.FragmentSplashBinding
import com.example.runners.model.User
import com.example.runners.model.UserCreate
import com.example.runners.ui.AppAuthenticationActivity
import com.example.runners.ui.adapter.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class ProfileEditFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataUser()

        initClicks()
    }


    private fun initClicks() {
        binding.btnSave.setOnClickListener { validateData() }
    }

    private fun getDataUser() {
        auth = Firebase.auth

        FirebaseHelper
            .getDatabase()
            .child("users")
            .child(FirebaseHelper.getIdUser().toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val dataUser = snapshot.getValue(User::class.java)

                        if (dataUser != null) {
                            binding.apply {
                                editEmail.setText(dataUser.email)
                                editName.setText(dataUser.name)
                                editDateOfBirth.setText(dataUser.editDateOfBirth)
                                when (dataUser.gender) {
                                    "Masculino" -> radioGroupGender.check(R.id.radioMale)
                                    "Feminino" -> radioGroupGender.check(R.id.radioFemale)
                                }
                            }
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error ao buscar usuário",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Error ao buscar o usuário",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
    }

    private fun validateData() {
        val editDateOfBirth = binding.editDateOfBirth.text.toString().trim()
        val name = binding.editName.text.toString().trim()

        val selectedGenderId = binding.radioGroupGender.checkedRadioButtonId
        val gender = if (selectedGenderId != -1) {
            val selectedGenderButton = binding.root.findViewById<RadioButton>(selectedGenderId)
            selectedGenderButton.text.toString().trim()
        } else {
            ""
        }
        when {
            name.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe seu nome.", Toast.LENGTH_SHORT).show()
            }
            editDateOfBirth.isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "Informe  sua data de nascimento.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            gender.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe seu gênero.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val user = User(
                    uid = FirebaseHelper.getIdUser() ?: "",
                    email = binding.editEmail.text.toString().trim(),
                    name = name,
                    gender = gender,
                    editDateOfBirth = editDateOfBirth
                )
                showLoading(true)
                saveUserToDatabase(user)


            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSave.isVisible = false
            binding.progressBar.isVisible = true
        } else {
            binding.btnSave.isVisible = true
            binding.progressBar.isVisible = false
        }
    }


    private fun saveUserToDatabase(user: User) {
        val myRef =
            FirebaseHelper.getDatabase().child("users").child(FirebaseHelper.getIdUser() ?: "")
        myRef.setValue(user).addOnSuccessListener {
            showLoading(false)
            navigateToAuthentication()
        }.addOnFailureListener { e ->
            Toast.makeText(
                requireContext(),
                "Erro ao salvar dados usuário: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
            showLoading(false)
        }
    }

    private fun navigateToAuthentication() {
        val intent = Intent(requireContext(), AppAuthenticationActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }



}