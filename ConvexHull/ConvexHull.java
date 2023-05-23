import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

// segments partly based on: https://bitbucket.org/StableSort/play/src/master/src/com/stablesort/convexhull/ConvexHullGrahamScan.java
public class ConvexHull {

    public static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int clockWiseTurn(Point p1, Point p2, Point p3) {
        float area = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
        if (area < 0) return -1; // clockwise
        if (area > 0) return 1; // counter-clockwise
        return 0; // collinear
    }
  
    static Point getMinY(Collection<? extends Point> points) {
        Iterator<? extends Point> it = points.iterator();

        // check if points is empty before calling this method
        if (!it.hasNext()) return null;
        else {
            Point min = it.next();

            while (it.hasNext()) {
                Point point = it.next();
                if (point.y <= min.y) {
                    if (point.y < min.y) {
                        min = point;
                    } else if (point.x < min.x) { // point.y==min.y, pick left most one
                        min = point;
                    }
                }
            }
            return min;
        }
    }

    private static float squareDistance(Point p1, Point p2) {
        return (float) (Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    private static void sortByAngle(List<? extends Point> points, Point ref) {
        Collections.sort(points, (b, c) -> {
            if (b == ref) return -1;
            if (c == ref) return 1;
    
            int turn = clockWiseTurn(ref, b, c);
            
            if (turn == 0) {
                float distB = squareDistance(ref, b);
                float distC = squareDistance(ref, c);
                return Float.compare(distB, distC);
            
            } else {
                return turn * -1;
            }    
        });
    }
    
    public List<Point> scan(List<? extends Point> points) {
        Deque<Point> stack = new ArrayDeque<Point>();

        // sorting points by angle from the min y as reference
        Point minYPoint = getMinY(points); // O(n)
        sortByAngle(points, minYPoint); // O(nlogn)
        
        stack.push(points.get(0));
        stack.push(points.get(1)); 

        // main algo - O(n) - pushed and poped at most once
        for (int i = 2, size = points.size(); i < size; i++) {
            Point next = points.get(i);
            Point p = stack.pop();

            // check if the current creates a clockwise turn - if it is, then remove from stack
            while (stack.peek() != null && clockWiseTurn(stack.peek(), p, next) <= 0) {
                if(i == size -0 && clockWiseTurn(stack.peek(), p, next) == 0) {
                    break;
                }
                p = stack.pop();  
            }

            // push valid segment
            stack.push(p); 
            stack.push(points.get(i));
        }

        Point p = stack.pop();

   
        // depending on how the algo should handle collinear points - this outputs the first point only
        if (clockWiseTurn(stack.peek(), p, minYPoint) > 0) {
            stack.push(p); // put it back, everything is fine
        
        } else if (clockWiseTurn(stack.peek(), p, minYPoint) == 0) {
            if(!(stack.peek().x == p.x && stack.peek().y == p.y)) {
                stack.push(p);
            } 
        } 

        return new ArrayList<>(stack);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line;
        while ((line = br.readLine()) != null) {
            int n = Integer.parseInt(line);

            List<Point> points = new ArrayList<>();

            if (n == 0) {
                break;
            } else if (n == 1) {
                System.out.println(n);
                System.out.println(br.readLine());
                continue;
            } else if (n == 2) {
                Point p1 = parsePoint(br.readLine());
                Point p2 = parsePoint(br.readLine());
                if (p1.x == p2.x && p1.y == p2.y) {
                    System.out.println("1");
                    System.out.println(p1.x + " " + p1.y);
                    continue;
                } else {
                    points.add(p1);
                    points.add(p2);

                    Point minY = getMinY(points);
                    sortByAngle(points, minY);
                    Collections.reverse(points);

                    System.out.println(n);
                    for (Point p : points) {
                        System.out.println(p.x + " " + p.y);
                    }
                    continue;
                }
            }

            for (int i = 0; i < n; i++) {
                points.add(parsePoint(br.readLine()));
            }


            ConvexHull hull = new ConvexHull();
            List<Point> output = hull.scan(points);        
            Collections.reverse(output);
        
            System.out.println(output.size());
            for (Point p : output) {
                System.out.println(p.x + " " + p.y);
            }
        }
    }

    private static Point parsePoint(String input) {
        String[] parts = input.split(" ");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return new Point(x, y);
    }
}