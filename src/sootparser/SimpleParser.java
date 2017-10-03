package sootparser;

import soot.Body;
import soot.Unit;
import soot.Value;
import soot.Transform;
import soot.SootClass;
import soot.SootMethod;
import soot.PackManager;
import soot.BodyTransformer;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JIfStmt;
import soot.Scene;


import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import dot.graph.DotGraph;

public class SimpleParser {

   private Integer counter, labelsID;

   private class BodyParser extends BodyTransformer {

      private UnitGraph unitCFG;
      private BlockGraph blockCFG;
      private Map<Unit, String> labels;

      private String getLabel(Unit u) {
         String label;
         if (labels.containsKey(u)) {
            label = labels.get(u);
         } else {
            label = "BB" + labelsID;
            labels.put(u, label);
            labelsID++;
         }
         return label;
      }           

     @SuppressWarnings("rawtypes")
     protected void internalTransform (Body body, String phase, java.util.Map options) {        
        SootClass declaringClass = body.getMethod().getDeclaringClass();
        SootMethod method = body.getMethod();
        List<String> lines = new ArrayList<String>();
        blockCFG = new ExceptionalBlockGraph(body);
        unitCFG = new ExceptionalUnitGraph(body);
        
        String className  = declaringClass.getName();
        String methodName = method.getName();
        
        DotGraph dotGraph = new DotGraph(className + '.' + methodName, 
              className, methodName, className + "." + methodName + "." + counter + ".dot");
        
        labels = new HashMap<Unit, String>();
        labelsID = 0;
                             
        for (Block block : blockCFG.getBlocks()) {        
           Unit head = block.getHead();           
           Iterator<Unit> uIt = block.iterator();
           String label = getLabel(head);    
           List<Block> succBlocks = block.getSuccs();
           List<String> succLabels = new ArrayList<String>();
           boolean ifNode = false;
           
           //System.out.println(block);
           
           // add instructions to the node block           
           while (uIt.hasNext()) {
              Unit i = uIt.next();
              if (i instanceof JGotoStmt) {
                 JGotoStmt g2 = (JGotoStmt) i;
                 Unit target = g2.getTarget();
                 String targetLabel = getLabel(target);
                 lines.add("goto " + targetLabel + "\\l");
              } else if (i instanceof JIfStmt) {
                 JIfStmt se = (JIfStmt) i;
                 Value condition = se.getCondition();
                 Unit trueTarget = (Unit) se.getTarget();                 
                 List<Unit> succs = unitCFG.getSuccsOf(i);
                 Map<Unit, String> ids = new HashMap<Unit, String>();                
                 
                 // should iterate only twice
                 for (Unit tgt : succs) {
                    if (trueTarget == tgt) 
                       ids.put(tgt, "s0");
                    else
                       ids.put(tgt, "s1");
                 }
                 
                 String line = "if " + condition.toString() + " goto " + getLabel(trueTarget);
                 
                 line += "\\l |{##s0###T|##s1###F}";
                 lines.add(line);
                 ifNode = true;                 
              } else {
                 lines.add(i.toString());
              }
           }           
           
           // add edges from this block to succ                      
           for (Block succ : succBlocks) {
              succLabels.add(getLabel(succ.getHead()));
           }           
           
           dotGraph.newNode(lines, label, " ", "record", "black", succLabels, ifNode);
           ifNode = false;
           lines.clear();
        }
        dotGraph.genDotFile();
      }
   }

   private void initiate () {
      counter = 0;
      BodyParser parser = new BodyParser();
      PackManager.v().getPack("stp").add(new Transform("stp.mysimpleparser", parser));
   }

   public static void main(String[] args) {
      //    Inject the analysis tagger into Soot
      SimpleParser sp = new SimpleParser();
      
      //prefer Android APK files// -src-prec apk
      Options.v().set_src_prec(Options.src_prec_apk);
      
      //output as APK, too//-f J
      Options.v().set_output_format(Options.output_format_dex);
      
        // resolve the PrintStream and System soot-classes
      Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
      Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
        
      Options.v().set_verbose(false);
      Options.v().set_whole_program(false);
      Options.v().set_app(true);
      Options.v().set_via_shimple(true);
      Options.v().set_src_prec(Options.src_prec_only_class);
      Options.v().set_output_format(Options.output_format_S);
      Options.v().set_allow_phantom_refs(true);
      Options.v().set_xml_attributes(false);
      Options.v().set_whole_shimple(false);

      Options.v().setPhaseOption("jb","preserve-source-annotations:true");
      Options.v().setPhaseOption("jj","use-original-names:true");
      Options.v().set_keep_line_number(true);
      
      sp.initiate();
      // Invoke soot.Main with arguments given

   
      soot.Main.main(args);
   }
}
