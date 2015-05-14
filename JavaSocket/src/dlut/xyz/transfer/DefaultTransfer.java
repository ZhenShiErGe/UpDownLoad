package dlut.xyz.transfer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import dlut.xyz.common.SocketWrapper;
import dlut.xyz.common.TransferTypeEnum;
import dlut.xyz.common.Utils;
import dlut.xyz.exception.ParamNotExistsException;

public class DefaultTransfer implements Transferable{
	private String message="\n发送消息使用message  上传文件使用uploadfile  下载文件使用downloadfile";
	public DefaultTransfer(String [] tokens) throws  ParamNotExistsException,UnsupportedEncodingException{
		Utils.println(message);
	}
	
	/**
	 * 返回传输类型
	 * @return
	 */
	@Override
	public TransferTypeEnum getTransferType(){
		return TransferTypeEnum.DEFAULT;
	}
	/**
	 * 进行传输
	 */
	@Override
	public void transfer(SocketWrapper socketWrapper) throws IOException{
	    
	}
}
