package control;

import gui.*;
import map.*;
import path.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

interface RobotMovement {
	void changeDirection();

	void moveSIM(MapManager map);
}

enum direction {
	E, S, W, N
}

class SIM implements RobotMovement {
	// �κ��� �̵��ؿԴ� path���� ����
	PathManager pathData = new PathManager();

	// �κ��� ���� ��ġ
	int x; // row
	int y; // col
	// robot's present direction
	direction robotdirection = direction.E;

	double unfoundhazardPercentage = 0.12;
	double unfoundcolorblobPercentage = 0.1;

	// �ʿ��� ���µ� sensor �����غ��� �̹߰� �������� �Ǵ� �߿����� �߰� Ȯ��
	boolean percentage(double p) {
		double number = Math.random();
		if (number <= p)
			return true;
		return false;
	}

	// �κ��� direction�� ���� �̵� ����
	public void moveSIM(MapManager map) {
		if (this.robotdirection == direction.E)
			this.y += 1;

		if (this.robotdirection == direction.W)
			this.y -= 1;

		if (this.robotdirection == direction.S)
			this.x += 1;

		if (this.robotdirection == direction.N)
			this.x -= 1;

		pathData.addPath(this.x, this.y);
		map.setvisit(this.x, this.y);
		//System.out.println("�κ��̵�:" + this.x + " , " + this.y);
	}

	// ���� ��ȯ...�ð�������� 90�� ȸ��
	public void changeDirection() {
		this.robotdirection = direction.values()[(robotdirection.ordinal() + 1) % 4];
	}

	 public int[] getPositionSensor()
	   {
	      int[] position = new int[2];
	      position[0] = this.x;
	      position[1] = this.y;
	      return position;
	   }
	 
	// ���� ��ĭ�� hazard sensor����
	boolean getHazardSensor(MapManager map) {
		try {
			// ���� 1ĭ�� �ش��ϴ� ���� ���������� ���
			if (map.isMapDataHazard(assumeLocationX(), assumeLocationY()))
				return true;
			// ����Ȯ���� �̹߰� hazard�� ���
			else if (map.isMapDataNormal(assumeLocationX(), assumeLocationY()))
				return percentage(unfoundhazardPercentage);
		} catch (Exception e) {
			// hazard sensor�� ������ �� ���� ���....array�� ũ�⸦ �Ѿ��
		}
		return false;
	}

	// colorblob sensor
	// ���ʿ� colorblob sensor����
	boolean getColorBlobSensorEast(MapManager map) {
		try {
			// ������ġ�� ���� ��ĭ�� colorblob�� ���
			if (map.isMapDataColorblob(this.x, this.y + 1))
				return true;
			else if (map.isMapDataUnfoundcolorblob(this.x, this.y + 1))
				return true;

		} catch (Exception e) {

		}
		return false;
	}

	//
	boolean getColorBlobSensorWest(MapManager map) {
		try {
			// ������ġ�� ���� ��ĭ�� colorblob�� ���
			if (map.isMapDataColorblob(this.x, this.y - 1))
				return true;
			else if (map.isMapDataUnfoundcolorblob(this.x, this.y - 1))
				return true;

		} catch (Exception e) {

		}
		return false;

	}

	boolean getColorBlobSensorSouth(MapManager map) {
		try {
			// ������ġ�� ���� ��ĭ�� colorblob�� ���
			if (map.isMapDataColorblob(this.x + 1, this.y))
				return true;
			else if (map.isMapDataUnfoundcolorblob(this.x + 1, this.y))
				return true;

		} catch (Exception e) {

		}
		return false;
	}

	boolean getColorBlobSensorNorth(MapManager map) {
		try {
			// ������ġ�� ���� ��ĭ�� colorblob�� ���
			if (map.isMapDataColorblob(this.x - 1, this.y))
				return true;
			else if (map.isMapDataUnfoundcolorblob(this.x - 1, this.y))
				return true;
		} catch (Exception e) {

		}
		return false;
	}

	// �������� ���� �������� ������ row...x ��
	int assumeLocationX() {
		if (this.robotdirection == direction.E)
			return this.x;
		else if (this.robotdirection == direction.N)
			return this.x - 1;
		else if (this.robotdirection == direction.S)
			return this.x + 1;
		else
			return this.x;
	}

	// �������� ���� �������� ������ col...y ��
	int assumeLocationY() {
		if (this.robotdirection == direction.E)
			return this.y + 1;
		else if (this.robotdirection == direction.N)
			return this.y;
		else if (this.robotdirection == direction.S)
			return this.y;
		else
			return this.y - 1;
	}

