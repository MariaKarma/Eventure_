package com.example.ev.bottom_sheet_dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.ev.R
import com.example.ev.adapters.CountryAdapter
import com.example.ev.data.Country
import com.example.ev.databinding.CountriesBottomSheetBinding
import com.example.ev.interfaces.CountryCallBack
import com.example.ev.utils.AppController
import com.example.ev.utils.DividerItemDecorator
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CountryBottomSheet(val callBack: CountryCallBack) : BottomSheetDialogFragment() {

    private lateinit var binding: CountriesBottomSheetBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomrec.addItemDecoration(
            DividerItemDecorator(
                view.context, R.drawable.divider

            )
        )
        val adapter = CountryAdapter(object : CountryCallBack {
            override fun onCountrySelected(country: Country) {
                callBack.onCountrySelected(country)
                dismiss()
            }
        }, AppController.filterList(""))
        val rec = view.findViewById<RecyclerView>(R.id.bottomrec)
        rec.adapter = adapter
        val searchbar = view.findViewById<EditText>(R.id.searchbar)
        searchbar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                adapter.setFilteredList(AppController.filterList(s.toString()))
                if (adapter.itemCount > 0) {
                    binding.emptystate.visibility = View.GONE

                } else {
                    binding.emptystate.visibility = View.VISIBLE

                }
            }
        })
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.let { bottomSheet ->
                val behavior =
                    com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet)
                val layoutParams = bottomSheet.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                bottomSheet.layoutParams = layoutParams
                behavior.state =
                    com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountriesBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
}
