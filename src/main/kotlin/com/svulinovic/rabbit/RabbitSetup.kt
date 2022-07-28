package com.svulinovic.rabbit

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import io.micronaut.rabbitmq.connect.ChannelInitializer
import jakarta.inject.Singleton
import java.io.IOException

@Singleton
class RabbitSetup : ChannelInitializer() {

    @Throws(IOException::class)
    override fun initialize(channel: Channel) {
        setupMainExchange(channel)
        setupRetryExchange(channel)
    }

    private fun setupMainExchange(channel: Channel) {
        channel.exchangeDeclare(Constants.Exchange.MAIN, BuiltinExchangeType.DIRECT)
        channel.queueDeclare(Constants.Queue.FIRST, false, false, false, null)
        channel.queueDeclare(Constants.Queue.SECOND, false, false, false, null)
        channel.queueBind(
            Constants.Queue.FIRST,
            Constants.Exchange.MAIN,
            Constants.Queue.FIRST
        )
        channel.queueBind(
            Constants.Queue.SECOND,
            Constants.Exchange.MAIN,
            Constants.Queue.SECOND
        )
    }

    private fun setupRetryExchange(channel: Channel) {
        channel.exchangeDeclare(Constants.Exchange.RETRY, BuiltinExchangeType.DIRECT)
        channel.queueDeclare(Constants.Queue.RETRY, false, false, false, null)
        channel.queueBind(
            Constants.Queue.RETRY,
            Constants.Exchange.RETRY,
            Constants.Queue.RETRY
        )
    }

}
