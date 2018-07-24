package main.infrastructure

import main.domain.model.Activity
import main.domain.model.ActivityRepository
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.util.*

/**
 * アクティビティテーブルの定義データ。
 */
private object Activities: Table() {
    // ID
    val id=integer("id").primaryKey()
    // タイトル
    val name=text("name")
}

/**
 * PostgreSqlを使用したアクティビティリポジトリ。
 *
 * 接続時にはDBのActivitiesテーブルから取得している。
 */
class ActivityDataSource: ActivityRepository {

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
}