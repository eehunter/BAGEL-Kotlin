package com.oyosite.ticon.bagel

import javafx.scene.media.AudioClip
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File


/**
 *  Load an audio file (sound or music).
 *  <br><br>
 *  Sound files are typically small, short audio files
 *  that are played when discrete game events occur
 *  (such as when an item is collected, or when two objects collide).
 *  Sound files are completely loaded into memory.
 *  <br><br>
 *  Music files are typically large, long audio files
 *  containing background music that is played continuously during the game.
 *  Music files are streamed from a file as they are played.
 */
class Audio private constructor(val sound: AudioClip?, val music: Media?, val musicPlayer: MediaPlayer?) {
    constructor(sound: AudioClip): this(sound, null, null)
    constructor(music: Media, musicPlayer: MediaPlayer): this(null, music, musicPlayer)
    constructor(music: Media): this(music,MediaPlayer(music))

    companion object{
        fun loadSound(fileName: String) = Audio(AudioClip(File(fileName).toURI().toString()))
        fun loadMusic(fileName: String) = Audio(Media(File(fileName).toURI().toString()))
    }

    var loop: Boolean get() = (sound?.cycleCount ?: musicPlayer?.cycleCount ?: 0) > 1; set(value){
        val repeatCount = if(value)Integer.MAX_VALUE else 1
        sound?.cycleCount = repeatCount
        musicPlayer?.cycleCount = repeatCount
    }

    var volume get() = sound?.volume?:musicPlayer?.volume?:0.0; set(value){
        sound?.volume = value
        musicPlayer?.volume = volume
    }

    fun play() {
        sound?.play()
        musicPlayer?.play()
    }

    fun stop(){
        sound?.stop()
        musicPlayer?.stop()
    }
}