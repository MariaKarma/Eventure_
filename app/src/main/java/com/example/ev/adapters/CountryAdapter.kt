package com.example.ev.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ev.R
import com.example.ev.data.Country
import com.example.ev.interfaces.CountryCallBack
import com.example.ev.utils.TextDrawable
import java.io.FileNotFoundException

class CountryAdapter(private val countryCallBack: CountryCallBack, private var list: List<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {


    fun setFilteredList(countries: List<Country>) {
        list = countries
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return CountryViewHolder(countryCallBack, view)


    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        try {
            val country = list[position]
            holder.bind(country)
        } catch (_: FileNotFoundException) {


        }


    }

    override fun getItemCount(): Int {
        return list.size

    }


    class CountryViewHolder(private val countryCallBack: CountryCallBack, view: View) :
        RecyclerView.ViewHolder(view) {
        private val img = view.findViewById<ImageView>(R.id.imageView3)
        private val txt = view.findViewById<TextView>(R.id.textView6)
        private val txtcode = view.findViewById<TextView>(R.id.textcode)

        fun bind(country: Country) {
            itemView.setOnClickListener {
                countryCallBack.onCountrySelected(country)
            }
            txt.text = country.name
            txtcode.text = country.dialcode
            val countryimg2 = "flags/" + country.code.lowercase() + ".png"

            Glide.with(itemView)
                .load("file:///android_asset/$countryimg2")
                .placeholder(TextDrawable(country.code))
                .into(img)


        }

    }
}