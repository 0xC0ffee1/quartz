package net.c0ffee1.db.exposed

import kotlinx.coroutines.future.future
import net.c0ffee1.db.core.impl.CommonHikariDatabase
import net.c0ffee1.db.coroutine.FixedScheduler
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.concurrent.CompletableFuture

open class QuartzExposedDatabase : CommonHikariDatabase() {
    var scheduler: FixedScheduler = FixedScheduler(1)

    override fun initDatabase(): Boolean {
        super.initDatabase()
        Database.connect(getDataSource())
        return true
    }

    fun <T> inTransaction(scheduler: FixedScheduler, operation: () -> T?): CompletableFuture<T?> {
        return scheduler.future {
            newSuspendedTransaction {
                operation()
            }
        }
    }

    fun <T> inTransaction(operation: () -> T?): CompletableFuture<T?> {
        return this.inTransaction(this.scheduler, operation)
    }
}