	void setx(int x) {
		this.x = x;
	}

	void sety(int y) {
		this.y = y;
	}

	// ����(hazard, colorblob) �����ϰ� ������ ����
	// ������ true�� �Ǹ��� ��Ȯ�� �������� �߰��Ѱ� �ƴϸ� ��Ȯ�� �������� �߰� ���Ѱ�
	

}

public class AddOn {
	static MapManager myMapmanager = new MapManager();
	static PathManager myPathmanager = new PathManager();
	static SIM mysim = new SIM();

	static ArrayList<int[][]> movePicture = new ArrayList<int[][]>();
	
	 static int checkSensor(MapManager map) {
		boolean unfoundhazard = false;
		boolean unfoundcolorblob = false;
		if (mysim.getHazardSensor(map) == true) {
			// �̹߰� hazard�̴�.
			if (map.isMapDataHazard(mysim.assumeLocationX(), mysim.assumeLocationY()) == false) {
				// �� ������ �ְ�....
				reviseMaptoHazard(map, mysim.assumeLocationX(), mysim.assumeLocationY());
				// ��� �缳�����ֱ�
				unfoundhazard = true;
			}
		}
		if (mysim.getColorBlobSensorEast(map) == true) {
			// ���ʿ� �̹߰� colorblob�̴�.
			if (map.isMapDataColorblob(mysim.x, mysim.y + 1) == false) {
				// �� ����
				reviseMaptoColorBlob(map, mysim.x, mysim.y + 1);
				unfoundcolorblob = true;
			}
		}
		if (mysim.getColorBlobSensorWest(map) == true) {
			// ���ʿ� �̹߰� colorblob�̴�.
			if (map.isMapDataColorblob(mysim.x, mysim.y - 1) == false) {
				// �� ����
				reviseMaptoColorBlob(map, mysim.x, mysim.y - 1);
				unfoundcolorblob = true;
			}
		}
		if (mysim.getColorBlobSensorSouth(map) == true) {
			// ���ʿ� �̹߰� colorblob�̴�.
			if (map.isMapDataColorblob(mysim.x + 1, mysim.y) == false) {
				// �� ����
				reviseMaptoColorBlob(map, mysim.x + 1, mysim.y);
				unfoundcolorblob = true;
			}
		}
		if (mysim.getColorBlobSensorNorth(map) == true) {
			// ���ʿ� �̹߰� colorblob�̴�.
			if (map.isMapDataColorblob(mysim.x - 1, mysim.y) == false) {
				// �� ����
				reviseMaptoColorBlob(map, mysim.x - 1, mysim.y);
				unfoundcolorblob = true;
			}
		}
		if (unfoundhazard == false && unfoundcolorblob == false)
			return 0;
		else if (unfoundhazard == false && unfoundcolorblob == true)
			return 2;
		else
			return 1;
	}
	   
	   
	   //�� picture���� ���� ��ġ�� �˾Ƴ��� ��ȯ
	   static int[] getPositionSensorinPicture(int[][] mapPicture)
	   {
	      int[] position = new int[2];
	      out:
	         for(int i=0;i<mapPicture.length;i++)
	         {
	            for(int j=0;j<mapPicture.length;j++)
	            {
	               if(mapPicture[i][j] == 4 || mapPicture[i][j] == 5 ||mapPicture[i][j] == 6 ||mapPicture[i][j] == 7 )
	               {
	                  position[0] = i;
	                  position[1] = j;
	                  break out;
	               }
	            }
	         }
	      return position;
	      
	   }
	   // �߸��� �̵��� ���� ������ ���ؼ� �߸��� �̵������� �˻��ϴ� �޼���
	   //false�̸� �߸��� �̵� ... true�̸� ���� �̵�
	   static boolean isImperfectMovement()
	   {
	      int[][] oldMap = movePicture.get(movePicture.size()-2);
	   
	      int[] oldPosition = getPositionSensorinPicture(oldMap);
	      
	      //position sensor�� ����ؼ� ������ġ�� �޾ƿ´�
	      int[] newPosition = mysim.getPositionSensor();
	      
	      int distance = Math.abs(newPosition[0]- oldPosition[0]) + Math.abs(newPosition[1] - oldPosition[1]);
	      if(distance != 1)
	      {
	         return false;
	      }
	      return true;
	   }

