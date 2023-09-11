package com.oyosite.ticon.bagel

import kotlin.math.PI

val Number.rad inline get() = this.toDouble() * PI / 180
val Number.deg inline get() = this.toDouble() * 180 / PI