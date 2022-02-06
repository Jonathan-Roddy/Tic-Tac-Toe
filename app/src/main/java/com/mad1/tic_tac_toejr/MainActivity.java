package com.mad1.tic_tac_toejr;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.view.View.OnClickListener;
import android.widget.Toast;

import static com.mad1.tic_tac_toejr.R.id.imageView;
import static com.mad1.tic_tac_toejr.R.layout.activity_main;

public class MainActivity extends AppCompatActivity implements OnEditorActionListener, OnClickListener, View.OnCreateContextMenuListener {
    // define variables for the app
    public TextView displayText;
    public ImageView displayGrid;
    // create 2d Array of buttons
    public Button[][] buttons = new Button[3][3];
    // setting player 1 'X' always starts
    private boolean player1Turn = true;
    private EditText playerOne, playerTwo;
    private String playerOneName = "Player 1", playerTwoName = "Player 2";
    private Button savePlayers;
    // count rounds in the game
    private int roundCount;
    private int player1Points, player2Points;
    private TextView textViewPlayer1, textViewPlayer2;

    // OnCreate functions to reference all buttons, text
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        // Calling reference to the Players TextViews
        textViewPlayer1= findViewById(R.id.text_view_p1);
        textViewPlayer2= findViewById(R.id.text_view_p2);
        displayText= findViewById(R.id.display);
        resetDisplay();
        // Button references
        // Create nested loop to call reference to each Button; button(i)++
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        displayGrid= findViewById(imageView);
        savePlayers= findViewById(R.id.savePlayers);
        checkPlayerForNames();

    }
    // create menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1: // new game
                //Toast.makeText(this, "New Game selected", Toast.LENGTH_SHORT).show();
                resetGame();
                return true;
            case R.id.item2: // settings - change name
                //Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.RelativeLayout, new SettingsFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onPause() {
        // save the instance variables

