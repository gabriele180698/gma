package gma.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * Entity implementation class for Entity: user
 *
 */
@Entity
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private int id;
	private String username;
	private String email;
	private String password;
	private int type;

	public User() {}
    public User(int id) {
        this.id = id;
    }
	
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
   
}
