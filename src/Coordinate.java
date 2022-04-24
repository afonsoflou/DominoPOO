public record Coordinate(int x, int y) implements Comparable<Coordinate>{
   public int compareTo(Coordinate other) {
      int result = other.y -y;
         if(result == 0)
            result =  x - other.x;
      return result;
   }
}

