/*

Animate a (moving) triangle.

NB: We're still using integer "screen" co-ordinates (i.e. (0,0) starts at the top left corner).

A later example will use floating point "2D world" co-ordinates (i.e. (0.0, 0.0) starts at the bottom left corner).

*/

import java.awt.*;
import javax.swing.*;

class Triangle
{
	public void init()
	{
		x = new int[3];
		y = new int[3];
	}

	public Triangle()
	{
		init();
	}

	public Triangle(int x0, int y0, int x1, int y1, int x2, int y2)
	{
		init();
		set(x0, y0, x1, y1, x2, y2);
	}

	public void set(int x0, int y0, int x1, int y1, int x2, int y2)
	{
		x[0] = x0;
		x[1] = x1;
		x[2] = x2;

		y[0] = y0;
		y[1] = y1;
		y[2] = y2;
	}

	public void draw(Graphics gfx, Color color)
	{
		gfx.setColor(color);
		gfx.drawLine(x[0], y[0], x[1], y[1]);
		gfx.drawLine(x[1], y[1], x[2], y[2]);
		gfx.drawLine(x[2], y[2], x[0], y[0]);
	}

	private int x[];
	private int y[];
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

		Triangle triangle = new Triangle();
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
