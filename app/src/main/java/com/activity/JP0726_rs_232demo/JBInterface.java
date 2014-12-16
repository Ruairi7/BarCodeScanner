package com.activity.JP0726_rs_232demo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;



import android.util.Log;
import android_serialport_api.SerialPort;


public class JBInterface {
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	public boolean begin;
	private ReadThread mReadThread;
	
	private OnReadSerialPortDataListener onReadSerialPortDataListener;
	public void read(OnReadSerialPortDataListener _onReadSerialPortDataListener) {
		this.onReadSerialPortDataListener = _onReadSerialPortDataListener;
	}
	
	
	private SerialPort getSerialPort(String port, int baudrate)
			throws SecurityException, IOException, InvalidParameterException {
		if (mSerialPort == null) {
			if ((port.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}			
			mSerialPort = new SerialPort(new File(port), baudrate, 0);
		}
		return mSerialPort;
	}

	public JBInterface(String port, int baudrate) {
		try {
			mSerialPort = this.getSerialPort(port, baudrate);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
			
			begin = true;
			mReadThread = new ReadThread();
			mReadThread.start();

		} catch (SecurityException e) {
			System.err.println("You do not have read/write permission to the serial port.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("The serial port can not be opened for an unknown reason.");
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			System.err.println("Please configure your serial port first.");
			e.printStackTrace();
		}
	}
	
	private class ReadThread extends Thread {
		public void run() {		
			while (begin) {			
				try {
					Thread.sleep(50);
					doRead();		
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}							
			}				
		}
	}
	
	public void write(String msg)
	{
		try {
				if(msg == null)
					msg = "";
//				Log.i("info", "moutputStream == "+mOutputStream);
				mOutputStream.write(msg.getBytes());
				mOutputStream.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write16(byte[] bytes){
		String str;
		
			try {
				
				if(bytes == null){
					str = "";
					mOutputStream.write(str.getBytes());
					mOutputStream.flush();
				}else{
				mOutputStream.write(bytes);
				mOutputStream.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public static byte uniteBytes(byte src0, byte src1) {  
	    byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();  
	    _b0 = (byte)(_b0 << 4);  
	    byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();  
	    byte ret = (byte)(_b0 ^ _b1);  
	    return ret;  
	    }   
	  
	      /** 
	       * Replace comment
	       * "2B44EFD9" � byte[]{0x2B, 0x44, 0xEF, 0xD9}
	       * @param src String 
	       * @return byte[] 
	       */  
	      public static byte[] HexString2Bytes(String src){  
	        byte[] ret = new byte[src.length()/2];  
	        byte[] tmp = src.getBytes();  
	        for(int i=0; i< tmp.length/2; i++){  
	          ret[i] = uniteBytes(tmp[i*2], tmp[i*2+1]);  
	        }  
	        return ret;  
	      }     
	
	private void doRead() {
		int size;
		try {
			if (mInputStream == null){
				return;				
			}		
			int cout = mInputStream.available();
			byte[] buffer1 = new byte[cout];	
					
				cout = 0;
				buffer1 = null;
//				Thread.sleep(400);
				cout = mInputStream.available();
				buffer1 = new byte[cout];
			
			
			size = mInputStream.read(buffer1);	
			
			if (size > 0) {
				SerialPortData data = new SerialPortData(buffer1, size);
				buffer1 = null;
				if (onReadSerialPortDataListener != null) {
					onReadSerialPortDataListener.onReadSerialPortData(data);
				}
			}		
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
			
	}
	public interface OnReadSerialPortDataListener {
		public void onReadSerialPortData(SerialPortData serialPortData);
	}

	public class SerialPortData {
		private byte[] dataByte;
		private int size;

		public SerialPortData(byte[] _dataByte, int _size) {
			this.setDataByte(_dataByte);
			this.setSize(_size);
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public byte[] getDataByte() {
			return dataByte;
		}

		public void setDataByte(byte[] dataByte) {
			this.dataByte = dataByte;
		}
	}
	public void closePort(){
		begin = false;
//		Log.i("info", "ms ==== "+mSerialPort);
		if(mSerialPort != null){
		mSerialPort.close();
		mSerialPort = null;
		}
	}
}
