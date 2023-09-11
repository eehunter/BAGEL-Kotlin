package com.oyosite.ticon.bagel

import java.util.*

/**
 *  A rectangle shape, defined by its position and size,
 *  particularly useful in collision detection
 *  (see [Sprite], [TileMap])
 *  and for specifying area of an image to be drawn
 *  (see [Texture]).
 *  Rectangles with width or height equal to 0 can be used to represent edges
 *  (see [Tile#edgeLeft], etc.)
 *
 */
class Rectangle(var left: Double, var top: Double, var width: Double, var height: Double) {
    constructor(): this(0.0,0.0,0.0,0.0)
    var right = left+width //get() = left+width; set(value) {width = value-left}
    var bottom = top+height //get() = top+height; set(value) {height = value-height}

    fun setValues(left: Double, top: Double, width: Double, height: Double): Rectangle{
        this.left = left
        this.top = top
        this.width = width
        this.height = height
        this.right = left+width
        this.bottom = top+height
        return this
    }

    /**
     * Determine if this rectangle overlaps with other rectangle.
     * @param other rectangle to check for overlap
     * @return true if this rectangle overlaps with other rectangle
     */
    infix fun overlaps(other: Rectangle) = !(other.right<=left || right<=other.left || other.bottom<=top || bottom<=other.top)
    /**
     * Assuming that this rectangle and other rectangle overlap,
     * calculate the minimum length vector required to translate this rectangle
     * so that there is no longer any overlap between them.
     * @param other rectangle to translate away from
     * @return minimum length vector required to translate by to avoid overlap
     */
    fun getMinTranslationVector(other: Rectangle): Vector2 {
        val differences = arrayOf(
            Vector2(other.right - left, 0.0),  // how to displace this to the right
            Vector2(other.left - right, 0.0),  // to the left
            Vector2(0.0, other.bottom - top),  // to the top
            Vector2(0.0, other.top - bottom)   // to the bottom
        )
        // sort method may be used since Vector2 implements Comparable interface
        Arrays.sort(differences)
        return differences[0]
    }

    /**
     * Determine if this rectangle contains the point (x,y).
     * @param x x-coordinate of point
     * @param y y-coordinate of point
     * @return true, if this rectangle contains the point (x,y)
     */
    fun contains(x: Double, y: Double) = (x in left..right) && (y in top..bottom)
}