package client;
import java.text.SimpleDateFormat;
import java.util.Date;
public class ClientMain {
	static Netio netio;
	public static void main(String[] args) {
		netio=new Netio();
		netio.estconnection("127.0.0.1",12712);
		//服务器的地址和名称，换服务器的时候记得改这里
	}
	public static void wlog(String msg) {//代替system.out,写日志用的 
		SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		System.out.println("["+formatter.format(date)+"]"+msg);
	}
}
