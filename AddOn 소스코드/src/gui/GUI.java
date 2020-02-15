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
	final static int RE = 4; // Robot�� East�� ����.
	final static int RW = 5; // Robot�� West�� ����.
	final static int RS = 6; // Robot�� South�� ����.
	final static int RN = 7; // Robot�� North�� ����.

// �̹��� �ϳ��� ũ��
	final static int IMG_X = 60;
	final static int IMG_Y = 60;

// GUI�� ǥ�õ� ��ġ
	final static int FRAME_X = 470;
	final static int FRAME_Y = 100;

// ��������
	int[][] rfield;
	int xframe;
	int yframe;

	ArrayList<int[][]> movePicture; // �κ��� �����ӵ��� ��� ����List

	GuiMap p1;
	private JFrame frame = new JFrame();
	Container contentPane = frame.getContentPane();

// �ʱ� ���� ����
	public GUI(ArrayList<int[][]> movePicture) {
		this.movePicture = movePicture;
		rfield = this.movePicture.get(0);
		xframe = rfield.length;
		yframe = rfield[0].length;

		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		contentPane.setLayout(null);

// �гμ���
		p1 = new GuiMap(rfield);
		p1.setLayout(null);
		contentPane.add(p1);
		p1.setBounds(20, 20, yframe * 60, xframe * 60);

// frame ����
		frame.pack();
		frame.setTitle("MAP");
		frame.setBounds(FRAME_X, FRAME_Y, yframe * 60 + 65, xframe * 60 + 73);
		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
	}

// GUI����
	public void start() {
		for (int i = 1; i < movePicture.size(); i++) {
			try {
				p1.field = movePicture.get(i);// �κ��� ������ ������ ������ �ϳ��� �޾ƿ´�.
				p1.repaint();// ������ ǥ���Ѵ�.
				Thread.sleep(650); // 1�ʸ��� ����
			} catch (Exception e) {
			}
		}

	}

}

//���� �׸��� Ŭ����
class GuiMap extends JPanel {
	final static int H = -1; // Hazard(-1)
	final static int DH = -2;// DetectedHazard(-2)
	final static int UC = 1; // UnvisitedColorBlob(1)
	final static int C = 2; // ColorBlob(2)
	final static int G = 3; // Spot(3)
	final static int VG = 8;// VisitedSpot(8)

// Robot direction
	final static int RE = 4; // Robot�� East�� ����.
	final static int RW = 5; // Robot�� West�� ����.
	final static int RS = 6; // Robot�� South�� ����.
	final static int RN = 7; // Robot�� North�� ����.

// �̹��� �ϳ��� ũ��
	final static int IMG_X = 60;
	final static int IMG_Y = 60;

	int[][] field;
	int xframe;
	int yframe;

// ���� �����鿡 ���� �̹����� �޾ƿ´�.
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

// ������۸� ����
	Image img_buffer = null;
	Graphics buffer = null;

// @������
	public GuiMap(int[][] mfield) {
		xframe = mfield.length;
		yframe = mfield[0].length;
		field = new int[xframe][yframe];
		for (int x = 0; x < xframe; x++)
			for (int y = 0; y < yframe; y++)
				this.field[x][y] = mfield[x][y];
	}

// ������ �ش�Ǵ� ������ �´� �̹����� �����Ͽ� ������ �׷�����.
	public void paint(Graphics g) {

		img_buffer = createImage(yframe * 60, xframe * 60);
		buffer = img_buffer.getGraphics();

//buffer�� �̹����� ���� �׷� ���´�.
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

//���ۿ� �׷��� �̹����� �гο� �׷� ����.
		g.drawImage(img_buffer, 0, 0, this);

	}

}
