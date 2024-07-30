package com.example.runners.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.runners.R

import com.example.runners.databinding.FragmentRegistrationEventBinding
import com.example.runners.model.Events
import com.example.runners.model.User
import com.example.runners.ui.adapter.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class RegistrationEventFragment : Fragment() {

    private var _binding: FragmentRegistrationEventBinding? = null
    private val binding get() = _binding!!

    private val args: RegistrationEventFragmentArgs by navArgs()

    private lateinit var auth: FirebaseAuth

    private lateinit var nameUser: String
    private lateinit var dateOfBirth: String
    private lateinit var email: String
    private lateinit var eventId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()

        initClicks()


    }

    private fun getArgs() {
        args.idEvent.let { id ->
            if (id != null) {
                eventId = id
                getEvents(id)
            }
        }
    }

    private fun initClicks() {
        binding.btnSend.setOnClickListener {
            validateAndRegisterUser()
        }

    }

    private fun getEvents(id: String) {
        FirebaseHelper
            .getDatabase()
            .child("events")
            .child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val event = snapshot.getValue(Events::class.java)
                        if (event != null) {

                            binding.apply {
                                nameEvent.text = event.name
                                dateEvent.text = event.date


                                radioGroupDistance.removeAllViews()

                                val uniqueDistances = event.distance.filterNotNull().toSet()
                                for (distance in uniqueDistances) {
                                    val radioButton = RadioButton(context).apply {
                                        text = distance
                                        textSize = 14f
                                        setTextColor(
                                            ContextCompat.getColor(
                                                context,
                                                R.color.shark_950
                                            )
                                        )
                                    }
                                    radioGroupDistance.addView(radioButton)
                                }
                            }

                            getDataUser()
                        } else {
                            binding.loadingMessage.text = "Erro ao converter o evento."
                        }
                    } else {
                        binding.loadingMessage.text = "Nenhum evento encontrado."
                    }

                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Erro ao buscar o evento", Toast.LENGTH_SHORT)
                        .show()
                }
            })
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
                            nameUser = dataUser.name
                            dateOfBirth = dataUser.editDateOfBirth
                            email = dataUser.email

                            binding.apply {
                                when (dataUser.gender) {
                                    "Masculino" -> radioGroupGender.check(R.id.radioMale)
                                    "Feminino" -> radioGroupGender.check(R.id.radioFemale)
                                }
                            }
                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Erro ao buscar dados do usuário",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao buscar o usuário",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }


    private fun validateAndRegisterUser() {
        val editDateOfBirth = dateOfBirth
        val name = nameUser

        val selectedGenderId = binding.radioGroupGender.checkedRadioButtonId
        val gender = if (selectedGenderId != -1) {
            val selectedGenderButton = binding.root.findViewById<RadioButton>(selectedGenderId)
            selectedGenderButton.text.toString().trim()
        } else {
            ""
        }

        val selectedDistanceId = binding.radioGroupDistance.checkedRadioButtonId
        val selectedDistance = if (selectedDistanceId != -1) {
            val selectedDistanceButton = binding.root.findViewById<RadioButton>(selectedDistanceId)
            selectedDistanceButton.text.toString().trim()
        } else {
            ""
        }

        when {
            name.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe seu nome.", Toast.LENGTH_SHORT).show()
            }
            editDateOfBirth.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe sua data de nascimento.", Toast.LENGTH_SHORT).show()
            }
            gender.isEmpty() -> {
                Toast.makeText(requireContext(), "Informe seu gênero.", Toast.LENGTH_SHORT).show()
            }
            selectedDistance.isEmpty() -> {
                Toast.makeText(requireContext(), "Selecione a distância.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val user = User(
                    uid = FirebaseHelper.getIdUser() ?: "",
                    email = email,
                    name = name,
                    gender = gender,
                    editDateOfBirth = editDateOfBirth
                )

                val registration = mapOf(
                    "userId" to user.uid,
                    "userName" to user.name,
                    "gender" to user.gender,
                    "dateOfBirth" to user.editDateOfBirth,
                    "distance" to selectedDistance
                )

                showLoading(true)
                registerUserInEvent(eventId, registration)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.btnSend.isVisible = false
            binding.progressBarSend.isVisible = true
        } else {
            binding.progressBarSend.isVisible = true
            binding.progressBar.isVisible = false
        }
    }

    private fun registerUserInEvent(eventId: String, registration: Map<String, Any>) {
        val myRef = FirebaseHelper.getDatabase().child("events").child(eventId).child("registrationEvents").child(FirebaseHelper.getIdUser() ?: "")
        myRef.setValue(registration).addOnSuccessListener {
            showLoading(false)
            Toast.makeText(requireContext(), "Inscrição realizada com sucesso!", Toast.LENGTH_SHORT).show()
            navigateHome(eventId)
        }.addOnFailureListener { e ->
            Toast.makeText(requireContext(), "Erro ao realizar inscrição: ${e.message}", Toast.LENGTH_SHORT).show()
            showLoading(false)
        }
    }




    private fun navigateHome(eventId: String) {
        findNavController().navigate(R.id.action_registrationEventFragment_to_homeScreenFragmentMain)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
