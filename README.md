# Rascal-Crossflow

A simple way to invoke Rascal metrics on a local project. Metrics must be stored in `metrics/<MetricName>.rsc`, and contain a method `compute` that takes as single argument a source location (the path to the project) and returns an arbitrarily-typed value. The following metric, for instance, simply counts the number of entries in the project's root directory:

```
module DummyMetric

import IO;
import List;

int compute(loc project) {
	return size(listEntries(project));
}
```

To invoke the metric from some Java code, use:

`RunRascalMetric::compute("<ProjectPath>", "<MetricName>")`

which returns a generic `Object`. According to the return type of the metric, it might be an IInteger, IString, IMap, etc. (Rascal's own primitive datatypes).

# Example

```
public static void main(String[] args) {
	String projectPath = "/home/dig/repositories/scava/";
	String metricName = "DummyMetric";
	RunRascalMetric run = new RunRascalMetric();
	
	// res is some kind of IValue (Rascal value)
	Object res = run.compute(projectPath, metricName);
	System.out.println("res = " + res);
}
```
