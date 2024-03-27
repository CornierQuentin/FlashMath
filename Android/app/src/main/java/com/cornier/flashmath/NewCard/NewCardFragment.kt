package com.cornier.flashmath.NewCard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cornier.flashmath.NewPack.NewPackArgs
import com.cornier.flashmath.NewPack.NewPackViewModel
import com.cornier.flashmath.R
import com.cornier.flashmath.databinding.FragmentNewCardBinding
import com.cornier.flashmath.databinding.FragmentNewPackBinding

class NewCardFragment : Fragment() {
    companion object;

    private lateinit var viewModel: NewCardViewModel

    private var _binding: FragmentNewCardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val args: NewCardFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[NewCardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set title
        binding.title.text = args.cardCreationTitle

        // Listener for Back Button
        binding.backButton.setOnClickListener { goBack() }

        //Listener for done button
        binding.doneButton.setOnClickListener { goToPackInfo() }
    }

    private fun goToPackInfo() {
        findNavController().navigate(R.id.action_newCardFragment_to_packInfoFragment)
    }

    private fun goBack() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}