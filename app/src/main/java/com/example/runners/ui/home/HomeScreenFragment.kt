package com.example.runners.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.runners.databinding.FragmentHomeScreenBinding


class HomeScreenFragment : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }


//    private fun initSlider() {
//        val imageList = ArrayList<SlideModel>()
//
//
//        imageList.add(
//            SlideModel(
//                "https://static.todamateria.com.br/upload/pr/ov/provasdeatletismonasolimpiadasrio201629032391801-cke.jpg?auto_optimize=low",
//
//                "Atletismo pelo mundo", ScaleTypes.FIT
//            )
//        )
//        imageList.add(
//            SlideModel(
//                "https://www.cnnbrasil.com.br/wp-content/uploads/sites/12/2021/08/Orgulhoso-Alison-dos-Santos-mostra-o-bronze-conquistado-nos-400m-com-barreiras-e1720521749613.jpg?w=1220&h=674&crop=1&quality=50",
//                "Olimpíada: CBAt anuncia atletas do atletismo em Paris 2024 | CNN Brasil.",
//                ScaleTypes.FIT
//            )
//        )
//        imageList.add(
//            SlideModel(
//                "https://medias.itatiaia.com.br/dims4/default/fee8260/2147483647/strip/false/crop/2731x2731+576+0/resize/1000x1000!/quality/90/?url=https%3A%2F%2Fk2-prod-radio-itatiaia.s3.us-east-1.amazonaws.com%2Fbrightspot%2F03%2F66%2F833f95734de48376da6b5ffb56b2%2Fgnpgm1fwwaaw65w.jpg",
//                "Alison dos Santos vence etapa da Diamond League e mostra força para Paris 2024.",
//                ScaleTypes.FIT
//            )
//        )
//
//        val imageSlider = binding.imageSlider
//
//        imageSlider.setImageList(imageList)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}