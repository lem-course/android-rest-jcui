package ep.rest

import java.io.Serializable

data class Book(
    var id: Int = 0,
    var title: String = "",
    var year: Int = 0,
    var author: String = "",
    var description: String = "",
    var price: Double = 0.0
) : Serializable
