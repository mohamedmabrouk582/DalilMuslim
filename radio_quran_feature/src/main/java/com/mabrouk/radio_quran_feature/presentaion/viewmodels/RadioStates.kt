package com.mabrouk.radio_quran_feature.presentaion.viewmodels

import com.mabrouk.radio_quran_feature.domain.models.Radio


/**
 * @name Mohamed Mabrouk
 * Copyrights (c) 06/09/2021 created by Just clean
 */
sealed class RadioStates{
    object Idle : RadioStates()
    data class LoadData(val data:ArrayList<Radio>) : RadioStates()

}
