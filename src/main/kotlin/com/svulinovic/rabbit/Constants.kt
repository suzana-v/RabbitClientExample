package com.svulinovic.rabbit

object Constants {

    object Exchange {
        const val MAIN = "sv.exchange.main"
        const val RETRY = "sv.exchange.retry"
    }

    object Queue {
        const val FIRST = "sv.queue.first"
        const val SECOND = "sv.queue.second"
        const val RETRY = "sv.queue.retry"
    }

    object Header {
        const val ORIGINAL_ROUTING_KEY = "original-routing-key"
    }

}