/*

Draw a straight line using Bresenham's line algorithm

Based off Wikipedia's entry for Bresenham's line algorithm.

Copyright (c) 2021 by David Shoon

*/

import java.awt.*;
import javax.swing.*;

class test extends JFrame {
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
		}
		catch (Exception e) { }
	}

	public void my_draw_pixel(Graphics g, Color c, int x, int y)
	{
		g.setColor(c);
		g.drawLine(x, y, x, y);
	}

	public void plotLineLow(Graphics g, Color c, int x0, int y0, int x1, int y1)
	{
		int dx = x1 - x0;
		int dy = y1 - y0;
		int yi = 1;

		if (dy < 0) {
			yi = -1;
			dy = -dy;
		}

		int D = (2 * dy) - dx;
		int y = y0;

		for (int x = x0; x <= x1; x++) {
			my_draw_pixel(g, c, x, y);

			if (D > 0) {
				y = y + yi;
				D = D + (2 * (dy - dx));
			}

			else {
				D = D + 2*dy;
			}
		}
	}


	public void plotLineHigh(Graphics g, Color c, int x0, int y0, int x1, int y1)
	{
		int dx = x1 - x0;
		int dy = y1 - y0;
		int xi = 1;

		if (dx < 0) {
			xi = -1;
			dx = -dx;
		}

		int D = (2 * dx) - dy;
		int x = x0;

		for (int y = y0; y <= y1; y++) {
			my_draw_pixel(g, c, x, y);

			if (D > 0) {
				x = x + xi;
				D = D + (2 * (dx - dy));
			}
			else {
				D = D + 2*dx;
			}
		}
	}

	public void my_draw_line(Graphics g, Color c, int x0, int y0, int x1, int y1)
	{
		//my_draw_pixel(g, c, x1, y1);
		//my_draw_pixel(g, c, x2, y2);

		if (Math.abs(y1 - y0) < Math.abs(x1 - x0)) {
			if (x0 > x1) {
				plotLineLow(g, c, x1, y1, x0, y0);
			}
			else {
				plotLineLow(g, c, x0, y0, x1, y1);
			}
		}
		else {
			if (y0 > y1) {
				plotLineHigh(g, c, x1, y1, x0, y0);
			}
			else {
				plotLineHigh(g, c, x0, y0, x1, y1);
			}
		}
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

		Color color = new Color(255, 0, 0);

		while (true) {
			int x1 = random.nextInt(800);
			int y1 = random.nextInt(600);
			int x2 = random.nextInt(800);
			int y2 = random.nextInt(600);

			my_draw_line(gfx, color, x1, y1, x2, y2);
			repaint();
			sleep(100);
		}
	}

	public static void main(String args[])
	{
		new test();
	}

}
