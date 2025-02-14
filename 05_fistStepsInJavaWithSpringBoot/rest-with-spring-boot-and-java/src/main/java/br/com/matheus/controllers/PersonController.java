package br.com.matheus.controllers;

import br.com.matheus.services.PersonServices;
import br.com.matheus.util.MediaType;
import br.com.matheus.vo.v1.PersonVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for managing people")
public class PersonController {

    @Autowired
    private PersonServices service;
    // private PersonServices service = new PersonServices();

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds all people", description = "Finds all people", tags = {"People"})
    @ApiResponses(value = {@ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonVO.class)))}),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Erro", responseCode = "500", content = @Content)})

    public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll(

            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "15") Integer limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(value = "/findPersonByName/{firstName}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds people by Name", description = "Finds people by Name", tags = {"People"})
    @ApiResponses(value = {@ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonVO.class)))}),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Erro", responseCode = "500", content = @Content)})

    public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findPersonByName(

            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "15") Integer limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(service.findPersonByName(firstName,pageable));
    }

    @Operation(summary = "Find people for ID", description = "Find people for ID", tags = {"People"})
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema =
                    @Schema(implementation = PersonVO.class))}),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Erro", responseCode = "500", content = @Content)
    })

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{id}")
    public PersonVO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
            "application/x-yaml"}, consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
            MediaType.APPLICATION_YML})
    @Operation(summary = "Add a new people", description = "Add a new people", tags = {"People"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PersonVO.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Erro", responseCode = "500", content = @Content)})
    public PersonVO create(@RequestBody PersonVO person) {
        return service.create(person);
    }

    /*
     * @PostMapping(value = "/v2", produces = MediaType.APPLICATION_JSON_VALUE,
     * consumes = MediaType.APPLICATION_JSON_VALUE) public PersonVOV2
     * createV2(@RequestBody PersonVOV2 person) { return service.createV2(person); }
     */
    @CrossOrigin(origins = {"http://localhost:8080", "https://matheus.com.br"})
    @PutMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
            MediaType.APPLICATION_YML}, consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
            MediaType.APPLICATION_YML})
    @Operation(summary = "Change data of the registered person by ID", description = "Change data of the registered person by ID", tags = {
            "People"}, responses = {
            @ApiResponse(description = "Updated", responseCode = "200", content = @Content(schema = @Schema(implementation = PersonVO.class))),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Erro", responseCode = "500", content = @Content)})
    public PersonVO update(@RequestBody PersonVO person) {
        return service.update(person);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
            MediaType.APPLICATION_YML})
    @Operation(summary = "Disable a specific Person by your ID", description = "Disable a specific Person by your ID", tags = {"People"})
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON, schema =
                    @Schema(implementation = PersonVO.class))}),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Erro", responseCode = "500", content = @Content)
    })
    public PersonVO disablePerson(@PathVariable Long id) {
        return service.disablePerson(id);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete person by passing ID", description = "Delete person by passing ID", tags = {
            "People"}, responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Erro", responseCode = "500", content = @Content)})

    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
