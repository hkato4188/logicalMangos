package model;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class Teacher implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue( strategy=GenerationType.IDENTITY )
    private int teacherId;
    private String salary;
    private String TeacherName;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "deptId")
    private Department department;

    public Teacher(String teacherName) {
        TeacherName = teacherName;
    }

    public Teacher(String salary, String teacherName) {
        this.salary = salary;
        TeacherName = teacherName;
    }
}
