package jwm.game.content;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import jwm.game.Consts;
import jwm.game.GameFactory;

@SuppressWarnings("serial")
public class GameCanvas extends JPanel implements MouseListener, MouseMotionListener, KeyListener, Runnable, IGameRoomChangeListener
{
	private GameRoom _activeRoom;
	//private ArrayList<GameRoom> _rooms;
	private Image _dbImage;
	private Thread animator;
	private volatile boolean running = false;
	public GameCanvas()
	{
		this.setFocusable(true);
		this.requestFocus();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
	}
	
	public void loadRooms()
	{
		GameFactory gf = new GameFactory();
		gf.configureGameRooms();
		//_rooms = GameFactory.getGameRooms();
		//_activeroom = _rooms.get(0);
		_activeRoom = gf.getStartRoom();
		_activeRoom.setGameRoomChangeListener(this);
		_activeRoom.startMusic();
	}


	@Override
	public void mouseClicked(MouseEvent e) 
	{
		_activeRoom.userClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) {	}
	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { }
	@Override
	public void mouseDragged(MouseEvent arg0) {	}

	@Override
	public void mouseMoved(MouseEvent arg0) {	}

	@Override
	public void keyPressed(KeyEvent e) 
	{	
		_activeRoom.userPressedKey(e);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
//		super.paintComponent(g2d);
		if (_dbImage != null)
		{
			g2d.drawImage(_dbImage, 0, 0, null);
		}
	}
	
	public void addNotify()
	{
		super.addNotify();
		startGame();
	}
	
	private void startGame()
	{
		if (animator == null || !running)
		{
			animator = new Thread(this);
			animator.start();
		}
	}
	
	private void renderGame()
	{
		if (_dbImage == null)
		{
			_dbImage = createImage(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
		}
		
		if (_dbImage != null)
		{
			_activeRoom.renderRoomContent(_dbImage);
		}
	}

	@Override
	public void GameRoomChanged(GameRoom newroom, int startX, int startY) 
	{
		/* stop him before changing rooms so he's not still walking when we change back*/
		_activeRoom.getPlayer().stopWalking();	
		_activeRoom = newroom;
		_activeRoom.configurePlayer(startX, startY);
		_activeRoom.setGameRoomChangeListener(this);
	}
	
	private void checkForPlayerPositionroomChange()
	{
		GameRoom currentroom = _activeRoom.getCurrentPlayerRoom();
		if (_activeRoom != currentroom)
		{
			_activeRoom.getPlayer().stopWalking();
			_activeRoom = currentroom;
			_activeRoom.setGameRoomChangeListener(this);	
		}
	}

	@Override
	public void run() 
	{
		
		running = true;
		while(running)
		{
			checkForPlayerPositionroomChange();
			_activeRoom.updateroomItems();
			renderGame();
			repaint();
			
			try
			{
				Thread.sleep(35);
			}
			catch(InterruptedException ex)
			{
				System.out.println(ex.toString());
			}
		}
		
	}
}
