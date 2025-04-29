package org.sea.rawg.features.settings

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sea.rawg.ui.viewmodel.SettingsViewModel

/**
 * Settings feature module - Contains all settings-related functionality
 *
 * This helps with organizing code by features rather than by technical layers
 */
object SettingsFeature {

    /**
     * Get Koin module for the settings feature
     */
    fun getModule(): Module = module {
        // ViewModels
        factoryOf(::SettingsViewModel)
    }
}