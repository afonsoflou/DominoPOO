public record Coordinate(int x, int y) implements Comparable<Coordinate>{
   public int compareTo(Coordinate other) {
      int result = other.y -y;
         if(result == 0)
            result =  x - other.x;
      return result;
   }

   @Override
   public boolean equals(Object o) {
      if(this == o) return true;
      if(o == null || getClass() != o.getClass()) return false;

      Coordinate that = (Coordinate) o;

      if(x != that.x) return false;
      return y == that.y;
   }

}

