/**
 * 
 */
package PA;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Transformer;
import soot.Unit;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.LoopNestTree;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;

/**
 * @author naman
 */
public class Driver extends SceneTransformer {

	private static Driver driver = new Driver();

	private static void printMethodInvocations(SootMethod method) {
		CallGraph cg = Scene.v().getCallGraph();

		Iterator<Edge> edgesOutOf = cg.edgesOutOf(method);
		while (edgesOutOf.hasNext()) {
			Edge edge = (Edge) edgesOutOf.next();
			SootMethod m = edge.getTgt().method();

			if (m.getName().equals("<init>")) {
				continue;
			}

			if (!m.getName().equals("<clinit>")) {
				System.out.print("\t\t" + m.getName());
				System.out.println("\tStatic?: " + m.isStatic());
				if (!m.isPrivate() && !m.isStatic()) {
					System.out.println("\t\tVirtual!");
				} else {
					System.out.println("\t\tNot Virtual!");
				}
			}
		}
	}

	@Override
	protected void internalTransform(String arg0, Map arg1) {

		String className = "PA.Test";
		System.out.println("\n\nGiven class: " + className);

		// Set up the class we’re working with
		SootClass c = Scene.v().loadClassAndSupport(className);
		c.setApplicationClass();

		System.out.print("Class fields are :");

		Chain<SootField> fields = c.getFields();
		for (SootField sootField : fields) {
			System.out.print(sootField.getSubSignature() + ", ");
		}

		System.out.println("\n");

		List<SootMethod> methods = c.getMethods();
		for (SootMethod sootMethod : methods) {

			if (sootMethod.getName().equals("<init>")) {
				continue;
			}

			System.out.println("METHOD: " + sootMethod.getName());
			System.out.println("\tReturn Type: " + sootMethod.getReturnType());

			System.out.println("\tParameter count: "
					+ sootMethod.getParameterCount());

			if (sootMethod.getParameterCount() > 0) {
				System.out.println("\t" + sootMethod.getNumberedSubSignature());
				System.out.println("\tParameter Type: "
						+ sootMethod.getParameterTypes());

			}

			/*
			 * if (!sootMethod.isConcrete()) { continue; }
			 */

			Body activeBody = sootMethod.retrieveActiveBody();

			if (activeBody != null) {
				System.out.println("\tLocal Variables");
				Chain<Local> locals = activeBody.getLocals();
				for (Local local : locals) {
					if (local.getName().contains("$")) {
						continue;
					}
					System.out.print("\t\tName: " + local.getName());
					System.out.println(", Type: " + local.getType());
				}

				System.out.println("\tMethod Invocations");

				printMethodInvocations(sootMethod);

				List<SootClass> exceptions = sootMethod.getExceptions();

				if (exceptions.size() == 0) {
					System.out.println("\tNo Exceptions thrown!");

				} else {

					System.out.println("\tThrows Exception!");

					for (SootClass sootClass : exceptions) {
						System.out.println("\t\t" + sootClass.getName());
					}
				}

				LoopNestTree loopNestTree = new LoopNestTree(activeBody);
				if (loopNestTree.size() == 0) {
					System.out.println("\tNo loop!");
				} else {
					System.out.println("\tHas a loop!");

					for (Loop loop : loopNestTree) {
						System.out.println("loop head: " + loop.getHead());
					}

				}

				boolean hasOperator = false;
				UnitGraph g = new ExceptionalUnitGraph(activeBody);
				for (Unit unit : g) {
					if (unit.toString().contains("*")) {
						System.out.println("\tContains * operator! "
								+ unit.toString());
						hasOperator = true;

					}
				}

				if (!hasOperator) {
					System.out.println("\tNo * operator!");
				}
			}

			System.out.println("");
		}
	}

	public static Transformer v() {
		return driver;
	}
}
