import io.confluent.parallelconsumer.ParallelConsumerOptions;
import io.confluent.parallelconsumer.ParallelStreamProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import static io.confluent.parallelconsumer.ParallelConsumerOptions.ProcessingOrder.UNORDERED;

@Slf4j
public class ConsumerService {

    Consumer<String, String> getKafkaConsumer() {
        Properties consumerProperties = new Properties();
        consumerProperties.setProperty("bootstrap.servers", "localhost:9092");
        consumerProperties.setProperty("group.id", "pc2");
        consumerProperties.setProperty("enable.auto.commit", "false");
        consumerProperties.setProperty("auto.offset.reset", "earliest");
        consumerProperties.setProperty("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.setProperty("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<>(consumerProperties);
    }

    Producer<String, String> getKafkaProducer() {
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "localhost:9092");
        producerProperties.put("acks", "all");
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(producerProperties);
    }


    private ParallelStreamProcessor<String, String> setupParallelConsumer() {
        Consumer<String, String> kafkaConsumer = getKafkaConsumer();
        Producer<String, String> kafkaProducer = getKafkaProducer();

        ParallelConsumerOptions<String, String> consumerOptions = ParallelConsumerOptions.<String, String>builder()
                .ordering(UNORDERED)
                .maxConcurrency(10)
                .consumer(kafkaConsumer)
                .producer(kafkaProducer)
                .batchSize(5)
                .build();

        ParallelStreamProcessor<String, String> parallelStreamProcessor = ParallelStreamProcessor
                .createEosStreamProcessor(consumerOptions);

        parallelStreamProcessor.subscribe(List.of("t1"));

        return parallelStreamProcessor;
    }

    public void startConfluentParallelConsumer() {
        ParallelStreamProcessor<String, String> parallelConsumer = setupParallelConsumer();
        parallelConsumer.pollBatch(recordList ->
            recordList.forEach(record -> {
                log.info("{}, {}, {}, {}", record.offset(), new Date(record.timestamp()), new Date(), record.serializedValueSize());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            })
        );
    }

}
