package com.oyosite.ticon.bagel

import javafx.scene.image.Image
import java.io.File

class Texture(val image: Image, val region: Rectangle) {

    companion object{
        fun load(imageFileName: String): Texture{
            //val fileName = File(imageFileName).toURI().toString()
            val image = Image(imageFileName)
            return Texture(image,Rectangle(0.0,0.0,image.width,image.height))
        }
    }
}