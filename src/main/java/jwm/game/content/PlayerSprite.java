package jwm.game.content;

import java.util.ArrayList;

public class PlayerSprite extends Sprite 
{
	public enum Direction { Left, Right }
	private Direction _dir;
	public PlayerSprite(String spriteFName, 
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
			ArrayList<Action> actions, 
			Direction dir) 
	{
		super(spriteFName, 
				firstFrame, 
				lastFrame, 
				frameWidth, 
				frameHeight, 
				scaledWidth,
				scaledHeight, 
				xPos, 
				yPos, 
				animationRate, 
				startAnimation,
				stopAtLastFrame, 
				actions);
		
		_dir = dir;
	}
	
	/** 
	 * Direction the player is facing
	 * @return
	 */
	public Direction getDirection() { return _dir; }
	
	/**
	 * Makes a clone of the object
	 */
	public PlayerSprite clone() 
	{
		PlayerSprite cln = new PlayerSprite(getFileName(), 
				getFirstFrame(), 
				getLastFrame(), 
				getImageFrameW(), 
				getImageFrameH(), 
				getWidth(), 
				getHeight(), 
				getXLeft(), 
				getYTop(), 
				getAnimationRate(), 
				getRunAnimation(), 
				getStopAtLastFrame(), 
				getActions(),
				_dir);
		return cln;
	}
}
