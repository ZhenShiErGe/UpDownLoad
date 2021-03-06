package dlut.xyz.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import dlut.xyz.common.Properties;
import dlut.xyz.common.SocketWrapper;
import dlut.xyz.common.Utils;
import dlut.xyz.exception.ServerException;

public class ServerMain {
   private static List<Worker> workers=new ArrayList<Worker>();
   public static void main(String [] args){
	   ServerSocket serverSocket=null;
	   try{
		      serverSocket=new ServerSocket(8888);
			  initPath();
			  Utils.println("端口8888已经打开，开启接受请求");
			   int index=1;
			   while(true){
				   Socket socket=serverSocket.accept();
				   workers.add(new Worker(new SocketWrapper(socket),"thread_" + index));
				   index++;
			   }
	   }catch(IOException e){
		   //Utils.println(e.getMessage());
		   e.printStackTrace();
	   }catch(ServerException e){
		   //Utils.println(e.getMessage());
		   e.printStackTrace();
	   }
	   finally{
		   try{
			   serverSocket.close();
			   interruptWorkers(workers);
		   }catch(IOException e){
			   //Utils.println("serverSocket关闭失败");
			   e.printStackTrace();
		   }
	   }
   }
  /**
   * 初始化上传文件路径
   */
	private static void initPath() throws ServerException{
		File dir=new File(Properties.FILE_SAVE_PATH);
		if(!dir.exists()){
			boolean result=dir.mkdirs();
			if(!result){
			    throw new ServerException("服务器无法初始化路径");
			}
		}
	}
	/**
	 * 中断worker线程
	 * @param workers
	 */
	private static void interruptWorkers(List<Worker> workers){
		for(Worker worker:workers){
			if(worker.isAlive())
				worker.interrupt();
		}
	}
}
