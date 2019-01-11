package com.mygdx.game.box2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.joer.Control


class Box2DWorld {
    var world: World = World(Vector2(.0f, .0f), true)
    private val debugRenderer: Box2DDebugRenderer = Box2DDebugRenderer()

    fun tick(camera: OrthographicCamera, control: Control) {
        if (control.debug) debugRenderer.render(world, camera.combined)

        world.step(Gdx.app.graphics.deltaTime, 12, 4)
    }

}