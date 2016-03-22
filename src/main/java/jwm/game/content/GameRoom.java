package jwm.game.content;

import jwm.game.Consts;
import jwm.game.GameFactory;
import jwm.game.MusicPlayback;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GameRoom
{
	private ArrayList<Hotspot> _hotspots;
	private ArrayList<Sprite> _sprites = new ArrayList<Sprite>();
	private ArrayList<Wall> _walls = new ArrayList<Wall>();
	private Player _player;
	private BufferedImage _backgroundImg;
	private String _musicFname;
	private TextDialog _textDialog;
	private Action.Type _playerAction;
	private Graphics2D _dbg;
	private IGameRoomChangeListener _scrChangeListener;
	private int _playerW;
	private int _playerH;
	private int _playerMovementRate;

	public GameRoom(ArrayList<Sprite> sprites, 
			ArrayList<Hotspot> hotspots, 
			ArrayList<Wall> walls,
			String backgroundFName, 
			String musicFName,
			Player player,
			int playerWidth,
			int playerHeight,
			int playerMovementRate)
	{
		URL imgUrl = GameFactory.class.getResource(Consts.RESOURCES_BACKGROUNDS_FOLDER+backgroundFName);
		try 
		{
			_backgroundImg = ImageIO.read(imgUrl);
		} 
		catch (Exception e) 
		{
			System.err.println("Error reading url: '"+imgUrl+"', which should be for '"+Consts.RESOURCES_BACKGROUNDS_FOLDER+backgroundFName+"':"+e);
			e.printStackTrace();
		}

		_player = player;
		_walls = walls;
		_hotspots = hotspots;
		_sprites = sprites;
		_playerW = playerWidth;
		_playerH = playerHeight;
		_playerMovementRate = playerMovementRate;
		//_player.configure(playerWidth, playerHeight, playerMovementRate, walls);
		_musicFname = musicFName;
		_playerAction = Action.Type.Walk; 		// start the player walking on the room
	}

	public Player getPlayer() { return _player; }

	public void configurePlayer(int xLeftPos, int yTopPos)
	{ 
		_player.configure(xLeftPos, yTopPos, _playerW, _playerH, _playerMovementRate, _walls);
	}

	public void startMusic()
	{
		if (_musicFname != null && _musicFname.length() > 0) {
			MusicPlayback music = new MusicPlayback();
			music.start(_musicFname);
		}
	}

	public void setGameRoomChangeListener(IGameRoomChangeListener listener) { _scrChangeListener = listener; }

	public void updateroomItems()
	{
		Boolean pauseAnimation = isTextDialogShown();

		if (!pauseAnimation)
		{
			for(int i = 0; i < _sprites.size(); i++)
			{

				_sprites.get(i).animate();
			}
			_player.update();
		}
	}

	public GameRoom getCurrentPlayerRoom()
	{
		Hotspot hs = null;
		for(int i = 0; i < _hotspots.size(); i++)
		{
			hs = _hotspots.get(i);
			if (hs.getZPos() == Consts.SPRITE_AND_ROOM_CHANGE_HOTSPOT_Z_POS)
			{
				int playerX = _player.getCurrentSprite().getXMiddle();
				int playerY = _player.getCurrentSprite().getYBottom();
				if (hs.containtsPoint(playerX, playerY) && hs.hasNewRoom())
				{
					GameRoom newRoom = hs.getNewRoom();
					newRoom.configurePlayer(hs.getNewRoomPlayerStartX(), hs.getNewRoomPlayerStartY());
					return newRoom;
				}
			}
		}
		return this;
	}

	/**
	 * Render  the image parameter to the room 
	 * @param dbImage
	 */
	public void renderRoomContent(Image dbImage)
	{
		_dbg = (Graphics2D) dbImage.getGraphics();

		_dbg.drawImage(_backgroundImg, 
				0,
				0, 
				Consts.WINDOW_WIDTH, 
				Consts.WINDOW_HEIGHT, 
				0, 
				0, 
				Consts.WINDOW_WIDTH, 
				Consts.WINDOW_HEIGHT, 
				null);


		/*******************debug*****************/ 
		//		if (_hotspots != null && _hotspots.size() > 0)
		//		{
		//			for (int i = 0; i < _hotspots.size(); i++)
		//			{
		//				_hotspots.get(i).paint(_dbg);
		//			}
		//		}
		//		ArrayList<Wall> walls = _player.getWalls();
		//		Color oldClr = _dbg.getColor();
		//		_dbg.setColor(Color.RED);
		//		for(int i = 0; i < walls.size(); i++)
		//		{
		//			walls.get(i).paint(_dbg);
		//		}
		//		_dbg.setColor(oldClr);
		/*******************debug*****************/ 


		Sprite s = null;
		for(int i = 0; i < _sprites.size(); i++)
		{
			s = _sprites.get(i);
			if (s.getYBottom() < _player.getCurrentSprite().getYBottom())
			{
				s.paintSprite(_dbg);
			}
		}

		_player.getCurrentSprite().paintSprite(_dbg);

		for(int i = 0; i < _sprites.size(); i++)
		{
			s = _sprites.get(i);
			if (s.getYBottom() >= _player.getCurrentSprite().getYBottom())
			{
				s.paintSprite(_dbg);
			}
		}		

		// show the dialog if it has been set
		if (isTextDialogShown())
		{
			_textDialog.paint(_dbg);
		}

		/*********** show the player action and help key link ***********/
		Color oldColor = _dbg.getColor();
		Font oldFont = _dbg.getFont();
		_dbg.setColor(new Color(20,25,30));
		_dbg.fillRect(15, 13, 65, 25);
		_dbg.setColor(new Color(175,180,200));
		_dbg.setFont(new Font("SanSerif", Font.PLAIN, 20));
		_dbg.drawString(_playerAction.toString(), 23, 32);
		_dbg.setColor(new Color(255,0,0));
		_dbg.setFont(new Font("SanSerif", Font.PLAIN, 12));
		_dbg.drawString("h=help", 970, 15);
		_dbg.setFont(oldFont);
		_dbg.setColor(oldColor);
	}

	private boolean isTextDialogShown() { return _textDialog != null; }
	private void setTextDialog(String text) { _textDialog = new TextDialog(text); }
	private void clearTextDialog() { _textDialog = null; }
	private void switchToNextPlayerAction()
	{
		if (_playerAction == Action.Type.Walk)
		{
			_playerAction = Action.Type.Look;
		}
		else if (_playerAction == Action.Type.Look)
		{
			_playerAction = Action.Type.Take;
		}
		else if (_playerAction == Action.Type.Take)
		{
			_playerAction = Action.Type.Use;
		}
		else if (_playerAction == Action.Type.Use)
		{
			_playerAction = Action.Type.Walk;
		}
	}

	private Hotspot getTopmostHotspot(int x, int y)
	{
		boolean clickedHotspot = false;
		ArrayList<Hotspot> clickedSpots = new ArrayList<Hotspot>();
		for(int i = 0; i < _hotspots.size() && !clickedHotspot; i++)
		{
			if (_hotspots.get(i).containtsPoint(x, y))
			{
				clickedSpots.add(_hotspots.get(i));
			}
		}

		if (clickedSpots.size() == 0)
		{
			return null;
		}
		else if (clickedSpots.size() == 1)
		{
			return clickedSpots.get(0);
		}
		else
		{
			// return the spot with the highest Z-index
			int highIndex = 0;
			int keepZ = 0;
			for(int i = 0; i < clickedSpots.size(); i++)
			{
				int curZ = clickedSpots.get(i).getZPos(); 
				if (curZ > keepZ)
				{
					keepZ = curZ;
					highIndex = i;
				}
			}
			return clickedSpots.get(highIndex);
		}
	}

	public void userClicked(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			if (!isTextDialogShown())
			{
				if (_playerAction == Action.Type.Walk)
				{			
					_player.setPlayerDestination(x, y);
				}
				else
				{
					// go through the room hotspots
					Hotspot hs = getTopmostHotspot(x, y);
					if (hs != null)
					{
						int playerX = _player.getCurrentSprite().getXMiddle();
						int playerY = _player.getCurrentSprite().getYBottom();
						if ((_playerAction == Action.Type.Use || _playerAction == Action.Type.Take)
								&& (hs.getDistance(playerX, playerY) > Consts.CHARACTER_MIN_INTERACTION_DISTANCE))
						{
							setTextDialog("You'll have to get closer to it.");
						}
						else
						{
							if (hs.getZPos() == Consts.SPRITE_AND_ROOM_CHANGE_HOTSPOT_Z_POS
									&& hs.hasNewRoom() && _playerAction == Action.Type.Use)
							{
								// if its a change room hotspot, and if the player is close enough to it, then change the room
								_player.stopWalking();
								_scrChangeListener.GameRoomChanged(hs.getNewRoom(), hs.getNewRoomPlayerStartX(), hs.getNewRoomPlayerStartY());
							}
							else
							{
								Action hsAction = hs.getAction(_playerAction);
								setTextDialog(hsAction.getDialogText());
								PlayerSprite oldPlayerSprite = _player.getCurrentSprite();
								PlayerSprite actionSprite = hsAction.getPlayerActionSprite(oldPlayerSprite.getDirection());
								if (actionSprite != null)
								{
									PlayerSprite sprClone = actionSprite.clone();
									/** need to get these temp vars because calling setCurrentSprite
									 * actually sets to the position of the former sprite, which in
									 * this case we don't want to happen.	*/
									int xPos = sprClone.getXLeft() + oldPlayerSprite.getXLeft();
									int yPos = sprClone.getYTop() + oldPlayerSprite.getYTop();
									_player.setCurrentSprite(sprClone);
									sprClone.addSpriteAnimationObserver(_player);
									_player.getCurrentSprite().setTopLeftPos(xPos, yPos);
									_player.stopWalking();
									oldPlayerSprite.setRunAnimation(false);
									_player.getCurrentSprite().setRunAnimation(true);
								}	
							}
						}
					}
					else
					{
						setTextDialog("I guess there's nothing to do there");
					}
				}
			}
			else
			{
				// close the text dialog
				clearTextDialog();
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3)
		{
			switchToNextPlayerAction();
		}
	}

	public void userPressedKey(KeyEvent e)
	{
		int key = e.getKeyCode();

		if (isTextDialogShown())
		{
			if (key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_ENTER)
			{
				clearTextDialog();
			}
		}
		else 
		{
			if(key == KeyEvent.VK_RIGHT)
			{
				_player.toggleRightDirection();
			}
			else if (key == KeyEvent.VK_LEFT)
			{
				_player.toggleLeftDirection();
			}
			else if (key == KeyEvent.VK_UP)
			{
				_player.toggleUpDirection();
			}
			else if (key == KeyEvent.VK_DOWN)
			{
				_player.toggleDownDirection();
			}
			else if (key == KeyEvent.VK_SPACE)
			{
				switchToNextPlayerAction();
			}
			else if (key == KeyEvent.VK_Q)
			{
				System.exit(0);
			}
			else if (key == KeyEvent.VK_H)
			{
				String helpText = "HELP:\n \nh: bring up this help menu." +
					"\n \nSpace or R-Click: cycle through player actions (shown in top-left corner)." +
					"\n \nESC or Enter: when a text dialog is open (like this one), close it." +
					"\n \nL-Click:  Perform the action shown in the top-left corner." +
					"\n \nq: Quit";
				setTextDialog(helpText);
			}
		}
	}

}
