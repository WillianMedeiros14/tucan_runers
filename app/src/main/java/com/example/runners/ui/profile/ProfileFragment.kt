package com.example.runners.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.runners.R
import com.example.runners.databinding.FragmentProfileBinding
import com.example.runners.model.ItemMenuButton
import com.example.runners.ui.AppAuthenticationActivity
import com.example.runners.ui.MainActivity

import com.example.runners.ui.adapter.ItemAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)

        auth = Firebase.auth

        setupRecyclerView()
        initClicks()

        val imageUrl = "https://th.bing.com/th/id/R.773aae0c88a981806f64a8752b0dc11b?rik=ufy45ivCi8gfDA&pid=ImgRaw&r=0"


        Glide.with(this)
            .load(imageUrl)
            .transform(CircleCrop())
            .into(binding.profileImageView)
    }


    private fun initClicks() {
        binding.backButton.setOnClickListener { backButton() }

    }

    private fun setupRecyclerView() {
        val items = listOf(
            ItemMenuButton(R.drawable.person_pesonal_data, "Dados pessoais"),
            ItemMenuButton(R.drawable.security, "Segurança"),
            ItemMenuButton(R.drawable.info, "Sobre"),
            ItemMenuButton(R.drawable.power, "Sair", isLogout = true)

        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        binding.recyclerView.adapter = ItemAdapter(requireContext(), items) { item ->
            if (item.isLogout) {
                logoutApp()
            }
        }

        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerView.context,
            (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
        )
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
    }


    private fun logoutApp() {
        auth.signOut()

        navigateToAuthentication()
    }
    private fun navigateToAuthentication() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun backButton() {
        val fragmentManager = parentFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            findNavController().navigateUp()
        }

    }
}