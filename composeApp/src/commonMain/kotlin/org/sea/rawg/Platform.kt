package org.sea.rawg

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform