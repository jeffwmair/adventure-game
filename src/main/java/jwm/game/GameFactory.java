package jwm.game;

import java.util.ArrayList;

import jwm.game.content.Action;
import jwm.game.content.GameRoom;
import jwm.game.content.Hotspot;
import jwm.game.content.Player;
import jwm.game.content.PlayerSprite;
import jwm.game.content.PlayerSprite.Direction;
import jwm.game.content.Sprite;
import jwm.game.content.Wall;


public class GameFactory 
{
	private GameRoom _startRoom;
	public GameRoom getStartRoom() { return _startRoom; }
	public void configureGameRooms()
	{

		Player p = getPlayer();

		/* 
		 * 
		 * room 1 - OUTSIDE THE HOUSE
		 * 
		 */
		ArrayList<Sprite> room1Sprites = new ArrayList<Sprite>();
			
		// character click actions
		ArrayList<Action> scr1Spr1ClickActions = new ArrayList<Action>();
		Action smokeActionLook = new Action(Action.Type.Look, "Looks like someone has the fire going.");
		Action smokeActionTake = new Action(Action.Type.Take, "How are you going to take the chimney smoke?");
		scr1Spr1ClickActions.add(smokeActionLook);
		scr1Spr1ClickActions.add(smokeActionTake);
		Sprite spriteSmoke = new Sprite("spriteChimneySmoke.png", 
				1,		// start frame
				4,		// end frame
				31, 	// frame W
				35,		// frame H
				31,		// scaled W
				55,		// scaled H
				805,	// screen X pos
				65,		// screen Y pos
				5,		// animation rate
				true,	// start animation
				false,	// stop on last frame
				scr1Spr1ClickActions);
		 
		room1Sprites.add(spriteSmoke);
		
		/***************** character extra animation - vomit **************/
		int vomitLeftSpriteX = -40;
		int vomitLeftSpriteY = 5;
		int vomitFrameWidth = 274;
		int spriteCharacterScaledW = 40;
		int spriteCharacterScaledH = 100;
		int spriteCharacterFrameH = 335;
		PlayerSprite sprCharacterVomitLeft = new PlayerSprite("spritePlayerVomit.png", 
				1,9,
				vomitFrameWidth, spriteCharacterFrameH, 
				spriteCharacterScaledW*2, spriteCharacterScaledH,	// scaled size
				vomitLeftSpriteX,vomitLeftSpriteY,
				3, 	// animation rate
				false,
				true,	// stop at the last frame
				null, 
				PlayerSprite.Direction.Left);
		
		int vomitRightSpriteX = -1;
		int vomitRightSpriteY = 5;
		PlayerSprite sprCharacterVomitRight = new PlayerSprite("spritePlayerVomit.png", 
				10,18,
				vomitFrameWidth, spriteCharacterFrameH, 
				spriteCharacterScaledW*2, spriteCharacterScaledH,	// scaled size
				vomitRightSpriteX,vomitRightSpriteY,
				3, 	// animation rate
				false,
				true,	// stop at the last frame
				null,
				PlayerSprite.Direction.Right);
		
		ArrayList<Wall> room1Walls = new ArrayList<Wall>();
		room1Walls.add(new Wall(110,625,160,655));	// entrance to field RIGHT
		room1Walls.add(new Wall(160, 655,290,610));	// 
		room1Walls.add(new Wall(290,610,390,500));	// diagonal along edge of grass & field
		room1Walls.add(new Wall(390,500,460,500)); 	// grass to left of back porch
		room1Walls.add(new Wall(460,500,460,525));	// grass to left of back porch
		room1Walls.add(new Wall(460,525,530,525));	// bottom of back porch
		room1Walls.add(new Wall(530,525,530,540));  // left corner of house	
		room1Walls.add(new Wall(530,540,855,540));	// long edge of the house
		room1Walls.add(new Wall(855,540,890,520));	// stairs corner
		room1Walls.add(new Wall(890,520,925,530));	// stairs
		room1Walls.add(new Wall(925,530,975,510));	// stairs
		room1Walls.add(new Wall(975,510,1020,510));	// stairs to grass
		room1Walls.add(new Wall(1020,510,1020,740));	// right edge of screen
		room1Walls.add(new Wall(1020,740, 125, 740));	// bottom edge of screen
		room1Walls.add(new Wall(125, 740, 50, 710));	// 
		room1Walls.add(new Wall(50, 710, 45, 625));	// entrance to field LEFT
		
		/****************** hotspots ***********************/
		ArrayList<Hotspot> room1Spots = new ArrayList<Hotspot>();
		
		/********************* room1 spot -- sky**********************/
		ArrayList<Action> scr1SpotSkyActions = new ArrayList<Action>();
		scr1SpotSkyActions.add(new Action(Action.Type.Look, "Looking up at the sky, you see the signs of a storm not far off."));
		Hotspot spotSky = new Hotspot(0, 0, 1024, 768, 0, scr1SpotSkyActions);
		
		/********************* room1 spot -- ground**********************/
		ArrayList<Action> scr1SpotGrndActions = new ArrayList<Action>();
		scr1SpotGrndActions.add(new Action(Action.Type.Look, "It's the ground."));
		Hotspot spotGrnd = new Hotspot(0, 577, 1024, 191, 1, scr1SpotGrndActions);
		
		/********************* room1 spot -- ground**********************/
		ArrayList<Action> scr1SpotFieldActions = new ArrayList<Action>();
		scr1SpotFieldActions.add(new Action(Action.Type.Look, "It's the field."));
		Action actUseField = new Action(Action.Type.Use, "You eat some dirt from the field.");
		//Action actUseField = new Action(Action.Type.Use, "You eat some dirt from the field.");
		actUseField.setPlayerActionSprite(sprCharacterVomitLeft, PlayerSprite.Direction.Left);
		actUseField.setPlayerActionSprite(sprCharacterVomitRight, PlayerSprite.Direction.Right);
		scr1SpotFieldActions.add(actUseField);
		Hotspot spotFld = new Hotspot(0, 500, 280, 100, 2, scr1SpotFieldActions);
		
		/********************* room1 spot -- ground**********************/
		//Action actUseField = new Action(Action.Type.Use, "You eat some dirt from the field.");
		//actUseField.setPlayerActionSprite(sprCharacterVomitLeft, PlayerSprite.Direction.Left);
		//actUseField.setPlayerActionSprite(sprCharacterVomitRight, PlayerSprite.Direction.Right);
		//scr1SpotFieldActions.add(actUseField);
		Hotspot spotEnterFld = new Hotspot(40, 630, 80, 15, Consts.SPRITE_AND_ROOM_CHANGE_HOTSPOT_Z_POS, new ArrayList<Action>());
		
		/********************* room1 spot -- house**********************/
		ArrayList<Action> scr1SpotHouseActions = new ArrayList<Action>();
		scr1SpotHouseActions.add(new Action(Action.Type.Look, "It's your house."));
		Hotspot spotHouse = new Hotspot(530, 336, 392, 277, 3, scr1SpotHouseActions);
		
		/********************* room1 spot 1 -- door**********************/
		ArrayList<Action> scr1SpotDoorActions = new ArrayList<Action>();
		scr1SpotDoorActions.add(new Action(Action.Type.Look, "That's the front door to the house."));
		Hotspot spotDoor = new Hotspot(900, 425, 30, 65, Consts.SPRITE_AND_ROOM_CHANGE_HOTSPOT_Z_POS, scr1SpotDoorActions);
		//spotDoor.setroomChange(room)
		
		
		/**************** game room **********************/
		GameRoom roomOutsideHouse = new GameRoom(room1Sprites, 
				room1Spots, 
				room1Walls,
				"backgroundHouseOutside.png", 
				"",
				p,
				40,				// player w
				100,			// player h
				4);				// player movement rate
		
		/* 
		 * 
		 * room 2 - IN THE KITCHEN
		 * 
		 */
		ArrayList<Sprite> room2Sprites = new ArrayList<Sprite>();
		
		
		/************* walls for the room *************/
		ArrayList<Wall> room2Walls = new ArrayList<Wall>();
		room2Walls.add(new Wall(230,280,260,280));	// horiz top left
		room2Walls.add(new Wall(260,280,270,340));	// stove
		room2Walls.add(new Wall(270,340,430,340));  // stove
		room2Walls.add(new Wall(430,340,460,275));  // stove
		room2Walls.add(new Wall(460,275,980,275));	// long edge of upper wall
		room2Walls.add(new Wall(980,265,815,665));	// right wall
		room2Walls.add(new Wall(815,665,50,690));	// long lower wall
		room2Walls.add(new Wall(50,690,230,280));	// left wall
		
		/****************** hotspots ***********************/
		ArrayList<Hotspot> scr2Spots = new ArrayList<Hotspot>();
		
		/********************* room2 spot -- floor**********************/
		ArrayList<Action> scr2SpotFloorActions = new ArrayList<Action>();
		scr2SpotFloorActions.add(new Action(Action.Type.Look, "It's the old hard wood floor."));
		Hotspot scr2SpotFloor = new Hotspot(0, 0, 1024, 768, 0, scr2SpotFloorActions);
		
		/********************* room2 spot -- ground**********************/
		ArrayList<Action> scr2SpotExitActions = new ArrayList<Action>();
		Action scr2SpotExitActionLook = new Action(Action.Type.Look, "That's the way back outside");
		scr2SpotExitActions.add(scr2SpotExitActionLook);
		Hotspot scr2SpotExitToroom1 = new Hotspot(860, 350, 20, 150, Consts.SPRITE_AND_ROOM_CHANGE_HOTSPOT_Z_POS, scr2SpotExitActions);
		scr2SpotExitToroom1.setroomChange(roomOutsideHouse, 950, 425);
		
		//scr2Spots.add(scr2SpotFloor);
		scr2Spots.add(scr2SpotExitToroom1);
		// sprites are also hotspots!
		scr2Spots.add(p.getLeftFacingSprite());
		scr2Spots.add(p.getRightFacingSprite());
//		scr2Spots.add(src2PlayerSpriteNormalLeft);
//		scr2Spots.add(scr2PlayerSpriteNormalRight);
		
		//room2Sprites.add(scr2KitchenTable);
		
		
		
		/**************** game room **********************/
		GameRoom roomKitchen = new GameRoom(room2Sprites, 
				scr2Spots, 
				room2Walls,
				"backgroundHouseKitchen.png", 
				"",
				p, 
				80,					// player w 
				200,				// player h
				7);					// player movement rate
		
		
		/* 
		 * 
		 * START room 3 - THE FIELD
		 * 
		 */
		//ArrayList<Action> scr3TreeClickActions = new ArrayList<Action>();
		Sprite scr3Trees = new Sprite("spriteFieldCentreTrees.png", 
				1,		// first frame
				1,		// last frame
				533, 	// width
				420,	// height
				533,	// width
				420,	// height
				188, 	// xPos
				130,	// yPos
				1,		// animationRate
				false,
				false,
				null);
		Sprite scr3Wagon = new Sprite("spriteFieldWagon.png", 
				1,
				1,
				122, 
				108,
				122,
				108,
				38, 
				550,
				1,
				false,
				false,
				null);
		
		ArrayList<Sprite> room3Sprites = new ArrayList<Sprite>();
		room3Sprites.add(scr3Trees);
		room3Sprites.add(scr3Wagon);
		
		ArrayList<Wall> room3Walls = new ArrayList<Wall>();
		
		/* field perimeter */
		room3Walls.add(new Wall(173,430, 1015,430));	// across the top, left->right
		room3Walls.add(new Wall(1015,430,1015,740));	// right side
		room3Walls.add(new Wall(1015,740,8,740));		// bottom
		room3Walls.add(new Wall(8,750,8,563));			// left-bottom, below trees
		room3Walls.add(new Wall(8,563,173,430));		// left- along trees
		
		/* tree's in the middle */
		room3Walls.add(new Wall(270,470,681,470));		// behind trees
		room3Walls.add(new Wall(681,470,681,550));		// right of trees
		room3Walls.add(new Wall(681,550,270,550));		// below trees
		room3Walls.add(new Wall(270,550,270,470));		// left of trees
		
		/* wagon */
		room3Walls.add(new Wall(75,660,165,660));		// wagon bottom right
		room3Walls.add(new Wall(165,660,165,600));		// wagon right
		room3Walls.add(new Wall(165,600,68,600));		// wagon top; R->L
		room3Walls.add(new Wall(68,600,26,647));		// wagon left
		room3Walls.add(new Wall(26,647,44,697));		// wagon hitch
		room3Walls.add(new Wall(44,697,75,658));		// wagon hitch
		
		
		Hotspot spotExitFld = new Hotspot(675, 430, 350, 12, Consts.SPRITE_AND_ROOM_CHANGE_HOTSPOT_Z_POS, new ArrayList<Action>());
		ArrayList<Hotspot> room3Spots = new ArrayList<Hotspot>();
		room3Spots.add(spotExitFld);
		
		GameRoom roomField = new GameRoom(room3Sprites, 
				room3Spots, 
				room3Walls,
				"backgroundField.png", 
				"",
				p, 
				30,					// player width 
				80,					// player height
				3);					// player movement rate
		

		/* 
		 * 
		 * 
		 * END room 3 - THE FIELD
		 * 
		 */
		
		/***************************************************************/
		/* add hotspots to hotspot lists last because room change hotspots
		 * need references to other rooms
		 */
		
		spotDoor.setroomChange(roomKitchen, 750, 280);
		room1Spots.add(spotDoor);
		spotEnterFld.setroomChange(roomField, 880, 400);
		room1Spots.add(spotFld);
		room1Spots.add(spotEnterFld);
		// sprites are also hotspots!
		room1Spots.add(spriteSmoke);
		room1Spots.add(p.getLeftFacingSprite());
		room1Spots.add(p.getRightFacingSprite());
		spotExitFld.setroomChange(roomOutsideHouse, 90, 630);		
		room3Spots.add(spotExitFld);
		_startRoom = roomKitchen;
		_startRoom.configurePlayer(780, 450);
	}
	
