package application

import domain.model.ActivityRepository
import domain.service.ActivityService
import infrastructure.PostgresActivityRepository
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.dsl.module.applicationContext
import org.koin.standalone.StandAloneContext.startKoin

fun main(args: Array<String>) {
    startKoin(listOf(appModule))
    embeddedServer(Netty, commandLineEnvironment(args)).start()
}

val appModule = applicationContext {
    factory { PostgresActivityRepository() as ActivityRepository }
    factory { ActivityService(get()) }
}

