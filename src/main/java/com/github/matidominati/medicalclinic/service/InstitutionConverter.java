package com.github.matidominati.medicalclinic.service;

import com.github.matidominati.medicalclinic.model.entity.Institution;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

//public class InstitutionConverter {
//    @Converter(autoApply = true)
//    public static class InstitutionDataConverter implements AttributeConverter<Institution, String> {
//        private static final String SEPARATOR = ", ";
//
//        @Override
//        public String convertToDatabaseColumn(Institution institutions){
//            if (institutions == null) {
//                return null;
//            }
//            StringBuilder sb = new StringBuilder();
//            if (institutions.getName() != null && !institutions.getName().isEmpty()) {
//                sb.append(institutions.getName());
//                sb.append(SEPARATOR);
//            }
//            if (institutions.getAddress() != null && !institutions.getAddress().isEmpty()) {
//                sb.append(institutions.getAddress());
//                sb.append(SEPARATOR);
//            }
//            return sb.toString();
//        }
//
//        @Override
//        public Institution convertToEntityAttribute(String dbInstituion) {
//            if (dbInstituion == null || dbInstituion.isEmpty()) {
//                return null;
//            }
//            String[] pieces = dbInstituion.split(SEPARATOR);
//            if (pieces == null || pieces.length == 0) {
//                return null;
//            }
//            Institution institution = new Institution();
//            String firstPiece = !pieces[0].isEmpty() ? pieces[0] : null;
//            if (dbInstituion.contains(SEPARATOR)) {
//                institution.setName(firstPiece);
//
//                if(pieces.length >= 2 && pieces[1] != null && !pieces[1].isEmpty()){
//                    institution.setAddress(pieces[1]);
//                }
//            } else {
//                institution.setAddress(firstPiece);
//            }
//            return institution;
//        }
//    }
public class InstitutionConverter {
    @Converter(autoApply = true)
    public static class InstitutionDataConverter implements AttributeConverter<List<Institution>, String> {
        private static final String SEPARATOR = ";";

        @Override
        public String convertToDatabaseColumn(List<Institution> institutions) {
            if (institutions == null || institutions.isEmpty()) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            for (Institution institution : institutions) {
                if (institution.getName() != null && !institution.getName().isEmpty()) {
                    sb.append(institution.getName());
                }
                sb.append(SEPARATOR);
                if (institution.getAddress() != null && !institution.getAddress().isEmpty()) {
                    sb.append(institution.getAddress());
                }
                sb.append(SEPARATOR);
            }
            return sb.toString();
        }

        @Override
        public List<Institution> convertToEntityAttribute(String dbInstitutions) {
            if (dbInstitutions == null || dbInstitutions.isEmpty()) {
                return new ArrayList<>();
            }
            String[] pieces = dbInstitutions.split(SEPARATOR);
            if (pieces == null || pieces.length == 0) {
                return new ArrayList<>();
            }
            List<Institution> institutions = new ArrayList<>();
            for (int i = 0; i < pieces.length; i += 2) {
                Institution institution = new Institution();
                if (!pieces[i].isEmpty()) {
                    institution.setName(pieces[i]);
                }
                if (i + 1 < pieces.length && !pieces[i + 1].isEmpty()) {
                    institution.setAddress(pieces[i + 1]);
                }
                institutions.add(institution);
            }
            return institutions;
        }
    }
}


