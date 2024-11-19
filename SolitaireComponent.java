import java.awt.*;
import java.awt.event.*;
import java.util.*;

class SolitaireComponent extends Component
{
 
 	public final static int NUMBERSTACK = 4;
 
    Pack				pack;
    Image				bufferImage;
    Graphics			bufferGraphics;
	
	CardStack[]			stacks = new CardStack[NUMBERSTACK];
	CardStack 			especialstack;
	CardPile			talon;
	CardWastePile		waste;
	CardFoundation[]	founds = new CardFoundation[4];
	
	CardPictureStore	store;
	
	CardRun				runCurrent = null;
	CardSet				sourceSet = null;
	Point				pDragOffset = new Point();
	
	public	int			nDrawCards;

	public	SolitaireComponent(CardPictureStore store)
	{
		nDrawCards = 3;
		SetStore(store);
		enableEvents(MouseEvent.MOUSE_EVENT_MASK | 
					 MouseEvent.MOUSE_MOTION_EVENT_MASK | 
					 ComponentEvent.COMPONENT_EVENT_MASK);
	}
	
	public void SetStore(CardPictureStore store)
	{
		this.store = store;
	}
	
	public void processComponentEvent(ComponentEvent e)
	{
		switch (e.getID())
		{
		case ComponentEvent.COMPONENT_RESIZED:
			InitBuffer();
			break;
		}
	}
	
	void InitBuffer()
	{
		// create a buffer to store the background image
		Dimension bufferSize = getSize();
		System.out.println("Resized to " + bufferSize.width + ", " + bufferSize.height);
		bufferImage = this.createImage(bufferSize.width, bufferSize.height);
		bufferGraphics = bufferImage.getGraphics();
	}
	
	public void processMouseEvent(MouseEvent e)
	{
		switch (e.getID())
		{
		case MouseEvent.MOUSE_PRESSED:
			MouseDown(e.getX(), e.getY());
			break;
		case MouseEvent.MOUSE_RELEASED:
			MouseUp(e.getX(), e.getY());
			break;
		case MouseEvent.MOUSE_CLICKED:
			if (e.getClickCount() == 2)
				DoubleClick(e.getX(), e.getY());
			break;
		}
	}
	
	public void processMouseMotionEvent(MouseEvent e)
	{
		switch (e.getID())
		{
		case MouseEvent.MOUSE_DRAGGED:
			if (runCurrent != null)
			{
				InvalidateCardSet(runCurrent);
				runCurrent.MoveTo(e.getX() - pDragOffset.x, e.getY() - pDragOffset.y);
				InvalidateCardSet(runCurrent);
				Redraw();
			}
			break;
		}
	}
	
	final int LEFTMARGIN = 50;
	final int TOPMARGIN = 50;
	final int HORZSPACING = 18;
	final int VERTSPACING = 30;
	
	public void Deal()
	{
		try
		{
			int			i, nStack;
			Dimension	dimCard = store.GetCardSize();
		
			pack = new Pack();
			pack.Shuffle();
			for (nStack = 0; nStack < NUMBERSTACK; nStack++)
			{
				stacks[nStack] = new CardStack(LEFTMARGIN + (dimCard.width + HORZSPACING) * nStack, TOPMARGIN + dimCard.height + VERTSPACING);
			
			// Add Card to Stack
			
			//	for (i = 0; i < nStack; i++)
			//		stacks[nStack].AddCard(pack.Deal());
				
				// add an upturned card
				Card c = pack.Deal();
				c.TurnCard();
				stacks[nStack].AddCard(c);
			}
			
			especialstack = new CardStack(LEFTMARGIN + (dimCard.width + HORZSPACING) * 6, TOPMARGIN + dimCard.height + VERTSPACING);
			especialstack.setCardSpacing(5);
			especialstack.setConditionFalse();
			
							
			for (i = 0; i < 13; i++)
					especialstack.AddCard(pack.Deal());
			Card c2 = pack.Deal();
			c2.TurnCard();
			especialstack.AddCard(c2);
		
			
			
			
			talon = new CardPile(LEFTMARGIN, TOPMARGIN);
			for (i = 0; i < 22; i++)
				talon.AddCard(pack.Deal());
			waste = new CardWastePile(LEFTMARGIN + dimCard.width + HORZSPACING, TOPMARGIN, nDrawCards);
		
			for (i = 0; i < 4; i++)
				founds[i] = new CardFoundation(LEFTMARGIN + (dimCard.width + HORZSPACING) * (3 + i), TOPMARGIN);
			
			repaint();
		}
		catch (IllegalAccessException e)
		{
			System.out.println("Logic error during dealing: " + e.getMessage());
		}
	}

