package jwm.game.content;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;

import jwm.game.Consts;

public class TextDialog 
{
	private String _text;
	private final int MAX_LINE_WIDTH = 500;
	private final int FONT_SIZE = 25;
	private final int LINE_SPACING = 5;
	private final int DIALOG_PADDING = 25;
	public TextDialog(String txt)
	{
		_text = txt;
	}
	
	public void paint(Graphics2D g)
	{
		// prepare to paint
		Font f = new Font("SanSerif", Font.PLAIN, FONT_SIZE);
		FontMetrics metrics = g.getFontMetrics(f);
		int maxLineLengthChars = MAX_LINE_WIDTH / metrics.stringWidth("F");
		ArrayList<String> lines = getDialogText(maxLineLengthChars);
		int stringWidth = 0;
		for(int i = 0; i < lines.size(); i++)
		{
			int thisStringWidth = metrics.stringWidth(lines.get(i));
			if (thisStringWidth > stringWidth)
			{
				stringWidth = thisStringWidth;
			}
		}
		int fontHeight = metrics.getHeight();
		int xPosText = (Consts.WINDOW_WIDTH / 2) - (stringWidth/2);
		int yPosText = (Consts.WINDOW_HEIGHT / 2) - (lines.size()*FONT_SIZE)/2;
		int xPosBox = xPosText - DIALOG_PADDING;
		int yPosBox = (int) (yPosText - fontHeight);
		int boxWidth = stringWidth + (DIALOG_PADDING*2);
		int heightBox = lines.size()*FONT_SIZE + 110;  // extra room for buttons
		
		// do the painting here
		paintBox(g, xPosBox, yPosBox, heightBox, boxWidth);
		paintText(g, f, lines, xPosText, yPosText);
	}
	
	private ArrayList<String> getDialogText(int maxLineLength)
	{
		String[] textLines = _text.split(" ");		// split the text into an array of words
		StringBuilder sbLine = new StringBuilder();
		ArrayList<String> dialogLines = new ArrayList<String>();
		boolean justAddedNewLine = false;
		for(int i = 0; i < textLines.length; i++)	// loop through each word;  some words may have \n at the start or end, so watch for that
		{
			if ((sbLine.toString().length() + textLines[i].length()) <= maxLineLength || textLines[i].contains("\n"))
			{
				if (textLines[i].contains("\n"))
				{
					String[] wordsWithNewline = textLines[i].split("\n");
					for(int j = 0; j < wordsWithNewline.length; j++)
					{
						if (j == 0)	// first element is before a newline
						{
							sbLine.append(wordsWithNewline[j]);
							dialogLines.add(sbLine.toString());
							sbLine = new StringBuilder();
							justAddedNewLine = true;
						}
						else if (j != wordsWithNewline.length-1)	// if not the last one in the array, it needs to be ended with a newline too
						{
							dialogLines.add(wordsWithNewline[j]);	
							justAddedNewLine = true;
						}
						else	// if the last word in the array of \n separated words, then don't end this one with a newline
						{
							sbLine.append(wordsWithNewline[j] + " ");
							justAddedNewLine = false;
						}
					}
				}
				else
				{
					sbLine.append(textLines[i] + " ");	
					justAddedNewLine = false;
				}
			}
			else 
			{
				dialogLines.add(sbLine.toString());
				sbLine = new StringBuilder();
				sbLine.append(textLines[i] + " ");
			}	
			
			// last word, so wrap it up
			if (i == textLines.length-1 && !justAddedNewLine)
			{
				dialogLines.add(sbLine.toString());
			}
		}
		return dialogLines;
	}
	
	private void paintBox(Graphics2D g, int xPos, int yPos, int height, int width)
	{
		Color oldColor = g.getColor();
		g.setColor(new Color(55,55,55));
		g.fillRect(xPos, yPos, width, height);
		g.setColor(new Color(175,195,220));
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(5));
		g.drawRect(xPos, yPos, width, height);
		g.setColor(oldColor);
		g.setStroke(oldStroke);
	}
	
	private void paintText(Graphics2D g, Font f, ArrayList<String> lines, int xPos, int yPos)
	{
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();
		g.setColor(new Color(200,202,207));
		g.setFont(f);
		for(int i = 0; i < lines.size(); i++)
		{
			g.drawString(lines.get(i), xPos, (i*(FONT_SIZE+LINE_SPACING))+yPos);
		}
		g.setFont(oldFont);
		g.setColor(oldColor);
	}
}
