package com.svulinovic

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
    // for application to start rabbitmq must be available
    // docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.10-management

    run(*args)
}
