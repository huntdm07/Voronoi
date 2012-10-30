import java.math.*;

public class FlagDrawer
{
    // instance variables - replace the example below with your own
    
    private SimpleCanvas sc;
    private int xPos;
    private int yPos;
    private int size;
    
    
    
    public FlagDrawer(int size) {
        this.size = size;
        sc = new SimpleCanvas("Flag Drawer", size, size, false);
        xPos = 0;
        yPos = 0;
    }
    
    //Methods to help construct this shit
    
    private void setColour(int red, int green, int blue) {
        sc.setForegroundColour(new java.awt.Color(red, green, blue));
    }
    
    private void rectangle(int x1, int y1, int x2, int y2) {
        xPos = x1;
        yPos = y1;
        
        for (int i = y1; i < y2; i++) {
            sc.drawLine(x1,i,x2,i);
        }
    }
        
    
    //Methods that actually draw the flags
    
    public void resetCanvas() {
        this.setColour(255,255,255);
        this.rectangle(0,0,size,size);
        this.setColour(0,0,0);
    }
    
    public void signalCharlie() {
        //white background
        this.setColour(255,255,255);
        this.rectangle(0,0,size,size);
        
        //two blue strips, top and bottom, of height size/5
        this.setColour(0,0,255);
        this.rectangle(0,0,size,size/5);
        this.rectangle(0,size*4/5,size,size);
        
        //centred red strip of height size/5 
        this.setColour(255,0,0);
        this.rectangle(0,size*2/5,size,size*3/5);
                
        sc.repaint();
    }
    
    public void signalDelta() {
        //yellow background
        this.setColour(255,255,0);
        this.rectangle(0,0,size,size);
        
        //very dark blue central strip
        this.setColour(0,0,153);
        this.rectangle(0,size/4,size,size*3/4);
        
        sc.repaint();
    }
    
    public void signalEcho() {
        //top very dark blue
        this.setColour(0,0,153);
        this.rectangle(0,0,size,size/2);
        
        //bottom red
        this.setColour(255,0,0);
        this.rectangle(0,size/2,size,size);
        
        sc.repaint();
    }
    
    public void signalFoxtrot() {
        //red diagonal square
        this.setColour(255,0,0);
        
        //top half
        xPos=size/2;
        for(int i = 0; i <= size/2; i++) {
            sc.drawLine(xPos-i,i,xPos+i,i);
        }
        
        //bottom half
        for(int i = 0; i <= size/2; i++) {
            sc.drawLine(xPos-i,size-i,xPos+i,size-i);
        }
                
        sc.repaint();
    }
    
    public void signalHotel() {
        //LHS white
        this.rectangle(0,0,size/2,size);
        
        //RHS red
        this.setColour(255,0,0);
        this.rectangle(size/2,0,size,size);
        
        sc.repaint();
    }
    
    public void signalIndia() {
        //yellow background
        this.setColour(255,255,0);
        this.rectangle(0,0,size,size);
        
        //centred black circle of radius size/4
        this.setColour(0,0,0);
        xPos = size/2;
        yPos = size/2;
      
       
       for (double i = -90; i < 90; i=i+0.1) {
            sc.drawLine((int) (xPos-size/4*Math.cos(i*Math.PI/180)),
                (int) (yPos-size/4*Math.sin(i*Math.PI/180)),
                (int) (xPos+size/4*Math.cos(i*Math.PI/180)),
                (int) (yPos-size/4*Math.sin(i*Math.PI/180)));
            }
        
        sc.repaint();
    }
    
    public void signalLima() {
        //yellow background
        this.setColour(255,255,0);
        this.rectangle(0,0,size,size);
        
        //diagonal top left and bottom right black squares
        this.setColour(0,0,0);
        this.rectangle(size/2,0,size,size/2);
        this.rectangle(0,size/2,size/2,size);
        
        sc.repaint();
    }
    
    public void signalPapa() {
        //very dark blue background
        this.setColour(0,0,153);
        this.rectangle(0,0,size,size);
        
        //centred white square with side length size/2 
        this.setColour(255,255,255);
        this.rectangle(size/4,size/4,size*3/4,size*3/4);
                
        sc.repaint();
    }
    
    public void signalWhiskey() {
        //very dark blue background
        this.setColour(0,0,153);
        this.rectangle(0,0,size,size);
        
        //centred white square with side length size*2/3 
        this.setColour(255,255,255);
        this.rectangle(size/6,size/6,size*5/6,size*5/6);
        
        //centred smaller red square with side length size*1/3
        this.setColour(255,0,0);
        this.rectangle(size/3,size/3,size*2/3,size*2/3);
                
        sc.repaint();
    }
    
    public void signalZulu() {
        //LHS black
        this.setColour(0,0,0);
        this.rectangle(0,0,size/2,size);
        
        //RHS blue
        this.setColour(0,0,255);
        this.rectangle(size/2,0,size,size);
        
        //top yellow triangle
        this.setColour(255,255,0);
        xPos=size/2;
        yPos=size/2;
        for(int i = 0; i <= size/2; i++) {
            sc.drawLine(xPos-(i-1),yPos-i,xPos+(i-1),yPos-i);
        }
        
        //bottom red triangle
        this.setColour(255,0,0);
        for(int i = 0; i <= size/2; i++) {
            sc.drawLine(xPos-i,yPos+i,xPos+i,yPos+i);
        }
                             
        sc.repaint();
    }
    
    
}
        