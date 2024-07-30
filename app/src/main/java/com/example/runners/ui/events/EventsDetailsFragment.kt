package com.example.runners.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.runners.R
import com.example.runners.databinding.FragmentEventsDetailsBinding
import com.example.runners.databinding.FragmentHomeEventsBinding
import com.example.runners.helper.EventsAdapter
import com.example.runners.helper.EventsDetailsAdapter
import com.example.runners.model.Events
import com.example.runners.ui.adapter.FirebaseHelper
import com.example.runners.ui.news.NewsDetailsFragmentArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class EventsDetailsFragment : Fragment() {
    private var _binding: FragmentEventsDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: EventsDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventTitle = arguments?.getString("eventTitle") ?: "Event Details"

        (activity as? AppCompatActivity)?.supportActionBar?.title = eventTitle

        getArgs()
    }

    private fun getArgs() {
        args.id.let { id ->
                if (id != null) {
                    getEvents(id)
                }
        }
    }

    private fun getEvents(id:String) {
        print(id)
        FirebaseHelper
            .getDatabase()
            .child("events")
            .child(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        for (snap in snapshot.children) {
                            val event = snapshot.getValue(Events::class.java)
                            if (event != null) {
                                bindEventDetails(event)
                                binding.loadingMessage.text = ""
                            } else {
                                binding.loadingMessage.text = "Erro ao converter o evento."
                            }
                        }
                    }else{
                        binding.loadingMessage.text = "Nenhum evento encontrado."
                    }

                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error ao buscar o evento", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun formatTextWithLineBreaks(text: String): String {
        return text.replace("quebraText", "\n\n")
    }

    private fun bindEventDetails(eventData: Events) {
        val aboutText = formatTextWithLineBreaks(eventData.about)
        val registrationsText = formatTextWithLineBreaks(eventData.registrations)

        binding.apply {
            about.text = aboutText
            registrations.text = registrationsText
            Glide.with(requireContext())
                .load(eventData.image)
                .placeholder(R.drawable.image_default)
                .into(image)

            Glide.with(requireContext())
                .load(eventData.route)
                .placeholder(R.drawable.image_default)
                .into(routeImage)

            // Esconder barra de progresso e mensagem de carregamento
            progressBar.visibility = View.GONE
            loadingMessage.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}