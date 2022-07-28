package com.svulinovic.rabbit

import io.micronaut.messaging.annotation.MessageHeader
import io.micronaut.rabbitmq.annotation.Binding
import io.micronaut.rabbitmq.annotation.RabbitClient

@RabbitClient(Constants.Exchange.RETRY)
interface RetryExchangeClient {

    fun send(
        @Binding binding: String,
        @MessageHeader(Constants.Header.ORIGINAL_ROUTING_KEY) originalRoutingKey: String,
        data: String
    )
}
