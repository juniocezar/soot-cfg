package dot.graph;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DotGraph {

  private String packageName, definingClassName, methodName, outfile;
  private List<String> methodArguments;
  private List<DotNode> nodes;
  private Map<String, List<String>> edges;
  private Integer autoId;

  public DotGraph (String pkg, String dc, String method, String file) {
    packageName = pkg;
    definingClassName = dc;
    outfile = file.replace('$','-').replace('<','.').replace('>','T');
    methodName = method;
    autoId = 0;
    nodes = new ArrayList<DotNode>();
    edges = new HashMap<String, List<String>>();
    methodArguments = new ArrayList<String>();
  }

  public DotGraph () {
    this("Package", "Class", "methodName", "default-output-file.dot");
  }

  public void setPackageName (String name) {
    packageName = name;
  }

  public void setDefiningClass (String name) {
    definingClassName = name;
  }

  public void setOutput (String name) {
    outfile = name;
  }

  public void setMethod (String name) {
    methodName = name;
  }

  // todo: define get methods

public void newNode (List<String> lines, String name, String label, String shape,
  String color, List<String> succs) {

    DotNode dotNode = new DotNode(name, label, shape, color);
    //dotNode.setId("block." + autoId);
    dotNode.setId(name);
    autoId++;

    dotNode.addLines(lines);
    nodes.add(dotNode);


    if (! edges.containsKey(name)) {
      List<String> adjList = new ArrayList<String>();
      edges.put(name, adjList);
    }

    List<String> adjList = edges.get(name);
    for (String succ : succs) {
      adjList.add(succ);
    }
  }

  public void genDotFile() {
    System.out.println("Generating dot file");
     
    PrintWriter pw = null;
    try {
      File file = new File(outfile);
      FileWriter fw = new FileWriter(file, true);
      pw = new PrintWriter(fw);

      pw.println("digraph \"CFG for '"+ methodName + "' method\" {");
      pw.println("\tlabel=\"digraph \\\"CFG for '"+ methodName + "' method\\\"\"; ");

      /* printing all edges */
      for (Map.Entry<String, List<String>> entry : edges.entrySet()) {
        String from = entry.getKey();
        for (String to : entry.getValue()) {
            pw.println("\t\"" + from + "\" -> \"" + to + "\";");
        }
      }

      /* printing nodes' body */
      for (DotNode dotNode : nodes) {
        pw.println(dotNode.toString() + "\n");
      }

      pw.println("}");
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (pw != null) {
        pw.close();
    }
  }
}

}
