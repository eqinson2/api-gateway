package com.cdjdgm.dip.util;

public class Result implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	/** 0-成功、1-失败、2-警告 */
	private int status;

	private String message = "";

	private Object data = null;
	
	/**
	 * 成功
	 */
	public static Result ok(){
		return new Result(0, "", null);
	}
	public static Result ok(String message){
		return new Result(0, message, null);
	}
	public static Result ok(String message, Object data){
		return new Result(0, message, data);
	}

	/**
	 * 错误
	 */
	public static Result error(){
		return new Result(1, "", null);
	}
	public static Result error(String message){
		return new Result(1, message, null);
	}
	public static Result error(String message, Object data){
		return new Result(1, message, data);
	}

	/**
	 * 警告
	 */
	public static Result warn(){
		return new Result(3, "", null);
	}
	public static Result warn(String message){
		return new Result(3, message, null);
	}
	public static Result warn(String message, Object data){
		return new Result(3, message, data);
	}

	private Result(int status, String message, Object data){
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@SuppressWarnings("unchecked")
	public <T> T data() {
		return (T)data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
