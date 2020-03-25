/*

Animate a (moving) triangle.

NB: Using "2d world" co-ordinates (i.e. (0.0, 0.0) is at bottom left)

*/

import java.awt.*;
import javax.swing.*;

class Triangle
{
	private double x_world[];
	private double y_world[];

	private int x_screen[];
	private int y_screen[];

	private double zoom;
	private int screen_width, screen_height;

	protected void init()
	{
		x_world = new double[3];
		y_world = new double[3];
		x_screen = new int[3];
		y_screen = new int[3];
	}

	public Triangle(int screen_width_, int screen_height_, double zoom_)
	{
		init();

		screen_width = screen_width_;
		screen_height = screen_height_;
		zoom = zoom_;
	}

	public int convert_x(double x)
	{
		return ((int) (x * zoom));
	}

	public int convert_y(double y)
	{
		return (screen_height - (int) (y * zoom));
	}

	public void set(double x0, double y0, double x1, double y1, double x2, double y2)
	{
		x_world[0] = x0;
		x_world[1] = x1;
		x_world[2] = x2;

		y_world[0] = y0;
		y_world[1] = y1;
		y_world[2] = y2;

		// convert world coordinates to screen coordinates...

		// NB: You could technically get away with assigning values to screen from the parameters directly,
		// but we're keeping world coordinates so that we can "transform" (scale,translate,rotate,etc) the coordinates
		// later on.

		for (int i = 0; i < 3; i++) {
			x_screen[i] = convert_x(x_world[i]);
			y_screen[i] = convert_y(y_world[i]);
		}
	}

	public void draw(Graphics gfx, Color color)
	{
		gfx.setColor(color);

		gfx.drawLine(x_screen[0], y_screen[0], x_screen[1], y_screen[1]);
		gfx.drawLine(x_screen[1], y_screen[1], x_screen[2], y_screen[2]);
		gfx.drawLine(x_screen[2], y_screen[2], x_screen[0], y_screen[0]);
	}
}

class test extends JFrame
{
	Image image, blank;
	Graphics gfx;
        boolean ok = false;
	java.util.Random random;


	public void paint(Graphics g)
	{
                if (ok)
                        g.drawImage(image, 40, 40, null);
	}

        public void sleep(int n)
        {
                try {
                        Thread.sleep(n);
                } catch (Exception e) { }
        }

        public test()
	{
		setSize(660, 500);
		setVisible(true);

		random = new java.util.Random();

		image = createImage(800, 600);
		blank = createImage(800, 600);
		gfx = image.getGraphics();
                ok = true;

		int x = 1;
		int y = 1;

		Triangle triangle = new Triangle(800, 600, 10.0);
		Color color = new Color(255, 0, 0);

		while (true) 
		{
			triangle.set(x, y, x+10, y+10, x, y+20); 
			triangle.draw(gfx, color);

//			x = random.nextInt(800);
//			y = random.nextInt(600);
			x++;
			y++;

	                repaint(); sleep(100);
			gfx.drawImage(blank, 0, 0, null);
			repaint();
                }
	}

	public static void main(String args[]) {
                new test();
	}

}