	// �̹߰� �������� �߽߰� ���� ����
	static void reviseMaptoHazard(MapManager map, int x, int y) {
		map.reviseMapDatatoHazard(x, y);
	}

	// �̹߰� �߿����� �߽߰� ���� ����
	static void reviseMaptoColorBlob(MapManager map, int x, int y) {
		map.reviseMapDatatoColorBlob(x, y);
	}

	/*
	static void printRobotMovement() {
		System.out.println("�κ� �̵���� ����Ʈ");
		mysim.pathData.printPath();
	}
	*/

	static void addmovePicture() {
		int[][] arr = myMapmanager.MapToMatrix();
		// ������ġ �� = 4
		// ������ġ �� = 5
		// ������ġ �� = 6
		// ������ġ �� = 7
		if (mysim.robotdirection == direction.E) {
			arr[mysim.x][mysim.y] = 4;
		} else if (mysim.robotdirection == direction.W) {
			arr[mysim.x][mysim.y] = 5;
		} else if (mysim.robotdirection == direction.S) {
			arr[mysim.x][mysim.y] = 6;
		} else if (mysim.robotdirection == direction.N) {
			arr[mysim.x][mysim.y] = 7;
		}

		movePicture.add(arr);
	}

	static boolean makePath() {
	      try {
	         int x = mysim.getPositionSensor()[0];
	         int y = mysim.getPositionSensor()[1];
	         // PathManager�� PathInfo�� ArrayList�� ��ΰ� ����ȴ�.
	         // spot ArrayList�� ���� ��ǥ�� �� predefinedspot�� �����Ѵ�.
	         ArrayList<Integer> spot = new ArrayList<Integer>();
	         // ���� ���̻� �湮���� ���� Ž�������� ���ٸ� firstspot�޼��尡 null�� ��ȯ�ϱ⿡ NullPointerException �߻�
	         spot = (ArrayList<Integer>) myMapmanager.firstspot(x, y).clone();
	         // firstspot�� ��ȯ���� null�̸�?...��,���̻� ��ǥ�� ���� ��ǥ�� ���ٸ�?
	         if (myPathmanager.setPath(myMapmanager, x, y, spot.get(0), spot.get(1)) == true)
	            return true;
	         else
	            return false;
	      } catch (NullPointerException e) {
	         return false;
	      }
	   }

	public static void main(String[] args) {

		myMapmanager.createMap();
		// sim ��ü�� ó�� ���� ������ ���� ��ġ�� ����
		mysim.setx(myMapmanager.getstartX());
		mysim.sety(myMapmanager.getstartY());

		myMapmanager.MapToMatrix();

		addmovePicture();
		ArrayList<Integer> pathIndexZero = new ArrayList<Integer>();
		while (true) {

			//myMapmanager.printMap();
			// ��θ� ����� ���� ��ΰ� ���ų� ���̻� �湮�� Ž�������� �������� �ʴٸ� false ��ȯ
			if (makePath() == false)
				break;
			// myPathmanager �ν��Ͻ��� ��θ� �����ϰ� �����Ѵ�.
			pathIndexZero = (ArrayList<Integer>) myPathmanager.pathIndexZero().clone();

			// �κ��� 90�� �� ȸ�� ���Ѽ� ���� ���߱�
			while (true) {

				if ((pathIndexZero.get(0) == mysim.assumeLocationX())
						&& (pathIndexZero.get(1) == mysim.assumeLocationY()))
					break;
				else {
					mysim.changeDirection();
					addmovePicture();
				}

			}

			// ���� üũ�ϱ�
	         int sensorNum = checkSensor(myMapmanager);
	         if (sensorNum == 1) {
	            //System.out.println("��Ȯ�� �������� �߰�");
	            addmovePicture();
	            continue;
	         } else if (sensorNum == 2) {
	            addmovePicture();
	            mysim.moveSIM(myMapmanager);
	            addmovePicture();
	            //�߸��� �̵��� �̷��°� �˻�
	            if(isImperfectMovement() == false)
	               System.out.println("Imperfect Robot Movement occured");
	         } else if (sensorNum == 0) {
	            mysim.moveSIM(myMapmanager);
	            addmovePicture();
	            //�߸��� �̵��� �̷��°� �˻�
	            if(isImperfectMovement() == false)
	               System.out.println("Imperfect Robot Movement occured");
	         }

		}
		// GUI����
		GUI gui = new GUI(movePicture);
		
		gui.start();

		//printRobotMovement();

	}

}

