import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class Main {

	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static int top, bottom, left, right;
	private static boolean isRunning = true;
	private static long lastFrame;
	private static Ball ball;
	private static Bat bat1, bat2;
	private static int AISpeed;
	private static int playerScore, AIScore;

	private static void setUpOpenGL() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	public static void main(String[] args) {
		setUpDisplay();
		setUpOpenGL();
		setUpObjects();
		while (isRunning) {
			render();
			logic(getDelta());
			//input();
			Display.update();
			Display.sync(60);

			if (Display.isCloseRequested()) {
				isRunning = false;
			}
		}
		Display.destroy();
		System.exit(0);
	}
	
	private static void input() {
		
		//Bat movement with collision detection
		int newBatLoc = HEIGHT - Mouse.getY() - bat1.getHeight()/2;
		if (newBatLoc < top)
			newBatLoc = top;
		else if(newBatLoc > bottom - bat1.getHeight())
			newBatLoc = bottom - bat1.getHeight();
		bat1.setY(newBatLoc);
		
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
		{
			Display.destroy();
			System.exit(0);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			bat2.move(-2);
			if(bat2.getY() < top)
				bat2.setY(top);
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			bat2.move(2);
			if(bat2.getY() > bottom - bat2.getHeight())
				bat2.setY(bottom - bat2.getHeight());
		}
				
	}

	private static void logic(int delta) {
		//Move ball
		ball.move(delta/10.00);
		
		//Ball collision detection
		int ballX = ball.getX() - ball.getRadius()/2;
		int ballY = ball.getY() - ball.getRadius()/2;
		if( ball.getBoundingBox().intersects(bat1.getBoundingBox()) )
		{
			ball.flipX();
			//ball.addXSpeed(Math.abs(Mouse.getDY())/4);
			ball.addXSpeed(bat1.getBoost());
		}
		else if( ball.getBoundingBox().intersects(bat2.getBoundingBox()) )
		{
			ball.flipX();
			ball.addXSpeed(bat2.getBoost());
		}
		else if( ballY <= -top + ball.getRadius() - ball.getYSpeed() || ballY >= bottom - ball.getRadius() - ball.getYSpeed() )
		{
			ball.flipY();
			ball.addXSpeed(-1);
			ball.addYSpeed(-1);
		}
		if( ball.getX() + ball.getRadius() < 0 )
		{
			System.out.println("You Lost");
			AIScore++;
			System.out.println("You: " + playerScore + " Me: " + AIScore);
			ball = new Ball(WIDTH/2, HEIGHT/2, WIDTH/64);
		}
		else if( ball.getX() > WIDTH  )
		{
			System.out.println("Computer Lost");
			playerScore++;
			System.out.println("You: " + playerScore + " Me: " + AIScore);
			ball = new Ball(WIDTH/2, HEIGHT/2, WIDTH/64);
		}
		
		//AI movement
		
	}

	private static void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		glRectd(left - 1, top - 1, right + 1, bottom + 1);
		glColor3f(0,0,0);
		glRectd(left, top, right, bottom);
		glColor3f(1,1,1);
		ball.draw();
		bat1.draw();
		bat2.draw();
	}

	private static void setUpObjects() {
		ball = new Ball(WIDTH/2, HEIGHT/2, WIDTH/64);
		bat1 = new Bat(WIDTH/128, HEIGHT/2 - HEIGHT/48, WIDTH/64, HEIGHT/6);
		bat2 = new Bat(WIDTH - WIDTH/64 - WIDTH/128, HEIGHT/2 - HEIGHT/48, WIDTH/64, HEIGHT/6);
		AISpeed = (int) Math.ceil( WIDTH/160.00 );
		playerScore = AIScore = 0;
		
		new Thread(new Runnable(){

			@Override
			public void run()
			{
				while(isRunning)
				{
					System.out.print("");
					if(ball.getXSpeed() > 0)
					{
						int plannedY = ball.getY(), plannedX = ball.getX();
						
						while( plannedX < right - bat2.getWidth() )
						{
							plannedX += ball.getXSpeed();
							plannedY += ball.getYSpeed();
						}
						
						int step = ( plannedY - (bat2.getY() + bat2.getHeight()/2) );
						bat2.move( Math.min(AISpeed, Math.abs(step)) * (int)Math.signum(step) );
						
						if(bat2.getY() < top)
						{
							bat2.setY(top);
							bat2.move(0);
						}

						if(bat2.getY() > bottom - bat2.getHeight())
						{
							bat2.setY(bottom - bat2.getHeight());
							bat2.move(0);
						}

						try 
						{
							Thread.sleep(20);
						}
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}

					}
					
				}
			}
		}).start();
		
		new Thread(new Runnable(){

			@Override
			public void run()
			{
				while(isRunning)
				{
					System.out.print("");
					if(ball.getXSpeed() < 0)
					{
						int plannedY = ball.getY(), plannedX = ball.getX();
						
//						while( plannedX < left + bat1.getWidth() )
//						{
//							plannedX += ball.getXSpeed();
//							plannedY += ball.getYSpeed();
//						}
						
						int step = ( plannedY - (bat1.getY() + bat1.getHeight()/2) );
						bat1.move( Math.min(AISpeed, Math.abs(step)) * (int)Math.signum(step) );
						
						if(bat1.getY() < top)
						{
							bat1.setY(top);
							bat1.move(0);
						}

						if(bat1.getY() > bottom - bat1.getHeight())
						{
							bat1.setY(bottom - bat1.getHeight());
							bat1.move(0);
						}

						try 
						{
							Thread.sleep(20);
						}
						catch (InterruptedException e) 
						{
							e.printStackTrace();
						}

					}
					
				}
			}
		}).start();
	}

	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	private static int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = currentTime;
		return delta;
	}

	private static void setUpDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setTitle("Pong");
			Display.create();
			top =  WIDTH/128;
			bottom = HEIGHT - WIDTH/128;
			left = WIDTH/128;
			right = WIDTH - WIDTH/128;
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

	}

}
