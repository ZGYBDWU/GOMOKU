package server;

import java.net.Socket;

class Sharedbuf{
	//�ͻ��������Ϣ��ͬʱ�󶨸�һ�����̺߳�һ�����߳�,�������̵߳����й�����Դ
	//����ͨ���õı�����������Ϸ״̬
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
		//�¿ͻ�������ʱ��������
		Sharedbuf sharedbuf=new Sharedbuf();
		ReceiveThread rthread=new ReceiveThread();
		SendThread sthread=new SendThread();
		rthread.reg(sharedbuf,socket,clientid);
		sthread.reg(sharedbuf,socket,clientid);
		rthread.start();
		sthread.start();
	}
}
