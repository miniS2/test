package application;
<<<<<<< HEAD


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends Application {
	//UI를 생성하고 실질적으로 프로그램을 작동시키는 메소드 입니다.

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		try {      
            Parent root = FXMLLoader.load(getClass().getResource("Server_Login.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("로그인");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
	
		
	}
	
	
	//프로그램 의 진입점입니다. 
	public static void main(String[] args) {
		launch(args);
	}
}



=======
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	//실제로 프로그램을 작동시키는 메소드 입니다.
	@Override
	public void start(Stage primaryStage) throws Exception {
	{
		try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("로그인");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	
}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
>>>>>>> refs/remotes/origin/master
