package jwm.game.content;

import java.util.ArrayList;

import jwm.game.Consts;

public class Player implements ISpriteAnimationObserver
{

	private int _travelRate, _xDest, _yDest;
	private boolean _moveUp, _moveDn, _moveR, _moveL;
	private ArrayList<Wall> _playerWalls;
	private PlayerSprite _currentPlayerSprite, _playerLeftSprite, _playerRightSprite;
	private ArrayList<ISpriteAnimationObserver> _animationObservers = new ArrayList<ISpriteAnimationObserver>();
	
	/**
	 * player constructor
	 * @param playerLeft
	 * @param playerRight
	 */
	public Player(PlayerSprite playerLeft, PlayerSprite playerRight) 
	{
		_playerLeftSprite = playerLeft;
		_playerRightSprite = playerRight;
	}
	
	public void setCurrentSprite(PlayerSprite sp) { _currentPlayerSprite = sp;	}
	public PlayerSprite getCurrentSprite() { return _currentPlayerSprite; }
	public PlayerSprite getLeftFacingSprite() { return _playerLeftSprite; }
	public PlayerSprite getRightFacingSprite() { return _playerRightSprite; }
	public void setWallsList(ArrayList<Wall> walls)	{ _playerWalls = walls;	}
	public ArrayList<Wall> getWalls() { return _playerWalls; }
	public void configure(int xLeftPos, int yTopPos, int w, int h, int movementRate, ArrayList<Wall> walls)
	{
		_playerLeftSprite.setTopLeftPos(xLeftPos, yTopPos);
		_playerRightSprite.setTopLeftPos(xLeftPos, yTopPos);
		_playerLeftSprite.setWidth(w);
		_playerRightSprite.setWidth(w);
		_playerLeftSprite.setHeight(h);
		_playerRightSprite.setHeight(h);
		_travelRate = movementRate;
		this.setWallsList(walls);
	}
	
	public void addSpriteAnimationObserver(ISpriteAnimationObserver o)
	{
		_animationObservers.add(o);
	}
	
	/**
	 * sets the player's destination that they will walk toward (until they hit a wall)
	 * @param x
	 * @param y
	 */
	public void setPlayerDestination(int x, int y)
	{	
		_xDest = x;
		_yDest = y;
		int xPos = _currentPlayerSprite.getXMiddle();
		int yPos = _currentPlayerSprite.getYBottom();
		int xDiff = _xDest - xPos;
		int yDiff = _yDest - yPos;
		
		if (Math.abs(xDiff) <= Consts.CHARACTER_STOPPING_POSITION_ERROR && Math.abs(yDiff) <= Consts.CHARACTER_STOPPING_POSITION_ERROR)
		{
			// the user clicked the character, so stop him
			_currentPlayerSprite.setRunAnimation(false);
			this.stopMovement();
		}
		else
		{
			// move up/down
			if (yDiff > 0)
				moveDown();
			else
				moveUp();
			
			// move left/right
			if (xDiff > 0)
				moveRight();
			else
				moveLeft();
		}
	}
	
	/**
	 * Initiates the player moving Up, or stops him if already moving up
	 */
	public void toggleUpDirection()
	{
		_yDest = 0;
		if (_moveUp)
		{
			_moveUp = false;
			_moveL = false;
			_moveR = false;
			_currentPlayerSprite.setRunAnimation(false);
		}
		else
		{
			moveUp();
		}
	}
	
	/**
	 * Initiates the player moving Down, or stops him if already moving Down
	 */
	public void toggleDownDirection()
	{
		_yDest = 0;
		if (_moveDn)
		{
			_moveDn = false;
			_moveL = false;
			_moveR = false;
			_currentPlayerSprite.setRunAnimation(false);
		}
		else
		{
			moveDown();
		}
	}
	
	/**
	 * Initiates the player moving Left, or stops him if already moving left
	 */
	public void toggleLeftDirection()
	{
		_xDest = 0;
		if (_moveL)
		{
			_moveL = false;
			_moveUp = false;
			_moveDn = false;
			_currentPlayerSprite.setRunAnimation(false);
		}
		else
		{
			moveLeft();
		}
	}
	
	/**
	 * Initiates the player moving Right, or stops him if already moving right
	 */
	public void toggleRightDirection()
	{
		_xDest = 0;
		if (_moveR)
		{
			_moveR = false;
			_moveUp = false;
			_moveDn = false;
			_currentPlayerSprite.setRunAnimation(false);
		}
		else
		{
			moveRight();
		}
	}
	
	public void stopWalking()
	{
		_moveR = false;
		_moveL = false;
		_moveUp = false;
		_moveDn = false;
		_currentPlayerSprite.setRunAnimation(false);
	}
	
