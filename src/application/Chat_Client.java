package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Chat_Client implements Initializable
{
	Socket socket;

	@FXML
	private TextField txtUserName;
	@FXML
	private TextField txtIP;
	@FXML
	private TextField txtPort;
	@FXML
	private TextArea txtArea;
	@FXML
	private TextField txtStatus;
	@FXML
	private Button btnAccept;
	@FXML
	private Button btnSend;
	
	@FXML
    private Label LabelStatus;
    
    @FXML
    private TextField txtId;
    
    @FXML
    private TextField txtPassword;
    
    
    public void Login(ActionEvent event) throws Exception{
        if(txtId.getText().equals("ezen") && txtPassword.getText().equals("1234"))
        {
        	LabelStatus.setText("Login Success");
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Chat_Client.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("채팅 프로그램");
            primaryStage.show();
        }else{
        	LabelStatus.setText("Login Failed");
        }
    }
    
	public void StatusInput(ActionEvent event)
	{
		send(txtUserName.getText() + " : " + txtStatus.getText() + "\n");
		txtStatus.setText("");
		txtStatus.requestFocus();
	}

	public void btnAcceptAction(ActionEvent event)
	{
			if(btnAccept.getText().equals("접속하기"))
			{
				int port = Integer.parseInt(txtPort.getText());
				try {
					port = Integer.parseInt(txtPort.getText());;
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
				startClient(txtIP.getText(),port);
				Platform.runLater(() -> {
					txtArea.appendText("[채팅방 접속] \n");
					
				});
				btnAccept.setText("종료하기");
				txtStatus.setDisable(false);
				btnSend.setDisable(false);
				txtStatus.requestFocus();
			}else {
				stopClient();
				Platform.runLater(() -> {
					txtArea.appendText("[채팅방 퇴장] \n");
					btnAccept.setText("접속하기");
					txtStatus.setDisable(true);
					btnSend.setDisable(true);
				});
	
			}
	}
	
	public void btnSendAction(ActionEvent event)
	{
		send(txtUserName.getText() + " : " + txtStatus.getText() + "\n");
		txtStatus.setText("");
		txtStatus.requestFocus();
		System.out.println("클라이언트 보내는 버튼 입니다.");
	}
	
	//클라이언트 프로그램의 작동을 시작하는 메소드 입니다.
	public void startClient(String IP, int port)
	{
		Thread thread = new Thread()
		{
			public void run()
			{
				try
				{
					socket = new Socket(IP, port);
					receive();
				}catch(Exception e2)
				{
					if(!socket.isClosed())
					{
						stopClient();
						System.out.println("[서버 접속 실패]");
						Platform.exit();
					}
				}
			}
		};
		thread.start();
	}
	
	//클라이언트 프로그램의 작동을 종료하는 메소드 입니다.
	public void stopClient()
	{
		try {
			if(socket != null && !socket.isClosed())
			{
				socket.close();	
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//서버로부터 메세지를 전달받는 메소드 입니다.
	public void receive()
	{
		while(true)
		{
			try {
					InputStream in = socket.getInputStream();
					byte[] buffer = new byte[512];
					
					int length = in.read(buffer);
					//예외처리    
					if(length == -1) throw new IOException();
					String message = new String(buffer, 0, length, "UTF-8");
					Platform.runLater(() -> {
						txtArea.appendText(message);			
				});
				}catch(Exception e)
				{
					stopClient();
					break;
				}
			}
	}
	
	//서버로부터 메시지를 전송하는 메소드 입니다.
	public void send(String message)
	{
		Thread thread = new Thread()
		{
			public void run()
			{
				try
				{
					OutputStream out = socket.getOutputStream();
					byte[] buffer =message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
				}catch(Exception e)
				{
					stopClient();
				}
			}
		};
		thread.start();
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private void actionCloseWindow(MouseEvent event) {
		stopClient();
		System.exit(0);
	}

}

