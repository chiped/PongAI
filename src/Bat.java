import java.awt.Rectangle;
import static org.lwjgl.opengl.GL11.*;

public class Bat {
	private int x, y, width, height, boost;

	public Bat(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getBoost()
	{
		return boost;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void move(int dy)
	{
		this.y += dy;
		this.boost = Math.abs(dy)/2;
	}
	
	public Rectangle getBoundingBox()
	{
		return new Rectangle(x, y, width, height);
	}
	
	public void draw()
	{
        glRectd(x, y, x + width, y + height);
    }

}
