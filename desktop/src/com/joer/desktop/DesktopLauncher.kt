package com.joer.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.joer.DungeonGame

class DesktopLauncher {
    companion object {
        @JvmStatic
        fun main(arg: Array<String>) {
            val config = LwjglApplicationConfiguration()
            config.width = 1920
            config.height = 1080
            LwjglApplication(DungeonGame(), config)
        }
    }

}
