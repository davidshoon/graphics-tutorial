import java.awt.*;
import javax.swing.*;

class test extends JFrame
{
	Image image;
	Graphics gfx;
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

		image = createImage(800,600);

		gfx = image.getGraphics();

                ok = true;

		int x = 1;
		int y = 1;

		while (true) 
		{
			gfx.setColor(new Color(255,0,0));
			gfx.drawLine(x, y, x, y);
			x++;
			y++;


	                repaint(); sleep(1000);
                }
	}

	public static void main(String args[]) {
                new test();
	}

}
