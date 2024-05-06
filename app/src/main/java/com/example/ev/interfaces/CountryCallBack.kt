package com.example.ev.interfaces

import com.example.ev.data.Country

interface CountryCallBack {
    fun onCountrySelected(country: Country)
}