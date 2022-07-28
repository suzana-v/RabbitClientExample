package com.svulinovic.controller

import com.svulinovic.rabbit.MessageListener
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

@Controller
class TestController(
    private val messageListener: MessageListener
) {

    private val latch = CountDownLatch(1)

    @Get("/test")
    fun test() {
        // resetting counters
        MessageListener.retryCount = 0
        MessageListener.incorrectHeaders = 0

        for (i in 0..5) {
            thread(start = true) {
                latch.await()
                messageListener.receiveFirst("")
            }
        }

        for (i in 0..5) {
            thread(start = true) {
                latch.await()
                messageListener.receiveSecond("")
            }
        }

        latch.countDown()
    }
}
