package jwm.game.content;

import java.awt.Graphics2D;

public class BoundingBox 
{
	private int _xLeft, _yTop, _width, _height;
	public BoundingBox(int xLeft, int yTop, int width, int height)
	{
		_xLeft = xLeft;
		_yTop = yTop;
		_width = width;
		_height = height;
	}
	
	public boolean containtsPoint(int x, int y)
	{
		boolean withinX = (x >= _xLeft && x <= (_xLeft + _width));
		boolean withinY = (y >= _yTop && y <= (_yTop + _height));
		return (withinX && withinY);
	}
	public double getDistance(int x, int y)
	{
		int yCentre = getYBottom() + (getHeight()/2);
		int yDiff = Math.abs(y - yCentre);
		int xDiff = Math.abs(x - getXMiddle());
		double xSqr = Math.pow(xDiff, 2);
		double ySqr = Math.pow(yDiff, 2);
		double distance = Math.sqrt(xSqr + ySqr);
		return distance;
	}
	
	/** for debugging purposes only **/
	public void paint(Graphics2D g)
	{
		g.fillRect(_xLeft, _yTop, _width, _height);
	}
	
	public int getXLeft() { return _xLeft; }
	public int getYTop() { return _yTop; }
	public int getYBottom() { return _yTop + _height; }
	public int getXMiddle() { return _xLeft + (_width/2); }
	public int getHeight() { return _height; }
	public int getWidth() { return _width; }
	public void setHeight(int h) { _height = h; }
	public void setWidth(int w) { _width = w; }
//	public void setXLeftPos(int val) { _xLeft = val; }
//	public void setYTopPos(int val) { _yTop = val; }
	public void setTopLeftPos(int x, int y) { _xLeft = x; _yTop = y; }

}
