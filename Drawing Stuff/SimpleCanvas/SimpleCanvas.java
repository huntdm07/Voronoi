/**
 * This is a stripped-down version of the Canvas class from the 
 * BlueJ team, retaining only the most fundamental features.
 * 
 * @author BlueJ team with modifications by Gordon Royle 
 * @version July 2003
 */
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleCanvas 
{
    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Image canvasImage;
    private boolean autoRepaint;
    
    
    /**
     * Creates and displays a SimpleCanvas of the specified size
     * with a white background. The client specifies whether repainting
     * after a drawing command should be manual or automatic.
     * 
     * @param   title title for the window
     * @param   width the desired width of the SimpleCanvas 
     * @param   height the desired height of the SimpleCanvas
     * @param   autoRepaint true for automatic repainting
     * 
     */
    
    public SimpleCanvas(String title, int width, int height, boolean autoRepaint) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width,height));
        frame.pack();
        Dimension size = canvas.getSize();
        canvasImage = canvas.createImage(size.width,size.height);
        graphic = (Graphics2D) canvasImage.getGraphics();
        graphic.setColor(Color.white);
        graphic.fillRect(0,0,size.width,size.height);
        graphic.setColor(Color.black);
        frame.setVisible(true);
        frame.setVisible(true);
	//        frame.show();
        frame.setVisible(true);
        this.autoRepaint = autoRepaint;
    }
    
   /**
     * Creates and displays a SimpleCanvas with a white background and
     * with automatic repainting after drawing commands.
     * 
     * @param   title title for the window
     * @param   width the desired width of the SimpleCanvas 
     * @param   height the desired height of the SimpleCanvas
     * 
     */
    
    public SimpleCanvas(String title, int width, int height) {
        this(title,width,height,true);
    }
    
    /**
     * Creates and displays a SimpleCanvas of size 400x400 with the
     * default title "SimpleCanvas" and with automatic repainting 
     * enabled.
     * 
     * @param   title title for the window
     * @param   width the desired width of the SimpleCanvas 
     * @param   height the desired height of the SimpleCanvas
     * 
     */
    
    public SimpleCanvas() {
        this("SimpleCanvas",400,400);
    }
    
    public SimpleCanvas(int red, int blue, int green) {
        this("SimpleCanvas",400,400);
        this.setForegroundColour( new java.awt.Color(red,blue,green) );
    }
    
    /** 
     * Draws a line on the SimpleCanvas between two points.
     * 
     * @param   x1 x-coordinate of the first point
     * @param   y1 y-coordinate of the first point
     * @param   x2 x-coordinate of the second point
     * @param   y2 y-coordinate of the second point
     *
     */
    
    public void drawLine(int x1, int y1, int x2, int y2) {
        graphic.drawLine(x1,y1,x2,y2);
        if (autoRepaint) canvas.repaint();
    }
    
    /** 
     * Changes the colour for subsequent 
     * drawing on this SimpleCanvas.
     * 
     * @param   newColour the new drawing colour
     * 
     */
    
    public void setForegroundColour(Color newColour) {
        graphic.setColor(newColour);
    }
    
    /**
     * Gets the colour currently used for 
     * drawing on this SimpleCanvas.
     * 
     */
    
    public Color getForegroundColour() {
        return graphic.getColor();
    }
    
    /**
     * Changes the font for subsequent String
     * drawing on this SimpleCanvas.
     *
     * @param   newFont the new Font
     * 
     */
    
    public void setFont(Font newFont) {
        graphic.setFont(newFont);
    }
    
    /**
     * Gets the font currently used for
     * String drawing on this Canvas
     */
    
    public Font getFont() {
        return graphic.getFont();
    }
    
    /**
     * Draws the specified String at the specified
     * location on this SimpleCanvas
     */
    
    public void drawString(String text, int x, int y) {
        graphic.drawString(text, x, y);
        if (autoRepaint) canvas.repaint();
    }
    
    /**
     * Sets the repaint mode to either manual or automatic.
     * 
     * @param   autoRepaint automatic repainting if this is true
     * 
     */
    
    public void setAutoRepaint(boolean autoRepaint) {
        this.autoRepaint = autoRepaint;
    }
     
     
    /**
     * If this SimpleCanvas does not automatically repaint 
     * after each drawing command, then this method can be
     * used to cause a manual repaint.
     */
    
    public void repaint() {
        canvas.repaint();
    }
    
    /**
     * Causes execution to pause for the specified amount of time.
     * This is usually used to produce animations in an easy
     * manner, by repeatedly drawing, pausing, and then redrawing
     * an object.
     */
    
    public void wait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            System.out.println("Interrruption in SimpleCanvas: "+ie);
        }
    }
    
    public void addMouseListener(MouseListener ml) {
        canvas.addMouseListener(ml);
    }
     
    class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage,0,0,null);
        }
    }
}

