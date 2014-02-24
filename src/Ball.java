import java.awt.Rectangle;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Ball {
	private int x, y, radius;
	private int xSpeed, ySpeed;

	public Ball(int x, int y, int radius)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;

		do
		{
			xSpeed = -5 + new Random().nextInt(11);
			ySpeed = -5 + new Random().nextInt(11);
		}while(xSpeed == 0 || ySpeed == 0);
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getRadius()
	{
		return radius;
	}

	public void move(double d){
		x += xSpeed;
		y += ySpeed;
	}

	public void addXSpeed(int dx)
	{
		int newM = Math.abs(xSpeed) + dx;
		if( newM < 3 )
			return;
		xSpeed = newM * (int)Math.signum(xSpeed);
	}

	public void addYSpeed(int dy) {
		int newM = Math.abs(ySpeed) + dy;
		if( newM < 3 )
			return;
		ySpeed = newM * (int)Math.signum(ySpeed);
	}

	public int getXSpeed()
	{
		return xSpeed;
	}

	public int getYSpeed()
	{
		return ySpeed;
	}
	public void flipX()
	{
		xSpeed = -xSpeed;
	}

	public void flipY()
	{
		ySpeed = -ySpeed;
	}

	public Rectangle getBoundingBox()
	{
		return new Rectangle(x - radius/2, y - radius/2, radius, radius);
	}

	public void draw()
	{
		glRectd(x - radius/2, y - radius/2, x + radius/2, y + radius/2);
	}


}
