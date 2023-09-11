package com.oyosite.ticon.bagel

import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import java.io.File
import kotlin.math.abs


class TileMap(val mapRows: Int, val mapCols: Int, val tileWidth: Int, val tileHeight: Int): Entity() {

    /**
     * a two-dimensional array storing all the text characters
     * used to specify this TileMap
     */
    var mapDataGrid: Array<Array<String?>> = Array(mapRows){ Array(mapCols){null} }

    /**
     * a two-dimensional array storing only the [Tile] objects
     * specified by this TileMap. used primarily to determine whether
     * a tile is adjacent to other tiles, and thereby which tile edges
     * should be ignored when calculating collision resolution.
     */
    var mapTileGrid: Array<Array<Tile?>> = Array(mapRows){ Array(mapCols){null} }

    /**
     * a list containing all the [Tile] objects specified by this TileMap.
     */
    var mapTileList = mutableListOf<Tile>()

    /**
     * a list containing all the available Tile textures
     * loaded by the [.loadTilesetImage] method
     */
    var tileTextureList = mutableListOf<Texture>()



    /**
     * Load a *tileset* -
     * an image consisting of smaller rectangular images
     * that represent possible features of the game world environment.
     * @param imageFileName file name of the image containing tile images
     */
    fun loadTilesetImage(imageFileName: String) {
        //val fileName: String = File(imageFileName).toURI().toString()
        val tileSetImage = Image(imageFileName)
        val tileImageRows = tileSetImage.width.toInt() / tileWidth
        val tileImageCols = tileSetImage.height.toInt() / tileHeight
        for (y in 0 until tileImageRows) {
            for (x in 0 until tileImageCols) {
                val texture = Texture(tileSetImage, Rectangle((x * tileWidth).toDouble(), (y * tileHeight).toDouble(), tileWidth.toDouble(), tileHeight.toDouble()))
                tileTextureList.add(texture)
            }
        }
    }

    /**
     * Load text data that indicates placement of Tiles and game world entities.
     * Map data is stored as an array of Strings, one String per row of the map.
     * Text characters indicating tiles are listed in `mapTileSymbolArray`,
     * and the corresponding tileset index values are listed in `tileTextureIndexArray`.
     * Entity placement data can be retrieved later from the method
     * [.getSymbolPositionList].
     * <br></br><br></br>
     * For example, consider a map where "W" represents a wall tile
     * whose texture has index 3 in the tileset,
     * and "D" represents a door tile
     * whose texture has index 7 in the tileset.
     * In addition,
     * "P" represents the starting location of the player sprite,
     * and each "E" represents the location of an enemy sprite.
     * The parameters for this method could be specified as follows:
     * <pre>`mapData = {"WWWWDWWWW",
     * "W.......W",
     * "W.E...E.W",
     * "W.......W",
     * "W...P...W",
     * "WWWWWWWWW"};
     *
     * mapTileSymbolArray = {"W", "D"};
     * tileTextureIndexArray = {3, 7};
    `</pre> *
     *
     * This method also determines if each Tile is adjacent to other tiles,
     * and sets tile edge data for use in collision resolution:
     * edges that occur between two adjacent tiles ("interior edges") are ignored.
     *
     * @param mapData an array of Strings, one String per row of the map,
     * each character representing a Tile or other game world entity
     * @param mapTileSymbolArray text characters indicating Tile objects
     * @param tileTextureIndexArray index for Tile textures corresponding to characters in `mapTileSymbolArray`
     */
    fun loadMapData(mapData: Array<String>, mapTileSymbolArray: Array<String>, tileTextureIndexArray: IntArray) {
        mapDataGrid = Array(mapRows) { arrayOfNulls(mapCols) }
        mapTileGrid = Array(mapRows) { arrayOfNulls(mapCols) }
        mapTileList = ArrayList()
        val mapTileSymbolList: List<String> = listOf(*mapTileSymbolArray)
        for (r in 0 until mapRows) {
            for (c in 0 until mapCols) {
                // add all data to mapDataGrid
                val data = mapData[r].substring(c, c + 1)
                mapDataGrid[r][c] = data

                // add Tile-specific data to mapTileGrid and list
                if (mapTileSymbolList.contains(data)) {
                    val i = mapTileSymbolList.indexOf(data)
                    val tileTextureIndex = tileTextureIndexArray[i]
                    val x = (c + 0.5) * tileWidth
                    val y = (r + 0.5) * tileHeight
                    val tile = Tile(x, y, tileWidth.toDouble(), tileHeight.toDouble(), tileTextureIndex)
                    mapTileGrid[r][c] = tile
                    mapTileList.add(tile)
                }
            }
        }

        // after all map data is loaded, use adjacency information to set Tile edge fields
        for (r in 0 until mapRows) {
            for (c in 0 until mapCols) {
                val tile = mapTileGrid[r][c]
                if (tile != null) {
                    val rect = tile.boundary
                    if (getTileAt(r, c - 1) == null) tile.edgeLeft = Rectangle(rect.left, rect.top, 0.0, tileHeight.toDouble())
                    if (getTileAt(r, c + 1) == null) tile.edgeRight = Rectangle(rect.left + tileWidth, rect.top, 0.0, tileHeight.toDouble())
                    if (getTileAt(r - 1, c) == null) tile.edgeTop = Rectangle(rect.left, rect.top, tileWidth.toDouble(), 0.0)
                    if (getTileAt(r + 1, c) == null) tile.edgeBottom = Rectangle(rect.left, rect.top + tileHeight, tileWidth.toDouble(), 0.0)
                }
            }
        }
    }

