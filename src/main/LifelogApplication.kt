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
                    form(action = "/activity", method = FormMethod.post) {
                        p {
                            textInput(name = "activity") { value = "" }
                        }
                        p {
                            submitInput { value = "記録する" }
                        }
                    }
                }
            }
        }

        post("/activity") {
            call.respond("記録しました。")
        }
    }
}

