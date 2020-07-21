package com.CyberNerdForHireGames.SlimeInvaders.in_game_screen;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.CyberNerdForHireGames.SlimeInvaders.end_game.EndGameScreen;

import java.io.IOException;
import java.lang.reflect.Array;


public class GameView extends SurfaceView implements Runnable {

    Context context;

    // Game Thread
    private Thread gameThread = null;

    // A boolean which we will set and unset
    // when the game is running- or not.
    private SurfaceHolder ourHolder;

    // Game is paused at the start
    private volatile boolean playing;

    // Game is paused at the start
    private boolean paused = true;

    // A Canvas and a Paint object
    private Canvas canvas;
    private Paint paint;

    // This variable tracks the game frame rate
    private long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // The size of the screen in pixels
    private int screenX;
    private int screenY;

    // The players ship
    private PlayerShip playerShip;

    // The player's bullet
    private Bullet bullet;

    // The invaders bullets
    private Bullet[] slimeBullets = new Bullet[200];
    private int nextBullet;
    private int maxSlimeBullets = 10;

    // Up to 60 slimes
    Slime[] slimeInvaders = new Slime[60];
    int numSlimeInvaders = 0;

    // The player's shelters are built from asteroid rock
    private NearbyAsteroid[] rocks = new NearbyAsteroid[400];
    private int numRocks;

    // For sound FX
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;

    // The score
    int score = 0;

    // Lives
    private int lives = 3;

    // How menacing should the sound be?
    private long menaceInterval = 1000;

    // Which menace sound should play next
    private boolean uhOrOh;

    // When did we last play a menacing sound
    private long lastMenaceTime = System.currentTimeMillis();

    //instance of the score
    private int finalScore;




    public GameView(Context context, int x, int y){
        super(context);
        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        // This SoundPool is deprecated but don't worry
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        try{
            // Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("shoot.ogg");
            shootID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderexplode.ogg");
            invaderExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerexplode.ogg");
            playerExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("uh.ogg");
            uhID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("oh.ogg");
            ohID = soundPool.load(descriptor, 0);
        }catch(IOException e){
                    // Print an error message to the console
                    Log.e("error", "failed to load sound files");
        }

        prepareLevel();
    }

