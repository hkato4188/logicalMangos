package model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor

public class Department implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deptId;
    private String deptName;

    @OneToMany(mappedBy = "department", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Teacher> teacherList = new ArrayList<>();

    public Department(String name) {
        this.deptName = name;
    }

    @Override
    public String toString() {
        return "Department{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                '}';
    }

    public void addTeacher(Teacher teacher) {
        teacherList.add(teacher);
        teacher.setDepartment(this);
    }

    public void removeTeacher(Teacher teacher) {
        teacherList.remove(teacher);
        teacher.setDepartment(null);
    }
}
