package com.ruairi.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.ar.printer_demo_58mm.JBInterface;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


// use example code for Printer functions






public class MainActivity extends Activity implements OnClickListener
{
	
	private Button scanBtn;
    private Button printBtn;
	private TextView formatTxt, contentTxt;
	// to establish device status
	private int statc1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        scanBtn = (Button) findViewById(R.id.scan_button);
        printBtn = (Button) findViewById(R.id.print_button);
        formatTxt = (TextView) findViewById(R.id.scan_format);
        contentTxt = (TextView) findViewById(R.id.scan_content);
        
        scanBtn.setOnClickListener(this);
        printBtn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onClick(View v) {
		// Check if scanning button was pressed
		if(v.getId() == R.id.scan_button)
		{
			// Try to activate Laser
			// can't read with laser on
			/*if (statc1 == 0) {
				//start laser
				int a1 = Ioctl.activate(22, 1);
				System.out.println("a1 ==== " + a1);
				//laser.setText("CLOSE LAISER");
				statc1 = 1;
			} else {
				int a2 = Ioctl.activate(22, 0);
				System.out.println("a2 ==== " + a2);
				//laser.setText("OPEN LAISER");
				statc1 = 0;
			}*/ 
			//scan
			 IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			 scanIntegrator.initiateScan();
		}
        else if(v.getId() == R.id.print_button)
        {
            final String toPrint =
                    "              *****                  %n"
                            + "       W*** OF ** AND *****        %n"
                            + "      4/400 kfjkasjfdkas ajdksa        %n"
                            + "  aksdka ajke ajeklaje  kajke ka  a   %n"
                            + " Date: %n"
                            + "--------------------------------------%n"
                            + "    Name            Qty     Price    %n"
                            + "--------------------------------------%n";

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    synchronized (this){
                        try{

                            byte[] write = JBInterface.printerENByte(toPrint);
                            JBInterface.initPrinter();
                            JBInterface.resetPrint();
                            JBInterface.setBold();

                            JBInterface.printPhoto(write);

                            wait(5000);

                            JBInterface.closePrinter();


                        }catch (Exception e){

                        }
                    }
                }
            };
            Thread mythread = new Thread(runnable);
            mythread.start();

        }
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// retrieve scan result
		// Try to parse result
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		
		// validate result
		if(scanningResult != null)
		{
			// we have a result
			String scanContent = scanningResult.getContents();
			String scanFormat = scanningResult.getFormatName();
			formatTxt.setText("FORMAT: " + scanFormat);
			contentTxt.setText("CONTENT: " + scanContent);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"No scan data recieved!", Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}

}
