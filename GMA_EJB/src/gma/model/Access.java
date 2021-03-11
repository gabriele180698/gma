package gma.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Access
 *
 */
@Entity
public class Access implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private int id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	public Access() {}
    public Access(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
     
}
