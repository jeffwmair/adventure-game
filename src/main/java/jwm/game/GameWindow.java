package jwm.game;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;

import jwm.game.content.GameCanvas;
import jwm.game.content.GameRoom;

public class GameWindow extends JFrame
{
	private GameCanvas _gameCanvas;
	ArrayList<GameRoom> _rooms;
	GameRoom _activeScreen;
	public GameWindow()
	{
		
		// set the initial position of the frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int xPos = (screenSize.width - Consts.WINDOW_WIDTH) / 2;
		int yPos = (screenSize.height - Consts.WINDOW_HEIGHT) / 2;
		this.setSize(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
			
		_gameCanvas = new GameCanvas();
		_gameCanvas.setSize(Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
		_gameCanvas.loadRooms();
		
		this.setContentPane(_gameCanvas);
	
		this.setLocation(xPos, yPos);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setVisible(true);
	}
}
