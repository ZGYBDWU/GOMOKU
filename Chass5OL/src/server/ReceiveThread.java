package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class ReceiveThread extends Thread{
	Socket socket;
	int clientid;
	Sharedbuf sendbuf;
	public void run() {
		BufferedReader bufferedReader=null;
		try {
			ServerMain.wlog("Connection established with client "+clientid);
			bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			String str;//ͨ��whileѭ�����϶�ȡ��Ϣ��
			sendbuf.clientid=clientid;
			while ((str = bufferedReader.readLine())!=null){
			//���
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				//sendbuf.str=str;
				ServerMain.msgprocess(str,clientid,sendbuf);
			}
		}catch (IOException e){
			ServerMain.wlog("Receiving thread terminated. Exception:"+e);
			sendbuf.game.reportConnectionLost();
			try {
				socket.close();
			} catch (IOException e1) {
				ServerMain.wlog("WARNING:Failed to close socket at client"+clientid);
			}
		}
	}
	public void reg(Sharedbuf buf,Socket usesocket,int id) {//ע��
		socket=usesocket;
		clientid=id;	
		sendbuf=buf;//��Щ�������շ��̹߳���ģ�Ҫע��
	}
}