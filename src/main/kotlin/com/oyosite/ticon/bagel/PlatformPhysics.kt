package com.oyosite.ticon.bagel

/**
 * Extension of the [Physics] class,
 * useful in games with a side-view perspective and platform games in particular.
 * In this context, [Physics.accelerationValue], [Physics.maximumSpeed],
 * and [Physics.decelerationValue] refer to horizontal movement;
 * this class includes additional variables to quantify jump strength,
 * the force of gravity (which constantly accelerates an object downwards),
 * and terminal velocity (maximum vertical speed).
 *
 */
open class PlatformPhysics(
    accValue: Double, maxHorizontalSpeed: Double, decValue: Double,
    /**initial vertical velocity when jumping*/
    var jumpSpeed: Double = 450.0,
    /** constant downwards acceleration*/
    var gravity: Double = 700.0,
    /**maximum vertical speed*/
    val terminalVelocity: Double = 1000.0
) :
    Physics(accValue, maxHorizontalSpeed, decValue) {
    // suggested values: 450, 700, 1000

    /**Simulate jumping, using value stored in [jumpSpeed].*/
    open fun jump() { velocityVector.y = -jumpSpeed }

    /**
     * Update the position of this object
     * according to velocity, acceleration, and gravity.
     * Deceleration is applied if no acceleration (other than gravity) is present.
     * Horizontal speed is bounded by [Physics.maximumSpeed]
     * and vertical speed is bounded by [PlatformPhysics.terminalVelocity].
     * @param dt elapsed time (seconds) since previous iteration of game loop
     * (typically approximately 1/60 second)
     */
    override fun update(dt: Double) {
        // decrease walk speed (decelerate) when not accelerating
        if (accelerationVector.length < 0.001) {
            val decelerationAmount: Double = decelerationValue * dt
            val walkDirection: Double
            walkDirection = if (velocityVector.x > 0) 1.0 else -1.0
            var walkSpeed: Double = Math.abs(velocityVector.x)
            walkSpeed -= decelerationAmount
            if (walkSpeed < 0) walkSpeed = 0.0
            velocityVector.x = walkSpeed * walkDirection
        }

        // apply gravity
        accelerationVector.addValues(0.0, gravity)

        // apply acceleration
        velocityVector.addValues(
            accelerationVector.x * dt,
            accelerationVector.y * dt
        )
        if (velocityVector.x < -maximumSpeed) velocityVector.x = -maximumSpeed
        if (velocityVector.x > maximumSpeed) velocityVector.x = maximumSpeed
        if (velocityVector.y < -terminalVelocity) velocityVector.y = -terminalVelocity
        if (velocityVector.y > terminalVelocity) velocityVector.y = terminalVelocity

        // update position according to value stored in velocity vector
        positionVector.addValues(
            velocityVector.x * dt,
            velocityVector.y * dt
        )

        // reset acceleration
        accelerationVector.setValues(0.0, 0.0)
    }
}