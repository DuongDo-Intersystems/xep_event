import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;






import com.intersystems.jdbc.IRIS;
import com.intersystems.xep.Event;
import com.intersystems.xep.EventPersister;
import com.intersystems.xep.PersisterFactory;
import com.intersystems.xep.XEPException;
import com.intersystems.jdbc.IRISConnection;

public class xep {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String user = "SuperUser";
		String pass = "SYS";
		String server = "localhost";
		int port = 51775;
		
		try {
			//Connect to database using EventPersister, which is based on IRISDataSource
	        EventPersister xepPersister = PersisterFactory.createPersister();
	        xepPersister.connect(server,port,"User",user,pass); 
	        System.out.println("Connected to InterSystems IRIS via JDBC."); 
	        xepPersister.deleteExtent("Demo.Manager");   // remove old test data
	        xepPersister.importSchemaFull("Demo.Manager");   // import flat schema
	        xepPersister.deleteExtent("Demo.Department");   // remove old test data
	        xepPersister.importSchema("Demo.Department");   // import flat schema
	       
	        //***Initializations***
	        //Create XEP Event for object access
	        Event xepEvent = xepPersister.getEvent("Demo.Department");

	        //Create JDBC statement object for SQL and IRIS Native access
	        Statement myStatement = xepPersister.createStatement();
	        
	        //Create IRIS Native object
	        IRIS irisNative = IRIS.createIRIS((IRISConnection)xepPersister);
	        
	        // Populate 5 airport objects and save to the database using XEP
	        populateDepartments(xepEvent);
			
			// Get all airports using JDBC
	        getDepartments(myStatement);
		    
			xepEvent.close();
		    xepPersister.close();
						
		} catch (SQLException e) {
			 System.out.println("Error: " + e.getMessage());
		}  
	}
	
	// Populate department data
	public static void populateDepartments(Event xepEvent){
		Demo.Department[] DepartmentArray = new Demo.Department[2];
		
		Demo.Department newDepartment = new Demo.Department();
		newDepartment.setName("Online Learning");
		newDepartment.setCode("OL");
		Demo.Manager mgr = new Demo.Manager();
		mgr.setSsn("123456789");
		mgr.setMname("Doug");
		mgr.setAge(50);
		newDepartment.setManager(mgr);
		DepartmentArray[0] = newDepartment;
		
		Demo.Department newDepartment2 = new Demo.Department();
		newDepartment2.setName("Online Playing");
		newDepartment2.setCode("OP");
		Demo.Manager mgr2 = new Demo.Manager();
		mgr2.setSsn("987654321");
		mgr2.setMname("Duong");
		mgr2.setAge(23);
		newDepartment2.setManager(mgr2);
		DepartmentArray[1] = newDepartment2;
		
		// Store the whole array will not work
		xepEvent.store(DepartmentArray);
		
		// Store each element in array will work
		//for (Demo.Department d : DepartmentArray){
		//	xepEvent.store(d);
		//}
		System.out.println("Stored 5 departments");
	}
	
	///Display all Departments using JDBC
		public static void getDepartments(Statement myStatement)
		{
			ResultSet myRS;
			try {
				myRS = myStatement.executeQuery("SELECT name, code, manager->ssn, manager->mname, manager->age FROM demo.Department");
			
				System.out.println("Name\t\t\tCode\t\tManager");
				while(myRS.next())
				{
					System.out.println(
							myRS.getString("name") + "\t\t" + 
							myRS.getString("code")+"\t\t" + 
							myRS.getString("mname") + ", " + 
							myRS.getString("ssn") + ", " + 
							myRS.getString("age")
							);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
}
