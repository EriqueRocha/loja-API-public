package dev.erique.loja.integracao.asaas;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Customer {

    private String object;
    private String id;
    private LocalDate dateCreated;
    private String name;
    private String email;
    private String company;
    private String phone;
    private String mobilePhone;
    private String address;
    private String addressNumber;
    private String complement;
    private String province;
    private String postalCode;
    private String cpfCnpj;
    private String personType;
    private boolean deleted;
    private String additionalEmails;
    private String externalReference;
    private boolean notificationDisabled;
    private String observations;
    private String municipalInscription;
    private String stateInscription;
    private boolean canDelete;
    private String cannotBeDeletedReason;
    private boolean canEdit;
    private String cannotEditReason;
    private String city;
    private String state;
    private String country;

}
