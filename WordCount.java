/* 

Map reduction - Inverted Indexer
Information Retrieval and Web Search Engines
Hadoop Map Reduction

*/

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class WordCount 
{
 	public static class TokenizerMapper extends Mapper<Object, Text, Text, Text>
 	{
        		//private final static IntWritable one = new IntWritable(1);
        		private Text word = new Text();

        		public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
        		{
         			// To get the document id from the text filename
        			FileSplit fileSplitObject = (FileSplit)context.getInputSplit();
        			String filePath = fileSplitObject.getPath()
                    String fileName = filePath.getName();
        			//store the docID after splitting full filename
        			Text docID = new Text(fileName.split("\\.")[0]);

  			        StringTokenizer itr_var = new StringTokenizer(value.toString());
       	 		  while (itr_var.hasMoreTokens()) 
       	 		  {
              			word.set(itr_var.nextToken());
              			//store docID with each word
              			context.write(word, docID);
               			// context.write(word, new IntWritable(Integer.parseInt(docID)));
            		}
        		} 
   	} 

 	public static class IntSumReducer extends Reducer<Text,Text,Text,Text> 
 	{
   		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException 
   		{
                String output = "";
      			HashMap<Integer, Integer> wordCounter = new HashMap<Integer, Integer>();
      			Set keys = wordCounter.keySet();
      			
      			//int sum = 0;
      			// sum += val.get();
     			// result.set(sum);

     		 	for (Text value : values) 
     		 	{
            Integer v = Integer.parseInt(value.toString());    
            IntWritable val = new IntWritable(v);
       			 	if(wordCounter.containsKey(val.get()))
        				// if the integer doc id is already present
          					wordCounter.put(val.get(), wordCounter.get(val.get()) + 1);
        				else
        				//else add it into the hashmap
          					wordCounter.put(val.get(), 1);   
      			}

      			for (Iterator i = keys.iterator(); i.hasNext();  ) 
      			{
        				Integer docID = (Integer)i.next();
        				Integer counts = wordCounter.get(docID);
        				output = output + docID.toString() + ":" + counts.toString() + "\t";
      			}
     		
     		 	result.set(countString.trim());
     		 	context.write(key, result);
      			context.write(key, new Text(output.trim()));
    		}
  	}

 	 public static void main(String[] args) throws Exception 
  	{
    		Configuration conf = new Configuration();
    		Job job = Job.getInstance(conf, "word count");
    		job.setJarByClass(WordCount.class);
    		job.setMapperClass(TokenizerMapper.class);

    		job.setCombinerClass(IntSumReducer.class);
    		job.setReducerClass(IntSumReducer.class);    
    		job.setMapOutputKeyClass(Text.class);
    		ob.setMapOutputValueClass(IntWritable.class);
    		job.setOutputKeyClass(Text.class);
    		job.setOutputValueClass(Text.class);
    		FileInputFormat.addInputPath(job, new Path(args[0]));
    		FileOutputFormat.setOutputPath(job, new Path(args[1]));
    		System.exit(job.waitForCompletion(true) ? 0 : 1);
  	}
}
