package net.c0ffee1.db.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.future.future
import org.slf4j.LoggerFactory
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * A scheduler designed to run tasks in order asynchronously
 * Useful in Minecraft environments, where database operations must not block the main thread,
 * but still have to be executed in order
 */

//Left for compability purposes

@Deprecated("Use FixedScheduler(1)")
class OrderedDatabaseScheduler<T : Enum<T>> (enumClass: Class<T>) : FixedScheduler<T>( 1, enumClass) {
}