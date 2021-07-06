import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Barber implements Runnable {

	BarberShop barberShop = null;

	public Barber(BarberShop barberShop) {
		this.barberShop = barberShop;
	}

	@Override
	public void run() {

		sleep();

		while (true) {
			barberShop.doHairCut();
		}

	}

	private void sleep() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

class Customer implements Runnable {

	BarberShop barberShop = null;
	
	private String customerName;
	
	public Customer(BarberShop barberShop) {
		this.barberShop = barberShop;
	}
	
	@Override
	public void run() {
		barberShop.add(this);
	}


	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
}


class BarberShop {
	
	int waitingChairs;
	LinkedList<Customer> waitingCustomers = null;
	
	public BarberShop(int chairs) {
		waitingChairs = chairs;
		waitingCustomers = new LinkedList();
	}
	
	public void doHairCut() {

		Customer customer;
		synchronized (waitingCustomers) {
			System.out.println("Barber is waiting for customer.");
			while (waitingCustomers.isEmpty()) {
				try {
					waitingCustomers.wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("Barber found customer.");
			customer = waitingCustomers.poll();
		}

		System.out.println("doing haircut for "+customer.getCustomerName());
		sleep();
		System.out.println("haircut is done for "+customer.getCustomerName());

	}

	public void add(Customer customer)
    {
        System.out.println(Thread.currentThread().getName()+ " entering the shop");
 
        synchronized (waitingCustomers)
        {
            if(waitingCustomers.size() == waitingChairs)
            {
                System.out.println("No seat available for "+customer.getCustomerName());
                System.out.println(customer.getCustomerName()+" left the shop");
                return ;
            }
 
            waitingCustomers.offer(customer);
            System.out.println(customer.getCustomerName()+" got the seat");
             
            if(!waitingCustomers.isEmpty())
                waitingCustomers.notify();
        }
        
    }
        
	private void sleep() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}



class Customers implements Runnable {

	BarberShop barberShop = null;
	public Customers(BarberShop barberShop) {
		this.barberShop = barberShop;
	}

	@Override
	public void run() {
		while (true) {
			Customer customer = new Customer(barberShop);
			Thread customerThread = new Thread(customer);
			customer.setCustomerName(customerThread.getName());
			customerThread.start();
			sleep();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

public class BarberShopDemo {

	public static void main(String[] args) {

		BarberShop barberShop = new BarberShop(3);

		Barber barber = new Barber(barberShop);
		Customers customers = new Customers(barberShop);

		Thread barberThread = new Thread(barber);
		Thread customersThread = new Thread(customers);

		barberThread.start();
		customersThread.start();
	}

}

