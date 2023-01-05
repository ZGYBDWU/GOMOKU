package server;

import java.net.Socket;

class Sharedbuf{
	//客户端相关信息，同时绑定给一个收线程和一个发线程,这两个线程的所有共享资源
	//包括通信用的变量，还有游戏状态
	String str="";
	String hostname="";
	int hashedhostname=0;
	int clientid;
	Game game=new Game();
	int order=0;
	void sharedbuf() {
		str="";
	}
	public void newclient(Socket socket,int clientid) {
		//新客户端连入时建立连接
		Sharedbuf sharedbuf=new Sharedbuf();
		ReceiveThread rthread=new ReceiveThread();
		SendThread sthread=new SendThread();
		rthread.reg(sharedbuf,socket,clientid);
		sthread.reg(sharedbuf,socket,clientid);
		rthread.start();
		sthread.start();
	}
}
