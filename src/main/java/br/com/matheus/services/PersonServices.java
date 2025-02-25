package br.com.matheus.services;

import br.com.matheus.controllers.PersonController;
import br.com.matheus.exceptions.RequiredObjectIsNullExeption;
import br.com.matheus.exceptions.ResourceNotFoundException;
import br.com.matheus.mapper.PersonMapper;
import br.com.matheus.model.Person;
import br.com.matheus.repositories.PersonRepository;
import br.com.matheus.vo.v1.PersonVO;
import br.com.matheus.vo.v2.PersonVOV2;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;
    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    @Autowired
    br.com.matheus.mapper.custom.PersonMapper mapper;

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {

        logger.info("Finding all persons");

        var personPage = repository.findAll(pageable);
        var personVoPage = personPage.map(p -> PersonMapper.parseObject(repository.save(p), PersonVO.class));
        personVoPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(),pageable.getPageSize(),"asc")).withSelfRel();
        return assembler.toModel(personVoPage,link);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstname, Pageable pageable) {

        logger.info("Finding person by name");

        var personPage = repository.findPersonByName(firstname, pageable);
        var personVoPage = personPage.map(p -> PersonMapper.parseObject(p, PersonVO.class));
        personVoPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(),pageable.getPageSize(),"asc")).withSelfRel();
        return assembler.toModel(personVoPage,link);
    }

    public PersonVO findById(Long id) {

        logger.info("Finding one person");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        PersonVO vo = PersonMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {

        if (person == null) {
            throw new RequiredObjectIsNullExeption();
        }
        logger.info("Create person");
        var entity = PersonMapper.parseObject(person, Person.class);
        var vo = PersonMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVOV2 createV2(PersonVOV2 person) {

        logger.info("Create person whith V2");
        var entity = mapper.convertVoToEntity(person);
        var vo = mapper.convertEntityToVo(repository.save(entity));
        return vo;
    }

    public PersonVO update(PersonVO person) {

        if (person == null) {
            throw new RequiredObjectIsNullExeption();
        }
        logger.info("Update person");

        var entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = PersonMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    @Transactional
    public PersonVO disablePerson(Long id) {

        logger.info("Disabling one person");

        repository.disablePerson(id);
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        var vo = PersonMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Delete person");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }

}
