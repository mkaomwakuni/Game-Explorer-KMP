package org.sea.rawg.features.games

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sea.rawg.domain.usecases.GetGameDetailsUseCase
import org.sea.rawg.ui.viewmodel.GameDetailsViewModel
import org.sea.rawg.ui.viewmodel.HomeViewModel
import org.sea.rawg.ui.viewmodel.GenresViewModel
import org.sea.rawg.ui.viewmodel.PublishersViewModel

/**
 * Game feature module - Contains all game-related functionality
 * 
 * This helps with organizing code by features rather than by technical layers
 */
object GameFeature {
    
    /**
     * Get Koin module for the game feature
     */
    fun getModule(): Module = module {
        // Use cases
        factory { GetGameDetailsUseCase(get()) }
        
        // ViewModels
        factoryOf(::GameDetailsViewModel)
        factoryOf(::HomeViewModel)
        factoryOf(::GenresViewModel)
        factoryOf(::PublishersViewModel)
    }
}