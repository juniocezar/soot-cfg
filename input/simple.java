public class simple {
  public static void main(String[] args) {
    int AAA = 30;
    int BBB = 30;
    String CCC = " ";
    if ( args[0] == "Nossa" ) {
      AAA = 0;
    } else {
      BBB = -99 + AAA;
      BBB = BBB + AAA;
    }
 
    for (String nova : args) {
       CCC += nova;
       AAA += AAA;
    }
  }
}
