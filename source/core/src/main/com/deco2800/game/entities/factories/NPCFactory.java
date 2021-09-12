package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.GhostKingConfig;
import com.deco2800.game.entities.configs.GhostRangedConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;


/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class NPCFactory {
    private static final NPCConfigs configs =
            FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

    private NPCFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * Creates a ghost entity.
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createGhost(Entity target) {
        Entity ghost = createBaseNPCNoAI();
        BaseEntityConfig config = configs.ghost;

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/meleeElf.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

//        animator.addAnimation("Left_Shoot", 0.1f, Animation.PlayMode.NORMAL);
//        animator.addAnimation("Right_Shoot", 0.1f, Animation.PlayMode.NORMAL);
//        animator.addAnimation("Up_Shoot", 0.1f, Animation.PlayMode.NORMAL);
//        animator.addAnimation("Down_Shoot", 0.1f, Animation.PlayMode.NORMAL);

        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ZigChaseTask(target, 11, 4f, 4f))
                        .addTask(new AlertableChaseTask(target, 10, 3f, 4f));

        ghost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(aiComponent)
                .addComponent(new GhostAnimationController());

        ghost.getComponent(AITaskComponent.class).
                addTask(new AlertableChaseTask(target, 10, 3f, 4f));
        ghost.getComponent(AITaskComponent.class).
                addTask(new ZigChaseTask(target, 11, 3f, 6f));

        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar.png", Texture.class));
        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar_decrease.png", Texture.class));
        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(HealthBar, HealthBarFrame, HealthBarDecrease);
        ghost.addComponent(healthBarComponent);

        ghost.getComponent(AnimationRenderComponent.class).scaleEntity();
        ghost.scaleWidth(1);
        ghost.scaleHeight(1);
        return ghost;
    }

//    public static Entity createGhostKing(Entity target) {
//        Entity ghostKing = createBaseNPCNoAI();
//        GhostKingConfig config = configs.ghostKing;
//        AITaskComponent aiTaskComponent = new AITaskComponent()
//                .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
//                .addTask(new AlertChaseTask(target, 10, 3f, 4f));
//        ghostKing.addComponent(aiTaskComponent);
//
//        AnimationRenderComponent animator =
//                new AnimationRenderComponent(
//                        ServiceLocator.getResourceService()
//                                .getAsset("images/ghost.atlas", TextureAtlas.class));
//        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.LOOP);
//        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.LOOP);
//        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.LOOP);
//        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.LOOP);
//
//        ghostKing
//                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
//                .addComponent(animator)
//                .addComponent(new GhostAnimationController());
//        ghostKing.setEntityType("AlertCaller");
//
//        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar.png", Texture.class));
//        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar_decrease.png", Texture.class));
//        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_border.png", Texture.class));
//        HealthBarComponent healthBarComponent = new HealthBarComponent(HealthBar, HealthBarFrame, HealthBarDecrease);
//        ghostKing.addComponent(healthBarComponent);
//
//        ghostKing.getComponent(AnimationRenderComponent.class).scaleEntity();
//        return ghostKing;
//    }
//
//    /**
//     * Creates an anchored ghost entity.
//     *
//     * @param target     entity to chase
//     * @param anchor     base entity to anchor to
//     * @param anchorSize how big the base's area
//     * @return entity
//     */
//
    public static Entity createAnchoredGhost(Entity target, Entity anchor, float anchorSize) {
        Entity anchoredGhost = createBaseNPCNoAI();
        BaseEntityConfig config = configs.ghost;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new AnchoredWanderTask(anchor, anchorSize, 2f))
                        .addTask(new AnchoredChaseTask(target, 3f, 4f, anchor, anchorSize))
                        .addTask(new AnchoredRetreatTask(anchor, anchorSize));
        anchoredGhost.addComponent(aiComponent);

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/meleeElf.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        anchoredGhost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController());

        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar.png", Texture.class));
        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar_decrease.png", Texture.class));
        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(HealthBar, HealthBarFrame, HealthBarDecrease);
        anchoredGhost.addComponent(healthBarComponent);

        anchoredGhost.getComponent(AnimationRenderComponent.class).scaleEntity();
        anchoredGhost.scaleWidth(1);
        anchoredGhost.scaleHeight(1);

        return anchoredGhost;
    }

