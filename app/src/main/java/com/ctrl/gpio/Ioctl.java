package com.ctrl.gpio;

import android.util.Log;

public class Ioctl {
//	eDISP_PWR	= 11,
//	eFINGER_PWR=12,
//	eUSBKEY_PWR=13,
//	eMAGCARD_PWR=14,
//	eLDTONG_PWR=15,
//	eQX_PWR=16,
//	eRFID_PWR=17,
//	eSCAN_PWR=18,
//	eSCAN_TRIG=19,
//	ePRINT_PWR=20,
//	eBEEP=21,
//	eLAISER=22,
//	eFLASH=23,
//* new port   ttyS1, RFID, 1D, 2D, MSR, ID, 
//* 			  ttyS2: RS2322
//			  ttyS3, FINGER, PRINTER, LED, IDREADER,   
	
	 static  
	    {  
		 try{
	        System.loadLibrary("ctrl_gpio");  
	    }catch(UnsatisfiedLinkError ule){
	    	System.err.println("WARNING: Could not load library!"); 
	    	Log.i("info", "error ===  "+ule.getMessage().toString());
	    }
	    }
	 //Replace comment
	    public native static int convertRfid();
	    public native static int convertScanner();
	    public native static int convertLed();
	    public native static int convertMagcard();
	    public native static int convertFinger();
	    public native static int convertPrinter();
	    public native static int convertIdReader();
	    public native static int convertPSAM();
	    public native static int convertRS232_1();
	    public native static int convertRS232_2();
	    
	//�?�模�?�上电函数
	    public native static int activate(int type,int open);//�?�数：上电或下电IO�?�
	    public native static int get_status(int type);//攻�?�IO状�?(0为下电，1为上电，负值为�?正常)
	    
}
