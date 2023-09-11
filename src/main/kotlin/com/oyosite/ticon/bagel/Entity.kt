package com.oyosite.ticon.bagel

import javafx.scene.canvas.GraphicsContext

open class Entity {
    var container: Group? = null

    fun remove() = container?.removeEntity(this)

    open fun draw(context: GraphicsContext) {}

    open fun act(deltaTime: Double) {}
}