package service;

import interfaces.ErrorMessage;
import medical.Appointment;
import medical.MedicalHistory;
import medical.Ticket;
import person.Patient;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalHistoryService implements ErrorMessage {

    private MedicalHistoryService(){}

    private static class SINGLETON_HOLDER{
        private static final MedicalHistoryService INSTANCE = new MedicalHistoryService();
    }

    public static MedicalHistoryService getInstance(){
        return SINGLETON_HOLDER.INSTANCE;
    }

    // din interfata
    @Override
    public String errorMessage(String name) {
        return "No patient found for the given name " + name + "!";
    }

    // intoarce pozitia in istoric a pacientului
    public static Integer getIndexOfMedicalHistory(List<MedicalHistory> medicalHistories, String patientName){
        Integer  ind = -1;
        for (MedicalHistory medical: medicalHistories) {
            if(medical.getPatient().getLastName().equalsIgnoreCase(patientName)) {
                ind = medicalHistories.indexOf(medical);
            }
        }
        return  ind;
    }

    // verifica daca pacientul dat cu nume exista in istoric
    public boolean patientInDatabase(List<MedicalHistory> medicalHistories, String patientName){
        for (MedicalHistory medical: medicalHistories) {
            if(medical.getPatient().getLastName().equalsIgnoreCase(patientName)) {
                return true;
            }
        }
        throw new RuntimeException(errorMessage(patientName));
    }

    // sigur exista in istoric pacientul dat
    public void addAppointment(List<MedicalHistory> medicalHistories, String  patientName, Appointment appointment){
        Integer index = getIndexOfMedicalHistory(medicalHistories, patientName);

        medicalHistories.get(index).addAppointment(appointment);
    }

    public List<String> diseasesByPatient(List<MedicalHistory> medicalHistories, String patientName) {
        for (MedicalHistory medical : medicalHistories) {
            if (medical.getPatient().getLastName().equalsIgnoreCase(patientName)) {
                return medical.getAppointments().stream()
                        .flatMap(appointment -> appointment.getDisease().stream())
                        .collect(Collectors.toList());
            }
        }
        System.out.println(errorMessage(patientName));
        return null;
    }

    // le afiseaza in ordine dupa doctor si data
    public List<Appointment> appointmentsByPatient(List<MedicalHistory> medicalHistories, String patientName){
        return medicalHistories.stream()
                .filter(medical -> medical.getPatient().getLastName().equalsIgnoreCase(patientName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(errorMessage(patientName)))
                .getAppointments()
                .stream()
                .sorted(Comparator.comparing(Appointment::getDoctor_name)
                        .thenComparing(Appointment::getDate))
                .collect(Collectors.toList());
    }

    public List<Ticket> ticketsByPatientSortedByDate(List<MedicalHistory> medicalHistories, String patientName) {
        List<Ticket> tickets = medicalHistories.stream()
                .filter(medicalHistory -> medicalHistory.getPatient().getLastName().equalsIgnoreCase(patientName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(errorMessage(patientName)))
                .getAppointments().stream()
                .map(appointment -> appointment.getTicket())
                .collect(Collectors.toList());

        Collections.sort(tickets, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return  tickets;
    }

    // Alphabetical list of patients
     public void printAllPatients(List<MedicalHistory> medicalHistories) {
         medicalHistories.stream()
                 .map(mHistory -> mHistory.getPatient())
                 .sorted(Comparator.comparing(Patient::getLastName))
                 .forEach(System.out::println);
     }
}
