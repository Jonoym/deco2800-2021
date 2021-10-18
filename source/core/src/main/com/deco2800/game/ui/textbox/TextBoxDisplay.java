package com.deco2800.game.ui.textbox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.BooleanObject;

import java.util.Timer;
import java.util.TimerTask;

public class TextBoxDisplay extends UIComponent {

    /**
     * The priority of the UI component to be displayed on the screen.
     */
    private static final float Z_INDEX = 10f;

    /**
     * Instance of the Text Box that will be displayed to the screen.
     */
    private TextBox textBox;

    /**
     * The name to be displayed on the text box for the main character.
     */
    private Label mainCharacterName;

    /**
     * The mainCharacterLabel that will be added to the screen to display the message.
     */
    private Label mainCharacterLabel;

    /**
     * Stores the image that will surround the text message.
     */
    private Image mainCharacterBox;

    /**
     * Stores the main character image that will surround the text message.
     */
    private Image mainCharacterImage;

    /**
     * The name to be displayed on the text box for the NPC.
     */
    private Label enemyName;

    /**
     * The mainCharacterLabel that will be added to the screen to display the message.
     */
    private Label enemyLabel;

    /**
     * Stores the image that will surround the text message.
     */
    private Image enemyBox;

    /**
     * Stores the enemy character image that will surround the text message.
     */
    private Image enemyImage;

    /**
     * Bars to indicate a cutscene is taking place.
     */
    private Image topBar;
    private Image botBar;

    /**
     * If the cutscene is currently opening.
     */
    private final BooleanObject opening = new BooleanObject(false);

    /**
     * If the cutscene is currently closing.
     */
    private final BooleanObject closing = new BooleanObject(false);

    private final float textBoxHeight = 400f;

    private final float textBoxWidth = 800f;

    private final float displayYPos = 115f;

    private final float textYPos = 250f;

    private final float characterImageYPos = 50f;

    private final float characterSize = 768f;

    private final float mainCharacterDisplayX = 70f;

    private final float nameY = 325f;

    private final float enemyDisplayX =
            ServiceLocator.getRenderService().getStage().getWidth() - textBoxWidth - mainCharacterDisplayX;

    private final float enemyTextX =
            ServiceLocator.getRenderService().getStage().getWidth() - textBoxWidth + 60f;

    private final float enemyCharacterX =
            ServiceLocator.getRenderService().getStage().getWidth() - characterSize - mainCharacterDisplayX + 100f;

    private final float enemyNameX = ServiceLocator.getRenderService().getStage().getWidth() - 705;

    private final float barHeight = 120f;

    @Override
    public void create() {
        super.create();
        textBox = entity.getComponent(TextBox.class);
        addActors();
    }

    /**
     * Adds the mainCharacterLabel actor to the screen which will be constantly updated to display changes.
     */
    private void addActors() {
        float MAIN_CHARACTER_NAME_X = 550f;
        float MAIN_CHARACTER_TEXT_X = 180f;
        float MAIN_CHARACTER_X_POS = -100f;

        topBar = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/black_bars.png", Texture.class));
        botBar = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/black_bars.png", Texture.class));
        topBar.setWidth(ServiceLocator.getRenderService().getStage().getWidth());
        topBar.setHeight(barHeight);
        topBar.setY(ServiceLocator.getRenderService().getStage().getHeight());
        botBar.setHeight(barHeight);
        botBar.setWidth(ServiceLocator.getRenderService().getStage().getWidth());
        botBar.setY(-barHeight);

        stage.addActor(topBar);
        stage.addActor(botBar);

        //Text box for the main character set up
        mainCharacterName = new Label("WARRIOR", skin);
        mainCharacterName.setAlignment(Align.center);
        mainCharacterName.setPosition(MAIN_CHARACTER_NAME_X, nameY);
        mainCharacterLabel = new Label("", skin);
        mainCharacterLabel.setPosition(MAIN_CHARACTER_TEXT_X, textYPos);
        mainCharacterBox = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/default_text_box.png", Texture.class));
        mainCharacterBox.setPosition(mainCharacterDisplayX, displayYPos);
        mainCharacterBox.setWidth(textBoxWidth);
        mainCharacterBox.setHeight(textBoxHeight);
        mainCharacterImage = new Image(ServiceLocator.getResourceService()
                .getAsset("images/textBoxDisplay/main_character_image.png", Texture.class));
        mainCharacterImage.setPosition(MAIN_CHARACTER_X_POS, characterImageYPos);
        mainCharacterImage.setWidth(characterSize);
        mainCharacterImage.setHeight(characterSize);

        stage.addActor(mainCharacterImage);
        stage.addActor(mainCharacterBox);
        stage.addActor(mainCharacterLabel);
        stage.addActor(mainCharacterName);

        setEnemyTextBox();
    }

    /**
     * Displays the message to the screen if the text box is open, the sub message will be
     * displayed.
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {
        if (textBox.shouldShowBars()) {
            if (!opening.getBoolean()) {
                closing.setFalse();
                opening.setTrue();
                openBars();
            }
            if (textBox.isOpen()) {
                if (textBox.isMainCharacterShowing()) {
                    setNPCVisible(false);
                    setMainCharacterVisible(true);
                    mainCharacterLabel.setText(textBox.getSubMessage());
                } else {
                    setMainCharacterVisible(false);
                    setNPCVisible(true);
                    setEnemyTextBox();
                    enemyLabel.setText(textBox.getSubMessage());
                }
            } else {
                setMainCharacterVisible(false);
                setNPCVisible(false);
            }
        } else {
            setMainCharacterVisible(false);
            setNPCVisible(false);
            if (!closing.getBoolean()) {
                closing.setTrue();
                opening.setFalse();
                closeBars();

            }
        }
    }

    /**
     * Sets all of the display for the main character to the boolean value of visible
     *
     * @param visible boolean which is true if the items should be displayed, false otherwise
     */
    private void setMainCharacterVisible(boolean visible) {
        mainCharacterName.setVisible(visible);
        mainCharacterImage.setVisible(visible);
        mainCharacterLabel.setVisible(visible);
        mainCharacterBox.setVisible(visible);
    }

