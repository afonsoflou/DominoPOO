//WARNING THIS CLASS DOES NOT HAVE DELETE() .
// algorithms taken from https://algs4.cs.princeton.edu/home/

import java.util.LinkedList;

public class RedBlackTree<Key extends Comparable<Key>, Value>{
   private Node root;

   public Value get(Key key) {
      return get(key,root);
   }

   private Value get(Key key,Node node){
      if(node == null) return null;
      int cmp = key.compareTo(node.key);
      if (cmp < 0) return get(key,node.left);
      else if(cmp > 0) return get(key,node.right);
      else return node.val;
    }

   public boolean contains(Key key){
      return contains(key,root);
   }

   private boolean contains(Key key,Node node){
      if(node == null) return false;
      int cmp = key.compareTo(node.key);
      if (cmp < 0) return contains(key,node.left);
      else if(cmp > 0) return contains(key,node.right);
      else return true;
   }

   public Iterable<Key> getKeysInorder(){
      var keys = new LinkedList<Key>();
      getKeysInorder(root,keys);
      return keys;
   }

   private void getKeysInorder(Node root,LinkedList<Key> list){
      if(root == null) return;
      getKeysInorder(root.left,list);
      list.add(root.key);
      getKeysInorder(root.right,list);
   }

   public Iterable<Value> getValuesInorder(){
      var values = new LinkedList<Value>();
      getValuesInorder(root,values);
      return values;
   }

   private void getValuesInorder(Node root,LinkedList<Value> list){
      if(root == null) return;
      getValuesInorder(root.left,list);
      list.add(root.val);
      getValuesInorder(root.right,list);
   }




   public void put(Key key,Value val){
      root = put(root,key,val);
      root.color = BLACK;
   }

   private Node put(Node h,Key key,Value val){
      if(h == null)  return new Node(key,val,1,RED);

      int cmp = key.compareTo(h.key);
      if     (cmp < 0) h.left = put(h.left,key,val);
      else if(cmp > 0) h.right = put(h.right,key,val);
      else h.val = val;

      if(isRed(h.right) && !isRed(h.left))     h = rotateLeft(h);
      if(isRed(h.left) && isRed(h.left.left))  h = rotateRight(h);
      if(isRed(h.left) && isRed(h.right))      flipColors(h);

      h.N = size(h.left) + size(h.right) + 1;
      return h;
   }

   void flipColors(Node h){
      h.color = RED;
      h.left.color = BLACK;
      h.right.color = BLACK;
   }

   Node rotateLeft(Node h){
      Node x = h.right;
      h.right = x.left;
      x.left = h;
      x.color = h.color;
      x.color = RED;
      x.N = h.N;
      h.N = 1 + size(h.left) + size(h.right);
      return x;
   }

   Node rotateRight(Node h){
      Node x = h.left;
      h.left = x.right;
      x.right = h;
      x.color = h.color;
      h.color = RED;
      x.N = h.N;
      h.N = 1 +size(h.left) + size(h.right);
      return x;
   }

   public int size(){ return size(root);}
   public int size(Node x){
      if(x == null) return 0;
      else return x.N;
   }

   private boolean isRed(Node x){
      if( x == null) return false;
      return x.color == RED;
   }

   private static final boolean RED = true;
   private static final boolean BLACK =false;
   private class Node{
      Key key;
      Value val;
      Node left, right;
      int N;
      boolean color;

      Node(Key key, Value val, int N, boolean color){
         this.key = key;
         this.val = val;
         this.N = N;
         this.color = color;
      }
   }
}
