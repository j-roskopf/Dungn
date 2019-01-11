package com.joer

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.mygdx.game.box2d.Box2DHelper
import com.mygdx.game.box2d.Box2DWorld

const val SMALLER = .01f
const val V_WIDTH = 600f
const val V_HEIGHT = 408f
const val MAX_ZOOM = 5f
const val MIN_ZOOM = 0.1f

class DungeonGame : ApplicationAdapter(), GestureDetector.GestureListener, InputProcessor {

    private var mapLoader: TmxMapLoader = TmxMapLoader()
    private lateinit var tiledMap: TiledMap
    private lateinit var renderer: OrthogonalTiledMapRenderer
    private var camera: OrthographicCamera = OrthographicCamera()
    private lateinit var atlas: TextureAtlas
    private lateinit var player: Player
    private lateinit var batch: SpriteBatch
    private lateinit var box2DWorld: Box2DWorld
    private lateinit var viewport: Viewport


    // Display Size
    private var displayW: Int = 0
    private var displayH: Int = 0

    private var inputMultiplexer = InputMultiplexer()
    private lateinit var control: Control


    override fun create() {
        batch = SpriteBatch()
        atlas = TextureAtlas("character/char.atlas")
        tiledMap = mapLoader.load("16.tmx")
        renderer = OrthogonalTiledMapRenderer(tiledMap, SMALLER)
        box2DWorld = Box2DWorld()

        for (objects in tiledMap.layers.get(0).objects.getByType(RectangleMapObject::class.java)) {
            Box2DHelper.createBody(box2DWorld.world,
                    objects.properties["width"] as Float * (SMALLER),
                    objects.properties["height"] as Float * (SMALLER),
                    Vector3(objects.properties["x"] as Float * (SMALLER),
                            objects.properties["y"] as Float * (SMALLER),
                            0f),
                    BodyDef.BodyType.StaticBody)

        }

        camera = OrthographicCamera()
        viewport = FitViewport(V_WIDTH * SMALLER, V_HEIGHT * SMALLER, camera)
        camera.position.set(viewport.worldWidth / 2f, viewport.worldHeight / 2f, 0f)

        player = Player(atlas.findRegion("knight iso char"), box2DWorld)
        control = Control(displayW, displayH, camera)

        inputMultiplexer.addProcessor(control)
        inputMultiplexer.addProcessor(this)
        inputMultiplexer.addProcessor(GestureDetector(this))
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        //logic
        player.update(control)

        //camera
        camera.position.lerp(player.position, .1f)
        renderer.render()
        camera.update()
        renderer.setView(camera)

        //render
        batch.projectionMatrix = camera.combined
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        batch.begin()
        player.draw(batch, Gdx.graphics.deltaTime)
        batch.end()

        // call tick method to draw debug lines
        // pass in control to check it debug is true
        box2DWorld.tick(camera, control)
    }


    override fun pause() {
    }

    override fun resume() {
    }


    override fun resize(width: Int, height: Int) {
        viewport.update((width), (height))
    }

    override fun dispose() {
    }


    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        return false
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        return false
    }

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        camera.translate(-1f * deltaX * SMALLER, deltaY * SMALLER)
        return true
    }

    override fun pinchStop() {
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        return false
    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun longPress(x: Float, y: Float): Boolean {
        return false
    }

    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        camera.zoom += amount * .1f
        camera.zoom = MathUtils.clamp(camera.zoom, MIN_ZOOM, MAX_ZOOM)
        camera.update()
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

}