//    /**
//     * Creates a anchored ghost entity.
//     * Anchor ghost only chase the target if the target approach the anchor point
//     *
//     * @param target      entity to chase
//     * @param anchor      base entity to anchor to
//     * @param anchorSizeX how big the base's area is in the X axis
//     * @param anchorSizeY how big the base's area is in the Y axis
//     * @return entity
//     */
//    public static Entity createAnchoredGhost(Entity target, Entity anchor, float anchorSizeX, float anchorSizeY) {
//        Entity anchoredGhost = createBaseNPCNoAI();
//        BaseEntityConfig config = configs.ghost;
//        AITaskComponent aiComponent =
//                new AITaskComponent()
//                        .addTask(new AnchoredWanderTask(anchor, anchorSizeX, anchorSizeY, 2f))
//                        .addTask(new AnchoredChaseTask(target, 3f, 4f, anchor, anchorSizeX, anchorSizeY))
//                        .addTask(new AnchoredRetreatTask(anchor, anchorSizeX, anchorSizeY));
//        anchoredGhost.addComponent(aiComponent);
//
//        AnimationRenderComponent animator =
//                new AnimationRenderComponent(
//                        ServiceLocator.getResourceService().getAsset("images/meleeElf.atlas", TextureAtlas.class));
//        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
//        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
//        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
//        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);
//
//        anchoredGhost
//                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
//                .addComponent(animator)
//                .addComponent(new GhostAnimationController());
//
//        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar.png", Texture.class));
//        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar_decrease.png", Texture.class));
//        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_border.png", Texture.class));
//        HealthBarComponent healthBarComponent = new HealthBarComponent(HealthBar, HealthBarFrame, HealthBarDecrease);
//        anchoredGhost.addComponent(healthBarComponent);
//
//        anchoredGhost.getComponent(AnimationRenderComponent.class).scaleEntity();
//        return anchoredGhost;
//    }

    /**
     * Creates a ranged ghost entity.
     * Ghost that shoot arrow at target
     * It will retreat if the target is approach in certain range
     *
     * @param target entity to chase
     * @return entity
     */
    public static Entity createRangedGhost(Entity target) {
        Entity ghost = createBaseNPCNoAI();
        GhostRangedConfig config = configs.ghostRanged;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new RangedChaseTask(target, 10, 15f, 20f));
        ShootProjectileTask shootProjectileTask = new ShootProjectileTask(target, 2000);
        shootProjectileTask.setProjectileType("normalArrow");
        shootProjectileTask.setMultishotChance(0.1);
        //shootProjectileTask.setProjectileType("trackingArrow");
        //shootProjectileTask.setMultishotChance(0.5);
        //shootProjectileTask.setProjectileType("fastArrow");
        //shootProjectileTask.setMultishotChance(0);
        aiComponent.addTask(shootProjectileTask);

        AnimationRenderComponent animator =

                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/minionEnemy.atlas", TextureAtlas.class));
//        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
//        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
//        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        animator.addAnimation("Left_Shoot", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Right_Shoot", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Up_Shoot", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("Down_Shoot", 0.1f, Animation.PlayMode.NORMAL);

        ghost
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController())
                .addComponent(aiComponent);
        ghost.setAttackRange(5);
        //ghost.getComponent(AnimationRenderComponent.class).scaleEntity();
        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar.png", Texture.class));
        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar_decrease.png", Texture.class));
        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(HealthBar, HealthBarFrame, HealthBarDecrease);
        ghost.addComponent(healthBarComponent);
        return ghost;
    }

    public static Entity createBossNPC(Entity target) {
        Entity boss = createBaseNPCNoAI();
        GhostKingConfig config = configs.ghostKing;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(target, 10, 7f, 10f))
                        .addTask(new TeleportationTask(target, 2000));

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService().getAsset("images/bossAttack.atlas", TextureAtlas.class));
        animator.addAnimation("floatLeft", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatRight", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatUp", 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("floatDown", 0.1f, Animation.PlayMode.NORMAL);

        boss
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
                .addComponent(animator)
                .addComponent(new GhostAnimationController())
                .addComponent(aiComponent);
        boss.setAttackRange(5);
        boss.getComponent(AnimationRenderComponent.class).scaleEntity();
        boss.scaleWidth(2);
        boss.scaleHeight(2);

        Sprite HealthBar = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar.png", Texture.class));
        Sprite HealthBarDecrease = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_bar_decrease.png", Texture.class));
        Sprite HealthBarFrame = new Sprite(ServiceLocator.getResourceService().getAsset("images/enemy_health_border.png", Texture.class));
        HealthBarComponent healthBarComponent = new HealthBarComponent(HealthBar, HealthBarFrame, HealthBarDecrease);
        boss.addComponent(healthBarComponent);
        //teleportation - despawn the enemy - if enemy health is lower than 50%
        // everytime the enemy get hit, it randomly teleport to a random position on the map.
        // weaknesses - it may teleport beside the character

        return boss;

    }

//    /**
//     * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
//     *
//     * @return entity
//     */
    private static Entity createBaseNPC(Entity target) {
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(2f, 2f), 2f))
                        .addTask(new ChaseTask(target, 10, 3f, 4f));
        Entity npc =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 2.5f))
                        .addComponent(aiComponent);

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }

    /**
     * Creates a generic NPC, with no ai,
     * to be used as a base entity by more specific NPC creation methods.
     *
     * @return entity
     */
    private static Entity createBaseNPCNoAI() {
        Entity npc =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0.5f));

        PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
        return npc;
    }
}
