package infrastructure

import domain.model.Activity
import domain.model.ActivityRepository
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*

/**
 * アクティビティテーブルの定義データ。
 */
private object Activities: Table() {
    // ID
    val id = long("id").primaryKey().autoIncrement()
    // タイトル
    val name = text("name")
}

/**
 * PostgreSqlを使用したアクティビティリポジトリ。
 *
 * 接続時にはDBのActivitiesテーブルへアクセスしている。
 */
class PostgresActivityRepository: ActivityRepository {

    init {
        DatabaseConnector.connect()
    }

    override fun get(): List<Activity> {
        val results = ArrayList<Activity>()

        transaction(transactionIsolation = Connection.TRANSACTION_SERIALIZABLE, repetitionAttempts = 3) {
            Activities.selectAll().forEach {
                results.add(Activity(it[Activities.id].toLong(), it[Activities.name], Date()))
            }
        }

        return results
    }

    override fun add(activity: Activity) {
        transaction {
            Activities.insert {
                it[name] = activity.name
            }
        }
    }
}