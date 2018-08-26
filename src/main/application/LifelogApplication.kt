package application

import domain.service.ActivityService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.PartData
import io.ktor.features.CallLogging
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.request.isMultipart
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import kotlinx.html.*
import org.koin.ktor.ext.inject

/**
 * メインのアプリケーション。
 */
fun Application.main() {
    install(CallLogging)

    install(Routing) {

        get("/") {
            call.respondHtml {
                body {
                    h2 { text("アクティビティを記録") }
                    form(action = "/activity", encType = FormEncType.multipartFormData, method = FormMethod.post) {
                        p {
                            textInput(name = "activityName") { value = "" }
                        }
                        p {
                            submitInput { value = "記録する" }
                        }
                    }
                    form(action = "/activity", method = FormMethod.get) {
                        p {
                            submitInput { value = "一覧を見る" }
                        }
                    }
                }
            }
        }

        post("/activity") {
            val multipart = call.receiveMultipart()
            var activityName: String = ""

            while (true) {
                val part = multipart.readPart() ?: break
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "activityName") {
                            activityName = part.value
                        }
                    }
                }
            }

            if (activityName.isEmpty()) {
                call.respond("記録するデータがありませんでした。")
            } else {
                val activityService: ActivityService by inject()
                activityService.registerActivity(activityName)
                call.respond("記録しました。")
            }
        }

        get("/activity") {
            val activityService: ActivityService by inject()
            val activities = activityService.getRegisteredActivities()
            if(activities.isEmpty()) {
                call.respond("記録はありません。")
            } else {
                call.respondHtml {
                    body {
                        h2 { text("記録されたアクティビティ一覧") }
                        table {
                            for(i in 0..(activities.size - 1)) {
                                tr {
                                    td { text(activities[i].id) }
                                    td { text(activities[i].name) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}