package client;
import java.text.SimpleDateFormat;
import java.util.Date;
public class ClientMain {
	static Netio netio;
	public static void main(String[] args) {
		netio=new Netio();
		netio.estconnection("127.0.0.1",12712);
		//�������ĵ�ַ�����ƣ�����������ʱ��ǵø�����
	}
	public static void wlog(String msg) {//����system.out,д��־�õ� 
		SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		System.out.println("["+formatter.format(date)+"]"+msg);
	}
}
