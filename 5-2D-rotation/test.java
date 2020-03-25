/*

Animate a (rotating) triangle.

To do this we need to understand some maths:


    /|
 r / |y
  /a)|
 /---+
    x

1) We have the following formulas:

sin(a) = opp / hyp = y / r

therefore, y = r * sin(a)

and

cos(a) = adj / hyp = x / r

therefore, x = r * cos(a)

2) We want to rotate the point (x, y) from angle "a" to angle "a + b" (i.e. rotating it by an additional "b" amount)

i.e.


     (new_x, new_y)
    /
 r2/    /|
  /  r / |y
 /b)  /a)|
/----/---+
       x

(Because this is bad ascii art, r2 actually equals r, i.e. we're rotating around a circle with radius 'r')

We have the following formulas:

new_x = r * cos(a+b)
new_y = r * sin(a+b)

Using the trig identities:

new_x = r * (cos(a)*cos(b) - sin(a)*sin(b))
new_y = r * (sin(a)*cos(b) + cos(a)*sin(b))

Expanding RHS:

new_x = r*cos(a)*cos(b) - r*sin(a)*sin(b)
new_y = r*sin(a)*cos(b) + r*cos(a)*sin(b)

Substituting our "x" and "y" formulas (from part #1)

i.e.

y = r * sin(a)
x = r * cos(a)

new_x = x*cos(b) - y*sin(b)
new_y = y*cos(b) + x*sin(b)

We can put that into a 2D rotation matrix:

   x		  y
__		_______
|cos(b)		-sin(b)|
|sin(b)		 cos(b)|
-------		-------|

(We won't use this 2D rotation matrix in our example, but just note it down as it will be used in our 3D engine.)

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

	protected int convert_x(double x)
	{
		return ((int) (x * zoom));
	}

	protected int convert_y(double y)
	{
		return (screen_height - (int) (y * zoom));
	}

	protected void convert_world_to_screen()
	{
		// convert world coordinates to screen coordinates...

		for (int i = 0; i < 3; i++) {
			x_screen[i] = convert_x(x_world[i]);
			y_screen[i] = convert_y(y_world[i]);
		}
	}

	public void set(double x0, double y0, double x1, double y1, double x2, double y2)
	{
		x_world[0] = x0;
		x_world[1] = x1;
		x_world[2] = x2;

		y_world[0] = y0;
		y_world[1] = y1;
		y_world[2] = y2;

		convert_world_to_screen();
	}

	protected double rotate_new_x(double x, double y, double radians)
	{
		return x * Math.cos(radians) - y * Math.sin(radians);
	}

	protected double rotate_new_y(double x, double y, double radians)
	{
		return y * Math.cos(radians) + x * Math.sin(radians);
	}

	public void rotate(double radians)
	{
		for (int i = 0; i < 3; i++) {
			double temp_x, temp_y;

			temp_x = x_world[i];
			temp_y = y_world[i];

			x_world[i] = rotate_new_x(temp_x, temp_y, radians);
			y_world[i] = rotate_new_y(temp_x, temp_y, radians);
		}

		convert_world_to_screen();
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

		int x = 10;
		int y = 10;
		double radians = 0.0;
		double radians_step = 0.001;

		Triangle triangle = new Triangle(800, 600, 10.0);
		Color color = new Color(255, 0, 0);

		triangle.set(x, y, x+10, y+10, x, y+20); 

		while (true) 
		{
			triangle.rotate(radians);
			triangle.draw(gfx, color);

	                repaint(); sleep(100);
			gfx.drawImage(blank, 0, 0, null);
			radians += radians_step;
			repaint();
                }
	}

	public static void main(String args[]) {
                new test();
	}

}
