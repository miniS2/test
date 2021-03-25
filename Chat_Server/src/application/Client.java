package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//한 명의 클라이언트와 통신하도록 해주는 클라이언트 클래스입니다.
public class Client {
	
	Socket socket;
	
	//생성자
	public Client(Socket socket)
	{
		this.socket = socket;
		receive();
	}
	
	// 반복적으로 클라이언트로부터 메세지를 받는 메소드 입니다.
	public void receive()
	{
		Runnable thread = new Runnable() {
			@Override
			public void run()
			{
				try {
					while(true)
					{
						InputStream in = socket.getInputStream();
						
						byte[] buffer = new byte[512];
						
						int length = in.read(buffer);
						
						//예외처리    
						if(length == -1) throw new IOException();
						System.out.println("[메세지 수신 성공]" +
						socket.getRemoteSocketAddress()
						+ " : " + Thread.currentThread().getName());
						
						String message = new String(buffer, 0, length, "UTF-8");
						for(Client client : Main.clients)
						{
							client.send(message);
						}
					}
				}catch (Exception e)
				{
					try
					{
						System.out.println("[메세지 수신 오류]" +
								socket.getRemoteSocketAddress()
								+ " : " + Thread.currentThread().getName());
						Main.clients.remove(Client.this);
						socket.close();
					}catch(Exception e2)
					{
						e2.printStackTrace();
					}
				}
			}
		};
		Main.threadpool.submit(thread);
	}
	
	public void send(String message)
	{
		Runnable thread = new Runnable() {
			@Override
			public void run()
			{
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer =message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
					
				}catch(Exception e)
				{
					try {
						System.out.println("[메세지 수신 오류]" +
								socket.getRemoteSocketAddress()
								+ " : " + Thread.currentThread().getName());
						Main.clients.remove(Client.this);
						socket.close();
					}catch(Exception e2)
					{
						e2.printStackTrace();
					}
				}
			}
		};
		Main.threadpool.submit(thread);
	}
}
