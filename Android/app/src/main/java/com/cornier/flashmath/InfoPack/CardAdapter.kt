package com.cornier.flashmath.InfoPack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cornier.flashmath.R

class CardAdapter: RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val rootView =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_card, parent, false)

        return CardViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    class CardViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {

        private val card: ConstraintLayout

        init {
            // Define click listener for the ViewHolder's View.
            card = rootView.findViewById(R.id.card_list_item)

            card.setOnClickListener {
                val direction = PackInfoFragmentDirections.actionPackInfoFragmentToNewCardFragment("Modifier")

                rootView.findNavController().navigate(direction)
            }
        }

    }
}