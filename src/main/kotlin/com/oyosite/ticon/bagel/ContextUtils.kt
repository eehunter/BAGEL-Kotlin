package com.oyosite.ticon.bagel

import javafx.scene.canvas.GraphicsContext
import javafx.scene.transform.Affine
import javafx.scene.transform.Transform

@JvmInline
value class ContextUtils(val context: GraphicsContext) {
    var transform: Transform get() = context.transform; set(value){context.transform = Affine(value)}
}