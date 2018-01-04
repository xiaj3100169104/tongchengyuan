package cn.tongchengyuan.util;

/**
 * custom load key error exception
 * @author kunkka
 * date 2016/5/12
 */
public class LoadkeyException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6625022850211113921L;

	public LoadkeyException(){
		super();
	}
	
	public LoadkeyException(String message) {
		super(message);
	}

	
}
