package com.chaipoint.service

import com.chaipoint.exception.OrderQueueFullException
import com.chaipoint.model.BeverageRecipe
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class BeverageService(private val outlets: Int) {
    private val semaphore = Semaphore(outlets)

    fun prepareBeverage(beverageRecipe: BeverageRecipe) {
        if(!semaphore.tryAcquire()) {
            throw OrderQueueFullException("Machine working at full capacity.")
        }

        try {
            TimeUnit.MILLISECONDS.sleep(500)
            println("${beverageRecipe.beverage} prepared")
        } catch (e: InterruptedException) {
            println(e.printStackTrace())
        }
        semaphore.release()
    }

    fun canTakeOrder(): Boolean {
        return semaphore.availablePermits() > 0
    }
}