    // a standard overwriting of update()
    public void update(Graphics g) 
	{ 
		paint(g);
	}

    public void paint(Graphics g) 
	{
		Dimension	dimCard = store.GetCardSize();
		Rectangle	rClip = g.getClipBounds();
		
		if (bufferGraphics == null)
			InitBuffer();
		
		//paint the background
//		bufferGraphics.setColor(Color.green.darker());
//		bufferGraphics.fillRect(rClip.x, rClip.y, rClip.width, rClip.height);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image im = tk.getImage(getClass().getResource("tapete.jpg"));
		bufferGraphics.drawImage(im,0,0,720,680,this);



		RepaintCardSet(bufferGraphics, rClip, talon);
		RepaintCardSet(bufferGraphics, rClip, waste);
		
		for (int nFound = 0; nFound < 4; nFound++)
			RepaintCardSet(bufferGraphics, rClip, founds[nFound]);
		
		for (int nStack = 0; nStack < NUMBERSTACK; nStack++)
			RepaintCardSet(bufferGraphics, rClip, stacks[nStack]);

		RepaintCardSet(bufferGraphics, rClip, especialstack);

		// Draw currently dragging card stack
		if (runCurrent != null)
			RepaintCardSet(bufferGraphics, rClip, runCurrent);
		
		// Then copy the off-screen buffer onto the screen
		g.drawImage(bufferImage, 0, 0, this);
    }
	
	void RepaintCardSet(Graphics gDest, Rectangle rClip, CardSet set)
	{		
		if (rClip.intersects(set.GetBounds(store)))
			set.Draw(store, gDest, this);

	}
	
	Rectangle	rInvalid;
	
	void InvalidateCardSet(CardSet set)
	{
		Rectangle r = new Rectangle(set.getX(), set.getY(), set.getWidth(store), set.getHeight(store));
		if (rInvalid == null)
			rInvalid = r;
		else
			rInvalid = rInvalid.union(r);
	}
	
	void Invalidate(CardSet set)
	{
		rInvalid = new Rectangle(0, 0, getSize().width, getSize().height);
	}
	
	void Redraw()
	{
		if (rInvalid != null)
			repaint(rInvalid.x, rInvalid.y, rInvalid.width, rInvalid.height);
		rInvalid = null;
	}
	
	public final static int HITTESTRESULT_NONE = 0;
	public final static int HITTESTRESULT_TALON = 1;
	public final static int HITTESTRESULT_WASTE = 2;
	public final static int HITTESTRESULT_STACK = 3;
	public final static int HITTESTRESULT_FOUND = 4;	
		
	class HitTestResult
	{
		public int		iSourceType;
		public CardSet	setSource;
		public Point	pCard;

		public HitTestResult()
		{
			pCard = new Point();
			setSource = null;
			iSourceType = HITTESTRESULT_NONE;
		}
	};
	
	public HitTestResult HitTest(int x, int y)
	{
		HitTestResult r = new HitTestResult();
		
		if (talon.HitTest(store, x, y, r.pCard))
		{
			r.iSourceType = HITTESTRESULT_TALON;
			r.setSource = talon;
			return r;
		}
		
		if (waste.HitTest(store, x, y, r.pCard))
		{
			r.iSourceType = HITTESTRESULT_WASTE;
			r.setSource = waste;
			return r;
		}

		for (int nFound = 0; nFound < 4; nFound++)
		{
			if (founds[nFound].HitTest(store, x, y, r.pCard))
			{
				r.iSourceType = HITTESTRESULT_FOUND;
				r.setSource = founds[nFound];
				return r;
			}
		}

		for (int nStack = 0; nStack < NUMBERSTACK; nStack++)
		{
			if (stacks[nStack].HitTest(store, x, y, r.pCard))
			{
				r.iSourceType = HITTESTRESULT_STACK;
				r.setSource = stacks[nStack];
				return r;
			}
		}
		
		if (especialstack.HitTest(store, x, y, r.pCard))
		{
			r.iSourceType = HITTESTRESULT_STACK;
			r.setSource = especialstack;
			return r;
		}
		
		
		
		
		
		
		return r;
	}
	
