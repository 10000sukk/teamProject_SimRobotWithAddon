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
	// 로봇이 이동해왔던 path들을 저장
	PathManager pathData = new PathManager();

	// 로봇의 현재 위치
	int x; // row
	int y; // col
	// robot's present direction
	direction robotdirection = direction.E;

	double unfoundhazardPercentage = 0.12;
	double unfoundcolorblobPercentage = 0.1;

	// 맵에는 없는데 sensor 수행해보니 미발견 위험지역 또는 중요지점 발견 확률
	boolean percentage(double p) {
		double number = Math.random();
		if (number <= p)
			return true;
		return false;
	}

	// 로봇의 direction을 보고 이동 수행
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
		//System.out.println("로봇이동:" + this.x + " , " + this.y);
	}

	// 방향 전환...시계방향으로 90도 회전
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
	 
	// 전방 한칸에 hazard sensor수행
	boolean getHazardSensor(MapManager map) {
		try {
			// 전방 1칸에 해당하는 점이 위험지역일 경우
			if (map.isMapDataHazard(assumeLocationX(), assumeLocationY()))
				return true;
			// 일정확률로 미발견 hazard일 경우
			else if (map.isMapDataNormal(assumeLocationX(), assumeLocationY()))
				return percentage(unfoundhazardPercentage);
		} catch (Exception e) {
			// hazard sensor를 수행할 수 없는 경우....array의 크기를 넘어설때
		}
		return false;
	}

	// colorblob sensor
	// 동쪽에 colorblob sensor수행
	boolean getColorBlobSensorEast(MapManager map) {
		try {
			// 현재위치의 동쪽 한칸이 colorblob일 경우
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
			// 현재위치의 서쪽 한칸이 colorblob일 경우
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
			// 현재위치의 남쪽 한칸이 colorblob일 경우
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
			// 현재위치의 북쪽 한칸이 colorblob일 경우
			if (map.isMapDataColorblob(this.x - 1, this.y))
				return true;
			else if (map.isMapDataUnfoundcolorblob(this.x - 1, this.y))
				return true;
		} catch (Exception e) {

		}
		return false;
	}

	// 방향으로 전진 했을때를 가정한 row...x 값
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

	// 방향으로 전진 했을때를 가정한 col...y 값
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

	// 센서(hazard, colorblob) 수행하고 지도를 수정
	// 센서가 true가 되면은 미확인 위험지역 발견한것 아니면 미확인 위험지역 발견 안한것
	

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
			// 미발견 hazard이다.
			if (map.isMapDataHazard(mysim.assumeLocationX(), mysim.assumeLocationY()) == false) {
				// 맵 수정해 주고....
				reviseMaptoHazard(map, mysim.assumeLocationX(), mysim.assumeLocationY());
				// 경로 재설정해주기
				unfoundhazard = true;
			}
		}
		if (mysim.getColorBlobSensorEast(map) == true) {
			// 동쪽에 미발견 colorblob이다.
			if (map.isMapDataColorblob(mysim.x, mysim.y + 1) == false) {
				// 맵 수정
				reviseMaptoColorBlob(map, mysim.x, mysim.y + 1);
				unfoundcolorblob = true;
			}
		}
		if (mysim.getColorBlobSensorWest(map) == true) {
			// 서쪽에 미발견 colorblob이다.
			if (map.isMapDataColorblob(mysim.x, mysim.y - 1) == false) {
				// 맵 수정
				reviseMaptoColorBlob(map, mysim.x, mysim.y - 1);
				unfoundcolorblob = true;
			}
		}
		if (mysim.getColorBlobSensorSouth(map) == true) {
			// 남쪽에 미발견 colorblob이다.
			if (map.isMapDataColorblob(mysim.x + 1, mysim.y) == false) {
				// 맵 수정
				reviseMaptoColorBlob(map, mysim.x + 1, mysim.y);
				unfoundcolorblob = true;
			}
		}
		if (mysim.getColorBlobSensorNorth(map) == true) {
			// 남쪽에 미발견 colorblob이다.
			if (map.isMapDataColorblob(mysim.x - 1, mysim.y) == false) {
				// 맵 수정
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
	   
	   
	   //맵 picture에서 현재 위치를 알아내서 반환
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
	   // 잘못된 이동에 대한 보상을 위해서 잘못된 이동인지를 검사하는 메서드
	   //false이면 잘못된 이동 ... true이면 옳은 이동
	   static boolean isImperfectMovement()
	   {
	      int[][] oldMap = movePicture.get(movePicture.size()-2);
	   
	      int[] oldPosition = getPositionSensorinPicture(oldMap);
	      
	      //position sensor를 사용해서 현재위치를 받아온다
	      int[] newPosition = mysim.getPositionSensor();
	      
	      int distance = Math.abs(newPosition[0]- oldPosition[0]) + Math.abs(newPosition[1] - oldPosition[1]);
	      if(distance != 1)
	      {
	         return false;
	      }
	      return true;
	   }

	// 미발견 위험지역 발견시 맵을 수정
	static void reviseMaptoHazard(MapManager map, int x, int y) {
		map.reviseMapDatatoHazard(x, y);
	}

	// 미발견 중요지점 발견시 맵을 수정
	static void reviseMaptoColorBlob(MapManager map, int x, int y) {
		map.reviseMapDatatoColorBlob(x, y);
	}

	/*
	static void printRobotMovement() {
		System.out.println("로봇 이동경로 프린트");
		mysim.pathData.printPath();
	}
	*/

	static void addmovePicture() {
		int[][] arr = myMapmanager.MapToMatrix();
		// 현재위치 동 = 4
		// 현재위치 서 = 5
		// 현재위치 남 = 6
		// 현재위치 북 = 7
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
	         // PathManager에 PathInfo의 ArrayList로 경로가 저장된다.
	         // spot ArrayList에 지금 목표로 할 predefinedspot을 저장한다.
	         ArrayList<Integer> spot = new ArrayList<Integer>();
	         // 만약 더이상 방문하지 않은 탐색지역이 없다면 firstspot메서드가 null을 반환하기에 NullPointerException 발생
	         spot = (ArrayList<Integer>) myMapmanager.firstspot(x, y).clone();
	         // firstspot의 반환값이 null이면?...즉,더이상 목표로 삼을 좌표가 없다면?
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
		// sim 객체에 처음 시작 지점을 현재 위치로 지정
		mysim.setx(myMapmanager.getstartX());
		mysim.sety(myMapmanager.getstartY());

		myMapmanager.MapToMatrix();

		addmovePicture();
		ArrayList<Integer> pathIndexZero = new ArrayList<Integer>();
		while (true) {

			//myMapmanager.printMap();
			// 경로를 만들고 만약 경로가 없거나 더이상 방문할 탐색지역이 남아있지 않다면 false 반환
			if (makePath() == false)
				break;
			// myPathmanager 인스턴스에 경로를 설정하고 저장한다.
			pathIndexZero = (ArrayList<Integer>) myPathmanager.pathIndexZero().clone();

			// 로봇을 90도 씩 회전 시켜서 방향 맞추기
			while (true) {

				if ((pathIndexZero.get(0) == mysim.assumeLocationX())
						&& (pathIndexZero.get(1) == mysim.assumeLocationY()))
					break;
				else {
					mysim.changeDirection();
					addmovePicture();
				}

			}

			// 센서 체크하기
	         int sensorNum = checkSensor(myMapmanager);
	         if (sensorNum == 1) {
	            //System.out.println("미확인 위험지역 발견");
	            addmovePicture();
	            continue;
	         } else if (sensorNum == 2) {
	            addmovePicture();
	            mysim.moveSIM(myMapmanager);
	            addmovePicture();
	            //잘못된 이동이 이루어졋는가 검사
	            if(isImperfectMovement() == false)
	               System.out.println("Imperfect Robot Movement occured");
	         } else if (sensorNum == 0) {
	            mysim.moveSIM(myMapmanager);
	            addmovePicture();
	            //잘못된 이동이 이루어졋는가 검사
	            if(isImperfectMovement() == false)
	               System.out.println("Imperfect Robot Movement occured");
	         }

		}
		// GUI구현
		GUI gui = new GUI(movePicture);
		
		gui.start();

		//printRobotMovement();

	}

}

