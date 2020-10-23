
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.Pos;


public class PProjection extends Application {

	
	//Global Transformation Matrix
	double[][] transformMatrix = {
			{1, 0, 0, 0},
			{0, 1, 0, 0}, 
            {0, 0, 1, 0 },
            {0, 0, 0, 1}};
	
	//Global Container for lineData
	ArrayList<Double> lineData = new ArrayList<Double>();
	ArrayList<Double> outputData = new ArrayList<Double>();
	
	
	
	// GUI variables
	BorderPane root = new BorderPane();
	HBox btnControls = new HBox();
	VBox varControls = new VBox();
	Canvas canvas = new Canvas(1350, 850);
	Button loadBtn = new Button("Load Line Data");
	Button writeBtn = new Button("Write Line Data");
	Button applyBtn = new Button("Apply Transformations");
	Button drawBtn = new Button("Draw Lines");
	Button resetBtn = new Button("Reset Matrix");
	Button clearBtn = new Button("Clear");

	TextField cyTxt = new TextField("100");
	TextField cxTxt = new TextField("100");
	TextField czTxt = new TextField("100"); //New
	TextField angleTxt = new TextField("0.75");
	TextField sxTxt = new TextField("1.5");
	TextField syTxt = new TextField("1.5");
	TextField szTxt = new TextField("1.5"); //New
	TextField fileTxt = new TextField("input.txt");
	TextField numLinesTxt = new TextField("16");
	Label syLabel = new Label("Y-Scale Factor:");
	Label sxLabel = new Label("X-Scale Factor:");	
	Label szLabel = new Label("Z-Scale Factor:"); //New
	Label cyLabel = new Label("Y-Translation Factor:");
	Label cxLabel = new Label("X-Translation Factor:");
	Label czLabel = new Label("Z-Translation Factor:"); //New
	Label numLinesLabel = new Label("Number of Lines:");
	Label angleLabel = new Label("Angle of Rotation:");
	Button basicSBtn = new Button("Scale");
	Button scaleBtn = new Button("Scale");
	//Button basicRBtn = new Button("Basic Rotate");
	Button rotateXBtn = new Button("X-Rotate");
	Button rotateYBtn = new Button("Y-Rotate");
	Button rotateZBtn = new Button("Z-Rotate");
	Button translateBtn = new Button("Translate");

	


	public static void main(String[] args){
		launch(args);
	}

	@SuppressWarnings("restriction")
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//add panes to root
		root.setBottom(btnControls);
		root.setCenter(canvas);
		root.setLeft(varControls);
		root.setPadding(new Insets(15));
		varControls.setPadding(new Insets(10));
		varControls.setPadding(new Insets(10));
		varControls.setSpacing(10);
		btnControls.setPadding(new Insets(10));
		btnControls.setAlignment(Pos.CENTER);
		

		// ControlPane Nodes
		btnControls.getChildren().add(new Label("Input File Name:"));
		btnControls.getChildren().add(fileTxt);
		btnControls.getChildren().add(loadBtn);
		btnControls.getChildren().add(applyBtn);
		btnControls.getChildren().add(drawBtn);
		btnControls.getChildren().add(resetBtn);
		btnControls.getChildren().add(clearBtn);
		//btnControls.getChildren().add(writeBtn);
		varControls.getChildren().addAll(cxLabel, cxTxt, cyLabel, cyTxt, czLabel, czTxt, sxLabel, sxTxt, syLabel, syTxt, szLabel, szTxt, angleLabel, angleTxt, numLinesLabel, numLinesTxt);
		varControls.getChildren().add(new Label("Basic Transformations:"));
		varControls.getChildren().addAll(scaleBtn, translateBtn, rotateXBtn, rotateYBtn, rotateZBtn);
		//varControls.getChildren().add(new Label("Standard Transformations:"));
		//varControls.getChildren().addAll(scaleBtn, rotateBtn);
		
		
		

		// Reset transformation matrix and lineData
		resetBtn.setOnAction(e -> {
			clearTransform();
			lineData = new ArrayList<Double>();
		});
		
		
		clearBtn.setOnAction(e -> {
			canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		});
	
		
		applyBtn.setOnAction(e -> {
			applyTransformation();
		});
		
		loadBtn.setOnAction(e -> {
			loadLineData("src/" + fileTxt.getText(), Integer.parseInt(numLinesTxt.getText()));
		});
		
		drawBtn.setOnAction(e -> {
			scanConvert();
			System.out.println(lineData);

		});
		
		basicSBtn.setOnAction(e -> {
			//basicScale(Double.parseDouble(sxTxt.getText()),Double.parseDouble(syTxt.getText()));
		});
		
		writeBtn.setOnAction(e -> {
			writeLineData();
		});
		
		translateBtn.setOnAction(e -> {
			System.out.println("Translating with factors (x,y,z) " + cxTxt.getText() + "," + cyTxt.getText() + "," + czTxt.getText());
			translate(Double.parseDouble(cxTxt.getText()),Double.parseDouble(cyTxt.getText()),Double.parseDouble(czTxt.getText()));
		});
		
