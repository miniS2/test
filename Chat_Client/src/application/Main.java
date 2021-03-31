package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
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