    /**
     * Get the tile at the corresponding map position (if one exists).
     * @param mapRow map row index
     * @param mapCol map column index
     * @return tile at the given position, if one exists; `null` otherwise
     */
    fun getTileAt(mapRow: Int, mapCol: Int): Tile? {
        return if (mapRow < 0 || (mapRow >= mapRows) || mapCol < 0 || mapCol >= mapCols) null else mapTileGrid[mapRow][mapCol]
    }

    /**
     * Return the game world coordinates (in pixels) of every occurrence
     * of `symbol` in map data.
     * @param symbol text character to locate in map data
     * @return list of positions where `symbol` occurs in map data
     */
    fun getSymbolPositionList(symbol: String): ArrayList<Vector2> {
        val positionList = ArrayList<Vector2>()
        for (r in 0 until mapRows) {
            for (c in 0 until mapCols) {
                if (mapDataGrid[r][c] == symbol) {
                    val x = (c + 0.5) * tileWidth
                    val y = (r + 0.5) * tileHeight
                    positionList.add(Vector2(x, y))
                }
            }
        }
        return positionList
    }

    override fun draw(context: GraphicsContext) {
        for (tile in mapTileList) {
            context.setTransform(1.0, 0.0, 0.0, 1.0, tile.x, tile.y)
            context.globalAlpha = 1.0
            val tex = tileTextureList[tile.tileTextureIndex]

            // image, 4 source parameters, 4 destination parameters
            context.drawImage(
                tex.image,
                tex.region.left,
                tex.region.top,
                tex.region.width,
                tex.region.height,
                (
                        -tileWidth / 2).toDouble(),
                (-tileHeight / 2).toDouble(),
                tileWidth.toDouble(),
                tileHeight.toDouble()
            )
        }
    }

    /**
     * Check if a sprite overlaps any Tile in this TileMap.
     * @param sprite the sprite to check for overlap
     * @return true if sprite overlaps any Tile in this TileMap
     */
    fun checkSpriteOverlap(sprite: Sprite): Boolean {
        val spriteBoundary = sprite.boundary
        for (tile in mapTileList) {
            if (spriteBoundary.overlaps(tile.boundary)) return true
        }
        return false
    }

    /**
     * Prevent sprite from overlapping with any Tile in this TileMap.
     * Tile edges are used when calculating collision resolution
     * to avoid "corner snag" or "internal edge" issues that interfere with Sprite movement;
     * edges that occur between two adjacent tiles are ignored.
     * @param sprite the sprite to prevent from overlapping with tiles
     */
    fun preventSpriteOverlap(sprite: Sprite) {
        val spriteBoundary = sprite.boundary
        for (tile in mapTileList) {
            if (spriteBoundary.overlaps(tile.boundary)) {
                val differences = ArrayList<Vector2>()
                if (tile.edgeLeft?.overlaps(spriteBoundary) == true) differences.add(Vector2(tile.boundary.left - spriteBoundary.right, 0.0)) // to the left
                if (tile.edgeRight?.overlaps(spriteBoundary) == true) differences.add(Vector2(tile.boundary.right - spriteBoundary.left, 0.0)) // how to displace this sprite to the right
                if (tile.edgeTop?.overlaps(spriteBoundary) == true) differences.add(Vector2(0.0, tile.boundary.top - spriteBoundary.bottom)) // to the bottom
                if (tile.edgeBottom?.overlaps(spriteBoundary) == true) differences.add(Vector2(0.0, tile.boundary.bottom - spriteBoundary.top)) // to the top

                println(tile.edgeLeft?.left)
                if (differences.size > 0) {
                    // sortable since Vector2 implements Comparable interface
                    differences.sort()

                    // get minimum (length) vector to translate by
                    val (x, y) = differences[0]
                    sprite.moveBy(x, y)

                    // if sprite is using physics, come to a stop in appropriate direction
                    if (sprite.physics != null) {
                        if (abs(x) > 0) {
                            sprite.physics!!.velocityVector.x = 0.0
                            sprite.physics!!.accelerationVector.x = 0.0
                        }
                        if (abs(y) > 0) {
                            sprite.physics!!.velocityVector.y = 0.0
                            sprite.physics!!.accelerationVector.y = 0.0
                        }
                    }
                }
            }
        }
    }
}