package edu.jhu.thrax.hadoop.features.annotation;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer.Context;

import edu.jhu.thrax.hadoop.datatypes.Annotation;
import edu.jhu.thrax.hadoop.datatypes.RuleWritable;
import edu.jhu.thrax.hadoop.jobs.ThraxJob;
import edu.jhu.thrax.util.Vocabulary;

@SuppressWarnings("rawtypes")
public class UnalignedTargetCounterFeature implements AnnotationFeature {
  private static final Text LABEL = new Text("UnalignedTarget");
  private static final IntWritable ZERO = new IntWritable(0);

  public Text getName() {
    return LABEL;
  }

  public IntWritable score(RuleWritable r, Annotation annotation) {
    byte[] e2f = annotation.e2f().points;
    int[] tgt = r.target;

    int count = 0;
    int i = 0, j = 0;
    for (i = 0; i < tgt.length; ++i) {
      if (Vocabulary.nt(tgt[i])) continue;
      if (j >= e2f.length || i != e2f[j]) count++;
      while (j < e2f.length && e2f[j] <= i)
        j += 2;
    }
    return new IntWritable(count);
  }

  public void unaryGlueRuleScore(Text nt, Map<Text, Writable> map) {
    map.put(LABEL, ZERO);
  }

  public void binaryGlueRuleScore(Text nt, Map<Text, Writable> map) {
    map.put(LABEL, ZERO);
  }

  @Override
  public void init(Context context) throws IOException, InterruptedException {}

  @Override
  public Set<Class<? extends ThraxJob>> getPrerequisites() {
    return null;
  }
}
