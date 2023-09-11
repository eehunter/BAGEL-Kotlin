package com.oyosite.ticon.games

import com.oyosite.ticon.bagel.Game
import com.oyosite.ticon.bagel.Sprite
import com.oyosite.ticon.bagel.TileMap
import kotlin.system.exitProcess

class MapExplorer: Game() {

    val ocean = Sprite().pos(400,300).tex("assets/map-explorer/water.png").addToGame
    val map = TileMap(10,12,64,64).addToGame

    init {
        map.loadMapData(
            arrayOf(
                "AAAAAAAAAAAA",
                "AT_____C___A",
                "A_BBB______A",
                "A__BBB__C__A",
                "A__________A",
                "A_C__BB__C_A",
                "A____BBB___A",
                "A__C__B___CA",
                "A_______C__A",
                "AAAAAAAAAAAA"
            ),
            arrayOf("A", "B", "C"),
            intArrayOf(4,14,2)
        )
        map.loadTilesetImage("assets/map-explorer/tileset.png")
    }

    val turtle = Sprite().pos(map.getSymbolPositionList("T")[0]).tex("assets/map-explorer/turtle.png").phys(512,64,512)

    init{
        //ocean.addToGame
        turtle.addToGame
        //println("${turtle.x}, ${turtle.y}")
        /*map.mapTileList.forEachIndexed{i,it->
            listOfNotNull(
                it.edgeBottom,
                it.edgeRight,
                it.edgeLeft,
                it.edgeTop
            ).forEachIndexed{ i2, e->println("Tile $i has edge type $i2 with bounds $e")} }*/
    }

    var previousMotionAngle = 0
    override fun update(dt: Double) {
        for(dir in arrayOf("UP" to 270, "DOWN" to 90, "RIGHT" to 0, "LEFT" to 180)) if(input.isKeyPressed(dir.first)) turtle.physics!!.accelerateAtAngle(dir.second.toDouble())

        map.preventSpriteOverlap(turtle)

        val motionAngle = turtle.physics!!.motionAngle.toInt() // -180 to +180

        //if(previousTime % 10000L == 0L) println(map.checkSpriteOverlap(turtle))//println("$motionAngle, ${motionAngle in -45..45}")

        if (turtle.physics!!.speed > 1 && motionAngle!=previousMotionAngle) turtle.angle = when(motionAngle){
            in -45..45 -> 0
            in 45..135 -> 90
            in -135..-45 -> 270
            else -> 180
        }

        previousMotionAngle = motionAngle

        //turtle.angle = 90



    }






    companion object{
        @JvmStatic
        fun main(args: Array<String>){
            try{
                windowWidth = 64 * 12.0
                windowHeight = 64 * 10.0
                launch(MapExplorer::class.java)
            }catch (e: Exception){ e.printStackTrace() }
            finally { exitProcess(0) }
        }
    }
}