package dot.graph;

import java.util.List;
import java.util.ArrayList;

public class DotNode {

  /* labels are sequence of string statements to print at final generation */
  private String name, label, shape, color, identifier;
  private List<String> labels;

  /* shapes: box, ellipse, oval, circle, point, triangle, hexagon, custon
             double....*/
  public DotNode (String arg1, String arg2, String arg3, String arg4) {
    identifier = " ";
    name  = arg1;
    label = arg2;
    shape = arg3;
    color = arg4;
    labels = new ArrayList<String>();
  }

  public DotNode (String arg1) {
    this(arg1, " ", "box", "black");
  }

  public DotNode () {
    this(" ", " ", "box", "black");
  }

  public void setName (String arg1) {
    name = arg1;
  }

  public void setLabel (String arg1) {
    label = arg1;
  }

  public void setColor (String arg1) {
    color = arg1;
  }

  public void setShape (String arg1) {
    shape = arg1;
  }

  public void setId (String id) {
    identifier = id;
  }

  public String getId () {
    return identifier;
  }

  public String getName () {
    return name;
  }

  public String getColor () {
    return color;
  }

  public String getShape () {
    return shape;
  }

  public String getLabel () {
    return label;
  }

  public void addLine (String line) {
    labels.add(line);
  }

  public void addLines (List<String> lines) {
    for (String line : lines) {
      labels.add(line);
    }
  }

  public Boolean hasLine (String line) {
    return labels.contains(line);
  }

  public Boolean removeLine (String line) {
    return labels.remove(line);
  }

  public String toString () {
    String body;

    body = "\t\"" + identifier + "\" [shape=" + shape + ", color=" + color + ", label=\"";
    body += name + "\\l\\l\n";
    for (String line: labels) {
      line = line.replace('\"','\'');
      body += "\t" + line + "  \\l\n";
    }
    body += "\"];";
    return body;
  }

}
