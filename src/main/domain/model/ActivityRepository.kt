package domain.model

interface ActivityRepository {

    fun get(): List<Activity>

    fun add(activity: Activity)
}