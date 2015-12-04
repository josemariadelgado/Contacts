import java.util.concurrent.atomic.AtomicInteger;


public class Contact {
	int id;
	String name;
	String lastName;
	String phone;
	String secondPhone;
	String email;
	String secondEmail;

	
	public Contact(int id, String name, String lastName, String phone, String secondPhone, String email, String secondEmail) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.phone = phone;
		this.secondPhone = secondPhone;
		this.email = email;
		this.secondEmail = secondEmail;	
		
	}
	
	
}
