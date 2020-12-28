package com.hoanganh.drugstore.Model

import com.hoanganh.drugstore.R

open class Flags(var flag: Int) {
    object Conuntries{
        private val flags = intArrayOf(
                R.drawable.ic_flag_japan,
                R.drawable.ic_flag_vietnam
        )

        var list: ArrayList<Flags>? = null
            get() {

                if (field != null)
                    return field

                field = ArrayList()
                for (i in flags.indices) {

                    val imageId = flags[i]


                    val country = Flags(imageId)
                    field!!.add(country)
                }

                return field
            }

    }

}