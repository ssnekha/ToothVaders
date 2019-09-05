import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public abstract class World extends Pane{
	private AnimationTimer timer;
    
    public World(){
    	timer = new AnimationTimer() {
    		@Override
    		public void handle(long now){
				act(now);
				// This was causing concurrent modification exception
				//  Moved this to act in world
				/*for(Node n: getChildren()){
					((Actor)(n)).act(now);
				}*/
    		}
        };
        sceneProperty().addListener(new ChangeListener<Scene>(){

			@Override
			public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
				if(newValue!=null){
					getScene().setOnKeyPressed(new EventHandler<KeyEvent>(){

						@Override
						public void handle(KeyEvent keyPress) {
							if(getOnKeyPressed() != null){
								getOnKeyPressed().handle(keyPress);
							}
							
							for(Node actor: getChildren()){
								if(actor.getOnKeyPressed() != null){
									actor.getOnKeyPressed().handle(keyPress);
								}
							}
						}
						
					});
					
					getScene().setOnKeyReleased(new EventHandler<KeyEvent>(){

						@Override
						public void handle(KeyEvent keyRelease) {
							if(getOnKeyReleased() != null){
								getOnKeyReleased().handle(keyRelease);
							}
							
							for(Node actor: getChildren()){
								if(actor.getOnKeyReleased() != null){
									actor.getOnKeyReleased().handle(keyRelease);
								}
							}
						}
						
					});
					
				}
				
			}
        	
        });
    }
    
    
    public abstract void act(long now);
    
    public void add(Actor actor){
    	getChildren().add(actor);
    }
    
    public <A extends Actor>List<A> getObjects(java.lang.Class<A> cls){
    	ArrayList<A> list = new ArrayList<A>();
    	for(Node node: getChildren()){
    		if(cls.isInstance(node)){
    			list.add(cls.cast(node));
    		}
    	}
		return list;	
    }

    public void remove(Actor actor){
    	getChildren().remove(actor);
    }
    
    public void start(){
    	timer.start();
    }
    
    public void stop(){
    	timer.stop();
    }
}