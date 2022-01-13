import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        ConsumerService consumerService = new ConsumerService();
        consumerService.startConfluentParallelConsumer();
    }

}

