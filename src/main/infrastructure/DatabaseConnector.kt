package infrastructure

import org.jetbrains.exposed.sql.Database
import java.net.URI

object DatabaseConnector {

    private val envUrl: URI
        get(): URI {
            return URI(System.getenv("DATABASE_URL"))
        }

    private val url: String = "jdbc:postgresql://" + envUrl.host + ':' + envUrl.port + envUrl.path

    private val username: String = envUrl.userInfo.split(":")[0]

    private val password: String = envUrl.userInfo.split(":")[1]

    fun connect() {
        Database.connect(url, "org.postgresql.Driver", user = username, password = password)
    }
}