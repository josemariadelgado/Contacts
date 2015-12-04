
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

public class Main extends JFrame {
	JPanel panel;
	JPanel panel2;
	JList list;
	JLabel nameLabel1;
	JLabel lastNameLabel1;
	JLabel phoneLabel1;
	JLabel secondPhoneLabel1;
	JLabel emailLabel1;
	JLabel secondEmailLabel1;
	static JFrame frame;
	JPanel listPanel;
	JPanel contactPanel;
	JPanel editContactPanel;
	JTextField nameField;
	JTextField lastNameField;
	JTextField phoneField;
	JTextField secondPhoneField;
	JTextField emailField;
	JTextField secondEmailField;
	JTextField editNameField;
	JTextField editLastNameField;
	JTextField editPhoneField;
	JTextField editSecondPhoneField;
	JTextField editEmailField;
	JTextField editSecondEmailField;
	JButton addButton;
	JFrame newContactFrame;
	ImageIcon contactImage;	
	ImageIcon contactImage2;
	Contact contact;
	
	public static void main(String[] args) {
	frame = new Main();
	frame.setTitle("Contacts");
	frame.setVisible(true);

	}
	
	public void updateContactsList() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/Contacts", "root", "123");
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("Select name from contact;");
			
			DefaultListModel listModel  = new DefaultListModel();
			
			while (rs.next()) {
				
				String name = rs.getString("name");
				listModel.addElement(name);

			}
			
