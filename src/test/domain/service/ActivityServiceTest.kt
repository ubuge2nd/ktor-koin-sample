package domain.service

import main.domain.model.Activity
import main.domain.model.ActivityRepository
import main.domain.service.ActivityService
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

/**
 * アクティビティサービスのテスト。
 */
class ActivityServiceTest {
    // アクティビティサービス
    private lateinit var activityService: ActivityService
    // アクティビティリポジトリのモック
    private lateinit var mockActivityRepository: ActivityRepository
    // アクティビティのテストデータ
    private val testActivities = mutableListOf<Activity>(Activity(0, "test1", Date()), Activity(1, "test2", Date()))

    @Before
    fun setUp() {

        mockActivityRepository = mock(ActivityRepository::class.java)

        activityService = ActivityService(mockActivityRepository)
    }

    @Test
    fun getRegisteredActivities_whenRepositoryHasActivities() {

        // arrange
        `when`(mockActivityRepository.get()).thenReturn(testActivities)

        // act
        val result = activityService.getRegisteredActivities()

        // assert
        assertEquals(testActivities, result)
    }

    @Test
    fun getRegisteredActivities_whenRepositoryDoesNotHaveActivity_isEmpty() {

        // arrange
        `when`(mockActivityRepository.get()).thenReturn(ArrayList<Activity>())

        // act
        val result = activityService.getRegisteredActivities()

        // assert
        assertTrue(result.isEmpty())
    }
}