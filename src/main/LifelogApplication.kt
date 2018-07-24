package com.sample

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.html.respondHtml
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.*
import main.infrastructure.ActivityDataSource
import main.domain.service.ActivityService

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}

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
                    form(action = "/activity", method = FormMethod.post) {
                        p {
                            textInput(name = "activity") { value = "" }
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
            call.respond("記録しました。")
        }

        get("/activity") {
            val activityService = ActivityService(ActivityDataSource())
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

