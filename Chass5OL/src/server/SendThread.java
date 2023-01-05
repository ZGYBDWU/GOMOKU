package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

class SendThread extends Thread{
	Socket socket;
	int clientid;
	Sharedbuf sendbuf;
	public void run() {
		BufferedWriter bufferedWriter=null;
		try {
			ServerMain.wlog("Preparing to send to client "+clientid+sendbuf);
			bufferedWriter =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
			while (true){
				//ServerMain.wlog("s"+sendbuf.str);
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				if(!(sendbuf.str=="")) {
					//ServerMain.wlog("str"+sendbuf.str);
					bufferedWriter.write(sendbuf.str);
					sendbuf.str="";
					bufferedWriter.write("\n");
					bufferedWriter.flush();
				}
			}
		}catch (IOException e){
			ServerMain.wlog("Sending thread terminated. Exception:"+e);
			//e.printStackTrace();
			}
		}
	public void reg(Sharedbuf buffer,Socket usesocket,int id) {
		socket=usesocket;
		clientid=id;	
		sendbuf=buffer;//这些属性是收发线程共享的，要注册
	}
}