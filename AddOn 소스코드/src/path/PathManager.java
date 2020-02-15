package path;

import java.util.ArrayList;

import map.*;

public class PathManager 
{
	//경로를 저장한다.
	ArrayList<PathInfo> pathList = new ArrayList<>();
	
	//현재 설정된 경로에서 가장 첫번째로 해당하는 경로의 좌표를 반환한다.
	//ArrayList 형식으로 반환하여서 get(0)은  x좌표 get(1)은 y좌표에 해당한다. 
	public ArrayList<Integer> pathIndexZero()
	{
		ArrayList<Integer> temp = new ArrayList<Integer>();
		temp.add(pathList.get(0).getpathX());
		temp.add(pathList.get(0).getpathY());
		return temp;
	}
	//지도를 바탕으로 경로를 찾아줌...x,y 는 현재 로봇의 위치
	//true면 경로 있는것...false면 경로 없는것
	//경로가 있을시에 경로를 계산해서 pathList에 저장
	public boolean setPath(MapManager map , int startX, int startY, int endX, int endY)
	{
		try {
			Astar astar = new Astar(map, startX, startY, endX, endY);
		
			//astar를 이용해서 경로를 찾아준다. 하지만 만약 경로가 없다면 여기서 NullPointerException이 일어남
			ArrayList<Node> pathNode = (ArrayList<Node>) astar.findPath().clone();
			
			ArrayList<PathInfo> temp = new ArrayList<PathInfo>();
			//findPaht로 Node List를 반환 받은 다음에 이를 PathInfo의 List로 변환
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

	//경로 출력해보는 함수
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

	int maxX;//맵 배열의 row
	int maxY;//맵 배열의 column
	
	int startX;
	int startY;
	int endX;
	int endY;
	int currentX;
	int currentY;
	
	ArrayList<ArrayList<Node>> mapList = new ArrayList<ArrayList<Node>>(); 
	
	//Astar의 생성자...map을 받아서 이를 Node List로 변환...x,y는 로봇의 현재 위치
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
		//hazard를 벽으로
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
		//처음 시작 지점을 openList에 추가
		mapList.get(this.currentX).get(this.currentY).g = 0;
		mapList.get(this.currentX).get(this.currentY).f = mapList.get(this.currentX).get(this.currentY).h;
		mapList.get(this.currentX).get(this.currentY).parent = null;
		this.openList.add(mapList.get(this.currentX).get(this.currentY));
	}
	
	//경로를 반환한다. 반환 형식을 Node들의 list형식이다
	ArrayList<Node> findPath()
	{
		ArrayList<Node> pathNode = new ArrayList<>();
		while(true)
		{
			insertCloseList();
			//목표에 도달
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
	//해당 좌표가 mapList사이즈 안에 위치하는지 확인하기 위해서
	boolean listNotOutOfBound(int x, int y)
	{
		if(x>=0 && x<this.maxX && y >=0 && y<this.maxY )
			return true;
		return false;
	}
	//맨해튼 거리 구하는것
	int manhatenDistance(int x1,int y1, int x2, int y2)
	{
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	//해당 좌표의 노드를 openList에 추가한다
	void insertOpenList(int x, int y)
	{
		//해당 x,y가 유효한것인지 검사
		if(listNotOutOfBound(x,y) == false)
			return;
		//해당 지점이 hazard인지 검사
		if(mapList.get(x).get(y).hazard == true)
			return;
		//해당 노드가 closeList에 포함되었는지 검사
		if(closeList.contains(mapList.get(x).get(y)) == true)
			return;
		//해당 노드가 openList에 포함되어 있는 것이라면 평가함수 검사
		//평가함수가 더 작은놈이 openList에 남는다.
		if(openList.contains(mapList.get(x).get(y)) == true)
		{
			for(int i=0;i<openList.size(); i++)
			{
				//openList에 같은 Node가 포함 되있다면?
				if(openList.get(i) == mapList.get(x).get(y))
				{
					int newG = mapList.get(this.currentX).get(this.currentY).g + 1;
					//두개의 노드 간의 평가함수 비교
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
	
	//openList중에서 평가함수가 최소가 되는 Node를 closeList에 추가한다.
	void insertCloseList()
	{
		try {
			Node FminNode = openList.get(0);//openList중 f가 최소가 되는 Node...만약 index 0번이 없다면 경로가 없는 것이다.
			int minindex = 0;
			for(int i=0; i < openList.size(); i++)
			{
				if(FminNode.f > openList.get(i).f)
				{
					FminNode = openList.get(i);
					minindex = i;
				}
			}
			openList.remove(minindex);//해당 Node를 openList에서 빼고
			int x = FminNode.x;
			int y = FminNode.y;
			this.currentX = x;
			this.currentY = y;
			closeList.add(mapList.get(x).get(y));
			//openList에서 뺀 노드의 자식 노드가 될만한 것들을 다시 opneList에 추가한다.
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
	int g;//지금까지 오기까지 지불한 비용
	int h;//앞으로 남은 멘헤튼 거리
	int x;//row
	int y;//column
	
	boolean hazard;
	
	public Node()
	{
		hazard = false;
	}
}




