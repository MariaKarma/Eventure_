package com.example.ev.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.example.ev.databinding.SampleOrBinding

class Or : RelativeLayout {


    constructor(context: Context):super(context){
        initializeView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeView()
    }

    lateinit var binding: SampleOrBinding

    private fun initializeView() {
        binding = SampleOrBinding.inflate(LayoutInflater.from(context), this, true)

    }

}