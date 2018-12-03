package chess;

public class Knight extends Piece{

	Board board;
	
	public boolean isRestricted(int newX, int newY) {
		
		boolean restrict = false;
		
		if(this.x == newX || this.y == newY) {
			restrict = true;
		}
		else if((Math.abs(this.x - newX != 2) || Math.abs(this.y - newY != 1)) {
			restrict = true;
		}
		else if((Math.abs(this.y - newY != 2) || Math.abs(this.x - newX != 1)) {
			restrict = true;
		}
		
		if(!restrict && board.checkSquare(newX, newY).getColor() == this.getColor()) {
			restrict = true;
		}
	}
}
