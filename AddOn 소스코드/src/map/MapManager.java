package map;

import java.util.ArrayList;
import java.util.Scanner;



public class MapManager {
   
   ArrayList<ArrayList<MapData>> mapArray = new ArrayList<ArrayList<MapData>>();
   int maxRow;
   int maxCol;
   
   int startX;
   int startY;
   //predefinedspot의 갯수
   int numberofpredefinedspot;
   //predefinedspot의 ArrayList
   ArrayList<Integer> spotX = new ArrayList<Integer>();
   ArrayList<Integer> spotY = new ArrayList<Integer>();
   
   //맨해튼 거리로 현재 지점에서 spot중에서 가장 가까운 spot구함
   public ArrayList<Integer> firstspot(int currentX, int currentY)
   {
      //만일 이미 방문한 지점은 spot ArrayList에서 제외 시킨다
      for(int i=0; i <this.numberofpredefinedspot;i++)
      {
         if(mapArray.get(spotX.get(i)).get(spotY.get(i)).getvisit() == true )
         {
            this.numberofpredefinedspot--;
            spotX.remove(i);
            spotY.remove(i);
         }
      }      
      //만일 아직 방문하지 않은 spot이 없다면 return
      if(this.numberofpredefinedspot == 0)
         return null;
      
      //ArrayList<Integer> 형식으로 반환할것이다... a.get(0)은 x좌표.. a.get(1)은  y좌표
      ArrayList<Integer> a = new ArrayList<Integer>();
      a.add(spotX.get(0));
      a.add(spotY.get(0));
      int min = Math.abs(a.get(0) - currentX) + Math.abs(a.get(1) - currentY);
      
      //현재 지점과 맨해튼 거리가 최소가 되는 spot고르기
      for(int i=0;i<this.numberofpredefinedspot; i++)
      {
         int temp =  Math.abs(spotX.get(i) - currentX) + Math.abs(spotY.get(i) - currentY);
         if(min > temp)
         {
            min = temp;
            //a list 교체
            a.set(0, spotX.get(i));
            a.set(1, spotY.get(i));
         }
      }
      
      return a;
   }
   
   /*
   //map의 정보를 print...내가 볼라고 만든거
   public void printMap()
   {
      for(int i=0; i<this.maxRow;i++)
      {
         for(int j=0;j<this.maxCol;j++)
         {
            if(this.mapArray.get(i).get(j).getnormal() == true)
            {
               System.out.print("( "+i + ", " + j + " ) = normal");
            }
            else if(this.mapArray.get(i).get(j).gethazard() == true)
            {
               System.out.print("( "+i + ", " + j + " ) = hazard");
            }
            else if(this.mapArray.get(i).get(j).getpredefinedspot() == true)
            {
               System.out.print("( "+i + ", " + j + " ) = predefinedspot");
            }
            else if(this.mapArray.get(i).get(j).getcolorblob() == true)
            {
               System.out.print("( "+i + ", " + j + " ) = colorblob");
            }
         }   
         System.out.println();
      }
   }
   */
   
