package jwm.game.content;

public class Action 
{
	public enum Type { Walk, Look, Take, Use, Changeroom }
	private Type _actionType;
	private String _dialogText;
	public Action(Action.Type actionType, String dialogText)
	{
		_actionType = actionType;
		_dialogText = dialogText;
	}
	
	public Type getActionType()
	{
		return _actionType;
	}
	
	public String getDialogText()
	{
		return _dialogText;
	}
	
	PlayerSprite _characterActionSpriteL;
	PlayerSprite _characterActionSpriteR;
	public void setPlayerActionSprite(PlayerSprite spr, PlayerSprite.Direction dir) 
	{ 
		if (dir == PlayerSprite.Direction.Left)
		{
			_characterActionSpriteL = spr;	
		}
		else if (dir == PlayerSprite.Direction.Right)
		{
			_characterActionSpriteR = spr;
		}
		else
		{
			throw new UnsupportedOperationException("setPlayerActionSprite with direction: " + dir.toString());
		}
	}
	public PlayerSprite getPlayerActionSprite(PlayerSprite.Direction dir)	
	{ 
		if (dir == PlayerSprite.Direction.Left)
		{
			return _characterActionSpriteL;
		}
		else if (dir == PlayerSprite.Direction.Right)
		{
			return _characterActionSpriteR;	
		}
		else
		{
			throw new UnsupportedOperationException("setPlayerActionSprite with direction: " + dir.toString());
		}
	}
}
