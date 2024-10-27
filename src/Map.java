import java.util.ArrayList;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;

	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 *
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {
		init(w, h, v);
	}

	/**
	 * Constructs a square map (size*size).
	 *
	 * @param size
	 */
	public Map(int size) {
		this(size, size, 0);
	}

	/**
	 * Constructs a map from a given 2D array.
	 *
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}

	@Override
	/**This method initializes a two-dimensional map with the specified width, height, and initial value.
	 * Each element in the map is set to the initial value.(v)
	 *  w - The width of the map.
	 *  h - The height of the map.
	 *  v - The initial value to set for each element in the map.
	 */
	public void init(int w, int h, int v) {//בתוך כל מקום במערך  v מערך דו מימדי שמציב
		_map = new int[w][h];
		if (_map.length != 0) {

			for (int i = 0; i < _map.length; i++) {
				for (int j = 0; j < _map[0].length; j++) {
					_map[i][j] = v;
				}
			}
		} else {
			throw new RuntimeException("null");
		}
	}

	@Override

        /**
		 * Initializes the map using the provided two-dimensional array.
		 *@param arr The two-dimensional array containing the values to initialize the map.
		 */
	public void init(int[][] arr) {
		if(arr==null){
			throw  new RuntimeException("null");
		}
		int colom= arr[0].length;

		for (int i = 0; i < arr.length; i++) {
			if(arr[i].length!=colom){
				throw  new RuntimeException("null");
			}
		}
		_map = new int[arr.length][arr[0].length];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				this._map[i][j] = arr[i][j];
			}
		}
	}

	@Override
	/**
	 * This method returns a copy of the map.
	 * It creates a new two-dimensional array with the same dimensions as the _map variable
	 * and copies the values from _map into the new array.
	 * @return a copy of the map.
	 */
	public int[][] getMap() {
		int[][] ans = new int[_map.length][_map[0].length];

		for (int i = 0; i < _map.length; i++) {
			for (int j = 0; j < _map[0].length; j++) {
				ans[i][j] = _map[i][j];
			}
		}

		return ans;
	}

	@Override
	/**
	 * This method returns the width of the map. It checks if the _map variable is null,
	 * has zero rows, or has a null first row, and returns 0 in those cases.
	 * Otherwise, it returns the length of the _map array, which represents the width of the map.
	 * @return The width of the map.
	 */

	public int getWidth() {
		if (_map == null || _map.length == 0 || _map[0] == null) {
			return 0;
		}
		return _map.length;
	}

	@Override
        /**
		 * This method returns the height of the map.
		 * It checks if the `_map` variable is null or has zero rows,
		 * and returns 0 in those cases. Otherwise, it returns the length of the first row of `_map`,
		 * which represents the height of the map.
         */
	public int getHeight() {
		if (_map == null || _map.length == 0) {
			return 0;
		}
		return _map[0].length;
	}

	@Override
	/**
	 * This method returns the value at the specified coordinates in the map.
	 */

	public int getPixel(int x, int y) {
		return _map[x][y];
	}//מקבל נקודות חדשות

	@Override
	/**
	 *This function retrieves the color value of a pixel in a 2D map based on its coordinates
	 * provided as a Pixel2D object p.
	 * It uses the getPixel(x, y) function with the x and y coordinates extracted from the Pixel2D object.
	 */

	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(), p.getY());
	}

	@Override
	/**
	 * This function sets the color value of a pixel in a 2D map at the specified coordinates (x, y)
	 * to a given value v.
	 * It updates the map by assigning the value 'v' to the corresponding position '_map[x][y]'.
	 */
	public void setPixel(int x, int y, int v) {// בנקודה על המפה v מציב אובייקט
		_map[x][y] = v;
	}

	@Override
	/**
	 *This function sets the color value of a pixel in a 2D map at
	 * the specified coordinates provided as a Pixel2D object p to a given value 'v'.
	 * It uses the setPixel(int x, int y, int v) function with the x and y
	 * coordinates extracted from the Pixel2D object.
	 */
	public void setPixel(Pixel2D p, int v) { //
		this.setPixel(p.getX(), p.getY(), v);
	}

	@Override
	/**
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 *
	 * This function implements a flood fill algorithm starting from the given
	 * pixel xy and fills all connected pixels with the specified value 'new_v'.
	 * It returns the total count of pixels that were filled during the operation.
	 * The flood fill algorithm explores neighboring pixels, either in 4 or 8 directions
	 * based on the cyclic flag, and fills them if they have the same value as the starting pixel.
	 * Parameters: xy (Pixel2D) - starting pixel coordinates, new_v (int) - new value to fill
	 * @return int - the total number of pixels filled during the flood fill operation.
	 */
	public int fill(Pixel2D xy, int new_v) {
		int ans = 0;

		if (!isInside(xy)) {
			throw new RuntimeException("not inside");
		}
		if (this.getPixel(xy) == new_v) {
			return ans;
		}
		int pixel = this.getPixel(xy);

		this.setPixel(xy, new_v);// set xy in the new color
		Index2D temp = new Index2D(xy);
		Index2D neighbor;

		ArrayList<Index2D> checkindex = new ArrayList<Index2D>();
		checkindex.add(temp);
		while (checkindex.isEmpty() == false) {//until there is no more index to fill
			ans++;
			temp = checkindex.remove(0);//the next index to fill

			if (_cyclicFlag == false) {
				neighbor = new Index2D(temp.getX() + 1, temp.getY());//right
				if (isInside(neighbor)) {//if the point inside
					if (this.getPixel(neighbor) == pixel) {//if the point is not fill
						this.setPixel(neighbor, new_v);
						checkindex.add(neighbor);// add to check the neighbor
					}
				}
				neighbor = new Index2D(temp.getX(), temp.getY() + 1);//up
				if (isInside(neighbor)) {
					if (this.getPixel(neighbor) == pixel) {
						this.setPixel(neighbor, new_v);
						checkindex.add(neighbor);
					}
				}
				neighbor = new Index2D(temp.getX() - 1, temp.getY());//left
				if (isInside(neighbor)) {
					if (this.getPixel(neighbor) == pixel) {
						this.setPixel(neighbor, new_v);
						checkindex.add(neighbor);
					}
				}
				neighbor = new Index2D(temp.getX(), temp.getY() - 1);//down
				if (isInside(neighbor)) {
					if (this.getPixel(neighbor) == pixel) {
						this.setPixel(neighbor, new_v);
						checkindex.add(neighbor);
					}
				}
			} else {
				int x_left = temp.getX() - 1;
				int y_up = temp.getY() + 1;
				int y_down = temp.getY() - 1;
				int x_right = temp.getX() + 1;
				if (temp.getX() == this.getWidth() - 1) {// if the point is rightmost place
					x_right = 0;
				}
				if (temp.getX() == 0) {
					x_left = this.getWidth() - 1;// if the point is leftmost place
				}
				if (temp.getY() == this.getHeight() - 1) {//if the point is the top
					y_up = 0;
				}
				if (temp.getY() == 0) {
					y_down = this.getHeight() - 1;// if the point is lowest place
				}

				neighbor = new Index2D(x_right, temp.getY());//right
				if (this.getPixel(neighbor) == pixel) {
					this.setPixel(neighbor, new_v);
					checkindex.add(neighbor);
				}
				neighbor = new Index2D(temp.getX(), y_up);//up
				if (this.getPixel(neighbor) == pixel) {
					this.setPixel(neighbor, new_v);
					checkindex.add(neighbor);
				}
				neighbor = new Index2D(x_left, temp.getY());//left
				if (this.getPixel(neighbor) == pixel) {
					this.setPixel(neighbor, new_v);
					checkindex.add(neighbor);
				}
				neighbor = new Index2D(temp.getX(), y_down);//down
				if (this.getPixel(neighbor) == pixel) {
					this.setPixel(neighbor, new_v);
					checkindex.add(neighbor);
				}

			}

		}

		return ans;
	}

	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 *
	 * This function calculates the shortest path between two pixels, p1 and p2, on a map
	 * considering obstacles of a specific color. It returns an array of Pixel2D objects representing
	 * the path from p1 to p2, including both endpoints. The function uses the allDistance() function
	 * to calculate the distances from p1 to all other pixels, and then backtracks from p2 to p1
	 * using the computed distances.
	 * @param p1 Starting pixel
	 * @param p2 Destination pixel
	 * @param obsColor Color value representing obstacles on the map.
	 * @return Array of Pixel2D objects representing the shortest path from p1 to p2.
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
		Pixel2D[] ans = null; // the result.
		Map2D newcopy = this.allDistance(p1, obsColor);
		ans = new Pixel2D[newcopy.getPixel(p2) + 1];
		if (newcopy.getPixel(p2) == -1) {
			return null;
		}
		ArrayList<Pixel2D> nextstep = new ArrayList<Pixel2D>();
		nextstep.add(p2);//the first point
		Pixel2D temp = p2;
		ans[0] = p2;
		int i = 0;
		while (temp.equals(p1)==false) {// until p1=p2
			boolean flag = false;
			i++;

			if (newcopy.isCyclic() == false) {

				Index2D left = new Index2D(temp.getX() - 1, temp.getY());
				Index2D right = new Index2D(temp.getX() + 1, temp.getY());
				Index2D up = new Index2D(temp.getX(), temp.getY() + 1);
				Index2D down = new Index2D(temp.getX(), temp.getY() - 1);

				if (isInside(left) && flag ==false) {//if he didn't find a neighbor
					if (newcopy.getPixel(left) == (newcopy.getPixel(temp) - 1)) {
						ans[i] = left;
						flag = true; //neighbor was found
					}
				}
				if (isInside(right) && flag==false) {
					if (newcopy.getPixel(right) == newcopy.getPixel(temp) - 1) {
						ans[i] = right;
						flag = true;
					}
				}
				if (isInside(up)&&flag==false) {
					if (newcopy.getPixel(up) == newcopy.getPixel(temp) - 1) {
						ans[i] = up;
						flag=true;
					}
				}
				if (isInside(down)&&flag==false) {
					if (newcopy.getPixel(down) == newcopy.getPixel(temp) - 1) {
						ans[i] = down;
						flag=true;
					}
				}
			} else {//cyclic
				int x_left = temp.getX()-1;
				int y_up = temp.getY()+1;
				int y_down = temp.getY()-1;
				int x_right = temp.getX()+1;

				if(temp.getX() == newcopy.getWidth()-1){
					x_right=0;
				}
				if(temp.getX()==0){
					x_left=newcopy.getWidth()-1;
				}
				if(temp.getY()==newcopy.getHeight()-1){
					y_up=0;
				}
				if(temp.getY()==0){
					y_down=newcopy.getHeight()-1;
				}
				Index2D left = new Index2D(x_left, temp.getY());
				Index2D right = new Index2D(x_right, temp.getY());
				Index2D up = new Index2D(temp.getX(), y_up);
				Index2D down = new Index2D(temp.getX(), y_down);

				if (isInside(left)&&flag==false) {
					if (newcopy.getPixel(left) == (newcopy.getPixel(temp) - 1)) {
						ans[i] = left;
						flag=true;
					}
				}
				if (isInside(right)&&flag==false) {
					if (newcopy.getPixel(right) == newcopy.getPixel(temp) - 1) {
						ans[i] = right;
						flag=true;
					}
				}
				if (isInside(up)&&flag==false) {
					if (newcopy.getPixel(up) == newcopy.getPixel(temp) - 1) {
						ans[i] = up;
						flag=true;
					}
				}
				if (isInside(down)&&flag==false) {
					if (newcopy.getPixel(down) == newcopy.getPixel(temp) - 1) {
						ans[i] = down;
						flag = true;
					}
				}

			}
			temp = ans[i];
//			if(temp==null){
//				System.out.println("");
//			}
		}
		ans[i] = p1;
		for (int j = 0; j < ans.length ; j++) {  //להפוך את האינדקס לפיקסל
			Pixel2D temp2 = ans[j];
			ans[j]= temp2;
		}
		return ans;
	}


	@Override
	/**
	 *This function determines whether a given Pixel2D object,
	 *represented by the parameter p, is inside a 2D map.
	 *It checks if the coordinates of the pixel fall within the bounds
	 *  of the map and returns a boolean value indicating the result.
	 * @return if p is inside
	 */
	public boolean isInside(Pixel2D p) {
		int x = p.getX();
		int y = p.getY();
		if (x < 0 || y < 0 || y >= _map[0].length||  x >= _map.length ) {
			return false;
		}
		return true;
	}

	@Override
	/**
	 * return if the map is cyclic or not.
	 */
	public boolean isCyclic () {
		return this._cyclicFlag;
	}

	@Override
	/**
	 * set if the map is cyclic.
	 */
	public void setCyclic ( boolean cy){
		this._cyclicFlag = cy;
	}
	@Override
	/**
	 * This function calculates the distances from a given starting point to
	 * all other points in a 2D map.
	 * It performs a breadth-first search algorithm to explore the map and assign
	 * distance values to each reachable point.
	 * The function takes a starting point and an obstacle color and returns a Map2D
	 * object with the calculated distances.
	 */
	public Map2D allDistance (Pixel2D start,int obsColor){
		Map2D map = null;  // the result.
		Pixel2D ans2;
		if(this.isInside(start)== false||this.getPixel(start)==obsColor){//If the starting point is not on the map or if it is -1
			throw new RuntimeException("No pass "+start.toString());
		}
		map= new Map(this.getMap());
		for (int i = 0; i < map.getWidth(); i++) {//-2 where it is possible to pass and -1 where it is not possible to pass
			for (int j = 0; j < map.getHeight(); j++) {
				if(map.getPixel(i,j)==obsColor){
					map.setPixel(i,j,-1);
				} else {
					map.setPixel(i,j,-2);
				}
			}
		}
		map.setPixel(start,0);//starting point and set a value of 0
		Index2D temp=new Index2D(start);
		Index2D Neighbor;

		ArrayList<Index2D> list= new ArrayList<Index2D>();//An array for neighborhood checking
		list.add(temp);

		while (list.isEmpty()==false){//Until he finished checking and placing numbers for all the neighbors

			temp= list.remove(0);
			if(_cyclicFlag==false){ //if not cyclic
				Neighbor = new Index2D(temp.getX()+1,temp.getY());//right
				if(isInside(Neighbor)) { //if inside
					if (map.getPixel(Neighbor) == -2) { // if the value is -2
						map.setPixel(Neighbor, map.getPixel(temp) + 1);//add my value +1
						list.add(Neighbor);
					}
				}
				Neighbor =new Index2D(temp.getX(),temp.getY()+1);//up
				if( isInside(Neighbor) ) {//if inside
					if (map.getPixel(Neighbor) == -2) {
						map.setPixel(Neighbor, map.getPixel(temp) + 1);// if the value is -2
						list.add(Neighbor);
					}
				}
				Neighbor =new Index2D(temp.getX()-1,temp.getY());//left
				if( isInside(Neighbor)) {//if inside
					if (map.getPixel(Neighbor) == -2) {// if the value is -2
						map.setPixel(Neighbor, map.getPixel(temp) + 1);//add my value +1
						list.add(Neighbor);
					}
				}
				Neighbor =new Index2D(temp.getX(),temp.getY()-1);//down
				if( isInside(Neighbor)){//if inside
					if (map.getPixel(Neighbor) == -2) {// if the value is -2
						map.setPixel(Neighbor, map.getPixel(temp) + 1);//add my value +1
						list.add(Neighbor);
					}
				}
			} else { //if cyclic
				int x_left = temp.getX()-1;
				int y_up = temp.getY()+1;
				int y_down = temp.getY()-1;
				int x_right = temp.getX()+1;

				if(temp.getX() == map.getWidth()-1){ // if the point is rightmost place
					x_right=0;
				}
				if(temp.getX()==0){ // if the point is leftmost place
					x_left=map.getWidth()-1;
				}
				if(temp.getY()==map.getHeight()-1){// if the point is lowest place
					y_up=0;
				}
				if(temp.getY()==0){// if the point is the top place
					y_down=map.getHeight()-1;
				}

				Neighbor = new Index2D(x_right,temp.getY());//right
				if( map.getPixel(Neighbor)== -2){
					map.setPixel(Neighbor,map.getPixel(temp)+1);
					list.add(Neighbor);
				}
				Neighbor =new Index2D(temp.getX(),y_up);//up
				if( map.getPixel(Neighbor)== -2){
					map.setPixel(Neighbor,map.getPixel(temp)+1);
					list.add(Neighbor);
				}
				Neighbor =new Index2D(x_left,temp.getY());//left
				if( map.getPixel(Neighbor)== -2){
					map.setPixel(Neighbor,map.getPixel(temp)+1);
					list.add(Neighbor);
				}
				Neighbor =new Index2D(temp.getX(),y_down);//down
				if(map.getPixel(Neighbor)== -2){
					map.setPixel(Neighbor,map.getPixel(temp)+1);
					list.add(Neighbor);
				}
			}
		}
		for (int i = 0; i < map.getWidth(); i++) {//If there are still points with a value of -2 change to 1
			for (int j = 0; j < map.getHeight(); j++) {
				if (map.getPixel(i, j) == -2) {
					map.setPixel(i,j,-1);
				}
			}
		}
		return map;
	}

	/**
	 * This function finds the nearest reachable point to the given starting pixel on the map,
	 * considering obstacles of a specific color. It returns the Pixel2D object representing
	 * the nearest reachable point. The function marks the map with values -2 for passable areas,
	 * -1 for obstacles, and -5 for "food" areas.
	 * @param start
	 * @param obsColor
	 * @return Pixel2D object representing the nearest reachable food point to the given starting pixel.
	 */

	public Pixel2D point(Pixel2D start, int obsColor){
		Map2D ans = null;  // the result.
		if(this.isInside(start)== false||this.getPixel(start)==obsColor){//If the starting point is not on the map or if it is -1
			throw new RuntimeException("No pass");
		}
		ans= new Map(this.getMap());
		for (int i = 0; i < ans.getWidth(); i++) {//-2 where it is possible to pass and -1 where it is not possible to pass
			for (int j = 0; j < ans.getHeight(); j++) {
				if(ans.getPixel(i,j)==obsColor){
					ans.setPixel(i,j,-1);
				} else if(ans.getPixel(i,j)== 0){ //אם הוא שחור
					ans.setPixel(i,j,-2);}
				else{
					ans.setPixel(i,j,-5); //אם הוא ורוד
				}
			}
		}
		ans.setPixel(start,0);//starting point and set a value of 0
		Index2D temp=new Index2D(start);
		Index2D Neighbor;

		ArrayList<Index2D> list= new ArrayList<Index2D>();//An array for neighborhood checking
		list.add(temp);

		while (list.isEmpty()==false){//Until he finished checking and placing numbers for all the neighbors

			temp= list.remove(0);
			if(_cyclicFlag==false) { //if not cyclic
				Neighbor = new Index2D(temp.getX() + 1, temp.getY());//right
				if (isInside(Neighbor)) { //if inside
					if (ans.getPixel(Neighbor) == -2) { // if the value is -2
						ans.setPixel(Neighbor, ans.getPixel(temp) + 1);//add my value +1
						list.add(Neighbor);
					} else if (ans.getPixel(Neighbor) == -5) { //להחזיר את הורוד או הירוק הכי קרוב
						return Neighbor;

					}
					Neighbor = new Index2D(temp.getX(), temp.getY() - 1);//down
					if (isInside(Neighbor)) {//if inside
						if (ans.getPixel(Neighbor) == -2) {// if the value is -2
							ans.setPixel(Neighbor, ans.getPixel(temp) + 1);//add my value +1
							list.add(Neighbor);
						} else if (ans.getPixel(Neighbor) == -5) {
							return Neighbor;
						}

				}
				Neighbor = new Index2D(temp.getX() - 1, temp.getY());//left
				if (isInside(Neighbor)) {//if inside
					if (ans.getPixel(Neighbor) == -2) {// if the value is -2
						ans.setPixel(Neighbor, ans.getPixel(temp) + 1);//add my value +1
						list.add(Neighbor);
					} else if (ans.getPixel(Neighbor) == -5) {
						return Neighbor;
					}
				}
					Neighbor = new Index2D(temp.getX(), temp.getY() + 1);//up
					if (isInside(Neighbor)) {//if inside
						if (ans.getPixel(Neighbor) == -2) {
							ans.setPixel(Neighbor, ans.getPixel(temp) + 1);// if the value is -2
							list.add(Neighbor);
						} else if (ans.getPixel(Neighbor) == -5) {
							return Neighbor;
						}
					}
				}
			} else { //if cyclic
				int x_left = temp.getX()-1;
				int y_up = temp.getY()+1;
				int y_down = temp.getY()-1;
				int x_right = temp.getX()+1;

				if(temp.getX() == ans.getWidth()-1){ // if the point is rightmost place
					x_right=0;
				}
				if(temp.getX()==0){ // if the point is leftmost place
					x_left=ans.getWidth()-1;
				}
				if(temp.getY()==ans.getHeight()-1){// if the point is lowest place
					y_up=0;
				}
				if(temp.getY()==0){// if the point is the top place
					y_down=ans.getHeight()-1;
				}

				Neighbor = new Index2D(x_right,temp.getY());//right
				if( ans.getPixel(Neighbor)== -2){
					ans.setPixel(Neighbor,ans.getPixel(temp)+1);
					list.add(Neighbor);
				} else if (ans.getPixel(Neighbor) == -5) {
					return Neighbor;
				}
				Neighbor =new Index2D(temp.getX(),y_up);//up
				if( ans.getPixel(Neighbor)== -2){
					ans.setPixel(Neighbor,ans.getPixel(temp)+1);
					list.add(Neighbor);
				} else if (ans.getPixel(Neighbor) == -5) {
					return Neighbor;
				}
				Neighbor =new Index2D(temp.getX(),y_down);//down
				if(ans.getPixel(Neighbor)== -2){
					ans.setPixel(Neighbor,ans.getPixel(temp)+1);
					list.add(Neighbor);
				} else if (ans.getPixel(Neighbor) == -5) {
					return Neighbor;
				}
				Neighbor =new Index2D(x_left,temp.getY());//left
				if( ans.getPixel(Neighbor)== -2){
					ans.setPixel(Neighbor,ans.getPixel(temp)+1);
					list.add(Neighbor);
				} else if (ans.getPixel(Neighbor) == -5) {
					return Neighbor;
				}

			}
		}
		return null;
	}
}

