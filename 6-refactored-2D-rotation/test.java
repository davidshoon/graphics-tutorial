/*

Animate a (rotating) triangle.

Refactored.

*/

import java.awt.*;
import javax.swing.*;

// our world coords to screen coords convertor
class Convertor
{
	private int width, height;
	private double zoom;

	public Convertor(int width_, int height_, double zoom_)
	{
		width = width_;
		height = height_;
		zoom = zoom_;
	}

	protected int convert_x(double x)
	{
		return ((int) (x * zoom) + width / 2); // sets (0,0) at middle of screen.
	}

	protected int convert_y(double y)
	{
		return (height / 2 - (int) (y * zoom));
	}

	public void convert(double x_world[], double y_world[], int x_screen[], int y_screen[])
	{
		for (int i = 0; i < x_world.length; i++) {
			x_screen[i] = convert_x(x_world[i]);
			y_screen[i] = convert_y(y_world[i]);
		}
	}
}

class Rotator
{
	protected double rotate_new_x(double x, double y, double radians)
	{
		return x * Math.cos(radians) - y * Math.sin(radians);
	}

	protected double rotate_new_y(double x, double y, double radians)
	{
		return y * Math.cos(radians) + x * Math.sin(radians);
	}

	public void rotate(double x[], double y[], double radians)
	{
		for (int i = 0; i < x.length; i++) {
			double temp_x, temp_y;

			temp_x = x[i];
			temp_y = y[i];

			x[i] = rotate_new_x(temp_x, temp_y, radians);
			y[i] = rotate_new_y(temp_x, temp_y, radians);
		}
	}
}


class Triangle
{
	private double x[];
	private double y[];

	public Triangle()
	{
		x = new double[3];
		y = new double[3];
	}

	public void set(double x0, double y0, double x1, double y1, double x2, double y2)
	{
		x[0] = x0;
		x[1] = x1;
		x[2] = x2;

		y[0] = y0;
		y[1] = y1;
		y[2] = y2;
	}

	public double[] getX()
	{
		return x;
	}

	public double[] getY()
	{
		return y;
	}
}

class Drawer
{
	public void draw(Graphics gfx, Color color, Convertor convertor, Triangle triangle)
	{
		gfx.setColor(color);

		int x[] = new int[3];
		int y[] = new int[3];

		convertor.convert(triangle.getX(), triangle.getY(), x, y);

		gfx.drawLine(x[0], y[0], x[1], y[1]);
		gfx.drawLine(x[1], y[1], x[2], y[2]);
		gfx.drawLine(x[2], y[2], x[0], y[0]);
	}
}

class test extends JFrame
{
        boolean ok = false;

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

//		java.util.Random random = new java.util.Random();

		Image image = createImage(800, 600);
		Image blank = createImage(800, 600);
		Graphics gfx = image.getGraphics();
                ok = true;

		int x = 10;
		int y = 10;
		double radians = 0.0;
		double radians_step = 0.001;

		Convertor convertor = new Convertor(800, 600, 10.0);
		Color color = new Color(255, 0, 0);

		Triangle triangle = new Triangle();
		Rotator rotator = new Rotator();
		Drawer drawer = new Drawer();

		triangle.set(x, y, x+10, y+10, x, y+20);

		while (true)
		{
			rotator.rotate(triangle.getX(), triangle.getY(), radians);
			drawer.draw(gfx, color, convertor, triangle);

	                repaint(); sleep(100);

			gfx.drawImage(blank, 0, 0, null); // clear screen.

			radians += radians_step; // increment rotation.

			repaint();
                }
	}

	public static void main(String args[]) {
                new test();
	}

}
