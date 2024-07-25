package com.example.runners.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.runners.R
import com.example.runners.databinding.FragmentHomeBinding

import com.example.runners.ui.home.HomeScreenFragment
import com.example.runners.ui.news.NewsFragment
import com.example.runners.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        replaceFragment(HomeScreenFragment())
        navigateScreen()

        initClicks()
    }

    private fun navigateScreen() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeScreenFragment())
                }
                R.id.news -> {
                    replaceFragment(NewsFragment())
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                }
                else -> false
            }
            true
        }
    }



    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout, fragment)

        fragmentTransaction.commit()
    }


    private fun initClicks() {

    }





//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}