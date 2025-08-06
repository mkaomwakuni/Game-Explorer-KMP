package org.sea.rawg.features.games

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sea.rawg.domain.usecases.GetGameDetailsUseCase
import org.sea.rawg.ui.viewmodel.GameDetailsViewModel
import org.sea.rawg.ui.viewmodel.HomeViewModel
import org.sea.rawg.ui.viewmodel.GenresViewModel
import org.sea.rawg.ui.viewmodel.PublishersViewModel


object GameFeature {
    
    fun getModule(): Module = module {
        
        factory { GetGameDetailsUseCase(get()) }
        
        
        factoryOf(::GameDetailsViewModel)
        factoryOf(::HomeViewModel)
        factoryOf(::GenresViewModel)
        factoryOf(::PublishersViewModel)
    }
}