package com.mabrouk.core.base

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * @name Mohamed Mabrouk
 * Copyright (c) 4/16/22
 */
open class BaseViewModel : ViewModel() {
    val loader: ObservableBoolean = ObservableBoolean()
    val error: ObservableField<String> = ObservableField()
}