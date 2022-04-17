import medical.*;
import person.Doctor;
import person.Patient;
import service.DoctorService;
import service.MedicalHistoryService;

import java.util.*;


public class Main {
    public static void main(String[] args) {

        MedicalHistoryService medicalHistoryService = MedicalHistoryService.getInstance();  // pentru functii legate de lista de istorice / pacienti
        DoctorService doctorService = DoctorService.getInstance();   // pentru functii legate de lista de doctori
        List<MedicalHistory> medicalHistories = new ArrayList<MedicalHistory>();

        // initial in lista sunt doar 2 doctori
        List<Doctor> doctors = new ArrayList<Doctor>();
        doctors.add(new Doctor("fdoctor1", "ldoctor1", "gender1", 122.9));
        doctors.add(new Doctor("fdoctor2", "ldoctor2", "gender2", 139.5));

        // adaug un patient in lista de istorice
        MedicalHistory medicalHistory = new MedicalHistory(new Patient("fpatient1", "lpatient1", "gender1", 21));
        Appointment appointment = new Appointment("ldoctor1");
        medicalHistory.addAppointment(appointment);
        medicalHistories.add(medicalHistory);

        Scanner sc = new Scanner(System.in);
        String input = "y";

        while(true){
            System.out.println("Choose a user mode: admin or doctor ('no' to exit)");

            String user = sc.nextLine();

            if(user.equalsIgnoreCase("no")){
                break;
            }
            if(user.equalsIgnoreCase("admin")){
                /// admin: poate sa vada totul si sa stearga/adauge doctori

                while(true) {

                    System.out.println("Choose one of the following options: ");
                    System.out.println("1. Alphabetical list of doctors");
                    System.out.println("2. Alphabetical list of patients");
                    System.out.println("3. Add doctor");
                    System.out.println("4. Delete doctor");
                    System.out.println("5. Change the salary for a given doctor");
                    System.out.println("0. Exit application");

                    String opt = sc.nextLine();

                    if (opt.equalsIgnoreCase("0")) {
                        break;
                    }

                    switch (opt) {
                        case "1": {  // Alphabetical list of doctors
                            doctorService.printAllDoctors(doctors);
                            break;
                        }

                        case "2": {  // Alphabetical list of patients
                            medicalHistoryService.printAllPatients(medicalHistories);
                            break;
                        }

                        case "3": {  // Add doctor
                            System.out.println("Enter 'y' to continue!");
                            input = sc.nextLine();

                            if(input.equalsIgnoreCase("y")){
                                System.out.println("Insert doctor Firstname");
                                String firstname = sc.nextLine();

                                System.out.println("Insert doctor Lastname");
                                String lastname = sc.nextLine();

                                System.out.println("Insert doctor gender");
                                String gender = sc.nextLine();

                                System.out.println("Insert doctor salary");
                                Double salary = Double.parseDouble(sc.nextLine());

                                Doctor doctor = new Doctor(firstname, lastname, gender, salary);
                                doctors.add(doctor);
                            }

                            break;
                        }

                        case "4":{  // Delete doctor given by name
                            System.out.println("Doctor name");
                            String doctor_Lastname = sc.nextLine();

                            try{
                                doctors.remove(doctorService.selectDoctorByName(doctors, doctor_Lastname));
                            }catch (Exception exception) {
                                System.out.println(exception.getMessage());
                            }
                            break;
                        }

                        case "5":{  // Change the salary for a given doctor
                            System.out.println("Doctor name");
                            String doctor_Lastname = sc.nextLine();

                            System.out.println("New salary");
                            Double salary = Double.parseDouble(sc.nextLine());

                            try{
                                doctorService.changeSalaryToDoctor(doctors, doctor_Lastname, salary);
                            }catch (Exception exception){
                                System.out.println(exception.getMessage());
                            }

                            break;
                        }

                        default:{
                            System.out.println("Invalid Option!!");
                        }
                    }
                }
            }

            if(user.equalsIgnoreCase("doctor")){
                /// doctor: se ocupa de patienti si poate sa vada doar ce are legatura cu ei
                Boolean ok = false;

                String doctorName = "";   // numele va mai fi folosit mai jos
                input = "y";
                while(!ok){

                    if(!input.equalsIgnoreCase("y")){
                        break;
                    }
                    System.out.println("Enter your lastname");
                    doctorName = sc.nextLine();

                    try{
                        ok = doctorService.doctorInDatabase(doctors, doctorName);

                    }catch (Exception exception){

                        System.out.println(exception.getMessage());
                        System.out.println("Do you want to try again? (y)");
                        input = sc.nextLine();
                    }
                }

                while(true && ok) {

                    System.out.println("Choose one of the following options: ");
                    System.out.println("1. Alphabetical list of patients");
                    System.out.println("2. New patient");
                    System.out.println("3. Existent patient");
                    System.out.println("4. Find past diseases for a given patient");
                    System.out.println("5. List all tickets for a given patient sorted by date");
                    System.out.println("6. List all appointments for a given patient (sorted by doctor, then by date)");
                    System.out.println("0. Exit application");

                    String opt = sc.nextLine();

                    if (opt.equalsIgnoreCase("0")) {
                        break;
                    }

                    switch (opt) {

                        case "1": {  // Alphabetical list of patients
                            medicalHistoryService.printAllPatients(medicalHistories);
                            break;
                        }

                        case "2":{  // New patient
                            System.out.println("Do you want to continue? (y)");
                            input = sc.nextLine();

                            if(!input.equalsIgnoreCase("y")){
                                break;
                            }

                            System.out.println("Insert patient Firstname");
                            String fname = sc.nextLine();

                            System.out.println("Insert patient Lastname");
                            String lname = sc.nextLine();

                            System.out.println("Insert patient gender");
                            String gender = sc.nextLine();

                            System.out.println("Insert patient age");
                            Integer age = Integer.parseInt(sc.nextLine());  // se comporta dubios sc.next impreuna cu sc.nextLine / sc.nextLine si sc.nextInt

                            Patient patient = new Patient(fname, lname, gender, age);

                           // parametri pentru un Appointment => ii definesc inainte pentru ca doctorul poate sa omita sa completeze toate variabilele
                            Appointment patientAppointment = new Appointment(doctorName);

                            while(true){

                                System.out.println("Insert details about the appointment");
                                System.out.println("a. Insert disease");
                                System.out.println("b. Insert prescription");
                                System.out.println("c. Insert ticket");
                                System.out.println("d. finish details about the patient");

                                String subOpt = sc.nextLine();

                                if (subOpt.equalsIgnoreCase("d")){

                                    try{ // verific ca nu cumva un asa zis patient nou sa fie deja existent in istorice
                                        medicalHistoryService.patientInDatabase(medicalHistories, patient.getLastName());
                                        System.out.println(patient.getLastName() + " was allready in.");

                                    }catch (Exception e){  // nu este pacientul in istoric
                                        medicalHistories.add(new MedicalHistory(patient));
                                    }

                                    // al doilea parametru este o lista de Appointment => este mai usor sa folosesc constructorul cu un parametru si apoi sa adaug si Appointmentdoctor
                                    medicalHistoryService.addAppointment(medicalHistories, lname, patientAppointment);
                                    System.out.println("Saving all the details about the patient " + patient.getFirstName() + " " + patient.getLastName());
                                    break;
                                }

                                switch (subOpt){
                                    case "a":{
                                        while(true) {
                                            System.out.println("a. Add disease");
                                            System.out.println("b. Exit");

                                            String add = sc.nextLine();

                                            if(add.equalsIgnoreCase("a")){
                                                System.out.println("Insert disease");
                                                String disease = sc.nextLine();
                                                patientAppointment.addDisease(disease);
                                                break;
                                            }
                                            else if(add.equalsIgnoreCase("b")){
                                                    break;
                                            }
                                            else{
                                                System.out.println("Choose 'a' or 'b'");
                                            }
                                        }
                                        break;
                                    }
                                    case "b":{
                                        while(true) {
                                            System.out.println("a. Add medicine to prescription");
                                            System.out.println("b. Exit");

                                            String add = sc.nextLine();

                                            if(add.equalsIgnoreCase("a")){
                                                System.out.println("Insert medicine name");
                                                String name = sc.nextLine();

                                                System.out.println("Insert how to use it");
                                                String use = sc.nextLine();
                                                patientAppointment.addPrescription(new Medicine(name, use));
                                                break;
                                            }
                                            else if (add.equalsIgnoreCase("b")){
                                                break;
                                            }
                                            else{
                                                System.out.println("Choose 'a' or 'b'");
                                            }
                                        }
                                        break;
                                    }
                                    case "c":{

                                        System.out.println("Do you want to continue? (y)");
                                        input = sc.nextLine();

                                        if(input.equalsIgnoreCase("y")){

                                            System.out.println("Insert specialization");
                                            String sp = sc.nextLine();

                                            System.out.println("Insert details about the pacient");
                                            String details = sc.nextLine();

                                            patientAppointment.setTicket(new Ticket(sp, details));
                                        }
                                        break;
                                    }
                                    default:{
                                        System.out.println("Invalid Option!!");
                                    }
                                }
                            }
                            break;
                        }

                        case "3":{   // Existent patient

                            input = "y";
                            // caz in care pacientul este deja in baza de date => trebuie sa adaug in istoricul sau vizita curenta
                            Boolean okPass = false;
                            String patientName = null;
                            while(!okPass) {

                                if(!input.equalsIgnoreCase("y")){
                                    break;
                                }

                                try{
                                    System.out.println("Insert patient Lastname");
                                    patientName = sc.nextLine();
                                    okPass = medicalHistoryService.patientInDatabase(medicalHistories, patientName);

                                    //am inserat un nume neexistent in istoric => cere altul nou
                                }catch (Exception exception){
                                    System.out.println(exception.getMessage());

                                    System.out.println("Do you want to try again? (y)");
                                    input = sc.nextLine();
                                }
                            }

                            // doctorul poate sa omita sa completeze toate variabilele
                            Appointment patientAppointment = new Appointment(doctorName);

                            while(true && okPass){

                                System.out.println("Insert details about the appointment");
                                System.out.println("a. Insert disease");
                                System.out.println("b. Insert prescription");
                                System.out.println("c. Insert ticket");
                                System.out.println("d. finish details about the patient");

                                String subOpt = sc.nextLine();

                                if (subOpt.equalsIgnoreCase("d")){
                                    medicalHistoryService.addAppointment(medicalHistories, patientName, patientAppointment);
                                    System.out.println("Saving all the details about the patient " + patientName);
                                    break;
                                }

                                switch (subOpt){
                                    case "a":{

                                        while(true) {
                                            System.out.println("a. Add disease");
                                            System.out.println("b. Exit");
                                            String add = sc.nextLine();

                                            if(add.equalsIgnoreCase("a")){
                                                System.out.println("Insert disease");
                                                String disease = sc.nextLine();
                                                patientAppointment.addDisease(disease);
                                                break;
                                            }
                                            else if(add.equalsIgnoreCase("b")){ //iesere din switch
                                                break;
                                            }
                                            else{
                                                System.out.println("Choose 'a' or 'b'");
                                            }
                                        }
                                        break;
                                    }

                                    case "b":{

                                        while(true) {

                                            System.out.println("a. Add medicine to prescription");
                                            System.out.println("b. Exit");

                                            String add = sc.nextLine();

                                            if(add.equalsIgnoreCase("a")){

                                                System.out.println("Insert medicine name");
                                                String name = sc.nextLine();

                                                System.out.println("Insert how to use it");
                                                String use = sc.nextLine();

                                                patientAppointment.addPrescription(new Medicine(name, use));
                                                break;
                                            }
                                            else
                                                if(add.equalsIgnoreCase("b")){ //iesire
                                                    break;
                                                }
                                                else {
                                                    System.out.println("Choose 'a' or 'b'");
                                                }
                                            }
                                        break;
                                    }

                                    case "c":{

                                        System.out.println("Do you want to continue? (y)");
                                        input = sc.nextLine();

                                        if(input.equalsIgnoreCase("y")){

                                            System.out.println("Insert specialization");
                                            String sp = sc.nextLine();

                                            System.out.println("Insert details about the pacient");
                                            String details = sc.nextLine();

                                            patientAppointment.setTicket(new Ticket(sp, details));
                                        }

                                        break;
                                    }
                                    default:{
                                        System.out.println("Invalid Option!!");
                                    }
                                }
                             }
                            break;
                        }

                        case "4": {  // Find past diseases for a given patient
                            System.out.println("Insert patient name");
                            String name = sc.nextLine();

                            List<String> listDiseases = medicalHistoryService.diseasesByPatient(medicalHistories, name);
                            if (listDiseases != null)
                                System.out.println(listDiseases);

                            break;
                        }

                        case "5": {   // List all tickets for a given patient sorted by date
                            System.out.println("Insert patient name");
                            String name = sc.nextLine();

                            try{
                                List<Ticket> tickets = medicalHistoryService.ticketsByPatientSortedByDate(medicalHistories, name);
                                System.out.println(tickets);

                            }catch (Exception exception){
                                System.out.println(exception.getMessage());
                            }
                            break;
                        }

                        case "6": {  // List all appointments for a given patient (sorted by doctor, then by date)
                            System.out.println("Insert patient name");
                            String name = sc.nextLine();

                            try{
                                List<Appointment> appointments = medicalHistoryService.appointmentsByPatient(medicalHistories, name);
                                System.out.println(appointments);

                            }catch (Exception exception){
                                System.out.println(exception.getMessage());
                            }
                            break;
                        }

                        default:{
                            System.out.println("Invalid Option!!");
                        }
                    }
                }
            }
        }
    }
}
