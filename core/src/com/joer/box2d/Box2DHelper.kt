package com.mygdx.game.box2d

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.*
import com.joer.SMALLER


object Box2DHelper {

    fun createBody(world: World, width: Float, height: Float, pos: Vector3, type: BodyDef.BodyType): Body {
        val body: Body
        val bodyDef = BodyDef()
        bodyDef.position.set(pos.x + width / 2, pos.y + height / 2)
        bodyDef.angle = 0f
        bodyDef.fixedRotation = true
        bodyDef.type = type
        body = world.createBody(bodyDef)

        val fixtureDef = FixtureDef()
        val boxShape = PolygonShape()
        boxShape.setAsBox(width / 2, height / 2)

        fixtureDef.shape = boxShape
        fixtureDef.restitution = 0.4f

        body.createFixture(fixtureDef)
        boxShape.dispose()

        return body
    }

    fun createPlayerBody(world: World, width: Float, height: Float, pos: Vector3, type: BodyDef.BodyType): Body {
        val body: Body
        val bodyDef = BodyDef()
        bodyDef.position.set(pos.x , pos.y)
        bodyDef.angle = 0f
        bodyDef.fixedRotation = true
        bodyDef.type = type
        body = world.createBody(bodyDef)

        val fixtureDef = FixtureDef()
        val circle = CircleShape()
        circle.radius = 6f * SMALLER
        fixtureDef.shape = circle
        fixtureDef.restitution = 0.4f

        body.createFixture(fixtureDef)
        circle.dispose()

        return body
    }

}