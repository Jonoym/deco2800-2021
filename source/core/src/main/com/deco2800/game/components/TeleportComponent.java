package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.screens.MainGameScreen;

import java.util.Scanner;

public class TeleportComponent extends TouchComponent {

    private Scanner scanner = new Scanner(System.in);
    private long start = 0;

    public TeleportComponent(short targetLayer) {
        super(targetLayer);
    }

    @Override
    public void create() {
        super.create();
    }

    /**
     * action apply when the hitbox component collide
     *
     * @param me    the owner of the hitbox
     * @param other the target of the hitbox
     */
    @Override
    protected void onCollisionStart(Fixture me, Fixture other) {

        if (!PhysicsLayer.notContains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        //Dissolve arrow attacks after hits
        if (getEntity().getComponent(HitboxComponent.class).getLayer() == PhysicsLayer.PROJECTILEWEAPON) {
            //Remove later on to make arrows stick into walls and more
            getEntity().prepareDispose();
        }

        // Try to teleport player
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null && ((System.currentTimeMillis() - start) / 1000.) > 0.5) {
            MainGameScreen.levelChange();
        }
    }
}
