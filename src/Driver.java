import java.io.File;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.audio.AudioPlayer;

public class Driver extends Application {
	Tooth tooth = new Tooth("file:images/tooth.png");
	Label livesRemaining = new Label();
	Label score = new Label();
	Insets margin1 = new Insets(4);
	Insets margin2 = new Insets(0,0,0,4);
	
	BorderPane bp = new BorderPane();
	MenuBar mb = new MenuBar();	
	Menu options = new Menu("Options");
	MenuItem restart = new MenuItem("Restart");
	MenuItem exit = new MenuItem("Exit");
	Menu help = new Menu("Help");
	MenuItem instructions = new MenuItem("How To Play");
	MenuItem about = new MenuItem("About");
	long startTime = 0;
	Timeline timeline;
	
	Label timeElapsed;
	Image iv;    
	ImageView imageView;
	VBox timerVbox;
	VBox vbox;
	VBox helpVbox;
	HBox hbox;
	HBox bigBox;
	boolean gameOver = false;
	boolean sadToothadded = false;
	int randx = 0;
	int prevRandX = 0;
	int prevRandX1 = 0;
	private AudioClip backgroundSound = new AudioClip("File:Sounds/Ring10.wav");
	private AudioClip gameOverSound = new AudioClip("File:Sounds/mobsong.mp3");
	private AudioClip gameWonSound = new AudioClip("File:Sounds/gameWon.m4a");
	boolean gameWon = false;
	
	World world = new World() {

	@Override
     public void act(long now) {
        	ObservableList<Node> children = this.getChildren();
        	long timeTaken = (System.currentTimeMillis() - startTime)/1000;
        	//System.out.println(timeTaken);
        	//End the game if number of lives less than 15 & time played greater than 30 secs
            boolean gameLost = (tooth.getLives() < 10) && (timeTaken > 40);
            boolean gameWon = (tooth.getLives() >= 10);
        	if( (tooth.getLives() > 0) && !gameLost && !gameWon) {
        	for (int i=0; i<children.size(); i++) {
        		((Actor)children.get(i)).act(now);
        	}
        	addFallingObjects();
        	livesRemaining.setText("Lives Remaining = " + tooth.getLives());
        	score.setText("Score: " + tooth.getLives() * 5);
        	
        	} else {
				if (!gameOver) { // Stop the previous song and start a new one
					backgroundSound.stop();
					if(gameWon) {
						gameWonSound.setCycleCount(backgroundSound.INDEFINITE);
						gameWonSound.play();
					}
					else {
						gameOverSound.setCycleCount(backgroundSound.INDEFINITE);
						gameOverSound.play();
					}
				}
        		gameOver = true;
        		if(gameWon)
        		    livesRemaining.setText("Game Won!!!");
        		else
        			livesRemaining.setText("Game Over");
        		// Remove all the objects from screen
        		for (int i=0; i<children.size(); i++) {
            		this.remove((Actor)children.get(i));
            	}
        		if(!sadToothadded) {
        			
	        		Image image;
	        		if(gameWon)
	        			image = new Image("file:images/tooth.png");
	        		else
	        			image = new Image("file:images/sadtooth.png");
	        	    imageView = new ImageView(image);
	        	    imageView.setFitWidth(200);
	    			imageView.setPreserveRatio(true);
	    			imageView.setX(world.getWidth()/2 - image.getWidth()/4);
	    			imageView.setY(world.getHeight()/2 - image.getHeight()/2);
	        		bp.getChildren().add(imageView);
	        		sadToothadded = true;
        		}
        		addRandomObjects();
        	}
        }
    };   
	
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("ToothVaders!");
		stage.setResizable(false);
		stage.setWidth(500);
		stage.setHeight(500);				
		
		tooth.setFitWidth(80);
		tooth.setPreserveRatio(true);
		
		tooth.setX(200);
		tooth.setY(350);  
		
