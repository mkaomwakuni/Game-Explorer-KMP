package org.sea.rawg.domain.usecases

import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.utils.Result


class GetPopularGamesUseCase(
    private val repository: RawgRepository
) {
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