	/**
	 * Update the player's position, and animation frame
	 */
	public void update()
	{
		checkForStoppingAtDestination();
		checkForStoppingAtWall();
		moveIfNecessary();
		_currentPlayerSprite.animate();
	}
	
	
	/*
	 *
	 *
	 * PRIVATE METHODS FOLLOW
	 *
	 * 
	 */
	
	
	private void moveLeft()
	{
		setCurrentSprite(_playerLeftSprite);
		_currentPlayerSprite.setRunAnimation(true);
		_moveR = false;
		_moveL = true;
	}
	
	private void moveRight()
	{
		setCurrentSprite(_playerRightSprite);
		_currentPlayerSprite.setRunAnimation(true);
		_moveL = false;
		_moveR = true;
	}
	
	private void moveUp()
	{
		PlayerSprite newPlayerSprite;
		if (_moveL)
		{
			newPlayerSprite = _playerLeftSprite;
		}
		else
		{
			newPlayerSprite = _playerRightSprite;
		}
		setCurrentSprite(newPlayerSprite);
		_currentPlayerSprite.setRunAnimation(true);
		_moveDn = false;
		_moveUp = true;
	}
	
	private void moveDown()
	{
		PlayerSprite newPlayerSprite;
		if (_moveL)
		{
			newPlayerSprite = _playerLeftSprite;
		}
		else
		{
			newPlayerSprite = _playerRightSprite;
		}
		setCurrentSprite(newPlayerSprite);
		_currentPlayerSprite.setRunAnimation(true);
		_moveUp = false;
		_moveDn = true;
	}

	private void stopMovement()
	{
		_moveL = false;
		_moveR = false;
		_moveUp = false;
		_moveDn = false;
	}
	
	private void moveIfNecessary()
	{
		int x = _currentPlayerSprite.getXLeft();
		if (_moveR)
		{
			x += _travelRate;
		}
		else if (_moveL)
		{
			x -= _travelRate;
		}
		
		int y = _currentPlayerSprite.getYTop();
		if (_moveUp)
		{
			y -= _travelRate;
		}
		else if (_moveDn)
		{
			y += _travelRate;
		}
		
		/* set both left and right so both of them are always together */
		_playerLeftSprite.setTopLeftPos(x, y);
		_playerRightSprite.setTopLeftPos(x, y);	
	}
	
	private void checkForStoppingAtDestination()
	{
		int curXPos = _currentPlayerSprite.getXMiddle();
		int curYPos = _currentPlayerSprite.getYBottom();
		boolean wasMoving = _moveR || _moveL || _moveUp || _moveDn;
		if ((_moveR || _moveL) && Math.abs(curXPos - _xDest) < Consts.CHARACTER_STOPPING_POSITION_ERROR)
		{
			_moveR = false;
			_moveL = false;
		}
		
		if ((_moveUp || _moveDn) && Math.abs(curYPos - _yDest) < Consts.CHARACTER_STOPPING_POSITION_ERROR)
		{
			_moveUp = false;
			_moveDn = false;
		}
		
		if (wasMoving && !(_moveR || _moveL || _moveUp || _moveDn))
		{
			_currentPlayerSprite.setRunAnimation(false);
		}
	}
	
	private void checkForStoppingAtWall()
	{
		int curYPos = _currentPlayerSprite.getYBottom();
		int curXPos = _currentPlayerSprite.getXMiddle();
		boolean wasMoving = false;
		if (_moveR || _moveL || _moveUp || _moveDn)
		{
			wasMoving = true;
			for(int i = 0; i < _playerWalls.size(); i++)
			{
				int wallX = _playerWalls.get(i).getXPoint(curYPos);
				if (wallX != -1) // -1 means ignore
				{
					if (_moveR && (wallX > curXPos) && (Math.abs(wallX - curXPos) < Consts.CHARACTER_STOPPING_POSITION_ERROR))
					{
						_moveR = false;
					}
					else if (_moveL && (wallX < curXPos) && (Math.abs(wallX - curXPos) < Consts.CHARACTER_STOPPING_POSITION_ERROR))
					{
						_moveL = false;
					}
				}
				
				int wallY = _playerWalls.get(i).getYPoint(curXPos);
				if (wallY != -1)
				{
					if (_moveUp && (wallY < curYPos) && (Math.abs(wallY - curYPos)) < Consts.CHARACTER_STOPPING_POSITION_ERROR)
					{
						_moveUp = false;
					}
					else if (_moveDn && (wallY > curYPos) && (Math.abs(wallY - curYPos)) < Consts.CHARACTER_STOPPING_POSITION_ERROR)
					{
						_moveDn = false;
					}
				}
			}
		}
		
		if (wasMoving && !(_moveR || _moveL || _moveUp || _moveDn))
		{
			_currentPlayerSprite.setRunAnimation(false);
		}
	}

	
	@Override
	public void animationFinished(Sprite s) {
		
		if (((PlayerSprite)s).getDirection() == PlayerSprite.Direction.Left)
		{
			this.setCurrentSprite(_playerLeftSprite);
		}
		else
		{
			this.setCurrentSprite(_playerRightSprite);
		}
	}

}