	public void MouseDown(int x, int y)
	{
		HitTestResult	r = HitTest(x, y);

		try
		{
			sourceSet = r.setSource;
			switch (r.iSourceType)
			{
			case HITTESTRESULT_TALON:
				// Mouse down on talon - remove card and add it to waste pile.
				if (!talon.IsEmpty())
				{
					waste.ResetDisplayCards();
					for (int i = 0; (i < nDrawCards) && !talon.IsEmpty(); i++)
					{	
						Card c = talon.RemoveTopCard();
						c.TurnCard();
						waste.AddCard(c);
					}
				}
				else
				{
					// Talon empty - empty waste pile back into talon
					while (!waste.IsEmpty())
					{
						Card c = waste.RemoveTopCard();
						c.HideCard();
						talon.AddCard(c);
					}
				}
				InvalidateCardSet(talon);
				InvalidateCardSet(waste);
				Redraw();
				break;

			// Look for things to remove cards from
			case HITTESTRESULT_WASTE:
			case HITTESTRESULT_FOUND:
				if (!sourceSet.IsEmpty())
				{
					InvalidateCardSet(sourceSet);
					runCurrent = CardRun.RunFromCard(sourceSet.RemoveTopCard());
				}
				break;

			case HITTESTRESULT_STACK:
				// must be a stack
				if (!sourceSet.IsEmpty())
				{
					CardStack stack = (CardStack)sourceSet;
					
					InvalidateCardSet(stack);
					if (!stack.IsFaceUp())
						stack.TurnTopCard();
					else
						runCurrent = stack.PickUpRunAtPoint(x, y);
				}
			}

			if (runCurrent == null)
				return;

			pDragOffset.x = x - r.pCard.x;
			pDragOffset.y = y - r.pCard.y;
			runCurrent.MoveTo(r.pCard.x, r.pCard.y);
			InvalidateCardSet(runCurrent);
			Redraw();
		}
		catch (IllegalAccessException e)
		{
			System.out.println("Logic error: " + e.getMessage());
		}
	}
	
	public void MouseUp(int x, int y)
	{
		if (runCurrent != null)
		{
			Dimension	dimCard = store.GetCardSize();
			HitTestResult	r = HitTest(x - pDragOffset.x + (dimCard.width / 2), 
										y - pDragOffset.y + (dimCard.height / 2));
			
			InvalidateCardSet(runCurrent);
			switch (r.iSourceType)
			{
			case HITTESTRESULT_STACK:
				if (((CardStack)r.setSource).DropRun(runCurrent))
				{
					InvalidateCardSet(r.setSource);
					runCurrent = null;
				}
				break;
				
			case HITTESTRESULT_FOUND:
				if (runCurrent.GetNumCards() == 1)
				{
					// we're only moving one card
					if (r.setSource.CanAddCard(runCurrent.GetBottomCard()))
					{
						r.setSource.AddCard(runCurrent.GetBottomCard());
						InvalidateCardSet(r.setSource);
						runCurrent = null;
					}
				}
				break;
			}
			if (runCurrent != null)
			{
				sourceSet.AddCardSet(runCurrent);
				runCurrent = null;
				InvalidateCardSet(sourceSet);
			}
		}
		Redraw();
	}
	
	public void DoubleClick(int x, int y)
	{
		HitTestResult	r = HitTest(x, y);
			
		if ((r.iSourceType == HITTESTRESULT_WASTE) ||
			(r.iSourceType == HITTESTRESULT_STACK))
		{
			if (r.setSource.IsEmpty())
				return;
		
			Card c = r.setSource.GetTopCard();
			// Search for a foundation on which to put this card.
			for (int nFound = 0; nFound < 4; nFound++)
			{
				if (founds[nFound].CanAddCard(c))
				{
					InvalidateCardSet(r.setSource);
					founds[nFound].AddCard(r.setSource.RemoveTopCard());
					InvalidateCardSet(founds[nFound]);
					Redraw();
					return;
				}
			}
		}
	}
}
