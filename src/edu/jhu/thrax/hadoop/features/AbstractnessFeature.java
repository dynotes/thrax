package edu.jhu.thrax.hadoop.features;

import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import edu.jhu.thrax.hadoop.datatypes.RuleWritable;
import edu.jhu.thrax.util.Vocabulary;

public class AbstractnessFeature implements SimpleFeature {
  private static final Text LABEL = new Text("Abstract");
  private static final IntWritable ZERO = new IntWritable(0);
  private static final IntWritable ONE = new IntWritable(1);

  public void score(RuleWritable r, Map<Text, Writable> map) {
    for (int word : r.source) {
      if (!Vocabulary.nt(word)) {
        map.put(LABEL, ZERO);
        return;
      }
    }
    for (int word : r.target) {
      if (!Vocabulary.nt(word)) {
        map.put(LABEL, ZERO);
        return;
      }
    }
    map.put(LABEL, ONE);
    return;
  }

  public void unaryGlueRuleScore(Text nt, Map<Text, Writable> map) {
    map.put(LABEL, ONE);
  }

  public void binaryGlueRuleScore(Text nt, Map<Text, Writable> map) {
    map.put(LABEL, ONE);
  }
}
