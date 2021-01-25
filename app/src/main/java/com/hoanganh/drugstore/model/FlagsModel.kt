package com.hoanganh.drugstore.model

import com.hoanganh.drugstore.R

open class FlagsModel(var flag: Int) {
    object Conuntries{
        private val flags = intArrayOf(
                R.drawable.ic_flag_japan,
                R.drawable.ic_flag_vietnam,
                R.drawable.ic_flag_usa,
                R.drawable.ic_flag_spain,
                R.drawable.ic_flag_south_korea,
                R.drawable.ic_flag_italy,
                R.drawable.ic_flag_germany,
                R.drawable.ic_flag_france,
                R.drawable.ic_flag_china,
                R.drawable.ic_flag_britain,
        )

        var list: ArrayList<FlagsModel>? = null
            get() {

                if (field != null)
                    return field

                field = ArrayList()
                for (i in flags.indices) {

                    val imageId = flags[i]


                    val country = FlagsModel(imageId)
                    field!!.add(country)
                }

                return field
            }

    }

}