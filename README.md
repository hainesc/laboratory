Laboratory
==========

Laboratory is a maven managed Java project that illustrates how to solve 
problem when data is huge, it is useful especially for big data interviewer.

Algorithm
---------
When data is huge, load all of your data in memory is impossible, Algorithm 
such as quick sort can not be run. I have some algorithm implemented below.

1.  Top K, when data is huge but K is small, You can solve this problem by 
    just make a Max-heap, read every record and compare it with the min in the 
    heap, it is always the root of the heap, replace the root if the record is 
    bigger, and adjust the heap(maybe auto done by adding). If single thread is 
    to slow for you, Just go to multi-thread for help, each thread get itself 
    top Ks, and merge the results.
  
2.  Top K, when data is huge and K is huge too, such as k = 0.5 * n, n is the 
    count of data records. You can not have K values in heap, in memory, then 
    Bucket is efficient to solve this problem. Keep in mind no hash bucket, 
    but sorted bucket which mean the values in bucket is orderless but inter 
    bucket, they are ordered. Maintain a counter for each bucket indexed how 
    many value in this bucket. 
  
  *  Sampling and statistics the records, Computing the split value, make some
     buckets.
  *  Reading a record each time. Computing which bucket the record should be 
     shuffled, put the value to corresponding queue. counter += 1. 
  *  Each queue has its corresponding write thread which writes the value to 
     disk.
  *  Optimization can be introduced when you have more than k values in bigger
     buckets, then the smaller ones can be thrown as trash.
  *  After bucketing, Get the counters, c1, c2, ... , cm, 
     if (sum(c1, c2, ... , ci) = k), Then top K values are bucket{1..i}, The 
     Kth value is the min(record : bucket[i]), else if (sum(c1, c2, ... , ci) 
     < K) && (sum(c1, c2, ... , ci+1) > K), Then the top K values are 
     bucket{1..i} and some values in bucket[i + 1]. Run Half Quick sort for 
     bucket[i + 1] the find out the mth ele, m = f(K). O(n)
  *  If data is too huge to one reader, just create more readers thread, No 
     interaction among readers. ...
    
3.  Most K, K always tiny compared to n and the distinct value set is small.
    Counting. for every distinct value maintain a counter. For example: Which 
    countries have most visit to our web? 
    Record format is: id, cookie, ... , country.
    
4.  Most K, K always tiny compared to n, the distinct value set in data is huge.
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

