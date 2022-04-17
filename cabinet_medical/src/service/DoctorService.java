package service;

import interfaces.ErrorMessage;
import person.Doctor;

import java.util.Comparator;
import java.util.List;

public class DoctorService implements ErrorMessage {

    private DoctorService(){}

    private static class SINGLETON_HOLDER{
        private static final DoctorService INSTANCE = new DoctorService();
    }

    public static DoctorService getInstance(){
        return DoctorService.SINGLETON_HOLDER.INSTANCE;
    }

    // din interfata
    @Override
    public String errorMessage(String name) {
        return "No doctor found for the given name " + name + "!";
    }

    public void printAllDoctors(List<Doctor> doctors){
        doctors.stream()
                .sorted(Comparator.comparing(Doctor::getLastName))
                .forEach(System.out::println);
    }

    public void changeSalaryToDoctor(List<Doctor> doctors, String doctorName, Double salary){
        doctors.stream()
                .filter(doctor -> doctor.getLastName().equalsIgnoreCase(doctorName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(errorMessage(doctorName)))
                .setSalary(salary);

    }

    public Doctor selectDoctorByName(List<Doctor> doctors, String doctorName){
        return doctors.stream()
                .filter(doctor -> doctor.getLastName().equalsIgnoreCase(doctorName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(errorMessage(doctorName)));

    }

    public Boolean doctorInDatabase(List<Doctor> doctors, String doctorName){
        for (Doctor doctor : doctors){
            if(doctor.getLastName().equalsIgnoreCase(doctorName)){
                return true;
            }
        }
        throw new RuntimeException(errorMessage(doctorName));
    }


}
