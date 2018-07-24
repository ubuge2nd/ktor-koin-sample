package main.domain.model

interface ActivityRepository {

    fun get(): List<Activity>
}