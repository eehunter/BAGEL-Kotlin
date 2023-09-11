package com.oyosite.ticon.bagel

import javafx.scene.canvas.GraphicsContext

open class Group : Entity() {

    private val list = mutableListOf<Entity>()

    val count get() = list.size

    fun addEntity(entity: Entity) = list.add(entity.apply { container=this@Group })
    operator fun plusAssign(entity: Entity) { addEntity(entity) }

    fun removeEntity(entity: Entity) = list.remove(entity.apply { container=null })
    operator fun minusAssign(entity: Entity) { removeEntity(entity) }

    fun list() = mutableListOf(*list.toTypedArray())

    operator fun get(index: Int): Entity = list[index]

    override fun draw(context: GraphicsContext) {for(e in list)e.draw(context)}

    override fun act(deltaTime: Double) {for(e in list)e.act(deltaTime)}
}