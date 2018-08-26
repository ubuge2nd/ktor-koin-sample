package domain.service

import com.nhaarman.mockito_kotlin.*
import domain.model.Activity
import domain.model.ActivityRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
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

        mockActivityRepository = mock<ActivityRepository> {}

        activityService = ActivityService(mockActivityRepository)
    }

    @Test
    fun getRegisteredActivities_whenRepositoryHasActivities() {

        // arrange
        whenever(mockActivityRepository.get()).thenReturn(testActivities)

        // act
        val result = activityService.getRegisteredActivities()

        // assert
        assertFalse(result.isEmpty())
        assertEquals(testActivities, result)
    }

    @Test
    fun getRegisteredActivities_whenRepositoryDoesNotHaveActivity_isEmpty() {

        // arrange
        val emptyActivity = ArrayList<Activity>()
        whenever(mockActivityRepository.get()).thenReturn(emptyActivity)

        // act
        val result = activityService.getRegisteredActivities()

        // assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun registerActivity() {

        // act
        activityService.registerActivity("test")

        // assert
        argumentCaptor<Activity>().apply {

            // ActivityRepositoryがアクティビティを保存している
            verify(mockActivityRepository, times(1)).add(capture())

            assertEquals(1, allValues.size)
            assertEquals(0, firstValue.id) // idは常に0となる
            assertEquals("test", firstValue.name)
        }
    }
}