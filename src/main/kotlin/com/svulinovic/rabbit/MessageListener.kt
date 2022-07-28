package com.svulinovic.rabbit

import com.rabbitmq.client.BasicProperties
import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener
import org.slf4j.LoggerFactory
import java.lang.RuntimeException

@RabbitListener
class MessageListener(
    private val mainExchangeClient: MainExchangeClient,
    private val retryExchangeClient: RetryExchangeClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        var retryCount = 0
        var incorrectHeaders = 0
    }

    @Queue(Constants.Queue.RETRY)
    fun receiveRetry(data: String, basicProperties: BasicProperties) {
        // cleanup
        if (retryCount >= 500) {
            log.info("dropping message, total retryCount exceeded, found $incorrectHeaders incorrectHeaders")
            throw RuntimeException("totalRetryCount exceeded")
        }

        retryCount = ++retryCount

        val routingKey = basicProperties.headers[Constants.Header.ORIGINAL_ROUTING_KEY].toString()

        // received message should contain matching original-routing-key header and message payload
        log.info("Processing message from retry queue; original-routing-key header: $routingKey, message: $data")

        // received message with incorrect header because @RabbitClient is not thread safe
        if (data != routingKey) {
            incorrectHeaders = ++incorrectHeaders
            log.info("dropping message, incorrect header found")
            throw RuntimeException("incorrect header found")
        }

        // sending back to original queue
        mainExchangeClient.send(routingKey, data)
    }

    @Queue(Constants.Queue.FIRST)
    fun receiveFirst(data: String) {
        log.info("Processing message from first queue; message: $data")

        // sending to retry queue with matching original-routing-key header and message payload
        retryExchangeClient.send(Constants.Queue.RETRY, Constants.Queue.FIRST, Constants.Queue.FIRST)
    }

    @Queue(Constants.Queue.SECOND)
    fun receiveSecond(data: String) {
        log.info("Processing message from second queue; message: $data")

        // sending to retry queue with matching original-routing-key header and message
        retryExchangeClient.send(Constants.Queue.RETRY, Constants.Queue.SECOND, Constants.Queue.SECOND)
    }
}
