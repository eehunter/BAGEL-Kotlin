package com.oyosite.ticon.bagel

import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.Color.BLACK
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment


open class Label(
    var fontName: String = "Arial",
    var fontSize: Double = 16.0,
    var font: Font = Font(fontName, fontSize),
    var fontColor: Color = BLACK,
    var text: String = " ",
    var x: Double = 0.0,
    var y: Double = 0.0,
    var alignment: String = "LEFT",
    var borderDraw: Boolean = false,
    var borderSize: Int = 1,
    var borderColor: Color = BLACK,
    var visible: Boolean = true
): Entity() {

    /**
     * Configure this label to use a font already installed on this system.
     * @param fontName name of font (e.g. "Arial", "Times New Roman", "Courier New"); must be installed on system
     * @param fontSize size of font
     */
    fun loadFontFromSystem(fontName: String, fontSize: Int) = apply {
        font = Font(fontName, fontSize.toDouble())
        this.fontName = fontName
        this.fontSize = fontSize.toDouble()
    }

    /**
     * Configure this label to use a font from a specified file.
     * @param fontFileName name of font file
     * @param fontSize size of font
     */
    fun loadFontFromFile(fontFileName: String, fontSize: Int) = apply{
        font = Font.loadFont("file:$fontFileName", fontSize.toDouble())
        fontName = font.name
        this.fontSize = fontSize.toDouble()
    }

    /**
     * Set the coordinates of the anchor position of this label;
     * this may be to the left, center, or right of the text
     * according to the value of [.alignment].
     * @param x x-coordinate of anchor of label
     * @param y y-coordinate of anchor of label
     */
    fun setPosition(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    /**
     * Render this text to a canvas used specified parameters.
     */
    override fun draw(context: GraphicsContext) {
        if (!visible) return
        context.font = font
        context.fill = fontColor
        context.textAlign = TextAlignment.valueOf(alignment)
        context.setTransform(1.0, 0.0, 0.0, 1.0, 0.0, 0.0)
        context.globalAlpha = 1.0
        context.fillText(text, x, y)
        if (borderDraw) {
            context.stroke = borderColor
            context.lineWidth = borderSize.toDouble()
            context.strokeText(text, x, y)
        }
    }
}