    private void prepareLevel(){



        // Here we will initialize all the game objects
        // Reset the menace level
        menaceInterval = 1000;

        // Make a new player space ship
        playerShip = new PlayerShip(context, screenX, screenY);

        // Prepare the players bullet
        bullet = new Bullet(screenY);

        // Initialize the slimeBullets array
        for(int i = 0; i < slimeBullets.length; i++){
            slimeBullets[i] = new Bullet(screenY);
        }
        // Build an army of slimes
        numSlimeInvaders = 0;
        for(int column = 0; column < 6; column ++ ){
            for(int row = 0; row < 5; row ++ ){
                slimeInvaders[numSlimeInvaders] = new Slime(context, row, column, screenX, screenY);
                numSlimeInvaders ++;
            }
        }
        // Build the shelters (Nearby Asteroids)
        numRocks = 0;
        for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 10; column ++ ) {
                for (int row = 0; row < 5; row++) {
                    rocks[numRocks] = new NearbyAsteroid(row, column, shelterNumber, screenX, screenY);
                    numRocks++;
                }
            }
        }

    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // We will do something new here towards the end of the project
            if(!paused) {
                if ((startFrameTime - lastMenaceTime) > menaceInterval) {
                    if (uhOrOh) {
                        // Play Uh
                        soundPool.play(uhID, 1, 1, 0, 0, 1);

                    } else {
                        // Play Oh
                        soundPool.play(ohID, 1, 1, 0, 0, 1);
                    }

                    // Reset the last menace time
                    lastMenaceTime = System.currentTimeMillis();
                    // Alter value of uhOrOh
                    uhOrOh = !uhOrOh;
                }
            }
        }
    }

    private void update(){

        // Did an slime bump into the side of the screen
        boolean bumped = false;

        // Has the player lost
        boolean lost = false;

        // Move the player's ship
        playerShip.update(fps);

        // Update all the slimes if visible
        for(int i = 0; i < numSlimeInvaders; i++){

            if(slimeInvaders[i].getVisibility()) {
                // Move the next slime
                slimeInvaders[i].update(fps);

                // Does he want to take a shot?
                if(slimeInvaders[i].takeAim(playerShip.getX(),
                        playerShip.getLength())){

                    // If so try and spawn a bullet
                    if(slimeBullets[nextBullet].shoot(slimeInvaders[i].getX()
                                    + slimeInvaders[i].getLength() / 2,
                            slimeInvaders[i].getY(), bullet.DOWN)) {

                        // Shot fired
                        // Prepare for the next shot
                        nextBullet++;

                        // Loop back to the first one if we have reached the last
                        if (nextBullet == maxSlimeBullets) {
                            // This stops the firing of another bullet until one completes its journey
                            // Because if bullet 0 is still active shoot returns false.
                            nextBullet = 0;
                        }
                    }
                }

                // If that move caused them to bump the screen change bumped to true
                if (slimeInvaders[i].getX() > screenX - slimeInvaders[i].getLength()
                        || slimeInvaders[i].getX() < 0){

                    bumped = true;

                }
            }

        }

        // Update all the slime's bullets if active
        for(int i = 0; i < slimeBullets.length; i++){
            if(slimeBullets[i].getStatus()) {
                slimeBullets[i].update(fps);
            }
        }

        // Did a slime bump into the edge of the screen
        if(bumped){

            // Move all the slimes down and change direction
            for(int i = 0; i < numSlimeInvaders; i++){
                slimeInvaders[i].dropDownAndReverse();
                // Have the invaders landed
                if(slimeInvaders[i].getY() > screenY - screenY / 10){
                    lost = true;
                }
            }

            // Increase the menace level
            // By making the sounds more frequent
            menaceInterval = menaceInterval - 80;
        }

        if(lost){
            prepareLevel();
        }

        // Update the players bullet
        if(bullet.getStatus()){
            bullet.update(fps);
        }

        // Has the player's bullet hit the top of the screen
        if(bullet.getImpactPointY() < 0){
            bullet.setInactive();
        }

        // Has an invaders bullet hit the bottom of the screen
        for(int i = 0; i < slimeBullets.length; i++){
            if(slimeBullets[i].getImpactPointY() > screenY){
                slimeBullets[i].setInactive();
            }
        }


        // Has the player's bullet hit an invader
        // Has the player's bullet hit an invader
        if(bullet.getStatus()) {
            for (int i = 0; i < numSlimeInvaders; i++) {
                if (slimeInvaders[i].getVisibility()) {
                    if (RectF.intersects(bullet.getRect(), slimeInvaders[i].getRect())) {
                        slimeInvaders[i].setInvisible();
                        soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        bullet.setInactive();
                        score = score + 10;

                        // Has the player won
                        if(score == numSlimeInvaders * 10){
                            paused = true;
                            lives = lives + 1;
                            prepareLevel();
                        }
                    }
                }
            }
        }

        // Has a slime bullet hit a shelter brick
        for(int i = 0; i < slimeBullets.length; i++){
            if(slimeBullets[i].getStatus()){
                for(int j = 0; j < numRocks; j++){
                    if(rocks[j].getVisibility()){
                        if(RectF.intersects(slimeBullets[i].getRect(), rocks[j].getRect())){
                            // A collision has occurred
                            slimeBullets[i].setInactive();
                            rocks[j].setInvisible();
                            soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                        }
                    }
                }
            }

        }

        // Has a player bullet hit a shelter brick
        if(bullet.getStatus()){
            for(int i = 0; i < numRocks; i++){
                if(rocks[i].getVisibility()){
                    if(RectF.intersects(bullet.getRect(), rocks[i].getRect())){
                        // A collision has occurred
                        bullet.setInactive();
                        rocks[i].setInvisible();
                        soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                    }
                }
            }
        }

        // Has an invader bullet hit the player ship
        for(int i = 0; i < slimeBullets.length; i++){
            if(slimeBullets[i].getStatus()){
                if(RectF.intersects(playerShip.getRect(), slimeBullets[i].getRect())){
                    slimeBullets[i].setInactive();
                    lives --;
                    soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);

                    // Is it game over?
                    if(lives <= 0){

                        finalScore = score;
                        leaveGame();



                    }
                }
            }
        }
    }



    private void draw(){
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255,  0, 255, 0));

            // Draw the player spaceship
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), screenY - playerShip.getHeight(), paint);

            // Draw the slimes
            // Draw the invaders
            for(int i = 0; i < numSlimeInvaders; i++){
                if(slimeInvaders[i].getVisibility()) {
                    if(uhOrOh) {
                        canvas.drawBitmap(slimeInvaders[i].getBitmap(), slimeInvaders[i].getX(), slimeInvaders[i].getY(), paint);
                    }else{
                        canvas.drawBitmap(slimeInvaders[i].getBitmap2(), slimeInvaders[i].getX(), slimeInvaders[i].getY(), paint);
                    }
                }
            }

            // Draw the bricks if visible
            for(int i = 0; i < numRocks; i++){
                if(rocks[i].getVisibility()) {
                    canvas.drawRect(rocks[i].getRect(), paint);
                }
            }

            // Draw the players bullet if active
            if(bullet.getStatus()){
                canvas.drawRect(bullet.getRect(), paint);
            }

            // Draw the invaders bullets if active
            for(int i = 0; i < slimeBullets.length; i++){
                if(slimeBullets[i].getStatus()) {
                    canvas.drawRect(slimeBullets[i].getRect(), paint);
                }
            }

            // Draw the score and remaining lives
            // Change the brush color
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

        // If the Activity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

        // If Activity is started then
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void leaveGame(){
        context.startActivity(new Intent(context,EndGameScreen.class));


    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                paused = false;

                if(motionEvent.getY() > screenY - screenY / 3) {
                    if (motionEvent.getX() > screenX / 2) {
                        playerShip.setMovementState(playerShip.RIGHT);
                    } else {
                        playerShip.setMovementState(playerShip.LEFT);
                    }

                }

                if(motionEvent.getY() < screenY - screenY / 8) {
                    // Shots fired
                    if(bullet.shoot(playerShip.getX()+
                            playerShip.getLength()/2,screenY,bullet.UP)){
                        soundPool.play(shootID, 1, 1, 0, 0, 1);
                    }
                }
                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                if(motionEvent.getY() > screenY - screenY / 3) {
                    playerShip.setMovementState(playerShip.STOPPED);
                }
                break;
        }
        return true;
    }
}


