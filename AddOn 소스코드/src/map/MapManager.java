package map;

import java.util.ArrayList;
import java.util.Scanner;



public class MapManager {
   
   ArrayList<ArrayList<MapData>> mapArray = new ArrayList<ArrayList<MapData>>();
   int maxRow;
   int maxCol;
   
   int startX;
   int startY;
   //predefinedspot�� ����
   int numberofpredefinedspot;
   //predefinedspot�� ArrayList
   ArrayList<Integer> spotX = new ArrayList<Integer>();
   ArrayList<Integer> spotY = new ArrayList<Integer>();
   
   //����ư �Ÿ��� ���� �������� spot�߿��� ���� ����� spot����
   public ArrayList<Integer> firstspot(int currentX, int currentY)
   {
      //���� �̹� �湮�� ������ spot ArrayList���� ���� ��Ų��
      for(int i=0; i <this.numberofpredefinedspot;i++)
      {
         if(mapArray.get(spotX.get(i)).get(spotY.get(i)).getvisit() == true )
         {
            this.numberofpredefinedspot--;
            spotX.remove(i);
            spotY.remove(i);
         }
      }      
      //���� ���� �湮���� ���� spot�� ���ٸ� return
      if(this.numberofpredefinedspot == 0)
         return null;
      
      //ArrayList<Integer> �������� ��ȯ�Ұ��̴�... a.get(0)�� x��ǥ.. a.get(1)��  y��ǥ
      ArrayList<Integer> a = new ArrayList<Integer>();
      a.add(spotX.get(0));
      a.add(spotY.get(0));
      int min = Math.abs(a.get(0) - currentX) + Math.abs(a.get(1) - currentY);
      
      //���� ������ ����ư �Ÿ��� �ּҰ� �Ǵ� spot����
      for(int i=0;i<this.numberofpredefinedspot; i++)
      {
         int temp =  Math.abs(spotX.get(i) - currentX) + Math.abs(spotY.get(i) - currentY);
         if(min > temp)
         {
            min = temp;
            //a list ��ü
            a.set(0, spotX.get(i));
            a.set(1, spotY.get(i));
         }
      }
      
      return a;
   }
   
   /*
   //map�� ������ print...���� ����� �����
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
   
   //arrayList �� ���� ��ķ� ��ȯ�Ͽ� ��ȯ
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
   //map�� row size�� ��´�
   public int getmaxRow()
   {
      return maxRow;
   }
   //map�� column size�� ��´�
   public int getmaxCol()
   {
      return maxCol;
   }
   //map�� ����� �Է°��� �������� �ʱ�ȭ�Ѵ�.
   public void createMap()
   {
      Scanner scan = new Scanner(System.in);
      System.out.println("����ũ�⸦ �Է��Ͻÿ�");
      System.out.print("row size: ");
      int mapsizeRow = scan.nextInt();
      System.out.print("column size: ");
      int mapsizeCol = scan.nextInt();
      System.out.println("�κ��� ���� ��ġ�� �Է��Ͻÿ�");
      System.out.print("�κ��� ���� �� :");
      this.startX = scan.nextInt();
      System.out.print("�κ��� ���� �� : ");
      this.startY = scan.nextInt();
      System.out.print("Ž�������� ������ �Է��Ͻÿ� : ");
      numberofpredefinedspot = scan.nextInt();

      for(int i=0; i<numberofpredefinedspot; i++)
      {
         System.out.println(i+1 + " ��° Ž�������� �Է��Ͻÿ� ");
         System.out.print("row: ");
         this.spotX.add(scan.nextInt());
         System.out.print("column: ");
         this.spotY.add(scan.nextInt());
      }
      System.out.println("Ž������ �Է� �Ϸ�\n");
      System.out.println("���������� ������ �Է��Ͻÿ�: ");
      int numberofhazard = scan.nextInt();
      int[] hazardX = new int[numberofhazard];
      int[] hazardY = new int[numberofhazard];
      for(int i=0; i<numberofhazard; i++)
      {
         System.out.println(i+1 + " ��° ���������� �Է��Ͻÿ� ");
         System.out.print("row: ");
         hazardX[i] = scan.nextInt();
         System.out.print("column: ");
         hazardY[i] = scan.nextInt();
      }
      System.out.println("�������� �Է� �Ϸ�\n");
      
      //�ʱ��� ��� colorblob�� ���� ���� ������ �Է¹޴´�
      System.out.println("����colorblob�� ����: ");
      int numberofcolorblob = scan.nextInt();
      
      
      
      //�ʱ� ���� ����
      this.maxRow = mapsizeRow;
      this.maxCol = mapsizeCol;
      for(int i=0; i<this.maxRow; i++)
      {
         ArrayList<MapData> templist = new ArrayList<>();
         
         for(int j=0; j<this.maxCol; j++)
         {
            //ó���� ���δ� ��ַ� ����
            MapData tempMapdata = new MapData();
            tempMapdata.setX(i);
            tempMapdata.setY(j);
            tempMapdata.setAllfalse();
            tempMapdata.setnormal(true);
            templist.add(tempMapdata);
         }
         mapArray.add(templist);
      }
      //�ʿ� Ž������ ǥ��
      for(int i=0;i<numberofpredefinedspot; i++)
      {
         this.mapArray.get(this.spotX.get(i)).get(this.spotY.get(i)).setAllfalse();
         this.mapArray.get(this.spotX.get(i)).get(this.spotY.get(i)).setpredefinedspot(true);
      }
      //�ʿ� �������� ǥ��
      for(int i=0; i< numberofhazard;i++)
      {
         this.mapArray.get(hazardX[i]).get(hazardY[i]).setAllfalse();
         this.mapArray.get(hazardX[i]).get(hazardY[i]).sethazard(true);
      }
      
      //�Է¹��� colorblob�� ������ ��������  ��Ȯ�� colorblob�� �������� ���ܵα�
      //��Ȯ�� colorblob�� normal�� unfoundcolorblob�� true�̴�.
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
      
      
      //�Է°����� �߸��Ǿ������� ����ؼ� �ٽ� �������ֱ�
      
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
   //mapdata�� �߰ߵ� �̹߰� ������������ ��ȯ
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