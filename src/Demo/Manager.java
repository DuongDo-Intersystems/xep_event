package Demo;
import com.intersystems.xep.annotations.Id;

public class Manager {
	@Id(generated=false)
	private String ssn;
	private String mname;
	private int age;
	
	
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
}
