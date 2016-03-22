package jwm.game.content;

import java.awt.Graphics;

public class Wall 
{	
	int _x1, _y1, _x2, _y2;
	public Wall(int x1, int y1, int x2, int y2)
	{
		_x1 = x1;
		_y1 = y1;
		_x2 = x2;
		_y2 = y2;
	}
	
	private double getSlope()
	{
		if (_x2 - _x1 != 0)
		{
			return (_y2 - _y1) / (double)(_x2 - _x1);
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * Gets the Y position on the line for a given X value. 
	 */
	public int getYPoint(int x)
	{
		if (_x1 == _x2 || (x > _x1 && _x1 > _x2) || (x > _x2 && _x2 > _x1) || (x < _x1 && _x1 < _x2) || (x < _x2 && _x2 < _x1))
		{
			return -1; // vertical line, or line is undefined at this x value
		}
		else
		{
			double m = getSlope();
			int b = _y1;
			int y = (int) (m*(x-_x1) + b);
			return y;
		}
	}
	
	public int getXPoint(int y)
	{
		if (_y1 == _y2 || (y > _y1 && _y1 > _y2) || (y > _y2 && _y2 > _y1) || (y < _y1 && _y1 < _y2) || (y < _y2 && _y2 < _y1))
		{
			return -1; // horizontal line, or line is undefined at this y value
		}
		else
		{
			double m = getSlope();
			int b, x;
			if (m == -1)
			{
				// -1 means vertical line
				x = _x1;
			}
			else
			{
				b = _y1;
				x = (int) ((y - b)/m) + _x1;
			}

			return x;
		}
	}

	public void paint(Graphics g)
	{
		g.drawLine(_x1, _y1, _x2, _y2);
	}
	
}
