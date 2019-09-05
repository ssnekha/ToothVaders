import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Actor extends javafx.scene.image.ImageView{
	public Actor(){     
		super();
	}
	
	public Actor(String imageFileName){
		super();
		Image actorImage = new Image(imageFileName);
		this.setImage(actorImage);
	}
	
	public abstract void act(long now);
	
	public World getWorld(){
		return (World) getParent();
	}
	
	public double getHeight(){
		return getBoundsInLocal().getHeight();
	}
	
	public double getWidth(){
		return getBoundsInLocal().getWidth();
	}
	
	public void setLocation(double x, double y){
		setX(x);
		setY(y);
	}
	
	public void	move(double dx, double dy){
		setX(getX() + dx);
		setY(getY() + dy);
	}
	
	public <A extends Actor> java.util.List<A> getIntersectingObjects(java.lang.Class<A> cls){
		ArrayList<A> list = new ArrayList<A>();
		
		for(Node node: getWorld().getObjects(cls)){
			if(node != this && node.intersects(this.getBoundsInLocal())){
				list.add((A) node);
			}
		}
		
		return list;
	}
	
	public <A extends Actor>A getOneIntersectingObject(java.lang.Class<A> cls){
		for(Node node: getWorld().getObjects(cls)){
			if(node != this && node.intersects(this.getBoundsInLocal())){
				return (A)node;
			}
		}
		return null;
	}
}