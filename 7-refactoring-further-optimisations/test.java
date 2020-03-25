/*

Animate a (rotating) triangle.

Further refactoring; you'll see the benefits of the previous
refactoring here, where we can now draw a set of triangles on
the screen, and convert and rotate them using arrays (which is
as fast as you can get on Java).

The idea is to treat a set of triangles as just points, stored
in x,y arrays. This way we can prevent fragmented memory access.

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

	public void convert(double x_world[], double y_world[], int x_screen[], int y_screen[], int total_num_points)
	{
		for (int i = 0; i < total_num_points; i++) {
			x_screen[i] = convert_x(x_world[i]);
			y_screen[i] = convert_y(y_world[i]);
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

class Triangles
{
	int index = 0;
	// maximum of 1024 triangles allowed. Can use vector (but slower).
	double x[] = new double[1024 * 3];
	double y[] = new double[1024 * 3];

	// add a triangle into our set.
	public void add(Triangle triangle)
	{
		x[index * 3 + 0] = triangle.getX()[0];
		x[index * 3 + 1] = triangle.getX()[1];
		x[index * 3 + 2] = triangle.getX()[2];

		y[index * 3 + 0] = triangle.getY()[0];
		y[index * 3 + 1] = triangle.getY()[1];
		y[index * 3 + 2] = triangle.getY()[2];

		index++;
	}

	public int getNumTriangles()
	{
		return index;
	}

	public double[] getX()
	{
		return x;
	}

	public double[] getY()
	{
		return y;
	}

	public void set(double x_[], double y_[], int total_triangles)
	{
		for (int i = 0; i < total_triangles * 3; i++) {
			x[i] = x_[i];
			y[i] = y_[i];
		}

		index = total_triangles;
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

	public void rotate(Triangles triangles, double radians)
	{
		double new_x[] = new double[triangles.getNumTriangles() * 3];
		double new_y[] = new double[triangles.getNumTriangles() * 3];

		rotate(triangles.getX(), triangles.getY(), radians, new_x, new_y, triangles.getNumTriangles() * 3);

		triangles.set(new_x, new_y, triangles.getNumTriangles());
	}

	public void rotate(double x[], double y[], double radians, double new_x[], double new_y[], int total_num_points)
	{
		for (int i = 0; i < total_num_points; i++) {
			double temp_x, temp_y;

			temp_x = x[i];
			temp_y = y[i];

			new_x[i] = rotate_new_x(temp_x, temp_y, radians);
			new_y[i] = rotate_new_y(temp_x, temp_y, radians);
		}
	}
}

class Drawer
{
	public void draw(Graphics gfx, Color color, Convertor convertor, Triangle triangle)
	{
		gfx.setColor(color);

		int x[] = new int[3];
		int y[] = new int[3];

		convertor.convert(triangle.getX(), triangle.getY(), x, y, 3);

		gfx.drawLine(x[0], y[0], x[1], y[1]);
		gfx.drawLine(x[1], y[1], x[2], y[2]);
		gfx.drawLine(x[2], y[2], x[0], y[0]);
	}

	public void draw(Graphics gfx, Color color, Convertor convertor, Triangles triangles)
	{
		gfx.setColor(color);

		int x[] = new int[triangles.getNumTriangles() * 3];
		int y[] = new int[triangles.getNumTriangles() * 3];

		convertor.convert(triangles.getX(), triangles.getY(), x, y, triangles.getNumTriangles() * 3);

		for (int i = 0; i < triangles.getNumTriangles(); i++) {
			int x0 = x[i * 3 + 0];
			int x1 = x[i * 3 + 1];
			int x2 = x[i * 3 + 2];

			int y0 = y[i * 3 + 0];
			int y1 = y[i * 3 + 1];
			int y2 = y[i * 3 + 2];

			gfx.drawLine(x0, y0, x1, y1);
			gfx.drawLine(x1, y1, x2, y2);
			gfx.drawLine(x2, y2, x0, y0);
		}
	}
}

class test extends JFrame
{
	Image image;
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

		java.util.Random random = new java.util.Random();

		image = createImage(800, 600);
		Image blank = createImage(800, 600);
		Graphics gfx = image.getGraphics();
                ok = true;

		double radians = 0.0;
		double radians_step = 0.001;

		Convertor convertor = new Convertor(800, 600, 1.0);
		Color color = new Color(255, 0, 0);

		Rotator rotator = new Rotator();
		Drawer drawer = new Drawer();

		Triangles triangles = new Triangles();

		// generate 100 random triangles.
		for (int i = 0; i < 100; i++) {
			int x = random.nextInt(800);
			int y = random.nextInt(600);
			Triangle triangle = new Triangle();
			triangle.set(x, y, x+10, y+10, x, y+20);

			triangles.add(triangle);
		}

		while (true)
		{
			rotator.rotate(triangles, radians);
			drawer.draw(gfx, color, convertor, triangles);

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