	private Player getPlayer()
	{

		int spriteCharacterStartFrameLeft = 1;
		int spriteCharacterEndFrameLeft = 8;
		int spriteCharacterStartFrameRight = 9;
		int spriteCharacterEndFrameRight = 16;
		
		int spriteCharacterFrameW = 137;
		/* this height hides feet a little; gives the impression of walking through
		 * long grass or mud
		 */
		int spriteCharacterFrameH = 395;
		int spriteCharacterScaledW = 40;
		int spriteCharacterScaledH = 100;
		int spriteCharacterInitialXPos = 750;
		int spriteCharacterInitialYPos = 280;
		int spriteCharacterAnimationRate = 1;
		
		// character click actions
		ArrayList<Action> characterClickActions = new ArrayList<Action>();
		Action playerLookAction = new Action(Action.Type.Look, "It's you buddy.");
		Action playerUseAction = new Action(Action.Type.Use, "You are well used.");
		Action playerTakeAction = new Action(Action.Type.Take, "You have yourself already.  That's more than enough.");
		characterClickActions.add(playerLookAction);
		characterClickActions.add(playerUseAction);
		characterClickActions.add(playerTakeAction);
		
		PlayerSprite playerSpriteNormalLeft = new PlayerSprite("spritePlayer.png", 
				spriteCharacterStartFrameLeft, 
				spriteCharacterEndFrameLeft,
				spriteCharacterFrameW, 
				spriteCharacterFrameH,
				spriteCharacterScaledW,
				spriteCharacterScaledH,
				spriteCharacterInitialXPos,
				spriteCharacterInitialYPos,
				spriteCharacterAnimationRate,
				false,
				false,
				characterClickActions,
				Direction.Left);
		PlayerSprite playerSpriteNormalRight = new PlayerSprite("spritePlayer.png", 
				spriteCharacterStartFrameRight, 
				spriteCharacterEndFrameRight,
				spriteCharacterFrameW, 
				spriteCharacterFrameH,
				spriteCharacterScaledW,
				spriteCharacterScaledH,
				spriteCharacterInitialXPos,
				spriteCharacterInitialYPos,
				spriteCharacterAnimationRate,
				false, 
				false,
				characterClickActions, Direction.Right);
		
		Player player = new Player(playerSpriteNormalLeft, playerSpriteNormalRight);
		player.setCurrentSprite(playerSpriteNormalLeft);
			
		return player;
	}
}
