package com.oyosite.ticon.bagel

import javafx.scene.Scene

class Input(val listeningScene: Scene) {
    val keyDownQueue = mutableListOf<String>()
    val keyUpQueue = mutableListOf<String>()
    val keyDownList = mutableListOf<String>()
    val keyPressedList = mutableListOf<String>()
    val keyUpList = mutableListOf<String>()

    var mouseButtonDownQueue = false
    var mouseButtonUpQueue = false
    var mouseButtonDown = false
    var mouseButtonUp = false

    var mouseX = 0.0
    var mouseY = 0.0

    init{
        listeningScene.run {
            setOnKeyPressed { keyDownQueue += it.code.toString() }
            setOnKeyReleased { keyUpQueue += it.code.toString() }
            setOnMousePressed { mouseButtonDownQueue = true }
            setOnMouseReleased { mouseButtonUpQueue = true }
            setOnMouseMoved { mouseX = it.x; mouseY = it.y }
        }
    }

    /**
     * Determine if key has been pressed / moved to down position (a discrete action).
     * @param keyName name of corresponding key (examples: "LEFT", "A", "DIGIT1", "SPACE", "SHIFT")
     * @return true if key was just pressed
     */
    fun isKeyDown(keyName: String) = keyName in keyDownList

    /**
     * Determine if key is currently being pressed / held down (a continuous action).
     * @param keyName name of corresponding key (examples: "LEFT", "A", "DIGIT1", "SPACE", "SHIFT")
     * @return true if key is currently pressed
     */
    fun isKeyPressed(keyName: String) = keyName in keyPressedList

    /**
     * Determine if key has been released / returned to up position (a discrete action).
     * @param keyName name of corresponding key (examples: "LEFT", "A", "DIGIT1", "SPACE", "SHIFT")
     * @return true if key was just released
     */
    fun isKeyUp(keyName: String) = keyName in keyUpList

    /**
     * Return current position of mouse on game canvas.
     * @return current position of mouse
     */
    val mousePosition get() = Vector2(mouseX,mouseY)

    /**
     * Determine if (any) mouse button has recently been pressed
     *   while mouse cursor position is contained with bounding area of a sprite.
     * @param sprite Sprite to check if clicked by mouse
     * @return true if the mouse has clicked on the sprite
     */
    fun isClicked(sprite: Nothing): Boolean = TODO()

    /**
     *  Update state information for keyboard and mouse.
     *  Automatically called by [Game] class during the game loop.
     */
    fun update(){
        keyDownList.clear()
        keyUpList.clear()
        mouseButtonDown = false
        mouseButtonUp = false

        for(k in keyDownQueue) if(k !in keyPressedList) {
            keyDownList+=k
            keyPressedList+=k
        }

        for(k in keyUpQueue){
            keyPressedList-=k
            keyUpList+=k
        }

        if(mouseButtonDownQueue) mouseButtonDown = true
        if(mouseButtonUpQueue) mouseButtonUp = true

        keyDownQueue.clear()
        keyUpQueue.clear()
        mouseButtonDownQueue = false
        mouseButtonUpQueue = false
    }
}