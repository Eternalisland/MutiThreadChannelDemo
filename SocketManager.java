package com.ergs;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.ergs.data.Volidate;

public class SocketManager {

	public static byte[] open_door = new byte[] { (byte) 0x80, 0x00, 0x02, 0x45, 0x01, (byte) 0xF7, 0x37 };
	public static byte[] close_door = new byte[] { (byte) 0x80, 0x00, 0x02, 0x45, 0x00, 0x7E, 0x26 };
	public static byte[] volidate_info = new byte[] { (byte) 0x80, 0x00, 0x01, 0x48, (byte) 0xB2, (byte) 0x9F };

	private Selector selector;
	int count = 0;
	StringBuffer sb = null;

	Object obj = new Object();
	
	private boolean flag =  true;

	/**
	 * 
	 * @param ip
	 * @param port
	 * @throws IOException
	 */
	public void initClient(String ip, int port) {
		SocketChannel channel;
		try {
			channel = SocketChannel.open();
			channel.configureBlocking(false);
			this.selector = Selector.open();
			
			channel.connect(new InetSocketAddress(ip, port));
			channel.register(selector, SelectionKey.OP_CONNECT);
			
			sb = new StringBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public void listen() {
		while (true) {
			try {
				selector.select();
				Iterator ite = this.selector.selectedKeys().iterator();
				while (ite.hasNext()) {
					SelectionKey key = (SelectionKey) ite.next();
					ite.remove();
					
					if (key.isConnectable()) {
						SocketChannel channel = (SocketChannel) key.channel();
						if (channel.isConnectionPending()) {
							channel.finishConnect();
							
						}
						channel.configureBlocking(false);
						
						new Thread(new SocketHeartThread(channel)).start();
						channel.register(this.selector, SelectionKey.OP_READ);
						
					} else if (key.isReadable()) {
						SocketChannel channel = (SocketChannel) key.channel();
						String readResult = read(key);
						System.out.println("------>"+readResult);
						if (readResult.equals("000B")) {
							String identy = readIdentity(key);
							System.out.println(identy);
							String message = identy.substring(10, identy.length());
							flag = false;
							new Thread(new OpenDoorThread(channel, message)).start();
							sb.delete(0, sb.length());
						}else if(readResult.equals("0002")){
							System.out.println(readReturnInfo(key));
							sb.delete(0, sb.length());
						}
					}
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	class SocketHeartThread implements Runnable{
		SocketChannel channel; 
		public SocketHeartThread(SocketChannel channel){
			
			this.channel = channel;
		}
		public void run() {
			// TODO Auto-generated method stub
			try {
				while (true) {
					synchronized (obj) {
						
						if(!flag){
							System.out.println("你已经把我锁住了！"+System.currentTimeMillis());
							obj.wait(10000);
						}
					}
					channel.write(ByteBuffer.wrap(volidate_info));
					Thread.sleep(500);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class OpenDoorThread implements Runnable{

		String message;
		SocketChannel channel; 
		public OpenDoorThread(SocketChannel channel ,String message){
			this.message = message;
			this.channel = channel;
		}
		
		@Override
		public void run() {
			try {
				if(Volidate.VLOIDATE_SUCCESS.equals(Volidate.volidate(message))){
					synchronized (obj) {
						channel.write(ByteBuffer.wrap(open_door));
						Thread.sleep(10000);
						channel.write(ByteBuffer.wrap(close_door));
						obj.notifyAll();
						System.out.println("解锁吧！----》"+System.currentTimeMillis());
						flag = true;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	private String readReturnInfo(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(2);
			channel.read(buffer);
			byte[] data = buffer.array();
			for (byte b : data) {
				String aa = Integer.toHexString(b & 0xFF).toUpperCase();
				if (aa.length() == 1) {
					aa = "0" + aa;
				}
				sb.append(aa);

			}
			
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return sb.toString();
	}

	private String readIdentity(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(11);
			channel.read(buffer);
			byte[] data = buffer.array();

			for (byte b : data) {
				String aa = Integer.toHexString(b & 0xFF).toUpperCase();
				if (aa.length() == 1) {
					aa = "0" + aa;
				}
				sb.append(aa);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();

	}

	/**
	 * 
	 * @param key
	 * @throws IOException
	 */
	public String read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(2);
		channel.read(buffer);
		byte[] data = buffer.array();
		
			if(sb.length()>0){
				sb.delete(0, sb.length());
			}
		for (byte b : data) {
			String aa = Integer.toHexString(b & 0xFF).toUpperCase();
			if (aa.length() == 1) {
				aa = "0" + aa;
			}
			sb.append(aa);
//			count++;
		}
		return sb.toString();

	}

	/**
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SocketManager client = new SocketManager();
		client.initClient("192.168.0.87", 5000);
		client.listen();
	}

}
