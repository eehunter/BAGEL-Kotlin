package com.oyosite.ticon.bagel

import javafx.animation.AnimationTimer
import java.util.function.LongConsumer

class BagelAnimationTimer(private val handleConsumer: (Long)->Unit): AnimationTimer() {
    override fun handle(now: Long) = handleConsumer(now)
}