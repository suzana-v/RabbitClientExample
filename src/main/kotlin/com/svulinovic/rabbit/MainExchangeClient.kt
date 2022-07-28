package com.svulinovic.rabbit

import io.micronaut.rabbitmq.annotation.Binding
import io.micronaut.rabbitmq.annotation.RabbitClient

@RabbitClient(Constants.Exchange.MAIN)
interface MainExchangeClient {

    fun send(
        @Binding binding: String,
        data: String
    )
}
