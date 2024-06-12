package net.c0ffee1.db.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext
import java.util.concurrent.*


/**
 * A simple scheduler designed to run tasks in order asynchronously using kotlin coroutine channels
 * Useful in Minecraft environments, where database operations must not block the main thread,
 * but still have to be executed in order
 */

open class FixedScheduler<T : Enum<T>>(nThreads: Int, private val enumClass: Class<T>) : CoroutineScope {
    private val executorService: ExecutorService = Executors.newFixedThreadPool(nThreads)
    private val resourceChannels = ConcurrentHashMap<T, Channel<Task<T>>>()
    private val defaultChannel = Channel<Task<T>>(Channel.UNLIMITED) // Default channel for tasks without specific resources
    override val coroutineContext: CoroutineContext = executorService.asCoroutineDispatcher()
    init {
        init()
    }

    private fun init(){
        enumClass.enumConstants.forEach { resource ->
            val channel = Channel<Task<T>>(Channel.UNLIMITED)
            resourceChannels[resource] = channel
            launch {
                for (task in channel) {
                    executeTask(task)
                }
            }
        }
        launch {
            for (task in defaultChannel) {
                executeTask(task)
            }
        }
    }

    fun <R> inFuture(callable: Callable<R>, vararg requiredResources: T): CompletableFuture<R> {
        val future = CompletableFuture<R>()
        val task = Task({ callable.call() as Any }, requiredResources.toList(), future as CompletableFuture<Any?>)
        if (requiredResources.isEmpty()) {
            // Send to default channel if no resources specified
            launch {
                defaultChannel.send(task)
            }
        } else {
            // Send to specific resource channels
            launch {
                requiredResources.forEach { resource ->
                    resourceChannels[resource]?.send(task)
                }
            }
        }
        return future
    }

    private fun executeTask(task: Task<T>) {
        try {
            val result = task.callable.call()
            task.future.complete(result)
        } catch (e: Exception) {
            task.future.completeExceptionally(e)
            println("Task failed with exception: $e")
        }
    }

    fun shutdown() {
        coroutineContext.cancel()
        executorService.shutdown()
        // Close all channels
        resourceChannels.values.forEach { it.close() }
    }
}

class Task<T : Enum<T>>(val callable: Callable<Any>, val requiredResources: List<T>, val future: CompletableFuture<Any?>) {

}