package server;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class ServerMain {
	static int MAXCLIENTNUMBER=65536;
	static int useport = 12712;
	String[] sendbuf=new String[MAXCLIENTNUMBER];
	static Map<Integer, Game> clientInviteCode=new HashMap<Integer, Game>();
	public static void main(String[] args) {
		wlog("service initialize");
		try {
			wlog("server using port "+useport+" and pid "+getProcessID());
			ServerSocket serverSocket =new ServerSocket(useport);
			Sharedbuf sb=new Sharedbuf();
			int clientcount=0;
			while(true) {
				clientcount+=1;
				Socket socket = serverSocket.accept();
				sb.newclient(socket,clientcount);
				wlog("Start receiving from client"+clientcount+".");
			}
			
		}catch (IOException e) {
			wlog("WARNING:Opps,something goes wrong. It's probably a connection issue\n"
					+ "Please provide the imformation below to the developer. Alan.Xue@crestwood.on.ca");
			e.printStackTrace();
			System.exit(1);
			}
	}
	
	
	static void msgprocess(String msg,int clientid,Sharedbuf shared) {
		String data=msg.substring(1);
		//要给客户端发东西，写入对应客户端的sharedbuf.str就ok了
		switch(msg.charAt(0)) {
			case 'c'://report hostname
				wlog("client"+clientid+" reported hostname "+data);
				//wlog("SHA-256:"+SHA(data, "SHA-256"));
				shared.hashedhostname=Math.abs(data.hashCode())%1000000;
				//wlog(shared.hashedhostname+"");
				break;
			case 't'://delete game
				clientInviteCode.remove(shared.hashedhostname);
				wlog("Client"+clientid+" delete room of join code "+shared.hashedhostname);
				break;
			case 'n'://newgame
				shared.game=new Game();
				clientInviteCode.put(shared.hashedhostname,shared.game);
				shared.order=0;
				shared.str+="i"+shared.hashedhostname;
				shared.game.hostbuf=shared;
				wlog("Client"+clientid+" create new game with join code "+shared.hashedhostname);
				break;
			case 'd'://drop-down chess
				try {
					int x=Integer.parseInt(data.substring(0,2))-1;
					int y=Integer.parseInt(data.substring(2,4))-1;
					if(shared.order==shared.game.stepcount%2&&shared.game.chessBoard[x][y]==0){
						//判断轮到没有,那个地方有没有子
						shared.game.stepcount++;
						shared.game.chessBoard[x][y]=shared.order+1;//1代表host黑子，2代表guest白子
						if(shared.game.stepcount%2==1) {//0代表host，1代表guest
							shared.game.guestbuf.str="d"+data;
							shared.game.hostbuf.str="dsuccess";
						}else{
							shared.game.hostbuf.str="d"+data;
							shared.game.guestbuf.str="dsuccess";
						}
						wlog("Client"+clientid+" made a step in game created by client"
								+shared.game.hostbuf.clientid);
					}else {
						shared.str="failed";
					}
				}catch(Exception e) {
					wlog("WARNING:drop-down falied beacause of "+e);
				}
				break;
			case 'j'://join game
				try{
					if(clientInviteCode.get(Integer.parseInt(data))!=null) {
						shared.game=clientInviteCode.get(Integer.parseInt(data));
						shared.order=1;
						shared.game.guestbuf=shared;
						wlog("Client"+clientid+" joined game created by client"+shared.game.hostbuf.clientid);
						shared.str="jjoined";
						shared.game.hostbuf.str="n";
					}else {
						shared.str="f";
					}
				}catch(Exception e) {
					shared.str="f";
				}
				break;
			case 'r'://gamereset
				break;
			default:
				wlog("WARNING:Client "+clientid+" unrecognized msg:"+msg);
		}
		return;
	}
	
	public static void wlog(String msg) {
		SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		System.out.println("["+formatter.format(date)+"]"+msg); 
		return;
	}
	
    public static final int getProcessID() {  //获取进程pid
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();  
    }
    
    private static String SHA(final String strText, final String strType)
    {
	    String strResult = null;
	    // 是否是有效字符串
	    if (strText != null && strText.length() > 0)
		    {
		    try{
			    // 创建加密对象 并傳入加密類型
			    MessageDigest messageDigest = MessageDigest.getInstance(strType);
			    // 传入要加密的字符串
			    messageDigest.update(strText.getBytes());
			    byte byteBuffer[] = messageDigest.digest();
			    StringBuffer strHexString = new StringBuffer();
			    for (int i = 0; i < byteBuffer.length; i++){
				    String hex = Integer.toHexString(0xff & byteBuffer[i]);
				    if (hex.length() == 1){
				    	strHexString.append('0');
				    }
				    strHexString.append(hex);
			    }
			    // 得到返回結果
			    strResult = strHexString.toString();
		    }
		    catch (NoSuchAlgorithmException e){
		    	e.printStackTrace();
		    }
	    }
	    return strResult;
    }
}
