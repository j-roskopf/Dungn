package com.joer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Control(// SCREEN
        internal var screen_width: Int, internal var screen_height: Int, // CAMERA
        internal var camera: OrthographicCamera) : InputAdapter(), InputProcessor {

    // DIRECTIONS
    var up: Boolean = false
    var down: Boolean = false
    var left: Boolean = false
    var right: Boolean = false

    // MOUSE
    var LMB: Boolean = false
    var RMB: Boolean = false
    var processed_click: Boolean = false
    var mouse_click_pos = Vector2()
    var map_click_pos = Vector2()

    // DEBUG
    var debug: Boolean = false

    private fun setMouseClickedPos(screenX: Int, screenY: Int) {
        // Set mouse position (flip screen Y)
        mouse_click_pos.set(screenX.toFloat(), (screen_height - screenY).toFloat())
        map_click_pos.set(get_map_coords(mouse_click_pos))
    }

    fun get_map_coords(mouse_coords: Vector2): Vector2 {
        val v3 = Vector3(mouse_coords.x, screen_height - mouse_coords.y, 0f)
        this.camera.unproject(v3)
        return Vector2(v3.x, v3.y)
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.DOWN -> down = true
            Input.Keys.UP -> up = true
            Input.Keys.LEFT -> left = true
            Input.Keys.RIGHT -> right = true
            Input.Keys.W -> up = true
            Input.Keys.A -> left = true
            Input.Keys.S -> down = true
            Input.Keys.D -> right = true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.DOWN -> down = false
            Input.Keys.UP -> up = false
            Input.Keys.LEFT -> left = false
            Input.Keys.RIGHT -> right = false
            Input.Keys.W -> up = false
            Input.Keys.A -> left = false
            Input.Keys.S -> down = false
            Input.Keys.D -> right = false
            Input.Keys.ESCAPE -> Gdx.app.exit()
            Input.Keys.BACKSPACE -> debug = !debug
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (pointer == 0 && button == 0) {
            LMB = true
        } else if (pointer == 0 && button == 0) {
            RMB = true
        }

        setMouseClickedPos(screenX, screenY)
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (pointer == 0 && button == 0) {
            LMB = false
            processed_click = false
        } else if (pointer == 0 && button == 0) {
            RMB = false
        }

        setMouseClickedPos(screenX, screenY)
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        setMouseClickedPos(screenX, screenY)
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

}