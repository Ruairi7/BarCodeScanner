package com.ar.printer_demo_58mm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.printer_demo_58mm.util.ConvertUtil;

import android.util.Log;
import android_serialport_api.SerialPort;

public class SerialPortTools {

    protected SerialPort mSerialPort;
    protected OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;

    public SerialPort getSerialPort(String port,int baudrate) throws SecurityException, IOException,
            InvalidParameterException {
        if (mSerialPort == null) {
            if ((port.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            mSerialPort = new SerialPort(new File(port), baudrate, 0);
        }
        return mSerialPort;
    }

    /**
     * @param port 端口
     * @param baudrate 波特率
     * */
    public SerialPortTools(String port,int baudrate)
    {
        try {
            mSerialPort = this.getSerialPort(port,baudrate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }

    void initp()
    {
        if (mOutputStream != null) {
            try {
                mOutputStream.write(new byte[]{0x1B,'@'});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReadThread extends Thread {
        public void run() {
            super.run();
            while(!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        System.out.println("接收到数据 大小: " + size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /** 关闭串口 */
    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    protected void destroy() {
        if (mReadThread != null)
            mReadThread.interrupt();
        this.closeSerialPort();
        mSerialPort = null;
    }

    // [s] 输出
    public void write(String msg)
    {
        try {
            if(allowToWrite())
            {
                if(msg == null)
                    msg = "";
                mOutputStream.write(msg.getBytes("unicode"));
                mOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void write_gb2312(String msg)
    {
        try {
            if(allowToWrite())
            {
                if(msg == null)
                    msg = "";
                mOutputStream.write(msg.getBytes("GB2312"));
                mOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // [s] 输出
    public void write_unicode(String msg)
    {
        try {
            if(allowToWrite())
            {
                if(msg == null)
                    msg = "";
                mOutputStream.write(msg.getBytes("unicode"));
                mOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // [s] 输出
    public void write_Unicode(String msg)
    {

        if(msg == null){
            msg = "";
        }

        try {
            String str = "مرحبا، صباح الخير";
//				mOutputStream.write
//(new byte[]{0x06,(byte) 0xF0,0x06,(byte) 0xF1,0x06,(byte) 0xF2,0x06,(byte) 0xF3,0x06,(byte) 0xF4,0x06,(byte) 0xF5,0x06,(byte) 0xF6,0x06,(byte) 0xF7,
//		0x06,(byte) 0xF8,0x06,(byte) 0xF9,0x06,(byte) 0xFA,0x06,(byte) 0xFB,0x06,(byte) 0xFC,0x06,(byte) 0xFD,0x06,(byte) 0xFE,0x06,(byte) 0xFF});
//				mOutputStream.flush();
            mOutputStream.write(msg.getBytes("GB2312"));
            Thread.sleep(50);
//				mOutputStream.write(str.getBytes("UTF-8"));
            mOutputStream.flush();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // [s] 输出
    public void write_Unicode(String msg,boolean test,String Persian)
    {
        try {

            if(msg == null){
                msg = "";
            }
            Log.i("info", "msg.length == "+msg.length());


//				if(msg.length()<=32){
            String[] str = new String[msg.length()];

            for(int i=0;i<msg.length();i++){
                str[i] = msg.substring(i, i+1);
            }
            if(Persian.equals("English")){
                write(JBInterface.SET_LEFT);
                Thread.sleep(10);
                for(int j=0;j<str.length;j++){
//						
                    byte[] a = {str[j].getBytes("unicode")[3],str[j].getBytes("unicode")[2]};

                    mOutputStream.write(a);
                    Thread.sleep(10);
                }
            }else{
                write(JBInterface.SET_RIGHT);
                Thread.sleep(10);

                for(int j=str.length-1;j>=0;j--){
//					
                    byte[] a = {str[j].getBytes("unicode")[3],str[j].getBytes("unicode")[2]};

                    mOutputStream.write(a);
                    Thread.sleep(10);
                }
//				Thread.sleep(300);
            }
            mOutputStream.flush();

//				}else{
////					
//				}
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //	public void write_Unicode(String msg, boolean test, String Persian) {
//		try {
//
//			if (msg == null) {
//				msg = "";
//			}
//			Log.i("info", "msg.length == " + msg.length());
//
//			// if(msg.length()<=32){
//
//			/**
//			 * 加入的代码
//			 */
//			ArrayList<String> arraystr = textArrayList(msg); // 加入的代码
//			ArrayList<String[]> arrayone = new ArrayList<String[]>(); // 加入的代码
//
//			/**
//			 * 加入的代码
//			 */
//			for (int i = 0; i < arraystr.size(); i++) {
//				String strs = arraystr.get(i);
//				String[] str = new String[strs.length()];
//				for (int j = 0; j < msg.length(); j++) {
//					str[j] = msg.substring(j, j + 1);
//				}
//				arrayone.add(str);
//
//			}
//
//			// for (int i = 0; i < msg.length(); i++) {
//			// str[i] = msg.substring(i, i + 1);
//			// }
//			if (Persian.equals("English")) {
//				write(JBInterface.SET_LEFT);
//				Thread.sleep(10);
//				// for (int j = 0; j < str.length; j++) {
//				// //
//				// byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2] };
//				//
//				// mOutputStream.write(a);
//				// Thread.sleep(5);
//				// }
//
//				/**
//				 * 加入的代码
//				 */
//				for (int i = 0; i < arrayone.size(); i++) {
//					String[] str = arrayone.get(i);
//					for (int j = 0; j < str.length; j++) {
//
//						byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2] };
//
//						mOutputStream.write(a);
//						Thread.sleep(5);
//					}
//				}
//			} else {
//				write(JBInterface.SET_RIGHT);
//				Thread.sleep(10);
//
////				for (int j = str.length - 1; j >= 0; j--) {
////					//
////					byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2] };
////
////					mOutputStream.write(a);
////					Thread.sleep(5);
////				}
//				
//				/**
//				 * 加入的代码
//				 */
//				for (int i = 0; i < arrayone.size(); i++) {
//					String[] str = arrayone.get(i);
//					for (int j = 0; j < str.length; j++) {
//
//						byte[] a = { str[j].getBytes("unicode")[3], str[j].getBytes("unicode")[2] };
//
//						mOutputStream.write(a);
//						Thread.sleep(5);
//					}
//				}
//				// Thread.sleep(300);
//			}
//			mOutputStream.flush();
//
//			// }else{
//			//
//			// }
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
    public ArrayList<String> textArrayList(String str) {

        System.out.println("字符长度：= " + str.length());
        int a = str.length() / 32;
        int b = str.length() % 32;
        System.out.println("a = " + a);
        System.out.println("b = " + b);

        String stra = str.substring(b, str.length());
        String strb = str.substring(0, b);
        ArrayList<String> cstr = new ArrayList<String>();
        if (a == 1 && str.length() > 32) {
            cstr.add(stra);
            cstr.add(strb);
            System.out.println("==1+1");
        } else if (a > 1) {
            for (int i = 0; i < a; i++) {
                String ca = stra.substring(0, 32);
                stra = stra.substring(32, stra.length());
                cstr.add(ca);
                System.out.println("大于1");
            }
            cstr.add(strb);
        } else {
            System.out.println("==1");
            cstr.add(str);
        }
        System.out.println("集合找度 = " + cstr.size());
        return cstr;
    }
    public  void ceshi(String msg){
        int k = msg.length()/32;
        int j = msg.length()%32;
        String[] str = new String[j];
        String[] str32 = new String[32];
        for(int i=0;i<j;i++){

            str[i] = msg.substring(k*32, k*32+1);

        }
        for(int a=k;a>0;a--){
            for(int i=32;i>0;i--){
                str32[i] = msg.substring(a*32, a*32+1);
            }
        }
    }
    private byte[] getBytes (char[] chars) {
        Charset cs = Charset.forName ("UTF-8");
        CharBuffer cb = CharBuffer.allocate (chars.length);
        cb.put (chars);
        cb.flip ();
        ByteBuffer bb = cs.encode (cb);

        return bb.array();

    }


    //判断是否为英文；
    private static boolean checkPwdChars(final String str){
        //先检查最后一位(提高效率)
        char tmp;
        int i=str.length()-1;
        for(;i>=0;i--){
            tmp=str.charAt(i);
            if(!(('0'<=tmp&&tmp<='9')
                    ||('a'<=tmp&&tmp<='z')
                    ||('A'<=tmp&&tmp<='Z'))){
                return false;
            }
        };
        return true;
    }
    //检测是否有纸
    public boolean getState(){
        try {
            if(allowToWrite())
            {

                mOutputStream.write(new byte[]{0x10,0x04,0x05});
                mOutputStream.flush(); // 1
            }
            Thread.sleep(50);

            int cout = mInputStream.available();
            byte[] buffer = new byte[cout];
            int size = mInputStream.read(buffer);
            if(buffer[0] == 0x00){
                return true;
            }else
                return false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }


    public static byte[] getByteArray(String hexString) {

        return new BigInteger(hexString,16).toByteArray();

    }

    public static String bytesToHexString123(byte[] src){

        StringBuilder stringBuilder = new StringBuilder("");

        if (src == null || src.length <= 0) {
            return null;
        }

        for (int i = 0; i < src.length; i++) {

            int v = src[i] & 0xFF;

            String hv = Integer.toHexString(v);

            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);

        }
        return stringBuilder.toString();
    }
    /**
     * 输出
     * */
    public void write(byte[] b)
    {
        try {
            if(allowToWrite())
            {
                if(b == null)
                    return;
                mOutputStream.write(b);
                mOutputStream.flush(); // 1
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出
     * */
    public void write(int oneByte)
    {
        try {
            if(allowToWrite())
            {
                mOutputStream.write(oneByte);
                mOutputStream.flush(); // 1
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否允许打印
     * */
    public boolean allowToWrite()
    {
        if (mOutputStream == null) {
            System.out.println("输出流为空! 不能打印! ");
            return false;
        }
        return true;
    }

    // [e]

    public void write_Unicode(String msg,String str)
    {
        try {
            if(allowToWrite())
            {
                if(msg == null)
                    msg = "";

                if(str.equals("中文（繁/简 体）")){
                    //对要打印的字符串逐个判断是否为中文、英文、符号、数字,逐个打印；
                    for(int i=0;i<msg.length();i++){
                        String s = msg.substring(i, i+1);
                        //若不为中文
                        if(!JBInterface.isChinese(s)){
                            byte[] bytes = s.getBytes();
                            byte[] writebytes = {0x00,bytes[0]};
                            mOutputStream.write(writebytes);
                        }else{
                            //若为中文
                            mOutputStream.write(JBInterface.getStringToHexBytes(s));
                        }
                    }
                }else if(str.equals("English")){
                    mOutputStream.write(printerENByte(msg));
                    mOutputStream.flush();
                    Thread.sleep(50);
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //打印英文重做byte[]
    public static byte[] printerENByte(String msg){
        byte[] b = msg.getBytes();
        byte[] writebytes = new byte[b.length *2];
        for(int i=0;i<b.length;i++){
            writebytes[i*2] = 0x00;
            writebytes[i*2+1] = msg.getBytes()[i];
        }
        return writebytes;
    }

    public static String transferString(String oldString) {
        StringBuffer newStringBuffer = new StringBuffer(oldString);

        int length = oldString.length();

        for (int i = 0; i < length / 2 + 1; i++) {
            char a = oldString.charAt(i);
            char b = oldString.charAt(length - i - 1);
            newStringBuffer.replace(i, i + 1, String.valueOf(b));
            newStringBuffer.replace(length - i - 1, length - i, String
                    .valueOf(a));
        }
        return new String(newStringBuffer);
    }
    public static void test1(String str){
        System.out.println("字符长度：= " + str.length());
        int a = str.length() / 32;
        int b = str.length() % 32;
        System.out.println("a = " + a);
        System.out.println("b = " + b);

        String stra = str.substring(b, str.length());
        String strb = str.substring(0, b);
        ArrayList<String> cstr = new ArrayList<String>();
        if (a == 1) {

        } else {
            for (int i = 0; i < a; i++) {
                String ca = stra.substring(0, 32);
                stra = stra.substring(32, stra.length());
                cstr.add(ca);
            }
        }

        System.out.println("集合找度 = " + cstr.size());
        if (a > 1) {
            for (int i = cstr.size(); i > 0; i--) {
                System.out.println(cstr.get(i - 1));
            }
            System.out.println(strb);
        } else if (a == 1 && str.length() > 32) {
            System.out.println(stra + "\n" + strb);
        } else {
            System.out.println("111 = " + str);
        }
    }


}
