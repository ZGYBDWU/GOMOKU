package server;

public class Game {
	int[][] chessBoard=new int[19][19];
	int stepcount=0;
	boolean isJoined=false;
	Sharedbuf hostbuf;//������sharedbuf����������������Ϣ
	Sharedbuf guestbuf;//���˵�sharedbuf�����������˷���Ϣ
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