   //arrayList 를 이차 행렬로 변환하여 반환
   public int[][] MapToMatrix()
   {
      int row = this.mapArray.size();
      int col = this.mapArray.get(0).size();
      int[][] arr = new int[row][col];
      
      //normal = 0 
      //hazard = -1 
      //detectedhazard = -2
      //unfoundcolorblob = 1
      //colorblob = 2
      //predefinedspot = 3
      //visitedpredefinedspot = 8
      
      for(int i=0;i<row;i++)
      {
         for(int j=0;j<col;j++)
         {
            if(this.mapArray.get(i).get(j).getnormal() == true)
            {
               arr[i][j] = 0;
            }
            else if(this.mapArray.get(i).get(j).getdetectedhazard() == true)
            {
               arr[i][j] = -2;
            }
            else if(this.mapArray.get(i).get(j).gethazard() == true)
            {
               arr[i][j] = -1;
            }
            else if(this.mapArray.get(i).get(j).getcolorblob() == true)
            {
               arr[i][j] = 2;
            }
            else if(this.mapArray.get(i).get(j).getunfoundcolorblob() == true)
            {
               arr[i][j] = 1;
            }
            else if(this.mapArray.get(i).get(j).getpredefinedspot() == true)
            {
               if(this.mapArray.get(i).get(j).getvisit() == true)
               {
                  arr[i][j] = 8;
               }
               else
                  arr[i][j] = 3;
            }
         }
      }
      return arr;
      
   }
   //map의 row size를 얻는다
   public int getmaxRow()
   {
      return maxRow;
   }
   //map의 column size를 얻는다
   public int getmaxCol()
   {
      return maxCol;
   }
   //map을 사용자 입력값을 바탕으로 초기화한다.
   public void createMap()
   {
      Scanner scan = new Scanner(System.in);
      System.out.println("지도크기를 입력하시오");
      System.out.print("row size: ");
      int mapsizeRow = scan.nextInt();
      System.out.print("column size: ");
      int mapsizeCol = scan.nextInt();
      System.out.println("로봇의 시작 위치를 입력하시오");
      System.out.print("로봇의 시작 행 :");
      this.startX = scan.nextInt();
      System.out.print("로봇의 시작 열 : ");
      this.startY = scan.nextInt();
      System.out.print("탐색지역의 갯수를 입력하시오 : ");
      numberofpredefinedspot = scan.nextInt();

      for(int i=0; i<numberofpredefinedspot; i++)
      {
         System.out.println(i+1 + " 번째 탐사지역을 입력하시오 ");
         System.out.print("row: ");
         this.spotX.add(scan.nextInt());
         System.out.print("column: ");
         this.spotY.add(scan.nextInt());
      }
      System.out.println("탐색지역 입력 완료\n");
      System.out.println("위험지역의 갯수를 입력하시오: ");
      int numberofhazard = scan.nextInt();
      int[] hazardX = new int[numberofhazard];
      int[] hazardY = new int[numberofhazard];
      for(int i=0; i<numberofhazard; i++)
      {
         System.out.println(i+1 + " 번째 위험지역을 입력하시오 ");
         System.out.print("row: ");
         hazardX[i] = scan.nextInt();
         System.out.print("column: ");
         hazardY[i] = scan.nextInt();
      }
      System.out.println("위험지역 입력 완료\n");
      
      //초기의 몇개의 colorblob을 숨겨 놓을 것인지 입력받는다
      System.out.println("숨길colorblob의 갯수: ");
      int numberofcolorblob = scan.nextInt();
      
      
      
      //초기 지도 구성
      this.maxRow = mapsizeRow;
      this.maxCol = mapsizeCol;
      for(int i=0; i<this.maxRow; i++)
      {
         ArrayList<MapData> templist = new ArrayList<>();
         
         for(int j=0; j<this.maxCol; j++)
         {
            //처음엔 전부다 노멀로 지정
            MapData tempMapdata = new MapData();
            tempMapdata.setX(i);
            tempMapdata.setY(j);
            tempMapdata.setAllfalse();
            tempMapdata.setnormal(true);
            templist.add(tempMapdata);
         }
         mapArray.add(templist);
      }
      //맵에 탐사지역 표시
      for(int i=0;i<numberofpredefinedspot; i++)
      {
         this.mapArray.get(this.spotX.get(i)).get(this.spotY.get(i)).setAllfalse();
         this.mapArray.get(this.spotX.get(i)).get(this.spotY.get(i)).setpredefinedspot(true);
      }
      //맵에 위험지역 표시
      for(int i=0; i< numberofhazard;i++)
      {
         this.mapArray.get(hazardX[i]).get(hazardY[i]).setAllfalse();
         this.mapArray.get(hazardX[i]).get(hazardY[i]).sethazard(true);
      }
      
      //입력받은 colorblob의 갯수를 바탕으로  미확인 colorblob을 랜덤으로 숨겨두기
      //미확인 colorblob은 normal과 unfoundcolorblob이 true이다.
      int n1=0;
      while(n1 < numberofcolorblob)
      {
         int random = (int)(Math.random()*100);
         int numberofnormal=0;
         
         for(int i=0; i<this.maxRow;i++)
         {
            for(int j=0;j<this.maxCol;j++)
            {
               if(this.mapArray.get(i).get(j).getnormal() == true)
                  numberofnormal++;
            }
         }
         
         random = random % numberofnormal;
         
         int n2=0;
         for(int i=0; i<this.maxRow;i++)
         {
            for(int j=0;j<this.maxCol;j++)
            {
               if(this.mapArray.get(i).get(j).getnormal() == true)
               {
                  n2++;
                  if(n2 == random)
                  {
                     this.mapArray.get(i).get(j).setAllfalse();
                     this.mapArray.get(i).get(j).setunfoundcolorblob(true);
                  }
               }
            }
         }
         n1++;
      }
      
      
      //입력값들이 잘못되었을때를 고려해서 다시 수정해주기
      
   }
   public boolean isMapDataNormal(int x, int y)
   {
      return mapArray.get(x).get(y).getnormal();
   }
   public boolean isMapDataHazard(int x, int y)
   {
      return mapArray.get(x).get(y).gethazard();
   }
   public boolean isMapDataColorblob(int x, int y)
   {
      return mapArray.get(x).get(y).getcolorblob();
   }
   public boolean isMapDataPredefinedspot(int x, int y)
   {
      return mapArray.get(x).get(y).getpredefinedspot();
   }
   public boolean isMapDataUnfoundcolorblob(int x, int y)
   {
      return mapArray.get(x).get(y).unfoundcolorblob;
   }
   //mapdata를 발견된 미발견 위험지역으로 변환
   public void reviseMapDatatoHazard(int x, int y)
   {
      mapArray.get(x).get(y).setAllfalse();
      mapArray.get(x).get(y).sethazard(true);
      mapArray.get(x).get(y).setdetectedhazard(true);
   }
   public void reviseMapDatatoColorBlob(int x, int y)
   {
      mapArray.get(x).get(y).setAllfalse();
      mapArray.get(x).get(y).setcolorblob(true);
      mapArray.get(x).get(y).setunfoundcolorblob(true);
   }
   public int getstartX()
   {
      return this.startX;
   }
   public int getstartY()
   {
      return this.startY;
   }
   public void setvisit(int x, int y)
   {
      mapArray.get(x).get(y).visit = true;
   }
   public int getnumberofpredefinedspot()
   {
      return this.numberofpredefinedspot;
   }
}


