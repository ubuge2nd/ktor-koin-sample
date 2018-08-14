package application

import com.sample.main
import io.ktor.application.Application
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import main.domain.model.Activity
import main.domain.model.ActivityRepository
import main.domain.service.ActivityService
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.koin.dsl.module.applicationContext
import org.koin.standalone.StandAloneContext
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

/**
 * アプリケーションのテスト。
 */
class ApplicationTest {
    // アクティビティリポジトリのモック
    private lateinit var mockActivityRepository: ActivityRepository
    // アクティビティのテストデータ
    private val testActivities = mutableListOf<Activity>(Activity(0, "test1", Date()), Activity(1, "test2", Date()))

    // テストモジュール
    private val testModule = applicationContext {
        factory { mockActivityRepository }
        factory { ActivityService(get()) }
    }

    @Before
    fun setUp() {

        mockActivityRepository = mock(ActivityRepository::class.java)

        StandAloneContext.startKoin(listOf(testModule))
    }

    @After
    fun tearDown() {

        StandAloneContext.closeKoin()
    }

    @Test
    fun request_getRoot() = withTestApplication(Application::main) {

        // arrange
        val method = io.ktor.http.HttpMethod.Get
        val path = "/"

        // act
        with(handleRequest(method, path)) {

            // assert
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.content!!.contains("アクティビティを記録"))
        }
    }

    @Test
    fun request_postActivity() = withTestApplication(Application::main) {

        // arrange
        val method = io.ktor.http.HttpMethod.Post
        val path = "/activity"

        // act
        with(handleRequest(method, path)) {

            // assert
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.content!!.contains("記録しました。"))
        }
    }

    @Test
    fun request_getActivity()  = withTestApplication(Application::main) {

        // arrange
        `when`(mockActivityRepository.get()).thenReturn(testActivities)

        val method = io.ktor.http.HttpMethod.Get
        val path = "/activity"

        // act
        with(handleRequest(method, path)) {

            // assert
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.content!!.contains("記録されたアクティビティ一覧"))
            assertTrue(response.content!!.contains("test1"))
            assertTrue(response.content!!.contains("test2"))
        }
    }

    @Test
    fun request_getActivity_andActivitiesIsEmpty()  = withTestApplication(Application::main) {

        // arrange
        `when`(mockActivityRepository.get()).thenReturn(ArrayList<Activity>())
        val method = io.ktor.http.HttpMethod.Get
        val path = "/activity"

        // act
        with(handleRequest(method, path)) {

            // assert
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(response.content!!.contains("記録はありません。"))
        }
    }

    // TODO: nullでなく404を返させるようにする
    @Test
    fun request_invalidPath() = withTestApplication(Application::main) {

        // arrange
        val method = io.ktor.http.HttpMethod.Get
        val path = "/invalid_path"

        // act
        with(handleRequest(method, path)) {

            // assert
            assertNull(response.status())
        }
    }
}