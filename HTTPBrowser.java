import java.io.*;
import java.net.*;
/*
 * class HTTPBrowser
 * 
 * It is very simple,just like the ShiYanYi.
 * 
 */
public class HTTPBrowser {
	static public PrintStream debug = System.out;
	static public int serverPort = 8888;
	public static String B_DIR = "C:/Users/Administrator/indix/HTTPBrowserWorkspace";
	
	static public String[] requests = {					//A group of requests,just for test.
			"/user.html keep-alive",
			"/ddemo.jpg close",
			"/demo.png keep-alive",
			"/ddemo.nothing close",
			"/demo.html keep-alive"
	};
	static public String serverAds = "127.0.0.1";		//server's IP address,just for test.
	
	public static void main(String[] args) {
		for(int i=0;i<requests.length;i++){				//this for loop,just for test.
			debug.println("request : "+requests[i]+"\r\nreturn : "+request(serverAds,requests[i].split(" ")[0],requests[i].split(" ")[1]));
														//Call request function and print the message.
		}
	}
	public static int request(String host,String reqFileName,String connectMode){
														//the function which can do request.
		int res = 600,length = 0; String retMess;
		try{
			String locFileName = reqFileName.replaceAll("/","_");
														//filename after dumping.
			Socket bro = new Socket(host,serverPort);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(bro.getInputStream()));
			PrintWriter writer = new PrintWriter(
					new OutputStreamWriter(bro.getOutputStream()));
			BufferedInputStream is = new BufferedInputStream(
					bro.getInputStream());
			String[] requests = {"GET "+reqFileName+" HTTP/1.1","Connection:"+connectMode,""};
			writer.println(requests[0]); writer.flush();
			for(int i=1;i<requests.length;i++)writer.println(requests[i]);
			writer.flush();
			retMess = reader.readLine();
			res = Integer.valueOf(retMess.split(" ")[1]).intValue();
			while(!(retMess=reader.readLine()).equals("")){
				if(retMess.startsWith("Content-Length")){
					length = Integer.valueOf(retMess.split(":")[1]);
				}
			}
			byte[] bb = new byte[1024];
			if(res == 200){								//Only when return 200 should we down the file.
				File f = new File(B_DIR+"/"+locFileName);
				BufferedOutputStream os = new BufferedOutputStream(
						new FileOutputStream(f));
				int len = 0;
				for(int i=0;i<length/1024;i++){
					len = is.read(bb);
					os.write(bb,0,len);
				}
				bb = new byte[length%1024];
				len = is.read(bb);
				os.write(bb,0,len);
				os.close();
			}
			reader.close(); writer.close(); is.close();
			bro.close();
		} catch (ArrayIndexOutOfBoundsException e){}
		catch (IOException e) {}
		return res;
	}
}