    /**
     * Sets all of the display for the NPC to the boolean value of visible
     *
     * @param visible boolean which is true if the items should be displayed, false otherwise
     */
    private void setNPCVisible(boolean visible) {
        enemyName.setVisible(visible);
        enemyImage.setVisible(visible);
        enemyLabel.setVisible(visible);
        enemyBox.setVisible(visible);
    }


    /**
     * Makes the bars fade into the screen.
     */
    private void openBars() {
        moveDown(topBar, opening);
        moveUp(botBar, opening);
    }

    /**
     * Makes the bars fade out of the screen.
     */
    private void closeBars() {
        moveDown(botBar, closing);
        moveUp(topBar, closing);
    }

    /**
     * Moves the bars down relative to their original height.
     *
     * @param bar  the image that will change position
     * @param type the boolean type that will be checked to repeat
     */
    private void moveDown(Image bar, BooleanObject type) {
        float initialHeight = -120;
        if (bar == topBar) {
            initialHeight = ServiceLocator.getRenderService().getStage().getHeight();
        }
        if (bar.getY() > initialHeight - barHeight && type.getBoolean()) {
            bar.setY(bar.getY() - 6);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveDown(bar, type);
                    timer.cancel();
                }
            }, 15);
        }
    }

    /**
     * Moves the bars up relative to their original height.
     *
     * @param bar  the image that will change position
     * @param type the boolean type that will be checked to repeat
     */
    private void moveUp(Image bar, BooleanObject type) {
        float initialHeight = -120;
        if (bar == topBar) {
            initialHeight = ServiceLocator.getRenderService().getStage().getHeight();
        }
        if (bar.getY() < initialHeight + barHeight && type.getBoolean()) {
            bar.setY(bar.getY() + 6);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    moveUp(bar, type);
                    timer.cancel();
                }
            }, 15);
        }
    }

    /**
     * Sets the display of the text box to match the NPC speaking.
     */
    private void setEnemyTextBox() {
        RandomDialogueSet set = textBox.getRandomDialogueSet();
        if (set == null) {
            return;
        }
        if (enemyName != null) {
            enemyName.remove();
        }
        if (enemyImage != null) {
            enemyImage.remove();
        }
        if (enemyBox != null) {
            enemyBox.remove();
        }
        if (enemyLabel != null) {
            enemyLabel.remove();
        }

        switch (set) {
            case TUTORIAL:
                enemyName = new Label("PRISONER", skin);
                enemyImage = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/prisoner_image.png", Texture.class));
                enemyBox = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/prison_text_box.png", Texture.class));
                break;
            case LOKI2_INTRODUCTION:
            case LOKI2_ENCOUNTER:
            case LOKI_INTRODUCTION:
            case LOKI_ENCOUNTER:
                enemyName = new Label("    LOKI", skin);
                enemyImage = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/loki_image.png", Texture.class));
                enemyBox = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/loki_text_box.png", Texture.class));
                break;
            case ELF_ENCOUNTER:
            case ELF_INTRODUCTION:
                enemyName = new Label("     Elf", skin);
                enemyImage = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/elf_image.png", Texture.class));
                enemyBox = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/prison_text_box.png", Texture.class));
                break;
            case ODIN_INTRODUCTION:
            case ODIN_ENCOUNTER:
            case ODIN_KILLED:
                enemyName = new Label("     ODIN", skin);
                enemyImage = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/odin_image.png", Texture.class));
                enemyBox = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/enemy_default_text_box.png", Texture.class));
                break;
            case THOR_ENCOUNTER:
            case THOR_INTRODUCTION:
                enemyName = new Label("     THOR", skin);
                enemyImage = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/thor_image.png", Texture.class));
                enemyBox = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/textBoxDisplay/outdoor_text_box.png", Texture.class));
                break;
            default:
                break;
        }
        setNPCPosition();
    }

    /**
     * Sets the position of all the items to be displayed for the updated NPC.
     */
    private void setNPCPosition() {
        enemyLabel = new Label("", skin);
        enemyLabel.setPosition(enemyTextX, textYPos);

        enemyName.setAlignment(Align.center);
        enemyName.setPosition(enemyNameX, nameY);

        enemyImage.setPosition(enemyCharacterX, characterImageYPos);
        enemyImage.setWidth(characterSize);
        enemyImage.setHeight(characterSize);

        enemyBox.setPosition(enemyDisplayX, displayYPos);
        enemyBox.setWidth(textBoxWidth);
        enemyBox.setHeight(textBoxHeight);

        stage.addActor(enemyImage);
        stage.addActor(enemyBox);
        stage.addActor(enemyName);
        stage.addActor(enemyLabel);
    }

    /**
     * Gets the priority of the text display.
     *
     * @return the priority of the text to be displayed to the screen.
     */
    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        super.dispose();
        mainCharacterName.remove();
        mainCharacterImage.remove();
        mainCharacterLabel.remove();
        mainCharacterBox.remove();
        enemyName.remove();
        enemyImage.remove();
        enemyLabel.remove();
        enemyBox.remove();
        topBar.remove();
        botBar.remove();
    }
}
