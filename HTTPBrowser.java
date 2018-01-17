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
	public static String B_DIR = "C:/Users/swc-209/Desktop/b";
	
	static public String[] requests = {					//A group of requests,just for test.
			"GET /user.html HTTP/1.1",
			"GET /ddemo.jpg HTTP/1.1",
			"GET /demo.png HTTP/1.1",
			"GET /ddemo.nothing HTTP/1.1",
			"GET /demo.html HTTP/1.1"
	};
	static public String serverAds = "127.0.0.1";		//server's IP address,just for test.
	
	public static void main(String[] args) {
		for(int i=0;i<requests.length;i++){				//this for loop,just for test.
			debug.println("request : "+requests[i]+"\r\nreturn : "+request(serverAds,requests[i]));
														//Call request function and print the message.
		}
	}
	public static int request(String host,String request){
														//the function which can do request.
		int res = 600; String retMess;
		try{
			String fileName = request.split(" ")[1].replaceAll("/","_");
														//filename after dumping.
			Socket bro = new Socket(host,serverPort);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(bro.getInputStream()));
			PrintWriter writer = new PrintWriter(
					new OutputStreamWriter(bro.getOutputStream()));
			BufferedInputStream is = new BufferedInputStream(
					bro.getInputStream());
			writer.println(request); writer.flush();
			retMess = reader.readLine();
			res = Integer.valueOf(retMess.split(" ")[1]).intValue();
			while(!(retMess=reader.readLine()).equals(""));
			byte[] bb = new byte[1024];
			if(res == 200){								//Only when return 200 should we down the file.
				File f = new File(B_DIR+"/"+fileName);
				BufferedOutputStream os = new BufferedOutputStream(
						new FileOutputStream(f));
				for(int len=is.read(bb);len!=-1;len=is.read(bb))
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
