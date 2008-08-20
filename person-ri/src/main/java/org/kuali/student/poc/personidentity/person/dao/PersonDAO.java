package org.kuali.student.poc.personidentity.person.dao;

import java.util.List;
import java.util.Set;

import org.kuali.student.poc.personidentity.person.dto.PersonCriteria;
import org.kuali.student.poc.personidentity.person.dto.PersonDisplayDTO;
import org.kuali.student.poc.personidentity.person.entity.Person;
import org.kuali.student.poc.personidentity.person.entity.PersonAttribute;
import org.kuali.student.poc.personidentity.person.entity.PersonAttributeSetType;
import org.kuali.student.poc.personidentity.person.entity.PersonAttributeType;
import org.kuali.student.poc.personidentity.person.entity.PersonName;
import org.kuali.student.poc.personidentity.person.entity.PersonType;

public interface PersonDAO {

	public PersonAttribute createPersonAttribute(PersonAttribute personAttribute);
	public boolean deletePersonAttribute(PersonAttribute personAttribute);
	public PersonAttributeType createPersonAttributeType(PersonAttributeType personAttributeType);
	public PersonType createPersonType(PersonType personType);
	public PersonType fetchPersonType(String id);
	public boolean deletePersonType(PersonType personType);
	public List<PersonAttributeSetType> findPersonAttributeSetTypes(String nameMatch);
	public List<PersonType> findPersonTypes(String nameMatch);
	public List<Person> findPeople(PersonCriteria criteria);
	public List<Person> findPeople(List<String> personIdList);
	public List<Person> findPeopleWithAttributeSetType(String personAttributeSetTypeId, PersonCriteria criteria);
	public List<Person> findPeopleWithPersonType(String personTypeId, PersonCriteria criteria);
	public List<PersonDisplayDTO> findPersonDisplayDTO(PersonCriteria criteria);
	public PersonType updatePersonType(PersonType personType);
	public PersonAttributeSetType createPersonAttributeSetType(PersonAttributeSetType personAttributeSetType);
	public Person createPerson(Person person);
	public Person updatePerson(Person person);
	public Person lookupPerson(String id);
	public boolean deletePerson(Person person);
	public PersonAttributeSetType fetchPersonAttributeSetType(String id);
	public PersonAttributeType fetchPersonAttributeType(String id);
	public PersonName createPersonName(PersonName personName);
	public boolean deletePersonName(PersonName personName);
	public List<PersonAttributeType> findPersonAttributeTypesFromPersonTypeIds(List<String> personTypeIds);
	public Set<PersonAttribute> fetchAttributesByPersonAttributeSetType(
			String personId, List<String> personAttributeSetTypeKeyList);
	public Set<PersonAttribute> fetchAttributesByPersonType(String personId,
			String personTypeKey);

}
