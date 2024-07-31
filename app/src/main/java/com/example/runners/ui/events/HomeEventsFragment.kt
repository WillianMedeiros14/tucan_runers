package com.example.runners.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runners.databinding.FragmentHomeEventsBinding
import com.example.runners.helper.EventsAdapter
import com.example.runners.model.Events
import com.example.runners.ui.adapter.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class HomeEventsFragment : Fragment() {
    private var _binding: FragmentHomeEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter:EventsAdapter

    private val eventList = mutableListOf<Events>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.show()

        getEvents()
    }

    private fun getEvents() {
        FirebaseHelper
            .getDatabase()
            .child("events")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        eventList.clear()
                        for (snap in snapshot.children) {
                                val event = snap.getValue(Events::class.java) as Events
                                eventList.add(event)
                        }

                        binding.loadingMessage.text = ""

                        eventList.reverse()

                        initAdapter()
                    }else{
                          binding.loadingMessage.text = "Nenhum evento encontrado."
                    }

                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error ao buscar os eventos", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun initAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)

        eventAdapter = EventsAdapter(requireContext(), eventList) { event, position ->


            val action = HomeEventsFragmentDirections.actionHomeEventsFragmentToEventsDetailsFragment(event.id, event.name)
            findNavController().navigate(action)

        }

        binding.recyclerView.adapter = eventAdapter
        binding.recyclerView.isVisible = true
    }

}