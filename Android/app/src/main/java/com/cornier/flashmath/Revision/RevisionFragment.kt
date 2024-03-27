package com.cornier.flashmath.Revision

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.cornier.flashmath.R
import com.cornier.flashmath.databinding.FragmentRevisionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Float.min
import java.lang.Math.abs
import kotlin.math.max
import kotlin.math.pow


class RevisionFragment : Fragment() {
    companion object;

    private lateinit var viewModel: RevisionViewModel

    private var _binding: FragmentRevisionBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    // Public var

    var cardSide = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RevisionViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRevisionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialisation de la première carte
        cardTouched()

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

        // Listener for Back Button
        binding.closeButton.setOnClickListener { goBack() }

        // Listener for card click
        binding.cardInfo.setOnTouchListener(
            View.OnTouchListener { view, event ->

                // variables to store current configuration of quote card.
                val displayMetrics = resources.displayMetrics
                val cardWidth = binding.cardInfo.width
                val cardStart = (displayMetrics.widthPixels.toFloat() / 2) - (cardWidth / 2)

                // -250 produces best result, feel free to change to your liking
                val MIN_SWIPE_DISTANCE = -250 // User should move at least -250 from mean position to load new card
                val MAX_SWIPE_DISTANCE = 250

                when (event.action) {

                    MotionEvent.ACTION_MOVE -> {
                        val newX = event.rawX

                        binding.cardInfo.animate()
                            .x(newX - (cardWidth / 2))
                            .rotation((newX - (cardWidth / 2)) / 90)
                            .setDuration(0)
                            .start()
                    }
                    MotionEvent.ACTION_UP -> {
                        var currentX = binding.cardInfo.x

                        binding.cardInfo.animate().x(cardStart).rotation(0F).alpha(1F).setDuration(200).setListener(
                            object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                                        delay(100)
                                        // check if the swipe distance was more than
                                        // minimum swipe required to load a new quote
                                        if (currentX < MIN_SWIPE_DISTANCE) {
                                            // Add logic to load a new quote if swiped adequately
                                            withContext(Dispatchers.Main) {
                                                cardTouched()
                                            }
                                            currentX = 0f
                                        }
                                        if (currentX > MAX_SWIPE_DISTANCE) {
                                            // Add logic to load a new quote if swiped adequately
                                            withContext(Dispatchers.Main) {
                                                cardTouched()
                                            }
                                            currentX = 0f
                                        }
                                    }
                                }
                            }
                        ).start()

                        if (currentX == cardStart) {
                            cardTouched()
                        }
                    }
                }

                // required to by-pass lint warning
                view.performClick()
                return@OnTouchListener true
            }
        )
    }

    private fun cardTouched() {
        // Initialisation temporaire des sides pour premier test
        val sides: Array<String> = arrayOf("Definition de relation d’ordre", "Relation binaire, réflexive, antisymétrique et transitive")

        // Gestion de l'indice
        if (cardSide == 1) {
            cardSide = 0
        } else if (cardSide == 0) {
            cardSide = 1
        }

        // Changement de coté de carte
        binding.cardContent.text = sides[cardSide]
    }

    private fun goBack() {
        requireActivity().onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}