//        SharedPreferences.Editor editor = savedValues.edit();
//        editor.putInt("roundCount", roundCount);
//        editor.putInt("player1Points", player1Points);
//        editor.putInt("player2Points", player2Points);
//        editor.putBoolean("player1Turn", player1Turn);
//        editor.commit();
        super.onPause();
        bestOutOfThree();
    }
    @Override
    public void onResume() {super.onResume(); bestOutOfThree(); }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
        outState.putString("playerOneName", playerOneName);
        outState.putString("playerTwoName", playerTwoName);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
        playerOneName = savedInstanceState.getString("playerOneName");
        playerTwoName = savedInstanceState.getString("playerTwoName");
    }

    // logic for when buttons are clicked
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (player1Turn) {
            ((Button) v).setText("X");
            ((Button) v).setTextColor(getResources().getColor(R.color.myXBlack));
        }
        else {
            ((Button) v).setText("O");
            ((Button) v).setTextColor(getResources().getColor(R.color.myOGreen));
        }

        roundCount++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        }
        else if (roundCount == 9) {
            draw();
            updateDisplayText();
        }
        else {
            player1Turn = !player1Turn;
        }

        updateDisplayText();
        updatePointsText();
        bestOutOfThree();
    }


    // Logic for checking buttons for a 3 way match

    // How I have referenced the buttons
    //[00][01][02]
    //[10][11][12]
    //[20][21][22]

    // check horizontally
    //[00][01][02] == win
    //[10][11][12] == win
    //[20][21][22] == win

    // Check vertically
    //[00][10][20] == win
    //[01][11][21] == win
    //[02][12][22] == win

    // Check diagonally
    //[00][11][22] == win
    //[02][11][20] == win
    private boolean checkForWin() {
        String[][] field = new String[3][3];
        // Get reference to all buttons and store in field String 2D array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }

    // Game ends with Player 1 winning
    private void player1Wins() {
        player1Points++;
        //Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        if(playerOneName == null){
            displayText.setText("Player 1 wins!");
            Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            displayText.setText(playerOneName + " wins!");
            Toast.makeText(this, playerOneName + " wins!", Toast.LENGTH_SHORT).show();
        }
        updatePointsText();
        resetBoard();
    }
    // Game ends with Player 2 winning
    private void player2Wins() {
        player2Points++;
        //Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        if(playerTwoName == null){
            displayText.setText("Player 2 wins!");
            Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            displayText.setText(playerOneName + " wins!");
            Toast.makeText(this, playerTwoName + " wins!", Toast.LENGTH_SHORT).show();
        }
        updatePointsText();
        resetBoard();
    }
    // Game ends in a Draw
    private void draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        displayText.setText("Draw!");
        resetBoard();
    }

    // Update Player 1 and Player 2 text Points and Display if they have been given a name
    private void updatePointsText() {
        if(playerOneName == null) {
            textViewPlayer1.setText("Player 1 : " + player1Points);
        }
        else
        {
            textViewPlayer1.setText(playerOneName + " : " + player1Points);
        }
        if(playerTwoName == null) {
            textViewPlayer2.setText("Player 2 : " + player2Points);
        }
        else
        {
            textViewPlayer2.setText(playerTwoName + " : " + player2Points);
        }
    }
    // Update Display text Points on if Player 1 or Player 2 have won and Display if they have been given a name
    private void updateDisplayText() {
        if (playerOneName == null) {
            if (player1Turn == true) {
                displayText.setText("Player 1's Turn");
            }
        }
        else {
            if (player1Turn == true) {
                displayText.setText(playerOneName + "'s Turn");
            }
        }

        if (playerTwoName == null) {
            if (player1Turn != true) {
                displayText.setText("Player 2's Turn");
            }
        } else {
            if (player1Turn != true) {
                displayText.setText(playerTwoName + "'s Turn");
            }
        }
    }

    // Reset Game
    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        updateDisplayText();
        resetBoard();
    }
    // Reset Board
    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        resetDisplay();
        roundCount = 0;
        player1Turn = true;
    }
    // Reset Display
    private void resetDisplay(){
        if(playerOneName == null) {
            displayText.setText("Player 1 to Start!");
        }
        else displayText.setText(playerOneName + " to Start!");
    }

    // Check to see if Player 1 and Player 2 have been given names
    private void checkPlayerForNames() {
        if (!playerOneName.equals("Player 1"))playerOneName = playerOne.getText().toString();
        if (!playerTwoName.equals("Player 2")) playerTwoName = playerTwo.getText().toString();
    }
    // Save Player 1 and Player 2 inputs as a string
    public void savePlayersName(View v) {
        playerOne = findViewById(R.id.changePlayerOneInput);
        playerTwo = findViewById(R.id.changePlayerTwoInput);

        if (!playerOne.getText().toString().equals("") && !playerTwo.getText().toString().equals("")) {
            // Storing players names from input as a string var.
            playerOneName = playerOne.getText().toString();
            playerTwoName = playerTwo.getText().toString();
            Toast.makeText(MainActivity.this, "Wellcome " + playerOneName + " and " + playerTwoName + " to Tic-Tac-Toe!", Toast.LENGTH_LONG).show();

        }
        else  Toast.makeText(MainActivity.this, "Please enter your names", Toast.LENGTH_LONG).show();
    }

    // Logic for Best out of 3
    public void bestOutOfThree(){
        if(player1Points == 3)  {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            playSound();
            dlgAlert.setMessage(playerOneName + "you have won 3 times! Feels bad for " + playerTwoName);
            dlgAlert.setTitle("Congratulations");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            resetGame();
        }
        else if( player2Points == 3 )
        {
            playSound();
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage(playerTwoName + "you have won 3 times! Feels bad for " + playerOneName);
            dlgAlert.setTitle("Congratulations");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();

            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            resetGame();
        }

    }
    // Play congratulations when player has won 3 games
    public void playSound(){
        MediaPlayer congratulations= MediaPlayer.create(MainActivity.this,R.raw.congratulations);
        congratulations.start();
    }



}