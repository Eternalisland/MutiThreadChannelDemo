package com.ergs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.NetworkChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ergs.data.Volidate;

public class DoorManagerMent {

	public static byte[] open_door = new byte[] { (byte) 0x80, 0x00, 0x02, 0x45, 0x01, (byte) 0xF7, 0x37 };
	public static byte[] close_door = new byte[] { (byte) 0x80, 0x00, 0x02, 0x45, 0x00, 0x7E, 0x26 };
	public static byte[] volidate_info = new byte[] { (byte) 0x80, 0x00, 0x01, 0x48, (byte) 0xB2, (byte) 0x9F };
	// static byte a= (byte) 0xFA;
	// System.out.println(Integer.toHexString(a & 0xFF).toUpperCase());
	// System.out.println(Byte.toString(a));
	Socket ss = null;

	public static void main(String[] args){
		System.out.println(Volidate.volidate("11761001C3B68825"));

	}

	public void start() throws UnknownHostException, IOException {
//		ss = new Socket("192.168.0.87", 5000);
//		ss.setSoTimeout(1000);
//		ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(2);
//		threadPool.scheduleAtFixedRate(call, (long)0.5, (long) 0.5, TimeUnit.SECONDS);
		
	}

	public void connect() throws Exception {
		Socket s = new Socket("192.168.0.87", 5000);
		s.setSoTimeout(5000);
		OutputStream os = s.getOutputStream();
		InputStream is = s.getInputStream();
		os.write(volidate_info, 0, volidate_info.length);
		os.flush();
		s.setTcpNoDelay(true);
	      byte[] b = new byte[1];
	      String bb="";
	      while(is.read(b, 0, 1) !=-1){
	    	 String aa = Integer.toHexString(b[0] & 0xFF);
	    	 if(aa.length()==1){
	    		 aa = "0"+aa;
	    	 }
	    	 bb +=aa;
	    	  System.out.print(aa);
	      }
	      System.out.println(bb);

	}

	Runnable call = new Runnable() {

		

		@Override
		public void run() {
          System.out.println("Runnable calls");
//			if (ss != null) {
//				try {
//					is = ss.getInputStream();
//					os = ss.getOutputStream();
//
//					os.write(volidate_info, 0, volidate_info.length);
//					os.flush();
//					while (is != null) {
//						String pid = "";
//						byte[] b = new byte[1];
//						while (is.read(b, 0, 1) != -1) {
//							String aa = Integer.toHexString(b[0] & 0xFF);
//							if (aa.length() == 1) {
//								aa = "0" + aa;
//							}
//
//							pid += aa;
//						}
//
//						System.out.println(pid);
//						String result = Volidate.volidate(pid);
//						if (result.equals(Volidate.VLOIDATE_SUCCESS)) {
//							os.write(open_door, 0, open_door.length);
//							os.flush();
//							try {
//								Thread.sleep(3000);
//								os.write(close_door, 0, close_door.length);
//								os.flush();
//
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					try {
//						start();
//					} catch (UnknownHostException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				}
//
//			} else {
//				try {
//					start();
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
		}
	};
	
	public void read(){
		if(ss != null){
		try {
			
			OutputStream os = ss.getOutputStream();
			InputStream is = ss.getInputStream();
			os.write(volidate_info, 0, volidate_info.length);
			os.flush();
			
			  byte[] b = new byte[1];
			  String bb = "";
			  while(is.read(b, 0, 1) !=-1){
				 String aa = Integer.toHexString(b[0] & 0xFF);
				 if(aa.length()==1){
					 aa = "0"+aa;
				 }
				 bb +=aa;
				  
				  if(bb.length() == 8){
					  bb = "";
					  continue;
					
					  
				  }
				  if(bb.length() == 26){
					  break;
				  }
				  
			  }
			 System.out.println(bb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	}
}

	}
	
