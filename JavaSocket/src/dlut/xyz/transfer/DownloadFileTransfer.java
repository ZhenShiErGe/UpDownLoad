package dlut.xyz.transfer;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import dlut.xyz.common.CharsetEnum;
import dlut.xyz.common.SocketWrapper;
import dlut.xyz.common.TransferTypeEnum;
import dlut.xyz.common.Utils;
import dlut.xyz.exception.FileCreateException;
import dlut.xyz.exception.FileNotExistsException;
import dlut.xyz.exception.ParamNotExistsException;
import dlut.xyz.exception.ServerException;

public class DownloadFileTransfer implements Transferable{
	
	private String remoteFileName;//要下载的文件名称
	private String fileDir;//下载的文件保存的路径
	public DownloadFileTransfer(String [] tokens) throws ParamNotExistsException{
			if(tokens.length>=3){
				remoteFileName=tokens[1];
				fileDir=tokens[2];
				if(!Utils.fileExists(fileDir)){
					File file=new File(fileDir);
					if(!file.mkdirs())
						throw new FileCreateException(fileDir);
				}
				
			}
			else{
				Utils.println("请在后面填写文件名称以及保存路径");
				throw new ParamNotExistsException();
			}
	}
	
	/**
	 * 返回传输类型
	 * @return
	 */
	@Override
	public TransferTypeEnum getTransferType(){
		return TransferTypeEnum.DOWNLOADFILE;
	}
	/**
	 * 进行传输
	 */
	@Override
	public void transfer(SocketWrapper socketWrapper) throws FileNotExistsException,IOException,UnsupportedEncodingException{
		Utils.println("我此时准备从服务器端下载文件");
		//通知服务器要下载的文件名称
		byte[] remoteFileNameBytes = this.remoteFileName.getBytes(CharsetEnum.UTF8.getCharsetName());
		socketWrapper.write(remoteFileNameBytes.length);
		socketWrapper.write(remoteFileNameBytes);
		int status=socketWrapper.readInt();
		if(status==1){
			//正常传输文件
			long fileLength=socketWrapper.readLong();
			socketWrapper.readToFile(new File(this.fileDir+this.remoteFileName),fileLength);
			int result=socketWrapper.readInt();
			if(result==1)
				Utils.println("下载完毕");
			else
				throw new ServerException("下载功能");
		}
		else if(status==-1){
			throw new FileNotExistsException(this.remoteFileName);
		}
		else if(status==-2){
			throw new ServerException(" 没有操作文件的权限");
		}
		else{
			throw new ServerException("下载功能");
		}
	}
}
