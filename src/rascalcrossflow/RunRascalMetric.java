package rascalcrossflow;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.NullRascalMonitor;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class RunRascalMetric {
	private final String metricsDirectory = "metrics/";
	private final IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public Object compute(String projectPath, String metricName) {
		IRascalMonitor mon = new NullRascalMonitor();
		Evaluator eval = createRascalEvaluator(vf);
		
		ISourceLocation projectLoc = vf.sourceLocation(projectPath);
		eval.doImport(mon, metricName);
		
		return eval.call("compute", projectLoc);
	}

	private Evaluator createRascalEvaluator(IValueFactory vf) {
		GlobalEnvironment heap = new GlobalEnvironment();
		ModuleEnvironment module = new ModuleEnvironment("$rascal-crossflow$", heap);
		PrintWriter stderr = new PrintWriter(System.err);
		PrintWriter stdout = new PrintWriter(System.out);
		Evaluator eval = new Evaluator(vf, stderr, stdout, module, heap);
		Path metricsPath = Paths.get(metricsDirectory).toAbsolutePath();

		eval.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
		eval.addRascalSearchPath(vf.sourceLocation(metricsPath.toString()));

		return eval;
	}
	
	public static void main(String[] args) {
		String projectPath = "/home/dig/repositories/scava/";
		String metricName = "DummyMetric";
		RunRascalMetric run = new RunRascalMetric();
		
		// res is some kind of IValue (Rascal value)
		Object res = run.compute(projectPath, metricName);
		System.out.println("res = " + res);
	}
}
