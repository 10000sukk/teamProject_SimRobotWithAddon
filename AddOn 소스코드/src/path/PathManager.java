package path;

import java.util.ArrayList;

import map.*;

public class PathManager 
{
	//��θ� �����Ѵ�.
	ArrayList<PathInfo> pathList = new ArrayList<>();
	
	//���� ������ ��ο��� ���� ù��°�� �ش��ϴ� ����� ��ǥ�� ��ȯ�Ѵ�.
	//ArrayList �������� ��ȯ�Ͽ��� get(0)��  x��ǥ get(1)�� y��ǥ�� �ش��Ѵ�. 
	public ArrayList<Integer> pathIndexZero()
	{
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(pathList.get(0).getpathX());
		temp.add(pathList.get(0).getpathY());
		return temp;
	}
	//������ �������� ��θ� ã����...x,y �� ���� �κ��� ��ġ
	//true�� ��� �ִ°�...false�� ��� ���°�
	//��ΰ� �����ÿ� ��θ� ����ؼ� pathList�� ����
	public boolean setPath(MapManager map , int startX, int startY, int endX, int endY)
	{
		try {
			Astar astar = new Astar(map, startX, startY, endX, endY);
		
			//astar�� �̿��ؼ� ��θ� ã���ش�. ������ ���� ��ΰ� ���ٸ� ���⼭ NullPointerException�� �Ͼ
			ArrayList<Node> pathNode = (ArrayList<Node>) astar.findPath().clone();
			
			ArrayList<PathInfo> temp = new ArrayList<PathInfo>();
			//findPaht�� Node List�� ��ȯ ���� ������ �̸� PathInfo�� List�� ��ȯ
			for(int i=pathNode.size() - 1; i >=0 ; i--)
			{
				temp.add(new PathInfo(pathNode.get(i).x , pathNode.get(i).y));
			}
			this.pathList = (ArrayList<PathInfo>) temp.clone();
			return true;
		}
		catch(NullPointerException e)
		{
			return false;
		}
		
	}
	public void addPath(int x, int y)
	{
		this.pathList.add(new PathInfo(x,y));
	}
	public int getpathInfoX(int index)
	{
		return this.pathList.get(index).getpathX();
	}
	public int getpathInfoY(int index)
	{
		return this.pathList.get(index).getpathY();
	}

	//��� ����غ��� �Լ�
	/*public void printPath()
	{
		System.out.println("printPath");
		for(int i=0;i<this.pathList.size(); i++)
		{
			System.out.println("{ x: " + getpathInfoX(i) + ", y: "+ getpathInfoY(i));
		}
	}*/
	
}
class PathInfo
{
	private int pathX;
	private int pathY;
	
	public PathInfo(int pathX, int pathY)
	{
		this.pathX = pathX;
		this.pathY = pathY;
	}
	
	void setpathX(int pahtX)
	{
		this.pathX = pathX;
	}
	void setpathY(int pathY)
	{
		this.pathY = pathY;
	}
	int getpathX()
	{
		return pathX;
	}
	int getpathY()
	{
		return pathY;
	}
	
}

class Astar
{
	ArrayList<Node> openList = new ArrayList<>();
	ArrayList<Node> closeList = new ArrayList<>();

	int maxX;//�� �迭�� row
	int maxY;//�� �迭�� column
	
	int startX;
	int startY;
	int endX;
	int endY;
	int currentX;
	int currentY;
	
	ArrayList<ArrayList<Node>> mapList = new ArrayList<ArrayList<Node>>(); 
	
	//Astar�� ������...map�� �޾Ƽ� �̸� Node List�� ��ȯ...x,y�� �κ��� ���� ��ġ
	public Astar(MapManager map, int currentX, int currentY, int endX, int endY)
	{
		this.maxX = map.getmaxRow(); this.maxY = map.getmaxCol();
		this.currentX = currentX;
		this.currentY = currentY;
		this.endX = endX;
		this.endY = endY;
		
		for(int i = 0; i < this.maxX; i++)
		{
			ArrayList<Node> templist = new ArrayList<Node>();
			for(int j=0; j< this.maxY; j++)
			{
				Node tempNode = new Node();
				tempNode.x = i;
				tempNode.y = j;
				tempNode.h = manhatenDistance(i,j,this.endX,this.endY);
				templist.add(tempNode);
			}
			mapList.add(templist);
		}
		//hazard�� ������
		for(int i = 0; i< this.maxX; i++)
		{
			for(int j= 0; j <this.maxY; j++)
			{
				
				if(map.isMapDataHazard(i, j) == true)
				{
					mapList.get(i).get(j).hazard = true;
				}
			}
		}
		//ó�� ���� ������ openList�� �߰�
		mapList.get(this.currentX).get(this.currentY).g = 0;
		mapList.get(this.currentX).get(this.currentY).f = mapList.get(this.currentX).get(this.currentY).h;
		mapList.get(this.currentX).get(this.currentY).parent = null;
		this.openList.add(mapList.get(this.currentX).get(this.currentY));
	}
	
