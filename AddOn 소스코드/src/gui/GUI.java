package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GUI {
	// normal(0)
	final static int H = -1; // Hazard(-1)
	final static int DH = -2;// DetectedHazard(-2)
	final static int UC = 1; // UnvisitedColorBlob(1)
	final static int C = 2; // ColorBlob(2)
	final static int G = 3; // Spot(3)
	final static int VG = 8;// VisitedSpot(8)

// Robot direction
	final static int RE = 4; // Robot이 East를 본다.
	final static int RW = 5; // Robot이 West를 본다.
	final static int RS = 6; // Robot이 South를 본다.
	final static int RN = 7; // Robot이 North를 본다.

// 이미지 하나의 크기
	final static int IMG_X = 60;
	final static int IMG_Y = 60;

// GUI가 표시될 위치
	final static int FRAME_X = 470;
	final static int FRAME_Y = 100;

// 지도변수
	int[][] rfield;
	int xframe;
	int yframe;

	ArrayList<int[][]> movePicture; // 로봇의 움직임들이 담긴 지도List

	GuiMap p1;
	private JFrame frame = new JFrame();
	Container contentPane = frame.getContentPane();

// 초기 지도 설정
	public GUI(ArrayList<int[][]> movePicture) {
		this.movePicture = movePicture;
		rfield = this.movePicture.get(0);
		xframe = rfield.length;
		yframe = rfield[0].length;

		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		contentPane.setLayout(null);

// 패널설정
		p1 = new GuiMap(rfield);
		p1.setLayout(null);
		contentPane.add(p1);
		p1.setBounds(20, 20, yframe * 60, xframe * 60);

// frame 설정
		frame.pack();
		frame.setTitle("MAP");
		frame.setBounds(FRAME_X, FRAME_Y, yframe * 60 + 65, xframe * 60 + 73);
		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
	}

// GUI실행
	public void start() {
		for (int i = 1; i < movePicture.size(); i++) {
			try {
				p1.field = movePicture.get(i);// 로봇의 움직인 상태의 지도를 하나씩 받아온다.
				p1.repaint();// 지도를 표시한다.
				Thread.sleep(650); // 1초마다 갱신
			} catch (Exception e) {
			}
		}

	}

}

//지도 그리는 클래스
class GuiMap extends JPanel {
	final static int H = -1; // Hazard(-1)
	final static int DH = -2;// DetectedHazard(-2)
	final static int UC = 1; // UnvisitedColorBlob(1)
	final static int C = 2; // ColorBlob(2)
	final static int G = 3; // Spot(3)
	final static int VG = 8;// VisitedSpot(8)

// Robot direction
	final static int RE = 4; // Robot이 East를 본다.
	final static int RW = 5; // Robot이 West를 본다.
	final static int RS = 6; // Robot이 South를 본다.
	final static int RN = 7; // Robot이 North를 본다.

// 이미지 하나의 크기
	final static int IMG_X = 60;
	final static int IMG_Y = 60;

	int[][] field;
	int xframe;
	int yframe;

// 각각 지점들에 대한 이미지를 받아온다.
	Toolkit tool = getToolkit();
	Image imgNormal = tool.getImage("Field.gif");
	Image imgSpot = tool.getImage("Spot.gif");
	Image imgVisitedSpot = tool.getImage("VisitedSpot.gif");
	Image imgHazard = tool.getImage("Hazard.gif");
	Image imgDetectedHazard = tool.getImage("Hazard2.gif");
	Image imgUndefinedColorblob = tool.getImage("Uncolor.gif");
	Image imgColorblob = tool.getImage("Color.gif");
	Image imgRobotE = tool.getImage("East.gif");
	Image imgRobotW = tool.getImage("West.gif");
	Image imgRobotS = tool.getImage("South.gif");
	Image imgRobotN = tool.getImage("North.gif");

// 더블버퍼링 변수
	Image img_buffer = null;
	Graphics buffer = null;

// @생성자
	public GuiMap(int[][] mfield) {
		xframe = mfield.length;
		yframe = mfield[0].length;
		field = new int[xframe][yframe];
		for (int x = 0; x < xframe; x++)
			for (int y = 0; y < yframe; y++)
				this.field[x][y] = mfield[x][y];
	}

// 각각의 해당되는 지점에 맞는 이미지를 삽입하여 지도를 그려낸다.
	public void paint(Graphics g) {

		img_buffer = createImage(yframe * 60, xframe * 60);
		buffer = img_buffer.getGraphics();

//buffer에 이미지를 먼저 그려 놓는다.
		for (int x = 0; x < xframe; x++) {
			for (int y = 0; y < yframe; y++) {
				switch (field[x][y]) {
				case 0:
					buffer.drawImage(imgNormal, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case G:
					buffer.drawImage(imgSpot, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case VG:
					buffer.drawImage(imgVisitedSpot, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case H:
					buffer.drawImage(imgHazard, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case DH:
					buffer.drawImage(imgDetectedHazard, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case UC:
					buffer.drawImage(imgUndefinedColorblob, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case C:
					buffer.drawImage(imgColorblob, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case RE:
					buffer.drawImage(imgRobotE, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case RW:
					buffer.drawImage(imgRobotW, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case RS:
					buffer.drawImage(imgRobotS, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;
				case RN:
					buffer.drawImage(imgRobotN, y * IMG_Y, x * IMG_X, 60, 60, this);
					break;

				}
			}
		}

//버퍼에 그려진 이미지를 패널에 그려 낸다.
		g.drawImage(img_buffer, 0, 0, this);

	}

}
