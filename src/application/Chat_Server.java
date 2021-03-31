package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Chat_Server implements Initializable {
	
	
	public static ExecutorService threadpool;
	public static Vector<Client> clients = new Vector<Client>();
	
	ServerSocket serverSocket;
	
	Client Server = new Client();

	@FXML
	private TextField textIP;
	@FXML
	private TextField textPort;
	@FXML
	private TextArea textArea;
	@FXML
	private TextField textStatus;
	@FXML
	private Button btnServerStart;
	@FXML
	private Button btnSend;
	@FXML
	private Button txtbtn;

	@FXML
	private Label LabelStatus1;

	@FXML
	private TextField textId;

	@FXML
	private TextField textPassword;

//    
	public void Login(ActionEvent event) throws Exception {
		if (textId.getText().equals("ezen") && textPassword.getText().equals("1234")) {
			LabelStatus1.setText("Login Success");
			Stage primaryStage2 = new Stage();
			Parent root2 = FXMLLoader.load(getClass().getResource("Chat_Server.fxml"));
			Scene scene = new Scene(root2);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage2.setScene(scene);
			primaryStage2.setTitle("채팅 프로그램");
			primaryStage2.show();
		} else {
			LabelStatus1.setText("Login Failed");
		}
	}

	public void btnStart(ActionEvent event) {
		String IP = "127.0.0.1";
		int Port = Integer.parseInt(textPort.getText());;

		if (btnServerStart.getText().equals("시작하기")) {
			startServer(IP, Port);
			Platform.runLater(() -> {
				String message = String.format("[서버 시작]\n", IP, Port);
				textArea.appendText(message);
				btnServerStart.setText("종료하기");

			});
		} else {
			stopServer();
			Platform.runLater(() -> {
				String message = String.format("[서버 종료]\n", IP, Port);
				textArea.appendText(message);
				btnServerStart.setText("시작하기");
			});
		}
	}

	public void StatusInput(ActionEvent event) {
		Server.send("운영자 : " + textStatus.getText() + "\n");
		textStatus.setText("");
		textStatus.requestFocus();
	}

	public void btnSendAction(ActionEvent event) {
		Server.send("운영자 : " + textStatus.getText() + "\n");
		textStatus.setText("");
		textStatus.requestFocus();
	}

	
	// 서버를 구동시켜 클라이언트의 연결을 기다리는 메소드 입니다.
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));
			System.out.println("서버 소켓 바인드");
		} catch (Exception e) {
			e.printStackTrace();
			if (!serverSocket.isClosed())
				stopServer();
			return;
		}

		Runnable thread = new Runnable()

		{
			@Override
			public void run() {
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						System.out.println("서버 소켓 열림");
						clients.add(new Client(socket));
						System.out.println("[클라이언트 접속]" + socket.getRemoteSocketAddress() + " : "
								+ Thread.currentThread().getName());
					} catch (Exception e) {
						if (!serverSocket.isClosed())
							stopServer();
						break;
					}
				}
			}
		};
		threadpool = Executors.newCachedThreadPool();
		threadpool.submit(thread);
	}

	// 서버의 작동을 중지시키는 메소드 입니다.
	public void stopServer() {
		try {
			// 현재 작동 중인 모든 소켓 닫기
			Iterator<Client> iterator = clients.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			// 서버 소켓 객체 닫기
			if (serverSocket != null && !serverSocket.isClosed())
				serverSocket.close();
			// 쓰레드 풀 종료하기
			if (threadpool != null && !threadpool.isShutdown())
				threadpool.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*
		 * 
		 * btnServerStart.setOnAction((AtionEvent)->{
		 * 
		 * System.out.println("출력");
		 * 
		 * });
		 */
		
		
	}
	@FXML
	private void actionCloseWindow(MouseEvent event) {
		stopServer();
		System.exit(0);
	}

}
