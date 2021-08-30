package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.movement.MovementController;
import com.deco2800.game.components.Component;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.deco2800.game.rendering.AnimationRenderComponent;

/** Movement controller for a physics-based entity. */
public class PhysicsMovementComponent extends Component implements MovementController {

  AnimationRenderComponent animator;


  private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);

  public PhysicsComponent physicsComponent;

  private Vector2 targetPosition;
  private boolean movementEnabled = true;
  private Vector2 maxSpeed = Vector2Utils.ONE;

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
  }

  @Override
  public void update() {
    if (movementEnabled && targetPosition != null) {
      Body body = physicsComponent.getBody();
      updateDirection(body);
    }
  }

  /**
   * Enable/disable movement for the controller. Disabling will immediately set velocity to 0.
   *
   * @param movementEnabled true to enable movement, false otherwise
   */
  @Override
  public void setMoving(boolean movementEnabled) {
    this.movementEnabled = movementEnabled;
    if (!movementEnabled) {
      Body body = physicsComponent.getBody();
      setToVelocity(body, Vector2.Zero);
    }
  }

  @Override
  public boolean getMoving() {
    return movementEnabled;
  }

  /** @return Target position in the world */
  @Override
  public Vector2 getTarget() {
    return targetPosition;
  }

  /**
   * Set a target to move towards. The entity will be steered towards it in a straight line, not
   * using pathfinding or avoiding other entities.
   *
   * @param target target position
   */
  @Override
  public void setTarget(Vector2 target) {
    logger.trace("Setting target to {}", target);
    this.targetPosition = target;
  }

  public void setMaxSpeed(Vector2 maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

  private void updateDirection(Body body) {
    Vector2 desiredVelocity = getDirection().scl(maxSpeed);
    setToVelocity(body, desiredVelocity);

<<<<<<< HEAD
    //entity.getEvents().trigger("floatDown");

    //if enemy is moving more on the x-axis than it is on the y, change direction using x-axis (left/right)
    if (this.getDirection().x>this.getDirection().y) {
        if (this.getDirection().x < 0) {
          this.getEntity().getEvents().trigger("LeftStart");
        } else if (this.getDirection().x > 0) {
          this.getEntity().getEvents().trigger("RightStart");
        }
      }
    else{
        if (this.getDirection().y < 0) {
          this.getEntity().getEvents().trigger("DownStart");
        } else if (this.getDirection().y > 0) {
          this.getEntity().getEvents().trigger("UpStart");
        }
      }
    }
=======
    //if enemy is moving more on the x-axis than it is on the y, change direction using x-axis (left/right)
    if (this.getDirection().x>this.getDirection().y) {
      if (this.getDirection().x < 0) {
        this.getEntity().getEvents().trigger("LeftStart");
      } else if (this.getDirection().x > 0) {
        this.getEntity().getEvents().trigger("RightStart");
      }
    }
    else{
      if (this.getDirection().y < 0) {
        this.getEntity().getEvents().trigger("DownStart");
      } else if (this.getDirection().y > 0) {
        this.getEntity().getEvents().trigger("UpStart");
      }
    }
  }
>>>>>>> efa83b0cbd9165ff08a786e46b0fbc1ddd770f83

  private void setToVelocity(Body body, Vector2 desiredVelocity) {
    // impulse force = (desired velocity - current velocity) * mass
    Vector2 velocity = body.getLinearVelocity();
    Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  public Vector2 getDirection() {
    return targetPosition.cpy().sub(entity.getPosition()).nor();
  }
}
