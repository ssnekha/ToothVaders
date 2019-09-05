import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class FallingObject extends Actor{
	private double dx = 0;
	private double dy = 1;
	private AudioClip popSound = new AudioClip("File:Sounds/cartoon008.wav");
	
	FallingObject(String imageFileName){
		super(imageFileName);    
	} 

	//rest doesn't matter because will be overridden
	@Override
	public void act(long now) {
		if(reachedBottom() || caughtByTooth()){
			getWorld().remove(this);
			
		}else{
			move(dx, dy);
		}
	}
		
	public boolean reachedBottom(){
		return (getY() > getWorld().getHeight() - 10);
	}
	
	public boolean caughtByTooth(){
		Tooth intersectingToothObject = (Tooth)getOneIntersectingObject(Tooth.class);
		if(intersectingToothObject!=null){
			intersectingToothObject.removeLife();
			this.popSound.play();
			return true;
		}
		return false;
	}
	
	public double getDx(){
		return dx;
	}
	
	public double getDy(){
		return dy;
	}
	
	public void setDx(double dx){
		this.dx = dx;
	}
	
	public void setDy(double dy){
		this.dy = dy;
	}
}