		    list.setModel(listModel);
			st.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
		
	}
	
	public void createContactsList() {
		
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/Contacts", "root", "123");
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("Select * from contact;");
			
			DefaultListModel listModel = new DefaultListModel();
			
			while (rs.next()) {
			
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String lastName = rs.getString("lastName");
			String phone = rs.getString("phone");
			String secondPhone = rs.getString("secondPhone");
			String email = rs.getString("email");
			String secondEmail = rs.getString("secondEmail");
			
			
			contact = new Contact(id, name, lastName, phone, secondPhone, email, secondEmail);
			System.out.println("Contact created, id = " + contact.id + ",name: " + contact.name);
			
			
			listModel.addElement(contact.name);
			
			}
			
		    list = new JList(listModel);
		    list.setFont(new Font("Ubuntu", 0, 19));
			list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION); 
			list.setBackground(new Color(246, 244, 242));
			JScrollPane scrollPane = new JScrollPane(list); 
			scrollPane.setBounds(0, 100, 350, 500); 
			listPanel.add(scrollPane); 
			st.close();
			
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
		}
	}
	
	
	public Main() {
		
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    }
		    
		} catch (Exception e) {
		    
		}
		
		
		MouseListener newContact = new MouseAdapter() {
			
			public void mouseClicked(MouseEvent arg0) {
				newContactFrame.setVisible(true);
				
				
			}
		};
		
		
		MouseListener addContact = new MouseAdapter() {
			
			public void mouseClicked(MouseEvent arg0){

				String name = nameField.getText();
				String lastName = lastNameField.getText();
				String phone = phoneField.getText();
				String secondPhone = secondPhoneField.getText();
				String email = emailField.getText();
				String secondEmail = secondEmailField.getText();
				
				
				if(name.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Name, Last Name and Phone fields cannot be empty");
					
				} else {
				
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/Contacts", "root", "123");
					Statement st = (Statement) con.createStatement();
					
					
					st.execute("insert into contact (name, lastName, phone, secondPhone, email, secondEmail) values ('" + name + "', '" + 
					             lastName + "', '" + phone + "', '" + secondPhone + "', '" + email + "', '" + secondEmail + "');");
					
					ResultSet rs = st.executeQuery("select id from contact order by id desc limit 1;");
					
					if (rs.next()) {
						int id = rs.getInt("id");
						contact = new Contact(id, name, lastName, phone, secondPhone,email, secondEmail);
						System.out.println("Contact created, id = " + contact.id + ",name: " + contact.name);
						
					}
					
					st.close();
					con.close();
					
					newContactFrame.setVisible(false);
					nameField.setText("");
					lastNameField.setText("");
					phoneField.setText("");
					secondPhoneField.setText("");
					emailField.setText("");
					secondEmailField.setText("");
					updateContactsList();
					
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					
				}
				
				}
				
			}
		};
		
		MouseListener deleteContact = new MouseAdapter() {
			
			public void mouseClicked(MouseEvent arg0) {
				
				String whereName = (String) list.getSelectedValue();
			
				
				int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this contact?", "Delete contact", 2);
				
				if (dialogResult == JOptionPane.YES_OPTION){
				
				try {
					
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/Contacts", "root", "123");
					Statement st = (Statement) con.createStatement();
					
					st.execute("delete from contact where name = '" + whereName + "';");
					
					
					st.close();
					con.close();
					updateContactsList();
					contactPanel.setVisible(false);
					frame.setTitle("Contacts");
					
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "An error has ocurred with the database connection");
					
				}
				
				}
				
			}
			
		};
		
		MouseListener editContact = new MouseAdapter() {
			
			public void mouseClicked(MouseEvent arg0) {
				
				editContactPanel.setVisible(true);
				contactPanel.setVisible(false);
				
				try {
	            	
	            	Class.forName("com.mysql.jdbc.Driver");
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/Contacts", "root", "123");
					
					String whereName = (String) list.getSelectedValue();
					
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery("Select * from contact where name = '" + whereName + "';");
					
					while (rs.next()) {
						String name = rs.getString("name");
						String lastName = rs.getString("lastName");
						String phone = rs.getString("phone");
						String secondPhone = rs.getString("secondPhone");
						String email = rs.getString("email");
						String secondEmail = rs.getString("secondEmail"); 
						
						editNameField.setText(name);
						editLastNameField.setText(lastName);
						editPhoneField.setText(phone);
						editSecondPhoneField.setText(secondPhone);
						editEmailField.setText(email);
						editSecondEmailField.setText(secondEmail);
						
						frame.setTitle("Edit Contact - " + name);
						
					    st.close();
	         
					}
					
				} catch (Exception e) {
					
					
				}
			}
		
		};
		
		MouseListener editDoneButton = new MouseAdapter() {
			
			public void mouseClicked(MouseEvent arg0) {
				
				String whereName = (String) list.getSelectedValue();
				
				String name = editNameField.getText();
				String lastName = editLastNameField.getText();
				String phone = editPhoneField.getText();
				String secondPhone = editSecondPhoneField.getText();
				String email = editEmailField.getText();
				String secondEmail = editSecondEmailField.getText();
				
				
				if(name.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Name, Last Name and Phone fields cannot be empty");
					
				} else {
				
				try {
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost/Contacts", "root", "123");
					Statement st = (Statement) con.createStatement();
					
					st.execute("update contact set name = '" + name + "', lastName = '" + lastName + "', phone = '" + 
					           phone + "', secondPhone = '" + secondPhone + "', email = '" + email + "', secondEmail = '" + 
							   secondEmail + "' where name = '" + whereName + "';");
					
					st.close();
					con.close();
					updateContactsList();
					editContactPanel.setVisible(false);
					
					frame.setTitle("Contacts");
					
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "An error has ocurred with the database connection");
					
				}
				
				}
				
			}
			
		};
		
		MouseListener editCancelButton = new MouseAdapter() {
			
			public void mouseClicked(MouseEvent arg0) {
				editContactPanel.setVisible(false);
				contactPanel.setVisible(true);
				
				String name = (String) list.getSelectedValue();
				frame.setTitle("Contact - " + name);
				
			}
			
		};
		
		
		MouseListener selectListContact = new MouseAdapter() {
		      public void mouseClicked(MouseEvent mouseEvent) {
		        list = (JList) mouseEvent.getSource();
		        if (mouseEvent.getClickCount() == 1) {
		          int index = list.locationToIndex(mouseEvent.getPoint());
		          if (index >= 0) {
		            Object o = list.getModel().getElementAt(index);
		            System.out.println("Clicked on: " + o.toString() + " " + contact.id);
		            
		            
		            contactPanel.setVisible(true);
		            
		            
		            try {
		            	
		            	Class.forName("com.mysql.jdbc.Driver");
						Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/Contacts", "root", "123");
						 
						String whereName = o.toString();
						
						Statement st = conn.createStatement();
						ResultSet rs = st.executeQuery("Select * from contact where name = '" + whereName + "';");
						
						while (rs.next()) {
							
							String name = rs.getString("name");
							String lastName = rs.getString("lastName");
							String phone = rs.getString("phone");
							String secondPhone = rs.getString("secondPhone");
							String email = rs.getString("email");
							String secondEmail = rs.getString("secondEmail"); 
							
							nameLabel1.setText(name);
							lastNameLabel1.setText(lastName);
							phoneLabel1.setText(phone);
							secondPhoneLabel1.setText(secondPhone);
							emailLabel1.setText(email);
							secondEmailLabel1.setText(secondEmail);
							
							
							frame.setTitle("Contacts - " + name);
							editContactPanel.setVisible(false);
							
							
						    st.close();
		         
						}
						
					} catch (Exception e) {
						
						
					}
		            	
		            } 
		            
		          }
		        
		        }
		      
		    };
		    
		    
		
		    
		    
		setBounds(600, 200, 700, 600);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		add(panel);
		listPanel = new JPanel();
		listPanel.setLayout(null);
		panel.add(listPanel);
		JLabel listTitle = new JLabel("All Contacts");
		listTitle.setBounds(15, 65, 125, 30);
		listTitle.setFont(new Font("FreeSans", 0, 16));
		listPanel.add(listTitle);
		listPanel.setSize(250, 600);
		JButton newContactButton = new JButton("New Contact");
		newContactButton.setBounds(10, 15, 125, 30);
		newContactButton.setFont(new Font("FreeSans", 0, 15));
		newContactButton.setFocusable(false);
		listPanel.add(newContactButton);
		JLabel label = new JLabel("Select a contact from list");
		label.setFont(new Font("Ubuntu", 0, 17));
		label.setBounds(425, 300, 250, 20);
		listPanel.add(label);
		
		contactPanel = new JPanel();
        contactPanel.setLayout(null);
        contactPanel.setBackground(Color.WHITE);
        contactPanel.setVisible(false);
		panel.add(contactPanel);
		JLabel phoneLabel = new JLabel("Phone");	
		JLabel secondPhoneLabel = new JLabel("Second Phone");
		JLabel emailLabel = new JLabel("Email");
		JLabel secondEmailLabel = new JLabel("Second Email");
		phoneLabel.setBounds(15, 150, 200, 20);
		phoneLabel.setFont(new Font("Ubuntu", 0, 15));
		contactPanel.add(phoneLabel);
		secondPhoneLabel.setBounds(15, 200, 200, 20);
		secondPhoneLabel.setFont(new Font("Ubuntu", 0, 15));
		contactPanel.add(secondPhoneLabel);
		emailLabel.setBounds(15, 300, 200, 20);
		emailLabel.setFont(new Font("Ubuntu", 0, 15));
		contactPanel.add(emailLabel);
		secondEmailLabel.setBounds(15, 350, 200, 20);
		secondEmailLabel.setFont(new Font("Ubuntu", 0, 15));
		contactPanel.add(secondEmailLabel);
		JButton editContactButton = new JButton("Edit");
		editContactButton.setBounds(40, 550, 125, 30);
		editContactButton.setFont(new Font("FreeSans", 0, 15));
		contactPanel.add(editContactButton);
		JButton deleteContactButton = new JButton("Delete");
		deleteContactButton.setFont(new Font("FreeSans", 0, 15));
		deleteContactButton.setBounds(190, 550, 125, 30);
		deleteContactButton.addMouseListener(deleteContact);
		contactPanel.add(deleteContactButton);
		contactImage = new ImageIcon(getClass().getResource("black302.png"));
		JLabel label1 = new JLabel(contactImage);
		label1.setBounds(35, 25, 64, 64);
		contactPanel.add(label1);
		contactImage2 = new ImageIcon(getClass().getResource("black302.png"));
		JLabel label2 = new JLabel(contactImage);
		label2.setBounds(480, 225, 64, 64);
		listPanel.add(label2);
		createContactsList();
		
		Font font = new Font("Ubuntu", 1, 16);
		nameLabel1 = new JLabel("");
		nameLabel1.setBounds(120, 27, 200, 30);
		nameLabel1.setFont(new Font("Ubuntu", 1, 25));
		lastNameLabel1 = new JLabel("");
		lastNameLabel1.setBounds(120, 55, 200, 20);
		lastNameLabel1.setFont(new Font("Ubuntu", 1, 15));
		phoneLabel1 = new JLabel("");
		phoneLabel1.setBounds(15, 170, 200, 20);
		phoneLabel1.setFont(font);
		secondPhoneLabel1 = new JLabel("");
		secondPhoneLabel1.setBounds(15, 220, 200, 20);
		secondPhoneLabel1.setFont(font);
		emailLabel1 = new JLabel("");
		emailLabel1.setBounds(15, 320, 300, 20);
		emailLabel1.setFont(font);
		secondEmailLabel1 = new JLabel("");
		secondEmailLabel1.setBounds(15, 370, 300, 20);
		secondEmailLabel1.setFont(font);
		contactPanel.add(nameLabel1);
		contactPanel.add(lastNameLabel1);
		contactPanel.add(phoneLabel1);
		contactPanel.add(secondPhoneLabel1);
		contactPanel.add(emailLabel1);
		contactPanel.add(secondEmailLabel1);	
		
		newContactButton.addMouseListener(newContact);
		list.addMouseListener(selectListContact);
		editContactButton.addMouseListener(editContact);
		newContactFrame = new JFrame("New Contact");
		newContactFrame.setBounds(800, 200, 300, 600);
		newContactFrame.setVisible(false);
		JPanel newContactPanel = new JPanel();
		newContactPanel.setLayout(null);
		newContactFrame.add(newContactPanel);  
		
		nameField = new JTextField();
		nameField.setBounds(30, 75, 200, 25);
		nameField.setFocusable(true);
		nameField.setColumns(10);
		lastNameField = new JTextField();
		lastNameField.setBounds(30, 145, 200, 25);
		lastNameField.setColumns(10);
		phoneField = new JTextField();
		phoneField.setBounds(30, 225, 200, 25);
		phoneField.setColumns(10);
		secondPhoneField = new JTextField();
		secondPhoneField.setBounds(30, 295, 200, 25);
		secondPhoneField.setColumns(10);
		emailField = new JTextField();
		emailField.setBounds(30, 365, 200, 25);
		emailField.setColumns(10);
		secondEmailField = new JTextField();
		secondEmailField.setBounds(30, 435, 200, 25);
		secondEmailField.setColumns(10);
		
		newContactPanel.add(nameField);
		newContactPanel.add(lastNameField);
		newContactPanel.add(phoneField);
		newContactPanel.add(secondPhoneField);
		newContactPanel.add(emailField);
		newContactPanel.add(secondEmailField);
		
		Font font1 = new Font("Ubuntu", 1, 16);
		JLabel nameLabel1 = new JLabel("Name *");
		nameLabel1.setBounds(30, 50, 100, 14);
		nameLabel1.setFont(font1);
		JLabel lastNameLabel1 = new JLabel("Last Name *");
		lastNameLabel1.setBounds(30, 120, 110, 14);
		lastNameLabel1.setFont(font1);
		JLabel phoneLabel1 = new JLabel("Phone number *");
		phoneLabel1.setBounds(30, 200, 120, 14);
		phoneLabel1.setFont(font1);
		JLabel secondPhoneLabel1 = new JLabel("Second phone number");
		secondPhoneLabel1.setBounds(30, 270, 200, 14);
		secondPhoneLabel1.setFont(font1);
		JLabel emailLabel1 = new JLabel("Email");
		emailLabel1.setBounds(30, 340, 100, 14);
		emailLabel1.setFont(font1);
		JLabel secondEmailLabel1 = new JLabel("Second email");
		secondEmailLabel1.setBounds(30, 410, 200, 14);
		secondEmailLabel1.setFont(font1);
		newContactPanel.add(nameLabel1);
		newContactPanel.add(lastNameLabel1);
		newContactPanel.add(phoneLabel1);
		newContactPanel.add(secondPhoneLabel1);
		newContactPanel.add(emailLabel1);
		newContactPanel.add(secondEmailLabel1);
		
		addButton = new JButton("Add Contact");
		addButton.setBounds(30, 500, 150, 30);
		newContactPanel.add(addButton);
		addButton.addMouseListener(addContact);    
		editContactPanel = new JPanel();
		editContactPanel.setLayout(null);
		editContactPanel.setBackground(Color.WHITE);
		editContactPanel.setVisible(false);
		panel.add(editContactPanel);      
         
		editNameField = new JTextField();
		editNameField.setBounds(30, 75, 200, 25);
		editNameField.setColumns(10);
		editLastNameField = new JTextField();
		editLastNameField.setBounds(30, 145, 200, 25);
		editLastNameField.setColumns(10);
		editPhoneField = new JTextField();
		editPhoneField.setBounds(30, 225, 200, 25);
		editPhoneField.setColumns(10);
		editSecondPhoneField = new JTextField();
		editSecondPhoneField.setBounds(30, 295, 200, 25);
		editSecondPhoneField.setColumns(10);
		editEmailField = new JTextField();
		editEmailField.setBounds(30, 365, 200, 25);
		editEmailField.setColumns(10);
		editSecondEmailField = new JTextField();
		editSecondEmailField.setBounds(30, 435, 200, 25);
		editSecondEmailField.setColumns(10);
		
		editContactPanel.add(editNameField);
		editContactPanel.add(editLastNameField);
		editContactPanel.add(editPhoneField);
		editContactPanel.add(editSecondPhoneField);
		editContactPanel.add(editEmailField);
		editContactPanel.add(editSecondEmailField);
	
		Font font2 = new Font("Ubuntu", 1, 16);
		JLabel nameLabel2 = new JLabel("Name *");
		nameLabel2.setBounds(30, 50, 100, 14);
		nameLabel2.setFont(font2);
		JLabel lastNameLabel2 = new JLabel("Last Name *");
		lastNameLabel2.setBounds(30, 120, 110, 14);
		lastNameLabel2.setFont(font2);
		JLabel phoneLabel2 = new JLabel("Phone number *");
		phoneLabel2.setBounds(30, 200, 120, 14);
		phoneLabel2.setFont(font2);
		JLabel secondPhoneLabel2 = new JLabel("Second phone number");
		secondPhoneLabel2.setBounds(30, 270, 200, 14);
		secondPhoneLabel2.setFont(font2);
		JLabel emailLabel2 = new JLabel("Email");
		emailLabel2.setBounds(30, 340, 100, 14);
		emailLabel2.setFont(font2);
		JLabel secondEmailLabel2 = new JLabel("Second email");
		secondEmailLabel2.setBounds(30, 410, 200, 14);
		secondEmailLabel2.setFont(font2);
		editContactPanel.add(nameLabel2);
		editContactPanel.add(lastNameLabel2);
		editContactPanel.add(phoneLabel2);
		editContactPanel.add(secondPhoneLabel2);
		editContactPanel.add(emailLabel2);
		editContactPanel.add(secondEmailLabel2);
		
		JButton doneButton = new JButton("Done");
		doneButton.setBounds(40, 550, 125, 30);
		doneButton.setFont(new Font("FreeSans", 0, 15));
		editContactPanel.add(doneButton);
		
		doneButton.addMouseListener(editDoneButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(190, 550, 125, 30);
		cancelButton.setFont(new Font("FreeSans", 0, 15));
		editContactPanel.add(cancelButton);
		cancelButton.addMouseListener(editCancelButton);	
			
		}
	
}