	//��θ� ��ȯ�Ѵ�. ��ȯ ������ Node���� list�����̴�
	ArrayList<Node> findPath()
	{
		ArrayList<Node> pathNode = new ArrayList<>();
		while(true)
		{
			insertCloseList();
			//��ǥ�� ����
			if(closeList.get(closeList.size() - 1).h == 0)
				break;
			//no path
			if(openList.size() == 0)
				return null;
		}
		Node last = closeList.get(closeList.size() - 1);
		int x;
		int y;
		while(true)
		{
			if(last.parent == null)
				break;
			Node temp = new Node();
			temp.x = last.x; 
			temp.y = last.y;
			pathNode.add(temp);
			last = last.parent;
		}
		return pathNode;
	}
	//�ش� ��ǥ�� mapList������ �ȿ� ��ġ�ϴ��� Ȯ���ϱ� ���ؼ�
	boolean listNotOutOfBound(int x, int y)
	{
		if(x>=0 && x<this.maxX && y >=0 && y<this.maxY )
			return true;
		return false;
	}
	//����ư �Ÿ� ���ϴ°�
	int manhatenDistance(int x1,int y1, int x2, int y2)
	{
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	//�ش� ��ǥ�� ��带 openList�� �߰��Ѵ�
	void insertOpenList(int x, int y)
	{
		//�ش� x,y�� ��ȿ�Ѱ����� �˻�
		if(listNotOutOfBound(x,y) == false)
			return;
		//�ش� ������ hazard���� �˻�
		if(mapList.get(x).get(y).hazard == true)
			return;
		//�ش� ��尡 closeList�� ���ԵǾ����� �˻�
		if(closeList.contains(mapList.get(x).get(y)) == true)
			return;
		//�ش� ��尡 openList�� ���ԵǾ� �ִ� ���̶�� ���Լ� �˻�
		//���Լ��� �� �������� openList�� ���´�.
		if(openList.contains(mapList.get(x).get(y)) == true)
		{
			for(int i=0;i<openList.size(); i++)
			{
				//openList�� ���� Node�� ���� ���ִٸ�?
				if(openList.get(i) == mapList.get(x).get(y))
				{
					int newG = mapList.get(this.currentX).get(this.currentY).g + 1;
					//�ΰ��� ��� ���� ���Լ� ��
					if(openList.get(i).g > newG)
					{
						openList.remove(i);
						mapList.get(x).get(y).parent = mapList.get(this.currentX).get(this.currentY);
						mapList.get(x).get(y).g = newG;
						mapList.get(x).get(y).f = newG + mapList.get(x).get(y).h;
					}
					return;
				}
			}
		
		}
		mapList.get(x).get(y).parent = mapList.get(this.currentX).get(this.currentY);
		mapList.get(x).get(y).g =  mapList.get(this.currentX).get(this.currentY).g + 1;
		mapList.get(x).get(y).f = mapList.get(x).get(y).g + mapList.get(x).get(y).h;
		openList.add(mapList.get(x).get(y));
	}
	
	//openList�߿��� ���Լ��� �ּҰ� �Ǵ� Node�� closeList�� �߰��Ѵ�.
	void insertCloseList()
	{
		try {
			Node FminNode = openList.get(0);//openList�� f�� �ּҰ� �Ǵ� Node...���� index 0���� ���ٸ� ��ΰ� ���� ���̴�.
			int minindex = 0;
			for(int i=0; i < openList.size(); i++)
			{
				if(FminNode.f > openList.get(i).f)
				{
					FminNode = openList.get(i);
					minindex = i;
				}
			}
			openList.remove(minindex);//�ش� Node�� openList���� ����
			int x = FminNode.x;
			int y = FminNode.y;
			this.currentX = x;
			this.currentY = y;
			closeList.add(mapList.get(x).get(y));
			//openList���� �� ����� �ڽ� ��尡 �ɸ��� �͵��� �ٽ� opneList�� �߰��Ѵ�.
			insertOpenList(x-1, y);
			insertOpenList(x+1, y);
			insertOpenList(x,y+1);
			insertOpenList(x,y-1);
			
		}
		catch(Exception e)
		{
			System.out.println("there is no path");
		}
	}
	
	
}

class Node
{
	Node parent;
	int f;//
	int g;//���ݱ��� ������� ������ ���
	int h;//������ ���� ����ư �Ÿ�
	int x;//row
	int y;//column
	
	boolean hazard;
	
	public Node()
	{
		hazard = false;
	}
}




