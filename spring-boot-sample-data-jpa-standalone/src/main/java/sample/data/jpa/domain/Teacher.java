package sample.data.jpa.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = "findTeachers", query="Select t from Teacher t")
public class Teacher extends Person{

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Slot> slots = new ArrayList<>();

    //empty constructor required
    public Teacher(){}

    public Teacher(String firstName, String lastName){
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public void addSlot(Slot slot){
        this.slots.add(slot);
        slot.setTeacher(this);
    }

}
