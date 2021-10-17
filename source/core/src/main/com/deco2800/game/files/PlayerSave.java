package com.deco2800.game.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Player save information
 * Ways to save and load it
 */
public class PlayerSave {
    private static final String ROOT_DIR = "DECO2800Game";
    private static final String SAVE_FILE = "playersave.save";

    /**
     * Function which takes in a PLayerSave.Save object
     * and writes it to the save file as is.
     *
     * @param pSave Takes a PlayerSave.Save to be written to file
     */
    public static void write(Save pSave) {

        try {
            FileWriter saveWrite = new FileWriter(SAVE_FILE);

            saveWrite.write(String.valueOf(pSave.hasPlayed) + '\n');
            saveWrite.write(String.valueOf(pSave.lokiEnc) + '\n');
            saveWrite.write(String.valueOf(pSave.thorEnc) + '\n');
            saveWrite.write(String.valueOf(pSave.odinEnc) + '\n');

            saveWrite.write(String.valueOf(pSave.lokiWins) + '\n');
            saveWrite.write(String.valueOf(pSave.thorWins) + '\n');
            saveWrite.write(String.valueOf(pSave.odinWins) + '\n');

            saveWrite.close();

            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Function which returns a default save file object with
     * default values as though the game had never been reset
     *
     * @return Returns a PlayerSave.Save object with default values
     */
    public static Save initial() {
        return new Save();
    }

    /**
     * Function which reads and then returns a save object from
     * the file at location in SAVE_FILE
     *
     * @return Returns the PlayerSave.Save object as stored in the written
     * save file
     */
    public static Save load() {
        Save playerSave = new Save();


        try {
            File saveFile = new File(SAVE_FILE);
            Scanner saveRead = new Scanner(saveFile);

            playerSave.hasPlayed = Boolean.parseBoolean(saveRead.nextLine());
            playerSave.lokiEnc = Integer.parseInt(saveRead.nextLine());
            playerSave.thorEnc = Integer.parseInt(saveRead.nextLine());
            playerSave.odinEnc = Integer.parseInt(saveRead.nextLine());

            playerSave.lokiWins = Integer.parseInt(saveRead.nextLine());
            playerSave.thorWins = Integer.parseInt(saveRead.nextLine());
            playerSave.odinWins = Integer.parseInt(saveRead.nextLine());


        } catch (FileNotFoundException | NumberFormatException e) {
            e.printStackTrace();
        }


        return playerSave;
    }

    /**
     * Stores player game progress
     * Values are defaulted to no progress at all
     */
    public static class Save {
        // whether or not the player has played the tutorial/game before
        public boolean hasPlayed = false;


        // number of times the player has encountered a specific boss
        public int lokiEnc = 0;
        public int thorEnc = 0;
        public int odinEnc = 0;

        // number of times the player has defeated a specific boss
        public int lokiWins = 0;
        public int thorWins = 0;
        public int odinWins = 0;
    }
}
