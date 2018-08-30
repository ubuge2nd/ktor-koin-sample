package infrastructure

import domain.model.Activity
import domain.model.ActivityRepository
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.sql.Connection
import java.util.*

/**
 * アクティビティテーブルの定義データ。
 */
private object Activities: Table() {
    // ID
    val id = long("id").primaryKey().autoIncrement()
    // タイトル
    val title = text("title")
    // 登録日時
    val entryDate = datetime("entry_date")
}

/**
 * Exposedによるアクティビティのリポジトリ。
 *
 * Activitiesテーブルへアクセスしている。
 */
class ExposedActivityRepository(dataBaseConnector: DataBaseConnector): ActivityRepository {

    init {
        dataBaseConnector.connect()
    }

    override fun get(): List<Activity> {
        val results = ArrayList<Activity>()

        transaction(transactionIsolation = Connection.TRANSACTION_SERIALIZABLE, repetitionAttempts = 3) {
            Activities.selectAll().forEach {
                results.add(Activity(it[Activities.id].toLong(), it[Activities.title], it[Activities.entryDate].toDate()))
            }
        }

        return results
    }

    override fun add(activity: Activity) {
        transaction {
            Activities.insert {
                it[title] = activity.title
                it[entryDate] = DateTime(activity.entryDate)
            }
        }
    }
}