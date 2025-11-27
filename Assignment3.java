/*
 * Author: Deniz Kilic
 * Date: November 21, 2025
 * Description: pig dice game (temp)
 */

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class Assignment3 extends JFrame implements ActionListener{
   
    // Game state variables
    private int player1Score = 0;
    private int player2Score = 0;
    private int turnScore = 0;
    private int currentPlayer = 1; // 1 or 2
    private boolean gameOver = false;
    private boolean isRolling = false; // Track if animation is in progress
    private Random random = new Random();
    private String player1Name = "";
    private String player2Name = "";
    
    // UI components
    private JTextField f1, f2, f3, f4, f5;
    private JLabel d1, d2;
    private JButton rollButton, holdButton;
    private JCheckBox speedModeCheckbox;
    
    // ESTABLISH FRAME SIZE + RESET VARS
    public Assignment3() {
        
        // Get player names at the start
        player1Name = JOptionPane.showInputDialog(null, "Enter Player 1's name:", "Player 1 Name", JOptionPane.QUESTION_MESSAGE);
        if (player1Name == null || player1Name.trim().isEmpty()) {
            player1Name = "Player 1";
        }
        
        player2Name = JOptionPane.showInputDialog(null, "Enter Player 2's name:", "Player 2 Name", JOptionPane.QUESTION_MESSAGE);
        if (player2Name == null || player2Name.trim().isEmpty()) {
            player2Name = "Player 2";
        }
             
        // Declare text fields
        setSize(662, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // LAYERING SETUP
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(getSize());
        setContentPane(layeredPane);


        // PIG BACKGROUND
        ImageIcon image1 = new ImageIcon("DiceBoard.png");
        JLabel bg = new JLabel(image1);
        bg.setBounds(0, 0, image1.getIconWidth(), image1.getIconHeight());
        layeredPane.add(bg, Integer.valueOf(0));


        // DICES
        // PALETTE_LAYER is the front layer
        d1 = new JLabel();
        ImageIcon dice1Icon = new ImageIcon("one.png");
        Image scaledDice1 = dice1Icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        d1.setIcon(new ImageIcon(scaledDice1));
        d1.setBounds(225, 60, 100, 100);
        layeredPane.add(d1, JLayeredPane.PALETTE_LAYER);
       
        d2 = new JLabel();
        ImageIcon dice2Icon = new ImageIcon("two.png");
        Image scaledDice2 = dice2Icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        d2.setIcon(new ImageIcon(scaledDice2));
        d2.setBounds(335, 60, 100, 100);
        layeredPane.add(d2, JLayeredPane.PALETTE_LAYER);
       
        // SCORE FIELDS
        // f1: Player 1 total score (top left)
        f1 = new JTextField(10);
        f1.setBounds(31, 78, 135, 47);
        f1.setEditable(false);
        f1.setText("0");
        f1.setHorizontalAlignment(JTextField.CENTER);
        f1.setFont(new Font("Arial", Font.BOLD, 24));
        layeredPane.add(f1, JLayeredPane.PALETTE_LAYER);
       
        // f2: Player 2 total score (top right)
        f2 = new JTextField(10);
        f2.setBounds(496, 78, 135, 47);
        f2.setEditable(false);
        f2.setText("0");
        f2.setHorizontalAlignment(JTextField.CENTER);
        f2.setFont(new Font("Arial", Font.BOLD, 24));
        layeredPane.add(f2, JLayeredPane.PALETTE_LAYER);
       
        // f3: Player 1 turn score (bottom left)
        f3 = new JTextField(10);
        f3.setBounds(31, 186, 135, 47);
        f3.setEditable(false);
        f3.setText("0");
        f3.setHorizontalAlignment(JTextField.CENTER);
        f3.setFont(new Font("Arial", Font.BOLD, 24));
        layeredPane.add(f3, JLayeredPane.PALETTE_LAYER);
       
        // f4: Player 2 turn score (bottom right)
        f4 = new JTextField(10);
        f4.setBounds(496, 186, 135, 47);
        f4.setEditable(false);
        f4.setText("0");
        f4.setHorizontalAlignment(JTextField.CENTER);
        f4.setFont(new Font("Arial", Font.BOLD, 24));
        layeredPane.add(f4, JLayeredPane.PALETTE_LAYER);
       
        // f5: Status message field (top center)
        f5 = new JTextField(10);
        f5.setBounds(240, 20, 200, 20);
        f5.setEditable(false);
        f5.setText(player1Name + "'s Turn");
        f5.setHorizontalAlignment(JTextField.CENTER);
        layeredPane.add(f5, JLayeredPane.PALETTE_LAYER);
       
         // BUTTONS
        rollButton = new JButton("Roll");
        rollButton.addActionListener(this);
        rollButton.setBounds(235, 170, 70, 35);
        layeredPane.add(rollButton, JLayeredPane.PALETTE_LAYER);
       
        holdButton = new JButton("Hold");
        holdButton.addActionListener(this);
        holdButton.setBounds(370, 170, 70, 35);
        layeredPane.add(holdButton, JLayeredPane.PALETTE_LAYER);


        JButton b3 = new JButton("Menu");
        b3.addActionListener(this);
        b3.setBounds(350, 225, 100, 35);
        layeredPane.add(b3, JLayeredPane.PALETTE_LAYER);


        JButton b4 = new JButton("Exit");
        b4.addActionListener(this);
        b4.setBounds(220, 225, 100, 35);
        layeredPane.add(b4, JLayeredPane.PALETTE_LAYER);
        
        // Speed Mode checkbox
        speedModeCheckbox = new JCheckBox("Speed Mode");
        speedModeCheckbox.setBounds(465, 225, 130, 35);
        speedModeCheckbox.setOpaque(false);
        speedModeCheckbox.setForeground(Color.BLACK);
        speedModeCheckbox.setFont(new Font("Arial", Font.BOLD, 11));
        layeredPane.add(speedModeCheckbox, JLayeredPane.PALETTE_LAYER);
           
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        
        // Exit button - close the game
        if (action.equals("Exit")) {
            System.exit(0);
        }
        
        // Menu button - reset the game
        else if (action.equals("Menu")) {
            resetGame();
        }
        
        // Roll button - roll the dice
        else if (action.equals("Roll")) {
            if (!gameOver && !isRolling) {
                rollDice();
            }
        }
        
        // Hold button - bank the turn score
        else if (action.equals("Hold")) {
            if (!gameOver && turnScore > 0 && !isRolling) {
                holdTurn();
            }
        }
    }
    
    // Roll two dice and update game state according to Two-Dice Pig rules
    private void rollDice() {
        // Disable buttons during animation
        isRolling = true;
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);
        
        // Final die values
        final int die1 = random.nextInt(6) + 1;
        final int die2 = random.nextInt(6) + 1;
        
        // Check if speed mode is enabled
        if (speedModeCheckbox.isSelected()) {
            // Instant roll - no animation
            updateDiceImage(d1, die1);
            updateDiceImage(d2, die2);
            processRollResult(die1, die2);
            
            // Re-enable buttons immediately
            isRolling = false;
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
        } else {
            // Animated roll
            animateRoll(die1, die2);
        }
    }
    
    // Animate the dice roll with slowing effect
    private void animateRoll(final int die1, final int die2) {
        // Animation parameters - shortened to ~2 seconds
        final int[] delays = {50, 60, 80, 120, 200, 350}; // Slowing down effect
        
        // Animate the dice roll
        final Timer[] animationTimer = new Timer[1];
        
        animationTimer[0] = new Timer(delays[0], new ActionListener() {
            private int frameCount = 0;
            private int delayIndex = 0;
            
            @Override
            public void actionPerformed(ActionEvent evt) {
                // Show random dice faces during animation
                int randomDie1 = random.nextInt(6) + 1;
                int randomDie2 = random.nextInt(6) + 1;
                updateDiceImage(d1, randomDie1);
                updateDiceImage(d2, randomDie2);
                
                frameCount++;
                
                // Gradually slow down the animation
                if (frameCount > 4 + delayIndex * 2 && delayIndex < delays.length - 1) {
                    delayIndex++;
                    animationTimer[0].setDelay(delays[delayIndex]);
                }
                
                // Stop animation after ~2 seconds
                if (frameCount >= 25) {
                    animationTimer[0].stop();
                    
                    // Show final result
                    updateDiceImage(d1, die1);
                    updateDiceImage(d2, die2);
                    
                    // Process the roll result
                    processRollResult(die1, die2);
                    
                    // Re-enable buttons
                    isRolling = false;
                    rollButton.setEnabled(true);
                    holdButton.setEnabled(true);
                }
            }
        });
        
        animationTimer[0].start();
    }
    
    // Process the dice roll result according to game rules
    private void processRollResult(int die1, int die2) {
        
        // Check for double 1s - lose all points
        if (die1 == 1 && die2 == 1) {
            String currentName = (currentPlayer == 1) ? player1Name : player2Name;
            f5.setText(currentName + " - Snake Eyes! Lost all points!");
            if (currentPlayer == 1) {
                player1Score = 0;
                f1.setText("0");
            } else {
                player2Score = 0;
                f2.setText("0");
            }
            turnScore = 0;
            updateTurnScore();
            switchPlayer();
        }
        // Check for single 1 - lose turn score
        else if (die1 == 1 || die2 == 1) {
            String currentName = (currentPlayer == 1) ? player1Name : player2Name;
            f5.setText(currentName + " - Rolled a 1! Lost turn score!");
            turnScore = 0;
            updateTurnScore();
            switchPlayer();
        }
        // Normal roll - add to turn score
        else {
            int rollTotal = die1 + die2;
            turnScore += rollTotal;
            updateTurnScore();
            
            // Check for doubles - must roll again
            if (die1 == die2) {
                String currentName = (currentPlayer == 1) ? player1Name : player2Name;
                f5.setText(currentName + " - Doubles! Must roll again!");
                // Disable hold button for doubles
                holdButton.setEnabled(false);
                
                // Re-enable after a delay to force another roll
                Timer timer = new Timer(1000, new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        holdButton.setEnabled(true);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                String currentName = (currentPlayer == 1) ? player1Name : player2Name;
                f5.setText(currentName + "'s Turn - Roll: " + rollTotal);
            }
        }
    }
    
    // Update the dice image based on the roll value
    private void updateDiceImage(JLabel diceLabel, int value) {
        String[] diceImages = {"one.png", "two.png", "three.png", "four.png", "five.png", "six.png"};
        ImageIcon icon = new ImageIcon(diceImages[value - 1]);
        // Scale the image to fit the dice display area (100x100)
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        diceLabel.setIcon(new ImageIcon(scaledImage));
    }
    
    // Player chooses to hold - add turn score to total score
    private void holdTurn() {
        if (currentPlayer == 1) {
            player1Score += turnScore;
            f1.setText(String.valueOf(player1Score));
            
            // Check for win
            if (player1Score >= 100) {
                f5.setText(player1Name + " Wins!");
                gameOver = true;
                rollButton.setEnabled(false);
                holdButton.setEnabled(false);
                return;
            }
        } else {
            player2Score += turnScore;
            f2.setText(String.valueOf(player2Score));
            
            // Check for win
            if (player2Score >= 100) {
                f5.setText(player2Name + " Wins!");
                gameOver = true;
                rollButton.setEnabled(false);
                holdButton.setEnabled(false);
                return;
            }
        }
        
        turnScore = 0;
        updateTurnScore();
        switchPlayer();
    }
    
    // Switch to the other player
    private void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
        String currentName = (currentPlayer == 1) ? player1Name : player2Name;
        f5.setText(currentName + "'s Turn");
        turnScore = 0;
        updateTurnScore();
    }
    
    // Update the turn score display
    private void updateTurnScore() {
        if (currentPlayer == 1) {
            f3.setText(String.valueOf(turnScore));
            f4.setText("0");
        } else {
            f4.setText(String.valueOf(turnScore));
            f3.setText("0");
        }
    }
    
    // Reset the game to initial state
    private void resetGame() {
        player1Score = 0;
        player2Score = 0;
        turnScore = 0;
        currentPlayer = 1;
        gameOver = false;
        
        f1.setText("0");
        f2.setText("0");
        f3.setText("0");
        f4.setText("0");
        f5.setText(player1Name + "'s Turn");
        
        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
        
        // Reset dice to initial position
        ImageIcon resetDice1 = new ImageIcon("one.png");
        Image scaledReset1 = resetDice1.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        d1.setIcon(new ImageIcon(scaledReset1));
        
        ImageIcon resetDice2 = new ImageIcon("two.png");
        Image scaledReset2 = resetDice2.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        d2.setIcon(new ImageIcon(scaledReset2));
    }


    public static void main(String args[]) {
        //adds a new class object and sets it to visible
        Assignment3 x = new Assignment3();
        x.setVisible(true);
    }
}