		tooth.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent event) {
				if (!gameOver) {
					if (event.getCode() == KeyCode.LEFT) {
						if (tooth.getX() > 10) {
							tooth.move(-20, 0);
						}
					}
					if (event.getCode() == KeyCode.RIGHT) {
						//System.out.println(tooth.getX());
						if (tooth.getX() < tooth.getWorld().getWidth() - tooth.getWidth() - 10) {
							tooth.move(20, 0);
						}
					}
					if (event.getCode() == KeyCode.UP) {
						if (tooth.getY() > 10) {
							tooth.move(0, -5);
						}
					}
					if (event.getCode() == KeyCode.DOWN) {
						if (tooth.getY() < tooth.getWorld().getHeight() - tooth.getHeight() - 10) {
							tooth.move(0, 5);
						}
					}
				}
			}
		});
		
		
		//Adding a background to Border Pane		
		Image image = new Image("file:images/background2.jpg", 500, 500, false, true);
	    imageView = new ImageView(image);
		bp.getChildren().add(imageView);
		
	    vbox = new VBox();
	    //vbox.setStyle("-fx-background-color: #FFFFFF;");
	    vbox.setAlignment(Pos.TOP_LEFT);
	    
	    helpVbox = new VBox();
	    //helpVbox.setStyle("-fx-background-color: #FFFFFF;");
	    helpVbox.setAlignment(Pos.TOP_RIGHT);
		
		livesRemaining.setFont(Font.font("Cambria", 16));
		score.setFont(Font.font("Cambria", 14));
		livesRemaining.setAlignment(Pos.CENTER);
		
		restart.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	/*for (int i=0; i<world.getChildren().size(); i++) {
            		world.remove((Actor)world.getChildren().get(0));
		    	}*/
		    	startTime = System.currentTimeMillis();
		    	timeElapsed.setText("00:00");
		    	gameOver = false;
		    	gameWon = false;
		    	gameOverSound.stop();
		    	gameWonSound.stop();
        		backgroundSound.setCycleCount(backgroundSound.INDEFINITE);
        		backgroundSound.play();
		    	tooth.setLives(5);
		    	tooth.setX(200);
				tooth.setY(350);
				bindToTime();
				
				if(sadToothadded){
					bp.getChildren().remove(imageView);
					sadToothadded = false;
					world.add(tooth);
				}
		    }
		});
		
		exit.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	System.exit(0);
		    }
		});
		
		options.getItems().addAll(restart,exit);
		
		instructions.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	Stage instructionsStage = new Stage();
		    	instructionsStage.setTitle("Instructions");
		    	instructionsStage.setResizable(false);
		    	instructionsStage.setWidth(460);
		    	instructionsStage.setHeight(320);
		    	WebView wb = new WebView();
		    	wb.getEngine().load("file:///" + new File("P1_Group_4_htmlFiles/instructions.htm").getAbsolutePath());
		    	
				Scene instructionsScene = new Scene(wb);
				instructionsStage.setScene(instructionsScene);
				instructionsStage.show();
				
		    }
		});
		
		about.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	Stage aboutStage = new Stage();
		    	aboutStage.setTitle("About");
		    	aboutStage.setResizable(false);
		    	aboutStage.setWidth(450);
		    	aboutStage.setHeight(200);
		    	WebView wb = new WebView();
		    	wb.getEngine().load("file:///" + new File("P1_Group_4_htmlFiles/about.htm").getAbsolutePath());
		    	
				Scene aboutScene = new Scene(wb);
				aboutStage.setScene(aboutScene);
				aboutStage.show();
				
		    }
		});
		help.getItems().addAll(instructions,about);
		
		mb.getMenus().addAll(options, help);
		vbox.getChildren().addAll(livesRemaining, score);
		vbox.setMargin(livesRemaining, margin1);
		vbox.setMargin(score, margin2);
		
		helpVbox.getChildren().addAll(mb);
		
		bp.setRight(helpVbox);
		bp.setRight(vbox);
		
		timerVbox = new VBox();
		HBox hboxTimer = new HBox();
	    
	    Label label1 = new Label("Time Elapsed: ");
	    label1.setStyle("-fx-font: 16 arial;");
	    timeElapsed = new Label("00:00  ");
	    timeElapsed.setStyle("-fx-font: 16 arial;");
	    hboxTimer.getChildren().addAll(label1, timeElapsed);
	    timerVbox.getChildren().addAll(hboxTimer);
		
		bigBox = new HBox();
		bigBox.getChildren().addAll( helpVbox, vbox, timerVbox);
		
		bigBox.setSpacing(10);
		bp.setTop(bigBox);
		bp.setCenter(world);
		
		Scene scene = new Scene(bp);

		world.add(tooth);
		bindToTime();
		
		stage.setScene(scene);		
		stage.show();
		
		// Play background sound
		backgroundSound.setCycleCount(backgroundSound.INDEFINITE);
		this.backgroundSound.play();
		world.start();
		
	}
	
	private String timerString(long elapsed) {
		String out = "";
		String hr="", min="", sec="";
		long hours = elapsed /(60 * 60);
		elapsed = elapsed % 3600;
		long mins = elapsed / 60;
		elapsed = elapsed % 60;
		long secs = elapsed % 60;
		
		if(hours<10) {
			hr = "0"+ Long.toString(hours);
		} else {
			hr = Long.toString(hours);
		}
		
		if(mins<10) {
			min = "0"+ Long.toString(mins);
		} else {
			min = Long.toString(mins);
		}
		
		if(secs<10) {
			sec = "0"+ Long.toString(secs);
		} else {
			sec = Long.toString(secs);
		}
		
		out = min + ":" + sec; 
		return out;
	}
	
	private void bindToTime() {
		startTime = System.currentTimeMillis();
	    timeline = new Timeline(
	    new KeyFrame(Duration.seconds(0),
	        new EventHandler<ActionEvent>() {
	          @Override public void handle(ActionEvent actionEvent) {
	        	long elapsed;
	        	
	        	   if(gameOver) {
	        		   timeline.stop();
	        	    } else 
	        	    
	        	    {
	        	    	elapsed = (System.currentTimeMillis() - startTime)/1000;
	        	    	timeElapsed.setText( timerString(elapsed) + "   ");
	        	    }
	        	    
	            	            
	          }
	        }
	      ),
	      new KeyFrame(Duration.seconds(1))
	    );
	    timeline.setCycleCount(Animation.INDEFINITE);
	    timeline.play();
	  }
	
	
	public void addRandomObjects() {
		int randz = (int) (Math.random() * 10);
		String imagename = "";
		
		switch (randz) {
		case 0:
			imagename = "file:images/cookie.png";
			break;
		case 1:
			imagename = "file:images/cupcake.png";
			break;
		case 2:
			imagename = "file:images/donut.png";
			break;
		case 3:
			imagename = "file:images/cake.png";				
			break;
		case 4:
			imagename = "file:images/icecream.png";
			break;
		case 5:
			imagename = "file:images/lollipop.png";
			break;
		case 6:
			imagename = "file:images/chocolate.png";
			break;
		case 7:
			imagename = "file:images/floss.jpg";
			break;
		case 8:
			imagename = "file:images/toothpaste.png";
			break;
		default:
			imagename = "file:images/cake.png";
		}
		Sweet sweet = new Sweet(imagename);
		sweet.setFitWidth(50);
		sweet.setPreserveRatio(true);
		int randx = (int) (Math.random() * 400 + 1);
		int randy = (int) (Math.random() * 400 + 1);
		sweet.setX(randx);
		sweet.setY(randy);
		world.add(sweet);
	} 
	
	public void addFallingObjects() {
		// Add some random falling objects
		int randy = (int) (Math.random() * 175 + 1);
		//System.out.println(randy);
		if (randy == 4 || randy == 5 ) {
			int randz = (int) (Math.random() * 7);
			String imagename = "";
			switch (randz) {
			case 0:
				imagename = "file:images/cookie.png";
				break;
			case 1:
				imagename = "file:images/cupcake.png";
				break;
			case 2:
				imagename = "file:images/donut.png";
				break;
			case 3:
				imagename = "file:images/cake.png";				
				break;
			case 4:
				imagename = "file:images/icecream.png";
				break;
			case 5:
				imagename = "file:images/lollipop.png";
				break;
			case 6:
				imagename = "file:images/chocolate.png";
				break;

			default:
				imagename = "file:images/cake.png";
			}
			Sweet sweet = new Sweet(imagename);
			sweet.setFitWidth(50);
			sweet.setPreserveRatio(true);
			randx = (int) (Math.random() * (world.getWidth() - sweet.getWidth() - 10) + 1);
//			System.out.println("Sweet initial generated randx: " + randx);
			while((Math.abs(randx - prevRandX) < 100) || (Math.abs(randx-prevRandX1)<100)){
				randx = (int) (Math.random() * (world.getWidth() - sweet.getWidth() - 10) + 1);
//				System.out.println("Sweet generated randx inside condition: " + randx);
			}
			prevRandX1 = prevRandX;
			prevRandX = randx;
//			System.out.println(" Sweet prevRandX: " + prevRandX +" Sweet prevRandX1: " + prevRandX1);
			sweet.setX(randx);
//			System.out.println("Sweet XPosition:" + sweet.getX());
			sweet.setY(0);
			world.add(sweet);
		}else if (randy == 6) {
			int randz = (int) (Math.random() * 2);
			String imagename = "";
			switch (randz) {
			case 0:
				imagename = "file:images/floss.jpg";
				break;
			default:
				imagename = "file:images/toothpaste.png";
			}
			PowerUp powerUp = new PowerUp(imagename);
			powerUp.setFitWidth(50);
			powerUp.setPreserveRatio(true);
			randx = (int) (Math.random() * (world.getWidth() - powerUp.getWidth() - 10) + 1);
//			System.out.println("Power initial generated randx: " + randx);
			while((Math.abs(randx - prevRandX) < 100) || (Math.abs(randx-prevRandX1)<100)){
				randx = (int) (Math.random() * (world.getWidth() - powerUp.getWidth() - 10) + 1);
//				System.out.println("Power generated randx inside condition: " + randx);
			}
			prevRandX1 = prevRandX;
			prevRandX = randx;
//			System.out.println("Power prevRandX: " + prevRandX);
			powerUp.setX(randx);
			powerUp.setY(0);
//			System.out.println("Power XPosition:" + powerUp.getX());
			world.add(powerUp);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}