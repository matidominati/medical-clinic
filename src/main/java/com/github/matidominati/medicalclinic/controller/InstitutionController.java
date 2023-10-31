package com.github.matidominati.medicalclinic.controller;

import com.github.matidominati.medicalclinic.exception.ErrorResponse;
import com.github.matidominati.medicalclinic.model.dto.InstitutionDto;
import com.github.matidominati.medicalclinic.model.dto.PatientDto;
import com.github.matidominati.medicalclinic.model.dto.commandDto.createCommand.CreateInstitutionCommand;
import com.github.matidominati.medicalclinic.model.dto.commandDto.editCommand.EditInstitutionCommand;
import com.github.matidominati.medicalclinic.service.InstitutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions")
public class InstitutionController {
    private final InstitutionService institutionService;

    @Operation(summary = "Get all institutions", tags = "Institution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = InstitutionDto.class)))}),
    })
    @GetMapping
    public List<InstitutionDto> getAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @Operation(summary = "Get institution by ID", tags = "Institution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = InstitutionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Data not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))}),
    })
    @GetMapping("/{id}")
    public InstitutionDto getInstitution(@PathVariable Long id) {
        return institutionService.getInstitutionById(id);
    }

    @Operation(summary = "Add a new institution", tags = "Institution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = InstitutionDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public InstitutionDto addInstitution(@RequestBody CreateInstitutionCommand createInstitution) {
        return institutionService.addInstitution(createInstitution);
    }

    @Operation(summary = "Delete institution", tags = "Institution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Institution not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/{id}")
    public void deleteInstitution(@PathVariable Long id) {
        institutionService.deleteInstitution(id);
    }

    @Operation(summary = "Update institution", tags = "Institution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = InstitutionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Institution not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PutMapping("/{id}")
    public InstitutionDto updateInstitution(@PathVariable Long id, @RequestBody EditInstitutionCommand editInstitution) {
        return institutionService.updateInstitution(id, editInstitution);
    }

    @Operation(summary = "Add doctor to institution", tags = "Institution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = InstitutionDto.class))}),
            @ApiResponse(responseCode = "404", description = "Data not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping("/{institutionId}/add-doctor/{doctorId}")
    public InstitutionDto addDoctor(@PathVariable Long doctorId, @PathVariable Long institutionId) {
        return institutionService.addDoctor(doctorId, institutionId);
    }
}
