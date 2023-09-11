package com.oyosite.ticon.bagel


import javafx.scene.canvas.GraphicsContext
import kotlin.math.cos
import kotlin.math.sin


open class Sprite : Entity(){
    var x = 0.0
    var y = 0.0
    var texture: Texture? = null; set(value) {field = value; value?:return; width = value.region.width; height = value.region.height}
    var width = 0.0
    var height = 0.0
    var visible = true
    var boundary = Rectangle(); get() = field.apply{setValues(x-.5*width,y-.5*height,width,height)}
    var angle: Number get() = angleRad.deg; set(value){angleRad = value.rad}
    var angleRad = 0.0
    var opacity = 1.0
    var mirrored = false
    var flipped = false
    var physics: Physics? = null
    val actionList = mutableListOf<Action<Sprite>>()
    var animation: Animation? = null; /**
     * Set the animation to be used by this Sprite.
     * Also sets texture, width, and height.
     * @param anim Animation to be used by this Sprite
     */ set(anim) {
        field = anim!!
        texture = anim.currentTexture
        anim.currentTexture?:return
        width = anim.currentTexture!!.region.width
        height = anim.currentTexture!!.region.height
    }



    fun pos(x: Number, y: Number) = apply{setPosition(x.toDouble(),y.toDouble())}
    fun pos(vector: Vector2) = pos(vector.x, vector.y)
    fun tex(textureLocation: String) = apply { texture = Texture.load(textureLocation) }
    fun phys(accel: Number, maxSpeed: Number, decel: Number) = apply { physics = Physics(accel.toDouble(),maxSpeed.toDouble(),decel.toDouble()) }

    fun setPosition(x: Double, y: Double) { this.x = x; this.y = y }
    fun moveBy(deltaX: Double, deltaY: Double) { this.x+=deltaX; this.y+=deltaY }
    fun setSize(width: Double, height: Double) { this.width = width; this.height = height }

    override fun draw(context: GraphicsContext) {
        if(!visible)return
        var scaleX = 1.0
        var scaleY = 1.0
        if(mirrored)scaleX*=-1
        if(flipped)scaleY*=-1
        val cosA = cos(angleRad)
        val sinA = sin(angleRad)
        context.run{
            setTransform(scaleX*cosA,scaleX*sinA,-scaleY*sinA,scaleY*cosA,x,y)
            globalAlpha = opacity
            val r by texture!!::region
            drawImage(texture!!.image, r.left, r.top, r.width, r.height, -.5*width, -.5*height, width, height)
        }

    }

    fun isOverlapping(other: Sprite) = boundary overlaps other.boundary

    fun preventOverlap(other: Sprite){
        if(!isOverlapping(other))return
        val v = boundary.getMinTranslationVector(other.boundary)
        moveBy(v.x, v.y)
    }

    fun rotateBy(deltaAngle: Number) {angle=angle.toDouble() + deltaAngle.toDouble()}
    fun rotateByRad(deltaAngle: Double) {angleRad+=deltaAngle}

    fun moveAtAngle(distance: Double, angle: Double) = moveAtAngleRad(distance, angle.rad)
    fun moveAtAngleRad(distance: Double, angle: Double){
        x+=distance*cos(angle)
        y+=distance*sin(angle)
    }

    fun moveForward(distance: Double) = moveAtAngleRad(distance, angleRad)

    fun boundToScreen(screenWidth: Double, screenHeight: Double){
        if(x-width/2<0)x=width/2
        if(x+width/2>screenWidth)x=screenWidth-width/2
        if(y-height/2<0)y=height/2
        if(y+height/2>screenHeight)y=screenHeight-height/2
    }

    /**
     * If sprite moves completely beyond one edge of the screen,
     * adjust position so that it reappears by opposite edge of the screen.
     * @param screenWidth width of screen
     * @param screenHeight height of screen
     */
    open fun wrapToScreen(screenWidth: Double, screenHeight: Double) {
        if (x + width / 2 < 0) x = screenWidth + width / 2
        if (x - width / 2 > screenWidth) x = -width / 2
        if (y + height / 2 < 0) y = screenHeight + height / 2
        if (y - height / 2 > screenHeight) y = -height / 2
    }

    /**
     * Check if sprite (boundary rectangle) remains on screen
     * @param screenWidth width of screen
     * @param screenHeight height of screen
     * @return true, if part of sprite (boundary rectangle) remains on screen
     */
    open fun isOnScreen(screenWidth: Double, screenHeight: Double) = !(x + width / 2 < 0 || x - width / 2 > screenWidth || y + height / 2 < 0 || y - height / 2 > screenHeight)

    /**
     * Initialize physics object and corresponding values.
     * @param accValue acceleration value
     * @param maxSpeed maximum speed
     * @param decValue deceleration value
     */
    open fun setPhysics(accValue: Double, maxSpeed: Double, decValue: Double) { physics = Physics(accValue, maxSpeed, decValue) }

    /**
     * Simulate this sprite bouncing off other sprite.
     * Requires [.physics] object to be initialized.
     * @param other Sprite acting as solid surface to bounce against
     */
    open fun bounceAgainst(other: Sprite) {
        if (isOverlapping(other)) {
            val mtv: Vector2 = boundary.getMinTranslationVector(other.boundary)

            // prevent overlap
            moveBy(mtv.x, mtv.y)

            // assume surface perpendicular to displacement
            val surfaceAngle = mtv.angle + 90
            // adjust velocity
            physics!!.bounceAgainst(surfaceAngle)
        }
    }



    /**
     * Add an [Action] to this Sprite. Actions are automatically run.
     * @param a action to add to this sprite
     */
    open fun addAction(a: Action<Sprite>) {
        actionList.add(a)
    }

    /**
     * Run any [Action] objects added to this Sprite.
     * Actions are run in parallel (except for those created with [ActionFactory.sequence]
     * and removed from the Sprite when they are finished.
     * @param deltaTime amount of time that has passed since the last iteration of the game loop
     */
    override fun act(deltaTime: Double) {
        // update physics, position (based on velocity and acceleration)
        //   if it has been initialized for this sprite
        if (physics != null) {
            // update position
            physics!!.positionVector.x = x
            physics!!.positionVector.y = y
            physics!!.update(deltaTime)
            x = physics!!.positionVector.x
            y = physics!!.positionVector.y
        }

        // update animation, current texture
        //   if it has been initialized for this sprite
        if (animation != null) {
            animation!!.update(deltaTime)
            texture = animation!!.currentTexture
        }

        // update all actions (in parallel, by default)
        val actionListCopy = ArrayList(actionList)
        for (a in actionListCopy) {
            val finished: Boolean = a(this, deltaTime)
            if (finished) actionList.remove(a)
        }
    }


}