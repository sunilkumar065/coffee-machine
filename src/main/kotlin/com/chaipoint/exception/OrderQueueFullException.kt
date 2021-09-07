package com.chaipoint.exception

class OrderQueueFullException: Exception {
    constructor(message: String): super(message)
}