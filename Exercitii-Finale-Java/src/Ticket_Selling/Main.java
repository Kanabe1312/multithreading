package Ticket_Selling;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws Exception {

        TicketStore ticketStore = new TicketStore(50);

        Statistics statistics = new Statistics();


        Thread liveDisplay = new Thread(new LiveDisplayTask(ticketStore, statistics));

        liveDisplay.setDaemon(true);
        liveDisplay.start();


        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= 80; i++) {
            String name = String.format("Client-%03d", i);
            customers.add(new Customer(name));
        }
        System.out.println(customers.size());

    }
}
