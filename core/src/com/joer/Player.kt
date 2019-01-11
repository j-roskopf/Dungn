package com.joer

import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.Array
import com.mygdx.game.box2d.Box2DHelper
import com.mygdx.game.box2d.Box2DWorld

const val PLAYER_SCALE = 0.3047619047f


class Player(val region: TextureAtlas.AtlasRegion, box2DWorld: Box2DWorld) : Sprite(region) {

    private var idleRegion: TextureRegion
    private var playerWidth = 84f
    private var playerHeight = 84f
    var position: Vector3 = Vector3()
    private var dirX = 0f
    private var dirY = 0f
    private var lastDirX = 0f
    private var lastDirY = 0f
    private var speed = 2

    private var runLeft: Animation<TextureRegion>
    private var runRight: Animation<TextureRegion>
    private var runDown: Animation<TextureRegion>
    private var runUp: Animation<TextureRegion>
    var body: Body
    private var stateTime = 0f

    init {
        setBounds(x * SMALLER, y * SMALLER, playerWidth * SMALLER * PLAYER_SCALE, playerHeight * SMALLER * PLAYER_SCALE)
        idleRegion = TextureRegion(region, 0, 0, playerWidth.toInt(), height.toInt())
        setRegion(idleRegion)

        var frames = Array<TextureRegion>()
        for (i in 1 until 4) {
            val xOffset = (4 * playerWidth).toInt()
            val yOffset = (2 * playerHeight.toInt())
            frames.add(TextureRegion(region, xOffset + (i * playerWidth).toInt(), yOffset, playerWidth.toInt(), playerHeight.toInt()))
        }
        runLeft = Animation(0.1f, frames)

        frames = Array()
        for (i in 1 until 2) {
            val xOffset = (6 * playerWidth).toInt()
            val yOffset = (1 * playerHeight.toInt())
            frames.add(TextureRegion(region, xOffset + (i * playerWidth).toInt(), yOffset, playerWidth.toInt(), playerHeight.toInt()))
        }
        for (i in 1 until 3) {
            val xOffset = (1 * playerHeight.toInt())
            val yOffset = (2 * playerHeight.toInt())
            frames.add(TextureRegion(region, xOffset + (i * playerWidth).toInt(), yOffset, playerWidth.toInt(), playerHeight.toInt()))
        }
        runRight = Animation(0.1f, frames)

        frames = Array()
        for (i in 1 until 5) {
            val xOffset = (1 * playerWidth).toInt()
            val yOffset = (1 * playerHeight.toInt())
            frames.add(TextureRegion(region, xOffset + (i * playerWidth).toInt(), yOffset, playerWidth.toInt(), playerHeight.toInt()))
        }
        runUp = Animation(0.1f, frames)

        frames = Array()
        for (i in 1 until 4) {
            val xOffset = (4 * playerWidth).toInt()
            val yOffset = (0 * playerHeight.toInt())
            frames.add(TextureRegion(region, xOffset + (i * playerWidth).toInt(), yOffset, playerWidth.toInt(), playerHeight.toInt()))
        }
        runDown = Animation(0.1f, frames)
        position.set(60f * SMALLER, 60f * SMALLER, 0f)
        body = Box2DHelper.createPlayerBody(box2DWorld.world,
                width  ,
                height  ,
                position, BodyDef.BodyType.DynamicBody)
    }

    fun update(control: Control) {
        if (dirX != 0f) {
            lastDirX = dirX
            lastDirY = 0f
        } else if (dirY != 0f) {
            lastDirY = dirY
            lastDirX = 0f
        }
        dirX = 0f
        dirY = 0f

        when {
            control.down -> dirY = -1f
            control.up -> dirY = 1f
            control.left -> dirX = -1f
            control.right -> dirX = 1f
        }

        body.setLinearVelocity(dirX * speed, dirY * speed)

        position.x = (body.position.x )
        position.y = (body.position.y )
    }

    private fun getFrame(delta: Float): TextureRegion {
        stateTime += delta
        return when (getDirection()) {
            DIRECTION.RIGHT -> runRight.getKeyFrame(stateTime, true)
            DIRECTION.LEFT -> runLeft.getKeyFrame(stateTime, true)
            DIRECTION.UP -> runUp.getKeyFrame(stateTime, true)
            DIRECTION.DOWN -> runDown.getKeyFrame(stateTime, true)
            DIRECTION.IDLE -> getIdlePosition()
        }
    }

    private fun getIdlePosition(): TextureRegion {
        if (lastDirX == 0f) {
            return when {
                lastDirY > 0 -> { //up
                    TextureRegion(region, (1 * playerWidth).toInt(), (1 * playerHeight.toInt()), playerWidth.toInt(), playerHeight.toInt())
                }
                else -> { //down
                    TextureRegion(region, 0, 0, playerWidth.toInt(), playerHeight.toInt())
                }
            }
        } else {
            return when {
                lastDirX > 0 -> { //right
                    TextureRegion(region, (6 * playerWidth).toInt(), (playerHeight.toInt()), playerWidth.toInt(), playerHeight.toInt())
                }
                else -> { //left
                    TextureRegion(region, (7 * playerWidth).toInt(), (2 * playerHeight.toInt()), playerWidth.toInt(), playerHeight.toInt())
                }
            }
        }
    }

    private fun getDirection(): DIRECTION {
        return when {
            dirX > 0 -> {
                DIRECTION.RIGHT
            }
            dirX < 0 -> {
                DIRECTION.LEFT
            }
            dirY > 0 -> {
                DIRECTION.UP
            }
            dirY < 0 -> {
                DIRECTION.DOWN
            }
            else -> {
                DIRECTION.IDLE
            }
        }
    }

    fun draw(batch: SpriteBatch, delta: Float) {
        println("${body.position} $position")

        batch.draw(getFrame(delta),
                position.x - width / 2,
                position.y,
                width ,
                height
        )
    }
}

enum class DIRECTION { RIGHT, LEFT, UP, DOWN, IDLE }