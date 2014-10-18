package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Sergey on 10/18/2014.
 */
@Entity
public class User extends Model {

    @Id
    public String id;

    public String name;
    
}
