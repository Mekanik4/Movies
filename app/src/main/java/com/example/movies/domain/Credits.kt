package com.example.movies.domain

import com.example.movies.service.model.TmdbCrewJobType
import kotlinx.serialization.Serializable

@Serializable
class Credits(
    var id: Int = 0,
    var cast: List<Person> = ArrayList(),
    var crew: List<Crew> = ArrayList()
) {
    fun getSortedCrew(): List<Crew> {
        val jobsSet = TmdbCrewJobType.importantJobs.toSet()
        val orderByJob = TmdbCrewJobType.importantJobs.withIndex().associate { it.value to it.index }
        return crew.filter { jobsSet.contains(it.job) }.sortedBy { orderByJob[it.job] }
    }

    fun getSortedCast(): List<Person> {
        return cast.sortedByDescending { it.popularity }
    }
}


@Serializable
class Person(
    var name: String = "",
    var popularity: Float = 0f
)

@Serializable
class Crew(
    var name: String = "",
    var job: String = "",
    var department: String = ""
)