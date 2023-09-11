package com.oyosite.ticon.bagel

import kotlin.math.*

data class Vector2(var x: Double, var y: Double): Comparable<Vector2>{
    constructor(): this(0.0,0.0)

    val copy get() = Vector2(x,y)
    operator fun plusAssign(other: Vector2){
        x+=other.x
        y+=other.y
    }
    operator fun plus(other: Vector2) = copy.also{it+=other}

    fun setValues(x: Double, y: Double){ this.x = x; this.y = y }
    fun addValues(x: Double, y: Double){ this.x+=x; this.y+=y }

    operator fun timesAssign(scalar: Double){ x*=scalar; y*=scalar }

    operator fun times(scalar: Double) = copy.also{it*=scalar}

    var length get() = sqrt(x*x+y*y); set(value) = angleRad.let{ x = value * cos(it); y = value * sin(it) }

    var angleRad get() = if(length==0.0)0.0 else atan2(y,x); set(value) = length.let{ x = it * cos(value); y = it * sin(value)}
    var angle get() = angleRad.deg; set(value) {angleRad = value.rad}

    override fun compareTo(other: Vector2): Int = this.length.compareTo(other.length)

    override fun toString(): String = "<${String.format("%3.5f",x)},${String.format("%3.5f",y)}>"
}

