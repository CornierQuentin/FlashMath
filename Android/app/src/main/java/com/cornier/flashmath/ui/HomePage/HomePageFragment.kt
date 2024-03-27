package com.cornier.flashmath.ui.HomePage

import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cornier.flashmath.Data.Pack
import com.cornier.flashmath.R
import com.cornier.flashmath.databinding.FragmentHomepageBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults

class HomePageFragment : Fragment() {

    companion object {
        fun newInstance() = HomePageFragment()
    }

    private lateinit var viewModel: HomePageViewModel

    private var _binding: FragmentHomepageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val config = RealmConfiguration.Builder(schema = setOf(Pack::class)).deleteRealmIfMigrationNeeded()
        .build()
    val realm: Realm = Realm.open(config)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
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

        // Lier l'adapter au recyclerView

        // all items in the realm
        val packs: RealmResults<Pack> = realm.query<Pack>().find()
        binding.packList.adapter = PackAdapter(packs)
        binding.packList.layoutManager = LinearLayoutManager(requireContext())

        // Listener for New Pack button
        binding.fab.setOnClickListener { onNewPackButtonClick() }
    }

    private fun onNewPackButtonClick() {

        val direction = HomePageFragmentDirections.actionHomePageFragmentToNewPack("Nouveau")

        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        realm.close()
        _binding = null
    }

}
