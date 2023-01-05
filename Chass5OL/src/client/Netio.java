package client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Netio{//网络通信类
	Socket socket;
	BufferedWriter netw;
	BufferedReader netr;
	BufferedReader sysin;
	boolean connectionIsRunning;
	String hostname;
	public Netio(){
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostname=addr.getHostName();
		} catch (UnknownHostException e1) {
			ClientMain.wlog("Failed to get hostname:"+e1);
		}
		try {
			sysin =new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			ClientMain.wlog("Receiving thread terminated. Exception:"+e);
		}
	}
	private void setbur(Socket socket) {
		try {
			netw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
			netr=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
		}catch(IOException e){
			ClientMain.wlog("Receiver thread terminated. Exception:"+e);
		}
		return;
	}
	public boolean estconnection(String ipv4,int port) {
		try {
			//初始化一个socket并建立连接
				socket =new Socket(ipv4,port);
				//网络发送，网络接收，系统输入
				setbur(socket); 
				new Thread(new Runnable() {
					public void run() {
						connectionIsRunning=true;
						try {
							ClientMain.wlog("Connection established with server "+ipv4);
							netw.write('c'+hostname+"\n");
							netw.flush();
							String str;//通过while循环不断读取信息
							while (connectionIsRunning){
								try {
									Thread.sleep(0);
								}catch(Exception e) {}
								str = netr.readLine();
								//System.out.print(str);
								if(str!=null) {
								ClientMain.wlog("Server:"+str);}
							}
						}catch (IOException e){
							ClientMain.wlog("Connection terminated due to server issue.\nInformation:"+e);
							}
					}
				}).start();
				while (true){
					String str = sysin.readLine();
					netw.write(str);
					netw.write("\n");
					netw.flush();
				}
			}catch (IOException e) {
				ClientMain.wlog("connection failed on "+ipv4+" at port "+port+".");
				ClientMain.wlog(""+e);
			}
		return true;
	}
}