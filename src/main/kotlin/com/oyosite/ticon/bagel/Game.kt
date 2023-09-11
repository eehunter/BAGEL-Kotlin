package com.oyosite.ticon.bagel

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.stage.Stage

open class Game : Application(){
    var canvas: Canvas = Canvas(windowWidth, windowHeight)
    val context = canvas.graphicsContext2D
    val contextUtils = ContextUtils(context)

    lateinit var input: Input private set

    var group = Group()

    var previousTime: Long = 0L
    var deltaTime: Double = 0.0


    private val gameLoop = BagelAnimationTimer{
        deltaTime = (it-previousTime)/1000000000.0
        previousTime = it

        input.update()

        group.act(deltaTime)
        update(deltaTime)

        context.fill = Color.GRAY
        context.fillRect(0.0,0.0, windowWidth, windowHeight)
        group.draw(context)
    }

    open fun update(dt: Double){}
    open fun create(){}

    override fun start(primaryStage: Stage) {
        val root = Pane()
        val mainScene = Scene(root)
        primaryStage.scene = mainScene
        primaryStage.sizeToScene()


        root.children+=canvas

        input = Input(mainScene)

        primaryStage.show()
        create()
        previousTime = System.nanoTime()
        gameLoop.start()
    }

    val <E: Entity> E.addToGame get() = this.also(group::addEntity)

    companion object{
        var windowWidth = 800.0
        var windowHeight = 600.0

    }
}