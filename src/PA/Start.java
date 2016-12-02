package PA;

import soot.Pack;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

public class Start {
	public static void main(String[] args) {

		if (args.length == 0) {
			System.err.println("Usage: java Start [options] classname");
			System.exit(0);
		}

		Pack wjtp = PackManager.v().getPack("wjtp");
		wjtp.add(new Transform("wjtp.profiler", Driver.v()));

		Options.v().setPhaseOption("jb", "use-original-names:true");
		Options.v().set_output_format(Options.output_format_J);
		// Options.v().set_src_prec(Options.src_prec_java);
		Options.v().set_whole_program(true);
		Options.v().set_main_class(args[0]);
		soot.Main.main(args);

	}
}
