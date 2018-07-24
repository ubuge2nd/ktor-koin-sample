package main.domain.service

import main.domain.model.Activity
import main.domain.model.ActivityRepository

class ActivityService(private val activityRepository: ActivityRepository) {

    fun getRegisteredActivities(): List<Activity> {
        return activityRepository.get()
    }
}