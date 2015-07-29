import javax.swing.JFrame;


public class MutableClass extends JFrame {

 private int value;

 public MutableClass(int aValue) {
  value = aValue;
 }

 public void setValue(int aValue) {
  value = aValue;
 }

 public int getValue() {
  return value;
 }

}