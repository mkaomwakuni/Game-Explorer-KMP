package org.sea.rawg.domain.usecases

import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.utils.Result

/**
 * Use case for fetching popular games from the repository.
 * 
 * This class encapsulates the business logic for retrieving popular games,
 * following the clean architecture principles by serving as an intermediary
 * between the ViewModel and repository layers.
 */
class GetPopularGamesUseCase(
    private val repository: RawgRepository
) {
    /**
     * Executes the use case to fetch popular games.
     * 
     * Popular games are determined by their "added" count (how many users added them to their lists).
     * 
     * @param page The page number for pagination
     * @param pageSize The number of items per page
     * @return A Result containing the paged response of games or an error
     */
    suspend operator fun invoke(
        page: Int = 1,
        pageSize: Int = 10
    ): Result<PagedResponse<Game>> {
        return repository.getGames(
            page = page,
            pageSize = pageSize,
            ordering = "-added",
            additionalParams = emptyMap()
        )
    }
}