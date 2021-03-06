package voiceRecognition;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chess.ChessController;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * This class translates the voice commands
 * @author alex
 *
 */
public class VoiceController {
	private static VoiceController instance;
	private ChessController chessController;
	private Map<String,String> radioAlphabet;
	private final int SIZE=8;
	private int originX;
	private int originY;
	private int destX;
	private int destY;
	private int index;
	
	public VoiceController() {
		String letters[] = new String []{"alfa","alpha", "bravo", "charlie","delta","eco","echo","foxtrot","golf","hotel"};
		String radioWordTranslated="";
		
		this.index=this.originX=this.originY=this.destX=this.destY=-1;
		this.radioAlphabet=new HashMap();
		this.chessController=ChessController.getInstance();

		this.radioAlphabet.put("alfa", "1");
		this.radioAlphabet.put("alpha", "1");
		this.radioAlphabet.put("bravo", "2");
		this.radioAlphabet.put("charlie", "3");
		this.radioAlphabet.put("delta", "4");
		this.radioAlphabet.put("eco", "5");
		this.radioAlphabet.put("echo", "5");
		this.radioAlphabet.put("foxtrot", "6");
		this.radioAlphabet.put("golf", "7");
		this.radioAlphabet.put("hotel", "8");	
	}
	
	/**
	 * Singleton pattern
	 * @return
	 */
	public static VoiceController getInstance() {
		if(instance==null) {
			instance=new VoiceController();
		}
		return instance;
	}
	
	public void parseGameMode(String gameMode) {
		gameMode=gameMode.toLowerCase();
		if(gameMode.contains("un jugador")) {
			this.chessController.setGameMode("1player");
			System.out.println("1 player mode set");
			/*try {
				FileInputStream fileInputStream= new FileInputStream("1player.mp3");
				Player player = new Player(fileInputStream);
				player.play();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}else if(gameMode.contains("2 jugadores")){
			this.chessController.setGameMode("2player");
			/*try {
				FileInputStream fileInputStream= new FileInputStream("2player.mp3");
				Player player = new Player(fileInputStream);
				player.play();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	/**
	 * Parses strings which can contain a command
	 * @param word
	 */
	public void parse(String possibleCommand) {
		possibleCommand=possibleCommand.toLowerCase();
		
		String translation="";
		String words []=possibleCommand.split(" ");
		String command="notACommand";//Used only for testing
		String color=this.chessController.getColor();
		parseGameMode(possibleCommand);
		if(possibleCommand.contains("enroque izquierda")) {
			if(color.equals("w")) {
				this.chessController.move("longW");
			}else if(color.equals("b")) {
				this.chessController.move("shortB");
			}		
			return;
		}else if(possibleCommand.contains("enroque derecha")) {
			if(color.equals("w")) {
				this.chessController.move("shortW");
			}else if(color.equals("b")) {
				this.chessController.move("longB");
			}		
			return;
		}else if(possibleCommand.contains("deshacer deshacer")) {
			this.chessController.undo();
		}
		if(findOriginX(words)) {
			if(findOriginY(words)) {
				if(findDestX(words)) { 
					if(findDestY(words)) {
						if(isPromotion(words)){
							this.chessController.promote(this.originX, this.originY, this.destX, this.destY,words[index]);
							command=this.originX+","+this.originY+","+ this.destX+","+ this.destY;
						}else {
							this.chessController.move(this.originX, this.originY, this.destX, this.destY);
							command=this.originX+","+this.originY+","+ this.destX+","+ this.destY;
						}
						
					}
				}
			}
		}
		
	}
	
	/**
	 * Finds a word from the radio alphabet (alpha,bravo,charlie...) and translates it to
	 * a numeric coordinate
	 * @param words
	 * @return true if found, false otherwise
	 */
	public boolean findOriginX(String [] words) {
		boolean found=false;
		this.index=0;
		for(int i=0;i<words.length;i++){
			if(this.radioAlphabet.containsKey(words[i])) {
				this.originX=Integer.valueOf(this.radioAlphabet.get(words[i]));
				this.index= i+1;
				found=true;
				break;
			}
		}
		return found;
	}
	
	
	/**
	 * Checks if the next word is a number from 1 to 8, both included.
	 * a numeric coordinate
	 * @param words
	 * @return true if it is, false otherwise
	 */
	public boolean findOriginY(String [] words) {
		boolean found=false;
		if(this.index>=words.length) {
			return false;
		}
		if(words[this.index].matches("^([1-8])$")) {
			this.originY=Integer.valueOf(words[this.index]);
			found=true;
			this.index++;
		}else {
			found=false;
			this.originX=-1;
		}
		return found;
	}
	
	
	/**
	 * Finds a word from the radio alphabet (alpha,bravo, charlie...) and translates it to
	 * a numeric coordinate
	 * @param words
	 * @return true if found, false otherwise
	 */
	public boolean findDestX(String [] words) {
		boolean found=false;
		if(this.index>=words.length) {
			return false;
		}
		for(int i=this.index;i<words.length;i++){
			if(this.radioAlphabet.containsKey(words[i])) {
				this.destX=Integer.valueOf(this.radioAlphabet.get(words[i]));
				this.index= i+1;
				found=true;
				break;
			}
		}
		if(!found) {
			this.originX=-1;
			this.originY=-1;
		}
		return found;
	}
	
	/**
	 * Checks if the next word is a number from 1 to 8, both included.
	 * a numeric coordinate
	 * @param words
	 * @return true if it is, false otherwise
	 */
	public boolean findDestY(String [] words) {
		boolean found=false;
		if(this.index>=words.length) {
			return false;
		}
		if(words[this.index].matches("^([1-8])$")) {
			this.destY=Integer.valueOf(words[this.index]);
			found=true;
			this.index++;
		}else {
			found=false;
			this.originX=-1;
			this.originY=-1;
			this.destX=-1;
		}
		return found;
	}
	
	
	/**
	 * Checks if the command is a promotion, that is, the command is like
	 * <originX> <originY> <destX> <destY> <piecePromoted>
	 * @param words
	 * @return
	 */
	public boolean isPromotion(String [] words) {
		if(this.index>=words.length) {
			return false;
		}
		boolean isPromotion=false;
		if(words[this.index].equals("reina") ||
				words[this.index].equals("torre")||
				words[this.index].equals("caballo")||
				words[this.index].equals("alfil")) {
			isPromotion=true;
		}
		return isPromotion;
	}
	
	
	
	
}
