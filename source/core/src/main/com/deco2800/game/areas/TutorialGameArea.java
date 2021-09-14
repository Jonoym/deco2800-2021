package com.deco2800.game.areas;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.Map;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.CutsceneTriggerFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.textbox.DialogueSet;
import com.deco2800.game.ui.textbox.RandomDialogueSet;
import com.deco2800.game.ui.textbox.TextBox;
import com.deco2800.game.utils.math.GridPoint2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Forest area for the demo game with trees, a player, and some enemies.
 */
public class TutorialGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(TestGameArea.class);
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
    private static final GridPoint2 TEST_TRIGGER = new GridPoint2(6, 15);
    private static final float WALL_WIDTH = 0.1f;
    private static final String[] forestTextures = {
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/trap.png",
            "images/test.png",
            "images/arrow_normal.png",
            "images/ghost_king.png",
            "images/ghost_crown.png",
            "images/ghost_1.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/hex_grass_1.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_1.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/mud.png",
            "images/player.png",
            "images/health_left.png",
            "images/health_middle.png",
            "images/health_right.png",
            "images/health_frame_left.png",
            "images/health_frame_middle.png",
            "images/health_frame_right.png",
            "images/hp_icon.png",
            "images/dash_icon.png",
            "images/prisoner.png"
    };
    private static String[] tileTextures = null;
    private static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/player.atlas", "images/health_bar.atlas"
    };
    private static final String[] forestSounds = {
            "sounds/Impact4.ogg", "sounds/impact.ogg", "sounds/swish.ogg"
    };
    private static final String[] arrowSounds = {
            "sounds/arrow_disappear.mp3",
            "sounds/arrow_shoot.mp3"
    };
    private static final String backgroundMusic = "sounds/RAGNAROK_MAIN_SONG_76bpm.mp3";
    private static final String[] forestMusic = {backgroundMusic};

    private final TerrainFactory terrainFactory;

    public TutorialGameArea(TerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        Map m = FileLoader.readClass(Map.class, "maps/test_map.json");
        tileTextures = m.TileRefsArray();

        super.create();
        loadAssets();
        displayUI();

        spawnTerrain();
        spawnPlayer();
        spawnCutsceneTrigger();

        playMusic();
        setDialogue();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Map Test"));
        spawnEntity(ui);
    }

    private void spawnCutsceneTrigger() {
        Entity trigger = CutsceneTriggerFactory.createDialogueTrigger(RandomDialogueSet.TUTORIAL,
                DialogueSet.ORDERED);
        spawnEntityAt(trigger, TEST_TRIGGER, true, true);

        Entity trigger3 = CutsceneTriggerFactory.createLokiTrigger(RandomDialogueSet.LOKI_OPENING,
                DialogueSet.BOSS_DEFEATED_BEFORE);
        spawnEntityAt(trigger3, new Vector2(7f, 9.5f), true, true);

        /*Entity moveTrigger = CutsceneTriggerFactory.createMoveTrigger(new Vector2(-1f, 0f), 5, 0);
        spawnEntityAt(moveTrigger, new Vector2(10,5.8f), true, true);
        Entity moveTrigger2 = CutsceneTriggerFactory.createMoveTrigger(new Vector2(0f, -1f), 0, 5);
        spawnEntityAt(moveTrigger2, new Vector2(10.2f,9), true, true); */

        Entity moveTrigger3 = CutsceneTriggerFactory.createAttackTrigger(3, Input.Keys.D);
        spawnEntityAt(moveTrigger3, new Vector2(10, 5.8f), true, true);

        Entity moveTrigger4 = CutsceneTriggerFactory.createMoveTrigger(new Vector2(1f, 0f), 4, 0);
        spawnEntityAt(moveTrigger4, new Vector2(2.2f, 3.3f), true, true);

        Entity moveTrigger5 = CutsceneTriggerFactory.createMoveTrigger(new Vector2(0f, 1f), 0, 3);
        spawnEntityAt(moveTrigger5, new Vector2(6.3f, 3.3f), true, true);

        Entity moveTrigger6 = CutsceneTriggerFactory.createMoveTrigger(new Vector2(1f, 0f), 4, 0);
        spawnEntityAt(moveTrigger6, new Vector2(6.3f, 6.5f), true, true);
    }

    private void spawnTerrain() {
        // Background terrain
        terrain = terrainFactory.createTerrain(TerrainType.TEST);
        spawnEntity(new Entity().addComponent(terrain));

        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        // Left
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
        // Right
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        // Top
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        // Bottom
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);

        //Walls imported from JSON (Not working as intended, leave for sprint 2)
        /*Map m = FileLoader.readClass(Map.class, "maps/test_map.json");
        HashMap<String, Integer>[] walls = m.getWallObjects();
        int X = 0;
        int Y = 2;
        int WIDTH = 1;
        int HEIGHT = 3;
        for (HashMap<String, Integer> wall : walls) {
            String wallString = wall.values().toString();
            String wallNoBracket = wallString.substring(1, wallString.length() - 1);
            String[] wallValues = wallNoBracket.split(", ");
            float xFloat = Float.parseFloat(wallValues[X]);
            int x = (int) xFloat;
            float yFloat = Float.parseFloat(wallValues[Y]);
            int y = (int) yFloat;
            float width = Float.parseFloat(wallValues[WIDTH]);
            float height = Float.parseFloat(wallValues[HEIGHT]);
            int unitHeight = (int) ((height/32f));
            spawnEntityAt(
                    ObstacleFactory.createWall((width/32f)*0.5f, (height/32f)*0.5f),
                    new GridPoint2(x, 25 - (y + unitHeight)),
                    false,
                    false);
        }*/

        //Manually placed walls, will be deleted in next sprint
        //Left Wall
        spawnEntityAt(ObstacleFactory.createWall(0.5f, 6f),
                new GridPoint2(3, 7),
                false,
                false);

        //Bottom-Left Wall
        spawnEntityAt(ObstacleFactory.createWall(5.5f, 0.5f),
                new GridPoint2(3, 6),
                false,
                false);

        //Bottom-Right Wall
        spawnEntityAt(ObstacleFactory.createWall(3.5f, 0.5f),
                new GridPoint2(14, 10),
                false,
                false);

        //Right Wall
        spawnEntityAt(ObstacleFactory.createWall(0.5f, 4.5f),
                new GridPoint2(21, 10),
                false,
                false);

        //Top-Right Wall
        spawnEntityAt(ObstacleFactory.createWall(3.5f, 0.5f),
                new GridPoint2(14, 19),
                false,
                false);

        //Top-Left Wall
        spawnEntityAt(ObstacleFactory.createWall(5.5f, 0.5f),
                new GridPoint2(3, 16),
                false,
                false);

        //Middle-Top Wall
        spawnEntityAt(ObstacleFactory.createWall(0.5f, 3f),
                new GridPoint2(13, 16),
                false,
                false);

        //Middle-Bottom Wall
        spawnEntityAt(ObstacleFactory.createWall(0.5f, 2f),
                new GridPoint2(14, 7),
                false,
                false);
    }

    private void spawnPlayer() {
        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
        player = newPlayer;
    }

    private void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(tileTextures);
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(forestSounds);
        resourceService.loadMusic(forestMusic);
        resourceService.loadSounds(arrowSounds);

        while (resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(forestTextures);
        resourceService.unloadAssets(tileTextures);
        resourceService.unloadAssets(forestTextureAtlases);
        resourceService.unloadAssets(forestSounds);
        resourceService.unloadAssets(forestMusic);
        resourceService.unloadAssets(arrowSounds);
    }

    /**
     * Sets the dialogue for when the game first loads.
     */
    private void setDialogue() {
        TextBox textBox = ServiceLocator.getEntityService()
                .getUIEntity().getComponent(TextBox.class);
        textBox.setRandomFirstEncounter(RandomDialogueSet.TUTORIAL);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}