package edu.jhu.thrax.hadoop.jobs;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import edu.jhu.thrax.hadoop.distributional.DistributionalContextMapper;
import edu.jhu.thrax.hadoop.distributional.SignatureWritable;

public class DistributionalContextSortingJob extends ThraxJob {

  private static HashSet<Class<? extends ThraxJob>> prereqs =
      new HashSet<Class<? extends ThraxJob>>();

  public Job getJob(Configuration conf) throws IOException {
    Job job = new Job(conf, "sorting");

    job.setJarByClass(DistributionalContextMapper.class);
    job.setMapperClass(Mapper.class);
    job.setReducerClass(Reducer.class);

    job.setInputFormatClass(SequenceFileInputFormat.class);
    job.setMapOutputKeyClass(SignatureWritable.class);
    job.setMapOutputValueClass(NullWritable.class);
    job.setOutputKeyClass(SignatureWritable.class);
    job.setOutputValueClass(NullWritable.class);
    job.setOutputFormatClass(SequenceFileOutputFormat.class);

//    int numReducers = conf.getInt("thrax.reducers", 4);
    job.setNumReduceTasks(1);

    String workDir = conf.get("thrax.work-dir");
    FileInputFormat.setInputPaths(job, new Path(workDir + "signatures"));

    int maxSplitSize = conf.getInt("thrax.max-split-size", 0);
    if (maxSplitSize != 0) FileInputFormat.setMaxInputSplitSize(job, maxSplitSize);

    String outputPath = conf.get("thrax.outputPath", "");
    FileOutputFormat.setOutputPath(job, new Path(outputPath));

    return job;
  }

  public String getName() {
    return "sorting";
  }

  public Set<Class<? extends ThraxJob>> getPrerequisites() {
    prereqs.add(DistributionalContextExtractionJob.class);
    return prereqs;
  }

  public String getOutputSuffix() {
    return null;
  }
}
