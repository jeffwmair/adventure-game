package jwm.game.content;

import java.util.ArrayList;

public class Hotspot extends BoundingBox
{
	private ArrayList<Action> _actions;
	private GameRoom _newRoom;
	private int _zPos;
	private int _newroomStartPosX;
	private int _newroomStartPosY;
	public Hotspot(int xLeft, int yTop, int width, int height, int zPos, ArrayList<Action> actions) 
	{
		super(xLeft, yTop, width, height);
		_actions = actions;
		_zPos = zPos;
	}
	
	public void setroomChange(GameRoom room, int startX, int startY) 
	{ 
		_newRoom = room; 
		_newroomStartPosX = startX;
		_newroomStartPosY = startY;
	}
	public GameRoom getNewRoom() { return _newRoom; }
	public boolean hasNewRoom() { return (_newRoom != null); }
	public int getNewRoomPlayerStartX() { return _newroomStartPosX; }
	public int getNewRoomPlayerStartY() { return _newroomStartPosY; }
	
	public Action getAction(Action.Type actionType)
	{
		Action retAction = null;
		if (_actions != null)
		{
			for(int i = 0; i < _actions.size(); i++)
			{
				if (_actions.get(i).getActionType() == actionType)
				{
					retAction = _actions.get(i);
					break;
				}
			}
		}
		if (retAction == null)
		{
			if (actionType == Action.Type.Look)
			{
				retAction = new Action(Action.Type.Look, "I guess there's nothing to look at there.");
			}
			else if (actionType == Action.Type.Take)
			{
				retAction = new Action(Action.Type.Take, "I guess there's nothing to take from there.");
			}
			else
			{
				retAction = new Action(Action.Type.Take, "I guess there's nothing to use there.");
			}
		}
		return retAction;
	}
	
	public ArrayList<Action> getActions() { return _actions; }
	
	public int getZPos() { return _zPos; }
}
