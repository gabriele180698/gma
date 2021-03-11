package gma.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Product
 *
 */
@Entity
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue
	private int id;
	private String name;
	private String img;
	
	public Product() {}
    public Product(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
   
}
