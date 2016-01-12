Laboratory
==========

Laboratory is a maven managed Java project that illustrates how to solve 
problem when data is huge, it is useful especially for both big data 
interviewee and interviewer.

Algorithm
---------
In the context of big data, data sets are so large or complex that 
traditional data processing algorithm are inadequate. MapReduce is the de 
facto algorithm to processing large data set on a cluster, and here, I have 
some algorithms implemented to show how to divide and conquer this kind of 
problems locally.

1.  Top K, when data set is huge but K is small, You can solve it by just 
    make a Max-heap. Read every record and compare it with the min in the 
    heap, which is the root of the heap, remove the root and add it to the 
    heap if the record is bigger. If single thread is too slow for the data 
    set, Just go to multi-thread for help, each thread get itself top K's, 
    and merge the results.
  
2.  Top K, when data is huge and K is a big number, such as K = 0.5 * n, here 
    n is the count of data records. You can not have K values in heap, then 
    Bucket is efficient to solve this kind of problem. Keep in mind not hash 
    bucket, but sorted bucket which means the values in bucket is orderless 
    but inter bucket they are ordered. Maintain a counter for each bucket 
    indexed how many values in this bucket. More detail below:
  
  *  Sampling and statistics the records, Computing the split value, make some 
     buckets.
  *  Reading a record each time. Computing which bucket the record should be 
     shuffled, put the value to corresponding queue and make counter += 1. 
  *  Each queue has its corresponding write thread which writes the values in 
     the queue to disk.
  *  Optimization can be introduced when you have more than K values in bigger 
     buckets, then values in the smaller ones can be thrown as trash.
  *  After bucketing, Get the counters, c1, c2, ... , cm, 
     if (sum(c1, c2, ... , ci) = k), Then top K values are bucket{1..i}, The 
     K'th value is the min(record : bucket[i]), else if (sum(c1, c2, ... , ci) 
     < K) && (sum(c1, c2, ... , ci+1) > K), Then the top K values are 
     bucket{1..i} and some values in bucket[i + 1]. Select algorithm will help 
     you find the top M in the bucket[i + 1], M = f(K) in O(n).
  *  If data is too huge to one reader, just create more readers, No 
     interaction among readers. ...
    
3.  Most K, K always tiny compared to n and the distinct value set is small.
    Counting. for every distinct value maintain a counter. For example: Which 
    countries have most visit to our web? 
    Record format is: id, cookie, ... , country.
    
4.  Most K, K always tiny compared to n, the distinct value set in data is 
    huge.
  *  Hash bucket(efficient!).
  *  Word count for every bucket by Hash map, get most k in every bucket.
  *  Merge sort.

  Yeah, that is what MapReduce does.

Code Compile
------------
Make sure git, java, mvn is available for current user.

```shell
cd workspace && git clone https://github.com/hainesc/laboratory
cd laboratory && mvn package
```
