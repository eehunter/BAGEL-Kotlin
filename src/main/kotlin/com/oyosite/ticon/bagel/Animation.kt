package com.oyosite.ticon.bagel

import javafx.scene.image.Image
import java.io.File

class Animation {
    var currentTexture: Texture? = null

    val textureList = mutableListOf<Texture>()
    var paused = false
    var elapsedTime = 0.0
    var frameDuration = 0.0
    val totalDuration get() = frameDuration*textureList.size
    var loop = false


    fun update(deltaTime: Double){
        if (paused) return

        elapsedTime += deltaTime

        if (loop && elapsedTime > totalDuration) elapsedTime -= totalDuration

        var textureIndex = Math.floor(elapsedTime / frameDuration).toInt()
        if (textureIndex >= textureList.size) textureIndex = textureList.size - 1
        currentTexture = textureList[textureIndex]
    }

    val clone: Animation get(){
        val anim = Animation()
        anim.textureList.addAll(textureList)
        anim.frameDuration = frameDuration
        anim.loop = loop
        anim.currentTexture = textureList[0]
        return anim
    }

    /**
     * Create an animation object from a sprite sheet.
     * @param imageFileName name of image file
     * @param rows number of rows of individual images in file
     * @param cols number of columns of individual images in file
     * @param frameDuration amount of time to display each individual image
     * @param loop whether this animation should repeat after last image is displayed
     * @return an Animation created according to the given parameters
     */
    fun load(imageFileName: String, rows: Int, cols: Int, frameDuration: Double, loop: Boolean): Animation {
        val anim = Animation()
        val fileName: String = File(imageFileName).toURI().toString()
        val image = Image(fileName)
        val frameWidth: Double = image.width / cols
        val frameHeight: Double = image.height / rows
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                val texture = Texture(image,Rectangle(x * frameWidth, y * frameHeight, frameWidth, frameHeight))
                anim.textureList.add(texture)
            }
        }
        anim.frameDuration = frameDuration
        anim.loop = loop
        anim.currentTexture = anim.textureList[0]
        return anim
    }

}