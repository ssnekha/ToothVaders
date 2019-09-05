public class Tooth extends Actor{
	private double dx = 0;
	private double dy = 0;
	private int lives = 5;
	
	
	public Tooth(String filename) {
		super(filename);     
	}

	@Override
	public void act(long now) {
		move(dx, dy);
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
	
	public void setLives(int l){
		lives = l;
	}
	public int getLives(){
		return lives;
	}
	
	public void addLife(){
		lives++;
	}
	
	public void removeLife(){
		lives--;
	}
}