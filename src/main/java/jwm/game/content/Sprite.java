package jwm.game.content;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import jwm.game.Consts;
import jwm.game.GameFactory;

public class Sprite extends Hotspot
{
	private int _firstFrame, _lastFrame, _frameW, _frameH, _currentFrame, _animationRate, _animationCount;
	private boolean _runAnimation = false;
	private boolean _stopAtLastFrame = false;
	private BufferedImage _spriteImg;
	private String _fileName;
	private ArrayList<ISpriteAnimationObserver> _animationObservers = new ArrayList<ISpriteAnimationObserver>();
	
	public Sprite(String spriteFName, 
			int firstFrame, 
			int lastFrame, 
			int frameWidth, 
			int frameHeight, 
			int scaledWidth, 
			int scaledHeight, 
			int xPos, 
			int yPos, 
			int animationRate, 
			boolean startAnimation, 
			boolean stopAtLastFrame, 
			ArrayList<Action> actions)
	{
		super(xPos, yPos, scaledWidth, scaledHeight, Consts.SPRITE_AND_ROOM_CHANGE_HOTSPOT_Z_POS, actions);
		
		_fileName = spriteFName;
		_currentFrame = firstFrame;
		_animationCount = 0;
		_firstFrame = firstFrame;
		_lastFrame = lastFrame;
		_frameW = frameWidth;
		_frameH = frameHeight;
		_animationRate = animationRate;
		_stopAtLastFrame = stopAtLastFrame;
		setRunAnimation(startAnimation);
		
		URL imgUrl = GameFactory.class.getResource(Consts.RESOURCES_SPRITES_FOLDER + spriteFName);
		try 
		{
			_spriteImg = ImageIO.read(imgUrl);
		} 
		catch (Exception e) 
		{
			System.err.println("Error reading url: '"+imgUrl+"', which should be for '"+Consts.RESOURCES_SPRITES_FOLDER+spriteFName+"':"+e);
			e.printStackTrace();
		}
	}
	
	protected String getFileName() { return _fileName; }
	protected int getFirstFrame() { return _firstFrame; }
	protected int getLastFrame() { return _lastFrame; }
	protected int getImageFrameW() { return _frameW; }
	protected int getImageFrameH() { return _frameH; }
	protected int getAnimationRate() { return _animationRate; }
	protected boolean getStopAtLastFrame() { return _stopAtLastFrame; }
	//public boolean isAbovePlayer() { return _abovePlayer; }
	
	
	protected void setRunAnimation(boolean val) { _runAnimation = val; }
	protected boolean getRunAnimation() { return _runAnimation; }
	protected void resetAnimation() { _currentFrame = _firstFrame; }
	
	public void addSpriteAnimationObserver(ISpriteAnimationObserver o)
	{
		_animationObservers.add(o);
	}
	
	protected void paintSprite(Graphics2D g)
	{
		/* this spriteXstart & End scheme assumes that there will be a 1 pixel border 
		 * to be ignored around each 'frame' in the sprite, on the left side.  
		 */
		int spriteXStart = ((_currentFrame-1) * _frameW)+1;		
		int spriteXEnd = spriteXStart + _frameW-1;
		
		// to scale the sprite, the x and y-PosEnd values need to be scaled
		int xPosEnd = getXLeft() + getWidth();
		int yPosEnd = getYTop() + getHeight();
		
		// to help debug, can draw a rectangle around the sprites
		// g.drawRect(_xPos, _yPos, _scaledW, _scaledH);
		
		int spriteYStart = 0;
		g.drawImage(_spriteImg, 
				getXLeft(), 
				getYTop(), 
				xPosEnd, 
				yPosEnd, 
				spriteXStart, 
				spriteYStart, 			
				spriteXEnd, 
				_frameH, 
				null);
	}
	
	public Sprite clone()
	{
		Sprite cln = new Sprite(_fileName, 
				_firstFrame, 
				_lastFrame, 
				_frameW, 
				_frameH, 
				getWidth(), 
				getHeight(), 
				getXLeft(), 
				getYTop(), 
				_animationRate, 
				getRunAnimation(), 
				_stopAtLastFrame, 
				this.getActions());
		return cln;
	}
	
	public void animate()
	{
		// animation rate determines how frequently to advance the animation
		if (getRunAnimation())
		{
			if (_animationCount == _animationRate)
			{
				_animationCount = 0;	// reset animation rate counter

				if (_currentFrame == (_lastFrame))	
				{
					if (!_stopAtLastFrame)
					{
						_currentFrame = _firstFrame;
					}
					else
					{
						// don't reset the frame, just leave it there, frozen on the last frame
						//_isFinished = true;
						setRunAnimation(false);
						for(int i = 0; i < _animationObservers.size(); i++)
						{
							_animationObservers.get(i).animationFinished(this);
						}
					}
				}
				else
				{
					_currentFrame++;
				}
			}
			else
			{
				_animationCount++;
			}	
		}
	}
}
