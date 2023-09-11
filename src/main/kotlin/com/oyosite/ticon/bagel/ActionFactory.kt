package com.oyosite.ticon.bagel

object ActionFactory {
    /**
     * Create an Action to move a Sprite by a fixed amount over a period of time.
     * <br></br>This Action is complete once movement has finished.
     * @param deltaX distance to move Sprite in the x-direction
     * @param deltaY distance to move Sprite in the y-direction
     * @param duration total time to be used for movement
     * @return Action to move a Sprite by a fixed amount over a period of time.
     */
    fun moveBy(deltaX: Double, deltaY: Double, duration: Double): Action<Sprite> {
        return Action { target: Sprite, deltaTime: Double, totalTime: Double ->
            target.moveBy(deltaX / duration * deltaTime, deltaY / duration * deltaTime)
            totalTime >= duration
        }
    }
}