		scaleBtn.setOnAction(e -> {
			scale(Double.parseDouble(sxTxt.getText()),Double.parseDouble(syTxt.getText()),Double.parseDouble(szTxt.getText()));
		});
		
		rotateXBtn.setOnAction(e -> {
			XRotate(Double.parseDouble(angleTxt.getText()));
		});
		rotateYBtn.setOnAction(e -> {
			YRotate(Double.parseDouble(angleTxt.getText()));
		});
		rotateZBtn.setOnAction(e -> {
			ZRotate(Double.parseDouble(angleTxt.getText()));
		});
		
//		rotateBtn.setOnAction(e -> {
//			//rotate(Double.parseDouble(angleTxt.getText()),Integer.parseInt(cxTxt.getText()),Integer.parseInt(cyTxt.getText()));
//		});
	
		
		
	
		// Initialize and Show Scene
		Scene scene = new Scene(root, 1450, 950);
		primaryStage.setTitle("Graphics Transformations");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();

	}	
	

	//Resets the Global Transformation Matrix to the Identity Matrix
	public void clearTransform() {
		transformMatrix[0][0] = 1;
		transformMatrix[0][1] = 0;
		transformMatrix[0][2] = 0;
		transformMatrix[0][3] = 0;
		
		transformMatrix[1][0] = 0;
		transformMatrix[1][1] = 1;
		transformMatrix[1][2] = 0;
		transformMatrix[1][3] = 0;

		transformMatrix[2][0] = 0;
		transformMatrix[2][1] = 0;
		transformMatrix[2][2] = 1;
		transformMatrix[2][3] = 0;
		
		transformMatrix[3][0] = 0;
		transformMatrix[3][1] = 0;
		transformMatrix[3][2] = 0;
		transformMatrix[3][3] = 1;
		}
	
	public void loadLineData(String filePath, int numLines) {
		//FileInputStream input;
		File input = new File(filePath);
		 BufferedReader br;
		  String st; 
		  try {
			  br = new BufferedReader(new FileReader(input));
			while ((st = br.readLine()) != null) {
				String[] points = st.split("\\s+");
				for(int i = 0; i < 6; i++) {
					lineData.add(Double.parseDouble(points[i]));
				}
				
			}
			  			  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  //Debug
		  System.out.println(lineData);
}
	
	//Apply the transformation matrix to points currently stored in the LineData matrix
	//TODO
	public void applyTransformation() {
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i = 0; i < lineData.size(); i+=3) {
			double[][] lineMatrix = {
					{lineData.get(i), lineData.get(i+1), lineData.get(i+2), 1}};
			lineMatrix = transformLine(lineMatrix);
			result.add(lineMatrix[0][0]);
			result.add(lineMatrix[0][1]);
			result.add(lineMatrix[0][2]);
		}
		lineData = result;
	}
	
	public void scanConvert(){
		outputData.clear();
		for(int i = 0; i < lineData.size(); i+=6) {
			//simpleLine(lineData.get(i),lineData.get(i+1),lineData.get(i+2),lineData.get(i+3));
			double[] xyA = PerspectiveProjection(lineData.get(i),lineData.get(i+1),lineData.get(i+2));
			double[] xyB = PerspectiveProjection(lineData.get(i+3),lineData.get(i+4),lineData.get(i+5));
			bhm_line((int)xyA[0],(int)xyA[1],(int)xyB[0],(int)xyB[1]);
			outputData.add(xyA[0]);
			outputData.add(xyA[1]);
			outputData.add(xyB[0]);
			outputData.add(xyB[1]);
			System.out.println("(" + xyA[0] + "," +  xyA[1] + ")" + "||" + "(" + xyB[0] + "," +  xyB[1] + ")");
		}
	}
	
	//Projects 3D coordinates to 2D coordinates
	public double[] PerspectiveProjection(double Xe, double Ye, double Ze) {
		double d = 2.5;
		double s = 50;
		double Vsy,Vsx,Vcy,Vcx;
		Vsy = Vsx = Vcy = Vcx = 100;
		double Xs = ((d*Xe)/(s*Ze))*(Vsx+Vcx);
		double Ys = ((d*Ye)/(s*Ze))*(Vsy+Vcy);
		double[] result = {Xs,Ys};
		return result;
	}
	
	public void writeLineData() {
	      try {
			FileWriter myWriter = new FileWriter("output.txt");
			for(int i = 0; i < outputData.size(); i+=6) {
			      myWriter.write(outputData.get(i) + " ");
			      myWriter.write(outputData.get(i+1) + " ");
			      myWriter.write(outputData.get(i+2) + " ");
			      myWriter.write(outputData.get(i+3) + " ");
			      myWriter.write(outputData.get(i+4) + " ");
			      myWriter.write(outputData.get(i+5) + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void translate(double Tx, double Ty, double Tz){
		double [][] translate = {
				{1, 0, 0, 0},
				{0, 1, 0, 0}, 
	            {0, 0, 1, 0},
				{Tx, Ty, Tz, 1}};

		applyToGlobalMatrix(translate);
	}
	
	public void scale(double Sx, double Sy, double Sz) {
		double[][] scale = {
				{Sx, 0, 0, 0},
				{0, Sy, 0, 0}, 
	            {0, 0, Sz, 0},
				{0, 0, 0, 1}};
		
		applyToGlobalMatrix(scale);
	}
	
	public void ZRotate(double theta){
		double[][] rotate = {
				{Math.cos(theta), Math.sin(theta), 0,0},
				{-Math.sin(theta), Math.cos(theta), 0,0}, 
				{0, 0, 1, 0},
				{0, 0, 0, 1}};
		
		applyToGlobalMatrix(rotate);
		
	}

	public void YRotate(double theta){
		double[][] rotate = {
				{Math.cos(theta), 0, -Math.sin(theta), 0},
				{0, 1, 0, 0},
				{Math.sin(theta), 0, Math.cos(theta), 0}, 
				{0, 0, 0, 1}};
		
		applyToGlobalMatrix(rotate);
		
	}
	
	public void XRotate(double theta){
		double[][] rotate = {
				{1,0,0,0},
				{0, Math.cos(theta), Math.sin(theta), 0},
				{0, -Math.sin(theta), Math.cos(theta), 0},
				{0, 0, 0, 1}};
		
		applyToGlobalMatrix(rotate);
		
	}


	
	public void rotate(double d, double Cx, double Cy, double Cz) {
//		basicTranslate(-Cx,-Cy, -Cz);
//		basicRotate(d);
//		basicTranslate(Cx,Cy, Cz);
	}
	
	//multiplies a lineMatrix by the global transformation
	//returns the result
	public double[][] transformLine(double[][] lineMatrix) {
		double[][] result = new double[1][4];
		for(int j = 0; j < 4; j++) {
				double sum = 0;
				sum+= lineMatrix[0][0] * transformMatrix[0][j]; 
				sum+= lineMatrix[0][1] * transformMatrix[1][j]; 
				sum+= lineMatrix[0][2] * transformMatrix[2][j];
				sum+= lineMatrix[0][3] * transformMatrix[3][j];
				result[0][j] = sum;
		}
		return result;
	}
	
	

	//multiplies the global transformation matrix by another transformation Matrix
	//sets the global matrix to the result
	public void applyToGlobalMatrix(int[][] otherTransform) {
		double[][] result = new double[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				double sum = 0;
				sum+= transformMatrix[i][0] * otherTransform[0][j]; 
				sum+= transformMatrix[i][1] * otherTransform[1][j]; 
				sum+= transformMatrix[i][2] * otherTransform[2][j];
				sum+= transformMatrix[i][3] * otherTransform[3][j];
				result[i][j] = sum;
			}
		}
		transformMatrix = result;
	}
	
	public void applyToGlobalMatrix(double[][] otherTransform) {
		double[][] result = new double[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				double sum = 0;
				sum+= transformMatrix[i][0] * otherTransform[0][j]; 
				sum+= transformMatrix[i][1] * otherTransform[1][j]; 
				sum+= transformMatrix[i][2] * otherTransform[2][j];
				sum+= transformMatrix[i][3] * otherTransform[3][j];
				result[i][j] = sum;
			}
		}
		transformMatrix = result;
	}
	
	
		// Activates a single pixel at (x,y)
		public void pixel(int x, int y) {
			canvas.getGraphicsContext2D().getPixelWriter().setColor(x, y, Color.BLACK);
		}
		
		void bhm_line(int x1, int y1, int x2, int y2) {
			int x, y, dx, dy, dx1, dy1, px, py, xe, ye, i;
			dx = x2 - x1;
			dy = y2 - y1;
			dx1 = Math.abs(dx);
			dy1 = Math.abs(dy);
			px = 2 * dy1 - dx1;
			py = 2 * dx1 - dy1;
			if (dy1 <= dx1) {
				if (dx >= 0) {
					x = x1;
					y = y1;
					xe = x2;
				} else {
					x = x2;
					y = y2;
					xe = x1;
				}
				pixel(x, y);
				for (i = 0; x < xe; i++) {
					x = x + 1;
					if (px < 0) {
						px = px + 2 * dy1;
					} else {
						if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) {
							y = y + 1;
						} else {
							y = y - 1;
						}
						px = px + 2 * (dy1 - dx1);
					}

					pixel(x, y);
				}
			} else {
				if (dy >= 0) {
					x = x1;
					y = y1;
					ye = y2;
				} else {
					x = x2;
					y = y2;
					ye = y1;
				}
				pixel(x, y);
				for (i = 0; y < ye; i++) {
					y = y + 1;
					if (py <= 0) {
						py = py + 2 * dx1;
					} else {
						if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) {
							x = x + 1;
						} else {
							x = x - 1;
						}
						py = py + 2 * (dx1 - dy1);
					}

					pixel(x, y);
				}
			}
		}
}
