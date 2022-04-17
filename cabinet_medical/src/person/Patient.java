package person;

public class Patient extends Person  {

    public final Integer age;

    public Patient(String firstName, String lastName, String gender, Integer age) {
        super(firstName, lastName, gender);
        this.age = age;
    }

//    public String generate_doctor(List<Doctor> doctors){
//
//        if (doctors.size() == 0){
//            throw new RuntimeException("The are no doctors to treat the patient, try again later!");
//        }
//        Random random = new Random();
//        int int_doctor = random.nextInt(doctors.size());
//        return doctors.get(int_doctor).getLastName();
//    }

    public Integer getAge() {
        return age;
    }


    @Override
    public String toString() {
        return "Patient {" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                '}';
    }
}
