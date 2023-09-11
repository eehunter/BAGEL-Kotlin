package com.oyosite.ticon.bagel

class Tile(var x: Double, var y: Double, var width: Double, var height: Double, var tileTextureIndex: Int): Entity() {
    var boundary: Rectangle = Rectangle(x-width/2, y-height/2, width, height)

    val edges get() = listOf(edgeLeft,edgeRight,edgeTop,edgeBottom)

    /**
     * the left edge of the tile, if accessible
     */
    var edgeLeft: Rectangle? = null

    /**
     * the right edge of the tile, if accessible
     */
    var edgeRight: Rectangle? = null

    /**
     * the top edge of the tile, if accessible
     */
    var edgeTop: Rectangle? = null

    /**
     * the bottom edge of the tile, if accessible
     */
    var edgeBottom: Rectangle? = null
}