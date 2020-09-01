package one.digitalinnovation.personapi.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResposeDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public MessageResposeDTO createPerson(PersonDTO personDTO){
        var personSaved = personRepository.save(personMapper.toModel(personDTO));
        return createMessageResponse(personSaved.getId(), "Created person ID: ");
    }

    public List<PersonDTO> findAll() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        var person = verifyIfExists(id);
        return personMapper.toDTO(person);
    }

    public void delete(Long id) throws PersonNotFoundException{
        verifyIfExists(id);
        personRepository.deleteById(id);
    }

    public MessageResposeDTO updatePerson(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        verifyIfExists(id);
        var personSaved = personRepository.save(personMapper.toModel(personDTO));
        return createMessageResponse(personSaved.getId(), "Updated person ID: ");
    }

    private Person verifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    private MessageResposeDTO createMessageResponse(Long id, String s) {
        return MessageResposeDTO.builder()
                .message(s + id)
                .build();
    }
}
