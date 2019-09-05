import javafx.scene.media.AudioClip;

public class PowerUp extends FallingObject{
	private double dx = 0;
	private double dy = 0;
	private AudioClip popSound = new AudioClip("File:Sounds/Pop.wav");
	
	PowerUp(String imageFileName) {
		super(imageFileName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean caughtByTooth(){
		Tooth intersectingToothObject = (Tooth)getOneIntersectingObject(Tooth.class);
		if(intersectingToothObject!=null){
			intersectingToothObject.addLife();
			this.popSound.play();
			return true;
		}
		return false;
	}
}
