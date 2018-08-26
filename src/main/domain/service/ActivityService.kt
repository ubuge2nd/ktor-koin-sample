package domain.service

import domain.model.Activity
import domain.model.ActivityRepository
import java.util.*

class ActivityService(private val activityRepository: ActivityRepository) {

    fun getRegisteredActivities(): List<Activity> {
        return activityRepository.get()
    }

    fun registerActivity(activityName: String) {
        activityRepository.add(Activity(0, activityName, Date()))
    }
}