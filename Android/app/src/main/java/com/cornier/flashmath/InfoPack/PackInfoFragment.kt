package com.cornier.flashmath.InfoPack

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornier.flashmath.Data.Pack
import com.cornier.flashmath.R
import com.cornier.flashmath.databinding.FragmentPackInfoBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.ObjectId

class PackInfoFragment : Fragment() {
    companion object;

    private lateinit var viewModel: PackInfoViewModel

    private var _binding: FragmentPackInfoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val config = RealmConfiguration.Builder(schema = setOf(Pack::class)).deleteRealmIfMigrationNeeded()
        .build()
    val realm: Realm = Realm.open(config)

    private val args: PackInfoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[PackInfoViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPackInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set text color to main gradient
        val paint = binding.homepageTitle.paint
        val width = paint.measureText(binding.homepageTitle.text.toString())
        // width/2 pour mettre le point de départ au milieu en haut et le point d'arrivée au milieu en bas
        // Sinon on a un dégradé de gauche à droite.
        // LinearGradient(xDébut, yDébut, xFin, yFin, intArrayOf(
        val textShader: Shader = LinearGradient(width/2, 0f, width/2, binding.homepageTitle.textSize, intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.main_gradient_light),
            ContextCompat.getColor(requireContext(), R.color.main_gradient_dark)
        ), null, Shader.TileMode.REPEAT)

        binding.homepageTitle.paint.shader = textShader

        // Récupération du pack dans Realm
        val packs: Pack? = realm.query<Pack>("_id == $0", ObjectId.from(args.pack)).first().find()

        // Lier l'adapter au recyclerView
        binding.cardList.adapter = CardAdapter()
        binding.cardList.layoutManager = LinearLayoutManager(requireContext())

        // Listener for Back Button
        binding.backButton.setOnClickListener { goBack() }

        // Listener for Edit Button
        binding.editButton.setOnClickListener { goToEditInfo() }

        // Listener for Add Card Button
        binding.buttonNewCard.setOnClickListener { goToNewCard() }

        // Listener for play button
        binding.playButton.setOnClickListener { gotToRevision() }

        // Changer le nom du paquet
        binding.packName.text = packs?.pack_name

        // Change la description
        binding.packDescription.text = packs?.pack_description
    }

    private fun gotToRevision() {
        findNavController().navigate(R.id.action_packInfoFragment_to_revisionFragment)
    }

    private fun goToNewCard() {
        val direction = PackInfoFragmentDirections.actionPackInfoFragmentToNewCardFragment("Nouvelle")

        findNavController().navigate(direction)
    }

    private fun goToEditInfo() {
        val direction = PackInfoFragmentDirections.actionPackInfoFragmentToNewPack("Modifier")

        findNavController().navigate(direction)
    }

    private fun goBack() {
        findNavController().navigate(R.id.action_packInfoFragment_to_homePageFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}