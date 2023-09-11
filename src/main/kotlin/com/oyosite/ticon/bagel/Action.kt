package com.oyosite.ticon.bagel

/**
 * A framework for storing a method that is applied to a target {@link Sprite} over time.
 * Actions are typically created with the static methods in the
 * ActionFactory class, added to the target Sprite, and will then be applied
 * automatically. Custom Action objects may be created by using lambda
 * expressions that conform to the Function interface, such as:
 * <pre>{@code
 * Action teleportRight = new Action(
 *   (target, deltaTime, totalTime) ->
 *   {
 *     target.moveBy(100, 0);
 *     return true;
 *   }
 * );
 * }</pre>
 */
open class  Action<T: Entity>(val function: Function<T>) {
    /**
     * This interface is used internally by the Action class to encapsulate a
     *  method that will be applied to a target Sprite.
     *
     */
     fun interface Function<T: Entity> {
        /**
         * @param target Entity to which this method will be applied
         * @param deltaTime elapsed time (seconds) since previous iteration of game loop (typically approximately 1/60 second)
         * @param totalTime  the total time that has elapsed since the encapsulating Action has started
         * @return true if the function has completed, false otherwise
         */
        operator fun invoke(target: T, deltaTime: Double, totalTime: Double): Boolean
    }

    var totalTime: Double = 0.0


    /**
     * Increments totalTime by deltaTime and applies function.run method to target.
     * @param target Sprite to which the function.run method will be applied
     * @param deltaTime elapsed time (seconds) since previous iteration of game loop (typically approximately 1/60 second)
     * @return true if the function.invoke method has completed, false otherwise
     */
    operator fun invoke(entity: T, deltaTime: Double): Boolean {
        totalTime+=deltaTime
        return function(entity, deltaTime, totalTime)
    }

    /**
     * Sets [totalTime] to 0, effectively restarting this Action.
     */
    fun reset() {totalTime = 0.0}


}