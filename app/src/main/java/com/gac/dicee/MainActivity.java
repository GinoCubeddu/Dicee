package com.gac.dicee;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int scoreToBeat;
    private int score;

    // Variables to hold the image views for the dice
    private ImageView leftDiceView;
    private ImageView rightDiceView;

    // Variables to hold the text view for the roll information, roll total and point information
    private TextView rollInformationView;
    private TextView rollTotalView;
    private TextView pointTotalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise variables
        scoreToBeat = 7;
        score = 0;
        leftDiceView = findViewById(R.id.image_leftDice);
        rightDiceView = findViewById(R.id.image_rightDice);
        rollInformationView = findViewById(R.id.roll_information);
        rollTotalView = findViewById(R.id.roll_total);
        pointTotalView = findViewById(R.id.points_total);

        // Get refrence to the dice images
        final int[] diceImages = {
                R.drawable.dice1,
                R.drawable.dice2,
                R.drawable.dice3,
                R.drawable.dice4,
                R.drawable.dice5,
                R.drawable.dice6
        };

        // Set up event listener for when the roll buton is clicked
        findViewById(R.id.button_roll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // roll the dice
                int[] rolls = {getRandomInt(diceImages.length), getRandomInt(diceImages.length)};
                // Check the roll
                score += checkRoll(rolls);
                pointTotalView.setText(String.format(getString(R.string.points_total), score));
                // Update the images of the dice
                leftDiceView.setImageResource(diceImages[rolls[0]]);
                rightDiceView.setImageResource(diceImages[rolls[1]]);
            }
        });
    }

    private int getRandomInt(int limit) {
        Random rand = new Random();
        return rand.nextInt(limit);
    }

    private int checkRoll(int[] rolls) {
        // perform checks on rolls
        int rollTotal = getRollTotal(rolls);
        boolean beatScore = rollTotal > scoreToBeat;
        boolean rollsMatch = checkRollsMatch(rolls);

        // Provide a text output showing what score they rolled
        rollTotalView.setText(String.format(getString(R.string.roll_total), rollTotal));

        // Check what score the player will receive and update the rollInformationView
        int pointsToAdd = 1;
        if (rollsMatch && beatScore) {
            rollInformationView.setText(getString(R.string.rolled_high_match));
            pointsToAdd = 2;
        }
        else if (rollsMatch)
            rollInformationView.setText(getString(R.string.rolled_match));
        else if (beatScore)
            rollInformationView.setText(getString(R.string.rolled_high));
        else {
            rollInformationView.setText(getString(R.string.rolled_no_high_match));
            pointsToAdd = 0;
        }

        return pointsToAdd;
    }

    private int getRollTotal(int[] rolls) {
        // Start at 0
        int total = 0;
        // Loop through the rolls and add the roll + 1 to the total
        // (The extra +1 is due to indexing of the dice array)
        for (int i = 0; i < rolls.length; i++)
            total += (rolls[i] + 1);

        return total;
    }

    private boolean checkRollsMatch(int[] rolls) {
        // Match will only change to false if below checks fail
        boolean rollsMatch = true;

        if (rolls.length > 1) {
            // Select the first roll then start at index one to check against the first
            // This is used because if we ever decide to add more dice the method
            // will not have to change.
            int previousRoll = rolls[0];
            for (int i = 1; i < rolls.length; i++){
                if(previousRoll != rolls[i]) {
                    // The the current die does not match the previous value
                    // then not all dice are matching
                    rollsMatch = false;
                    break;
                }
            }
        } else {
            rollsMatch = false;
        }
        return rollsMatch;
    }
}
