package com.example.movies.service.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieModel(
    @SerialName("backdrop_path") val backdropPath: String?,
    val budget: Long,
    @SerialName("genres") val genres: List<GenreModel>,
//    val homepage: String? = null,
    val id: Int,
//    @SerialName("imdb_id") val imdbId: String? = null,
    val title: String,
    val runtime: Int? = null,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("vote_average") val voteAverage: Float,
//    @SerialName("vote_count") val voteCount: Int,
//    @SerialName("external_ids") val externalIds: TmdbExternalIds? = null,
//    val status: TmdbMovieStatus,
    val tagline: String,
//    val video: Boolean,
//    val popularity: Float,
//    @SerialName("release_date")
//    @Serializable(LocalDateSerializer::class)
//    val releaseDate: LocalDate?,
//    val revenue: Long,
//    @SerialName("release_dates") val releaseDates: TmdbResult<TmdbReleaseDates>? = null,
//    @SerialName("production_companies") val productionCompanies: List<TmdbCompany>? = null,
//    @SerialName("production_countries") val productionCountries: List<TmdbCountry>? = null,
//    @SerialName("watch/providers") val watchProviders: TmdbWatchProviderResult? = null,
    @SerialName("credits") val credits: CreditsModel? = null,
//    @SerialName("videos") val videos: TmdbResult<TmdbVideo>? = null,
//    @SerialName("images") val images: TmdbImages? = null
)

@Serializable
data class AllMoviesModel(
    @SerialName("genre_ids") val genres: List<Int>,
    val id: Int,
    val title: String,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("vote_average") val voteAverage: Float
)

@Serializable
data class TmdbGenre(
    val id: Int,
    val name: String
)

@Serializable
enum class TmdbMovieStatus(val value: String) {
    @SerialName("Rumored")
    RUMORED("Rumored"),

    @SerialName("Planned")
    PLANNED("Planned"),

    @SerialName("In Production")
    IN_PRODUCTION("In Production"),

    @SerialName("Post Production")
    POST_PRODUCTION("Post Production"),

    @SerialName("Released")
    RELEASED("Released"),

    @SerialName("Canceled")
    CANCELED("Canceled");

    companion object {
        fun find(value: String?) = values().find { it.value == value }
    }
}

//internal class LocalDateSerializer : KSerializer<LocalDate?> {
//
//    private val delegate = String.serializer().nullable
//
//    override val descriptor: SerialDescriptor get() = delegate.descriptor
//    override fun deserialize(decoder: Decoder): LocalDate? = delegate.deserialize(decoder)?.tryLocalDate()
//    override fun serialize(encoder: Encoder, value: LocalDate?) = delegate.serialize(encoder, value?.toString())
//}