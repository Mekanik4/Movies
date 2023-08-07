package com.example.movies.repository.mapper

import android.util.Log
import com.example.movies.domain.Credits
import com.example.movies.domain.Crew
import com.example.movies.domain.Genre
import com.example.movies.domain.GenreList
import com.example.movies.domain.Movie
import com.example.movies.domain.Person
import com.example.movies.repository.entity.CreditsEntity
import com.example.movies.repository.entity.CrewEntity
import com.example.movies.repository.entity.GenreEntity
import com.example.movies.repository.entity.MovieEntity
import com.example.movies.repository.entity.PersonEntity
import com.example.movies.service.model.CreditsModel
import com.example.movies.service.model.CrewModel
import com.example.movies.service.model.GenreListModel
import com.example.movies.service.model.GenreModel
import com.example.movies.service.model.MovieModel
import com.example.movies.service.model.PersonModel
import com.example.movies.service.model.ResponseModel
import io.realm.kotlin.ext.toRealmList
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryMapper @Inject constructor() {
    fun toEntity(movie: Movie): MovieEntity {
        return MovieEntity().apply {
            movieId = movie.movieId
            title = movie.title
            originalTitle = movie.originalTitle
            originalLanguage = movie.originalLanguage
            id = movie.id
            posterPath = movie.posterPath
            backdropPath = movie.backdropPath
            budget = movie.budget.toFloat()
            runtime = movie.runtime
            overview = movie.overview
            voteAverage = movie.voteAverage
            tagline = movie.tagline
            credits = movie.credits?.let { toEntity(it) }!!
            genres = movie.genres.stream().map {
                toEntity(it)
            }.collect(Collectors.toList()).toRealmList()
        }
    }

    private fun toEntity(credits: Credits) : CreditsEntity {
        val credit = CreditsEntity()
        credit.movieId = credits.id
        credit.cast = credits.cast.stream().map {
            toEntity(it)
        }.collect(Collectors.toList()).toRealmList()
        credit.crew = credits.crew.stream().map {
            toEntity(it)
        }.collect(Collectors.toList()).toRealmList()

        return credit
    }

    private fun toEntity(cast: Person) : PersonEntity {
        return PersonEntity().apply {
            name = cast.name
            popularity = cast.popularity
        }
    }

    private fun toEntity(crew: Crew) : CrewEntity {
        return CrewEntity().apply {
            name = crew.name
            job = crew.job
            department = crew.department
        }
    }

    private fun toEntity(genre: Genre): GenreEntity {
        return GenreEntity().apply {
            id = genre.id
            name = genre.name
        }
    }

    fun toDomain(movieEntity: MovieEntity): Movie {
        return Movie().apply {
            movieId = movieEntity.movieId
            title = movieEntity.title
            originalTitle = movieEntity.originalTitle
            originalLanguage = movieEntity.originalLanguage
            id = movieEntity.id
            posterPath = movieEntity.posterPath
            backdropPath = movieEntity.backdropPath
            budget = movieEntity.budget.toLong()
            runtime = movieEntity.runtime
            overview = movieEntity.overview
            voteAverage = movieEntity.voteAverage
            tagline = movieEntity.tagline
            credits = movieEntity.credits?.let { toDomain(it) }
            genres = movieEntity.genres.stream().map {
                toDomain(it)
            }.collect(Collectors.toList())
        }
    }

    fun toDomain(model: ResponseModel) : MutableList<Movie> {
        val movies = mutableListOf<Movie>()
        for (m in model.results) {
            val movie = Movie()
            movie.id = m.id
            movie.title = m.title
            movie.originalLanguage = m.originalLanguage
            movie.originalTitle = m.originalTitle
            movie.overview = m.overview
            movie.posterPath = m.posterPath
            movie.voteAverage = m.voteAverage
            movie.genres = m.genres.stream().map {
                toDomain(it)
            }.collect(Collectors.toList())
            movies.add(movie)
        }
        return movies
    }

    private fun toDomain(genreId: Int): Genre {
        return Genre().apply {
            id = genreId
            name = ""
        }
    }
    fun toDomain(model: MovieModel) : MutableList<Movie> {
        val movies = mutableListOf<Movie>()

        val movie = Movie()
        movie.id = model.id
        movie.title = model.title
        movie.originalLanguage = model.originalLanguage
        movie.originalTitle = model.originalTitle
        movie.overview = model.overview
        movie.posterPath = model.posterPath
        movie.voteAverage = model.voteAverage
        movie.genres = model.genres.stream().map {
            toDomain(it)
        }.collect(Collectors.toList())
        Log.d("serverResult", "toDomain OK")
        movie.runtime = model.runtime
        movie.backdropPath = model.backdropPath
        movies.add(movie)

        return movies
    }

    fun toDomain(model: CreditsModel) : MutableList<Credits> {
        val credits = mutableListOf<Credits>()
        val credit = Credits()
        credit.id = model.id
        credit.cast = model.cast.stream().map {
            toDomain(it)
        }.collect(Collectors.toList())
        credit.crew = model.crew.stream().map {
            toDomain(it)
        }.collect(Collectors.toList())
        credits.add(credit)
        return credits
    }

    private fun toDomain(cast: PersonModel) : Person {
        return Person().apply {
            name = cast.name
            popularity = cast.popularity
        }
    }

    private fun toDomain(crew: CrewModel) : Crew {
        return Crew().apply {
            name = crew.name
            job = crew.job
            department = crew.department
        }
    }

    private fun toDomain(genre: GenreModel) : Genre {
        return Genre().apply {
            id = genre.id
            name = genre.name
        }
    }

    private fun toDomain(entity: CreditsEntity) : Credits {
        val credit = Credits()
        credit.id = entity.movieId
        credit.cast = entity.cast.stream().map {
            toDomain(it)
        }.collect(Collectors.toList())
        credit.crew = entity.crew.stream().map {
            toDomain(it)
        }.collect(Collectors.toList())

        return credit
    }

    private fun toDomain(cast: PersonEntity) : Person {
        return Person().apply {
            name = cast.name
            popularity = cast.popularity
        }
    }

    private fun toDomain(crew: CrewEntity) : Crew {
        return Crew().apply {
            name = crew.name
            job = crew.job
            department = crew.department
        }
    }

    private fun toDomain(genre: GenreEntity) : Genre {
        return Genre().apply {
            id = genre.id
            name = genre.name
        }
    }

    fun toDomain(genresModel: GenreListModel): GenreList {
        return GenreList().apply {
            genres = genresModel.genres.stream().map {
                toDomain(it)
            }.collect(Collectors.toList())
        }
    }
}