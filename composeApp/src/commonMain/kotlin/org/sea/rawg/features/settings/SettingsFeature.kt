package org.sea.rawg.features.settings

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sea.rawg.ui.viewmodel.SettingsViewModel


object SettingsFeature {

    fun getModule(): Module = module {
        
        factoryOf(::SettingsViewModel)
    }
}