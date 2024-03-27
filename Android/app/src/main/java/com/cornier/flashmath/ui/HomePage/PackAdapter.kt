package com.cornier.flashmath.ui.HomePage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cornier.flashmath.Data.Pack
import com.cornier.flashmath.R
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.ObjectId

class PackAdapter(packs: RealmResults<Pack>): RecyclerView.Adapter<PackAdapter.PackViewHolder>() {

    private val packs = packs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackViewHolder {
        val rootView =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_pack, parent, false)

        return PackViewHolder(rootView, packs)
    }

    override fun onBindViewHolder(holder: PackViewHolder, position: Int) {
        val packName = packs[position].pack_name
        val packDescription = packs[position].pack_description
        val pack = packs[position]._id

        holder.setPackName(packName)
        holder.setPackDescription(packDescription)
        holder.setClickListener(pack.toString())
    }

    override fun getItemCount(): Int {
        return packs.size
    }

    class PackViewHolder(rootView: View, packs: RealmResults<Pack>) : RecyclerView.ViewHolder(rootView) {

        private val pack: ConstraintLayout
        private val packName: TextView
        private val packDescription: TextView
        private val view: View

        init {
            // Define click listener for the ViewHolder's View.
            pack = rootView.findViewById(R.id.pack_list_item)
            packName = rootView.findViewById(R.id.pack_name)
            packDescription = rootView.findViewById(R.id.pack_description)
            view = rootView
        }

        fun setPackName(name: String) {
            packName.text = name
        }

        fun setPackDescription(description: String) {
            packDescription.text = description
        }

        fun setClickListener(packInfo: String) {
            pack.setOnClickListener {

                val direction = HomePageFragmentDirections.actionHomePageFragmentToPackInfoFragment(packInfo)

                view.findNavController().navigate(direction)
            }
        }
    }
}