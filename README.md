# Solving the Kafka slow consumer problem with the Confluent Parallel Consumer

The degree of parallelism of a Kafka consumer application is usually limited by the number of partitions of a topic. 
When the processing of a single message takes significant time, a very large number of parallel threads working on a data stream is needed. 
In some cases the number of partitions cannot be increased or it is unsuitable to host a very large number of Kafka consumer instances. 
This is called the <em>Slow Consumer Problem</em>. 
Slow consumers may be when a complex database operation must be performed for each message or when some external web service must be called. 
The Confluent Parallel Consumer (https://github.com/confluentinc/parallel-consumer) can help to resolve this issue, essentially
allowing multiple threads to consume from a single partition while still guaranteeing at least once or even exactly once processing. 

In some cases messages need to be additionally processed in micro-batches, i.e. when messages are forwarded to external systems as batches
or when multiple messages are stored in a relational databases within a single transaction. 
The Confluent parallel consumer up until now does not support retrieving and processing batches of events / messages. 
This repository uses a modified version of the parallel consumer that supports batches to solve the slow consumer problem. 
The modified version is based upon previous work on a pull request of the base repository. 












