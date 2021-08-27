package com.deco2800.game.components.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler uses keyboard input and mouse input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();
  private int lastKeyPressed;

  public KeyboardPlayerInputComponent() {
    super(5);
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    Vector2 newVector;
    System.out.println(keycode);
    switch (keycode) {
      case Keys.W:
        lastKeyPressed = Keys.W;
        walkDirection.add(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Keys.A:
        lastKeyPressed = Keys.A;
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        lastKeyPressed = Keys.S;
        walkDirection.add(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        lastKeyPressed = Keys.D;
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Keys.SPACE:
        entity.getEvents().trigger("attack", lastKeyPressed);
        return true;
      default:
        return false;
    }
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Keys.W:
        walkDirection.sub(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Keys.A:
        walkDirection.sub(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Keys.S:
        walkDirection.sub(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Keys.D:
        walkDirection.sub(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      default:
        return false;
    }
  }

  /**
   * Triggers player events on a mouse click. Direction is determined by
   * mouse click coordinates (screenX, screenY).
   *
   * @return whether the mouse input was processed.
   * @see InputProcessor#touchDown(int, int, int, int)
   */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (button == Buttons.LEFT) { // Attack using left mouse button
      entity.getEvents().trigger("mouseAttack",
              new Vector2(screenX, screenY));
      return true;
    }
    // other mouse buttons go here (TBD).
    return false;
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }
}
