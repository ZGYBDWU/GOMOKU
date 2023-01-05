package server;

public class Game {
	int[][] chessBoard=new int[19][19];
	int stepcount=0;
	boolean isJoined=false;
	Sharedbuf hostbuf;//房主的sharedbuf，用来给房主发信息
	Sharedbuf guestbuf;//客人的sharedbuf，用来给客人发信息
	void game() {
		stepcount=0;
	}
	public void dd(int color,int x,int y) {
		chessBoard[x][y]=color;
		stepcount++;
		return;
	}
	public String getstr() {
		String ans=null;
		for(int i=0;i<19;i++) {
			for(int j=0;j<19;j++) {
				ans=ans+chessBoard[i][j];
			}
		}
		return ans;
	}
	public void reportConnectionLost() {
		try {
			hostbuf.str="eGame closed";
		}catch(Exception e) {
		}
		try {
			guestbuf.str="eGame closed";
		}catch(Exception e) {
			
		}
	}
}
