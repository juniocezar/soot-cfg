package sootparser;

import soot.PackManager;
import soot.Transform;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.options.Options;
import soot.jimple.BinopExpr;
import soot.jimple.Constant;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.Stmt;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JCastExpr;
import soot.jimple.internal.JGotoStmt;
import soot.jimple.internal.JIdentityStmt;
import soot.jimple.internal.JIfStmt;
import soot.jimple.internal.JInstanceFieldRef;
import soot.jimple.internal.JInterfaceInvokeExpr;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JLengthExpr;
import soot.jimple.internal.JNewExpr;
import soot.jimple.internal.JReturnStmt;
import soot.jimple.internal.JReturnVoidStmt;
import soot.jimple.internal.JStaticInvokeExpr;
import soot.jimple.internal.JTableSwitchStmt;
import soot.jimple.internal.JVirtualInvokeExpr;
import soot.jimple.internal.JimpleLocal;
import soot.shimple.internal.SPhiExpr;


import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import dot.graph.DotGraph;


public class SimpleParser {

   private Integer counter, labelsID;

   private class BodyParser extends BodyTransformer {

      private UnitGraph cfg;
      private Map<Unit, String> labels;

      private List<String> units2str (List<Unit> uList, boolean map) {
         List<String> sList = new ArrayList<String>();
         for (Unit u : uList) {
            if (map) {
               String label;

               if (labels.containsKey(u)) {
                  label = labels.get(u);
               } else {
                  label = "TargetBlock." + labelsID;
                  labels.put(u, label);
                  labelsID++;
               }
               System.out.println("Created label [" + label + "] for stmt [" 
                   + u.toString() + "]");
               sList.add(label);

            } else {
               sList.add(u.toString());
            }
         }
         return sList;
     }

     @SuppressWarnings("rawtypes")
	   protected void internalTransform (Body body, String phase, java.util.Map options) {
   	    SootClass declaringClass = body.getMethod().getDeclaringClass();
   		  SootMethod meth = body.getMethod();
        List<String> lines = new ArrayList<String>();
     		PatchingChain<Unit> units =  body.getUnits();
     		labels = new HashMap<Unit, String>();
     		labelsID = 0;
     		boolean target = false, buildCurNode = false, buildNextNode = false;
     		String label = " ", cLabel = "Entry-node";

     	  String dc = body.getMethod().getDeclaringClass().getName();
     	  String method = body.getMethod().getName();

     	  cfg = new ExceptionalUnitGraph(body);
     	  DotGraph dotGraph = new DotGraph(dc + '.' + method, dc, method, dc + "." + method + "." + counter + ".dot");
     	  System.out.println("Metodo: " + method);

     	  for (Unit u : units) {
     	     if (u instanceof JGotoStmt) {
     	        List<Unit> succ = cfg.getSuccsOf(u);
     	        List<String> succLabels = units2str(succ, true);
     	     }
     	  }

    	  for (Unit u : units) {
     		   if (labels.containsKey(u)) {
     		      target = false;
     		      label = labels.get(u);
     		      System.out.println("Found target in contains! " + label);

     		      // close current block and start a new one
     		      if (buildCurNode) {
     		         lines.add("GOTO " + label + ";");
     		         dotGraph.newNode(lines, cLabel, " ", "box", "black", new ArrayList<String>(Arrays.asList(label)));
     		         lines.clear();
     		         buildCurNode = false;
     		      }

     		      cLabel = label;
     		      buildNextNode = true;
     		      //lines.clear();
     		   }

     		   System.out.println("\t" + u.toString());

     		   if (target) {
     		      target = false;
        		   if (labels.containsKey(u)) {
                  label = labels.get(u);
                  System.out.println("Found target in target! " + label);
               } else {
                  label = "TargetBlock." + labelsID;
                  labels.put(u, label);
                  System.out.println("Created label [" + label + "] for stmt [" + u.toString() + "]");
                  label = " ";
                  labelsID++;
               }
        		   // close current block and start a new one
              // dotGraph.newNode(lines, cLabel, " ", "box", "black", new ArrayList<String>(Arrays.asList(label)));

               cLabel = label;
     		   }

     		   buildCurNode = buildNextNode;

     		   if (u instanceof JGotoStmt) {
     		      List<Unit> succ = cfg.getSuccsOf(u);
     		      List<String> succLabels = units2str(succ, true);
     		      lines.add("goto " + succLabels.toString() + ";");

     		      dotGraph.newNode(lines, cLabel, " ", "box", "black", succLabels);
     		      // clear current list of units, and create a new block with a new target
     		      lines.clear();
     		      target = true;
     		      buildCurNode = false;
     		   } else if (u instanceof JIfStmt) {
        		   List<Unit> succ = cfg.getSuccsOf(u);
               List<String> succLabels = units2str(succ, true);
               JIfStmt ifStmt = (JIfStmt) u;

               lines.add("if " + ifStmt.getCondition().toString() +
                     " GOTO " + succLabels.get(1) + " : " + succLabels.get(0));

               dotGraph.newNode(lines, cLabel, " ", "box", "black", succLabels);

               // clear current list of units, and create a new block with a new target
               lines.clear();
               buildCurNode = false;
     		   } else {
     		      lines.add(u.toString() + ";");
     		   }
     		}
     		// in case there's no branch within this block
     		//if (label == "Entry-node")
     		   dotGraph.newNode(lines, cLabel, " ", "box", "black", new ArrayList<String>());

     		dotGraph.genDotFile();
         counter++;
      }
	}

   private void initiate () {
      counter = 0;
		BodyParser parser = new BodyParser();
		PackManager.v().getPack("stp").add(new Transform("stp.mysimpleparser", parser));
   }

	public static void main(String[] args) {
		// 	Inject the analysis tagger into Soot
	   SimpleParser sp = new SimpleParser();
	   sp.initiate();
		// Invoke soot.Main with arguments given

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
	
		soot.Main.main(args);
	}
}
