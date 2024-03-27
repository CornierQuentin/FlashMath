package com.cornier.flashmath.NewPack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cornier.flashmath.Data.Pack
import com.cornier.flashmath.R
import com.cornier.flashmath.databinding.FragmentNewPackBinding
import com.cornier.flashmath.ui.HomePage.HomePageFragmentDirections
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class NewPack : Fragment() {
    companion object;

    private lateinit var viewModel: NewPackViewModel

    private var _binding: FragmentNewPackBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: NewPackArgs by navArgs()

    val config = RealmConfiguration.Builder(schema = setOf(Pack::class)).deleteRealmIfMigrationNeeded()
        .build()
    val realm: Realm = Realm.open(config)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[NewPackViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set title
        binding.title.text = args.packCreationTitle

        // Listener for Back Button
        binding.backButton.setOnClickListener { goBack() }

        //Listener for add card button
        binding.buttonNewCard.setOnClickListener { goToNewCard() }

        //Listener for done button
        binding.doneButton.setOnClickListener { goToPackInfo() }

        // Initialize values for the access spinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.access_array,
            R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.accessSpinner.adapter = adapter
        }
    }

    private fun goToPackInfo() {

        val pack = Pack().apply {
            pack_name = binding.rectoInput.text.toString()
            pack_description = binding.descriptionInput.text.toString()
        }

        realm.writeBlocking {
            copyToRealm(pack)
        }

        val direction = NewPackDirections.actionNewPackToPackInfoFragment(pack._id.toString())

        findNavController().navigate(direction)
    }

    private fun goToNewCard() {
        val direction = NewPackDirections.actionNewPackToNewCardFragment("Nouvelle")

        findNavController().navigate(direction)
    }

    private fun goBack() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        realm.close()
        _binding = null
    }
}