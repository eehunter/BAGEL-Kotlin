package com.oyosite.ticon.bagel

open class Physics(val accelerationValue: Double, val maximumSpeed: Double, val decelerationValue: Double) {
    val positionVector = Vector2()
    val velocityVector = Vector2()
    val accelerationVector = Vector2()

    var speed by velocityVector::length
    var motionAngle by velocityVector::angle
    var motionAngleRad by velocityVector::angleRad

    open fun bounceAgainst(surfaceAngleDegrees: Number) = bounceAgainstRad(surfaceAngleDegrees.rad)
    open fun bounceAgainstRad(surfaceAngleRadians: Number){ motionAngleRad = 2*surfaceAngleRadians.toDouble()-motionAngleRad }

    open fun accelerateAtAngle(angleDegrees: Number) = accelerateAtAngleRad(angleDegrees.rad)
    open fun accelerateAtAngleRad(angleRad: Number){ accelerationVector += Vector2().also{ it.length = accelerationValue; it.angleRad = angleRad.toDouble() } }

    open fun update(dt: Double){
        velocityVector+=accelerationVector*dt
        var s = speed
        if(accelerationVector.length<0.001) s -= decelerationValue*dt
        if(s<0) s = 0.0
        if(s>maximumSpeed) s = maximumSpeed
        speed = s
        positionVector+=velocityVector*dt
        accelerationVector.setValues(0.0,0.0)
    }
}