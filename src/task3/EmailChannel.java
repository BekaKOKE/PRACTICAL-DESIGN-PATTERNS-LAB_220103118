package task3;

public class EmailChannel implements MessageChannel {
    @Override
    public void transmit(String recipient, String message) {
        System.out.printf("[EMAIL PROTOCOL -> %s]%n%s%n", recipient, message);
    }
}
