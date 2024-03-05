# Main Test Scenario

Use this code sample to test the project. The scenario is as follows:

1. Create the table STUDENT.
2. Insert 2 students into the table.
3. Select all students to view the inserted students.
4. Select a student by AM.
5. Delete a student.
6. Select all students again to view the remaining student.

```java
 public static void main(String[] args) {
        // Step 1: Create Table
        createTable();

        // Step 2: Insert Student
        StudentGenerated student1 = new StudentGenerated("mpsp2317", "mpsp2317@unipi.gr", 2, "Kalliopi Koutsiouki", true);
        StudentGenerated student2 = new StudentGenerated("mpsp2330", "mpsp2330@unipi.gr", 2, "Panagiotis Papakostas", false);
        insertStudent(student1);
        insertStudent(student2);

        // Step 3: Select All Students
        System.out.println("All Students:");
        List<StudentGenerated> allStudents = getAllStudents();
        allStudents.stream()
                .forEach(System.out::println);

        // Step 4: Select Student by AM
        System.out.println("\nStudent with AM 'mpsp2330':");
        StudentGenerated studentByAM = getStudentByAM("mpsp2330");
        System.out.println(studentByAM);

        // Step 5: Delete Student
        System.out.println("\nDeleting Student with AM 'mpsp2330'");
        int rowsAffected = deleteStudent("mpsp2330");
        System.out.println("Rows affected: " + rowsAffected);

        // Step 6: Select All Students after deletion
        System.out.println("\nAll Students after deletion:");
        List<StudentGenerated> allStudentsAfterDeletion = getAllStudents();
        allStudentsAfterDeletion.stream()
                .forEach(System.out::println);
    }