class MapData
{
   private int mapX;
   private int mapY;
   

   boolean normal;
   boolean hazard;
   boolean colorblob;
   boolean predefinedspot;
   boolean detectedhazard;
   boolean unfoundcolorblob;
   boolean visit;
   
   MapData()
   {
      this.normal = false; this.hazard = false; this.colorblob = false; this.predefinedspot = false;
      this.detectedhazard = false; this.unfoundcolorblob = false; this.visit =false;
   }
   void setAllfalse()
   {
      this.normal = false; this.hazard = false; this.colorblob = false; this.predefinedspot = false;
      this.detectedhazard = false; 
   }
   int getX()
   {
      return this.mapX;
   }
   int getY()
   {
      return this.mapY;
   }
   boolean getnormal()
   {
      return this.normal;
   }
   boolean gethazard()
   {
      return this.hazard;
   }
   boolean getcolorblob()
   {
      return this.colorblob;
   }
   boolean getpredefinedspot()
   {
      return this.predefinedspot;
   }
   boolean getdetectedhazard()
   {
      return this.detectedhazard;
   }
   boolean getunfoundcolorblob()
   {
      return this.unfoundcolorblob;
   }

   boolean getvisit()
   {
      return this.visit;
   }
   
   void setX(int x)
   {
      this.mapX = x;
   }
   void setY(int y)
   {
      this.mapY = y;
   }
   void setnormal(boolean normal)
   {
      this.normal = normal;
   }
   void sethazard(boolean hazard)
   {
      this.hazard = hazard;
   }
   void setcolorblob(boolean colorblob)
   {
      this.colorblob = colorblob;
   }
   void setpredefinedspot(boolean predefinedspot)
   {
      this.predefinedspot = predefinedspot;
   }
   void setdetectedhazard(boolean detectedhazard)
   {
      this.detectedhazard = detectedhazard;
   }
   void setunfoundcolorblob(boolean unfoundcolorblob)
   {
      this.unfoundcolorblob = unfoundcolorblob;
   }

   
   
   
}