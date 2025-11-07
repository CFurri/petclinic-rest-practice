package org.springframework.samples.petclinic.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OwnerTest {

    private Owner owner;

    @BeforeEach
    void setup() {
        // Aquest mètode s'executa abans de CADA test per tenir un Owner net
        owner = new Owner();
        owner.setId(1);
        owner.setFirstName("Carles");
        owner.setLastName("Villar");
        owner.setAddress("Carrer de la Universitat");
        owner.setCity("Vic");
        owner.setTelephone("666111222");
    }

    @Test
    void testHasPet() {
        // Comprovem que inicialment no té cap mascota
        assertTrue(owner.getPets().isEmpty());

        Pet pet = new Pet();
        pet.setName("Toby");
        owner.addPet(pet);

        // Comprovem que ara sí en té
        assertFalse(owner.getPets().isEmpty());
        assertEquals(1, owner.getPets().size());
        
        // Verifiquem que la relació bidireccional s'ha establert
        assertEquals(owner, pet.getOwner());
    }

    @Test
    void testGetPetExistingIgnoringCase() {
        Pet pet = new Pet();
        pet.setName("Garfield");
        owner.addPet(pet);

        // Cas 1: Cerca exacta
        assertEquals(pet, owner.getPet("Garfield"));
        
        // Cas 2: Cerca amb diferents majúscules/minúscules (prova la lògica de toLowerCase())
        assertEquals(pet, owner.getPet("garfield"));
        assertEquals(pet, owner.getPet("GARFIELD"));
    }

    @Test
    void testGetPetNotExists() {
        Pet pet = new Pet();
        pet.setName("Garfield");
        owner.addPet(pet);

        // Comprovem que retorna null si no troba la mascota
        assertNull(owner.getPet("Snoopy"));
    }

    @Test
    void testGetPetsReturnsUnmodifiableList() {
        Pet pet = new Pet();
        pet.setName("Lassie");
        owner.addPet(pet);

        List<Pet> pets = owner.getPets();
        
        // Intentar modificar la llista retornada hauria de fallar 
        // perquè Owner.java retorna Collections.unmodifiableList(sortedPets)
        try {
            pets.add(new Pet());
            // Si arribem aquí, el test hauria de fallar perquè hauria d'haver saltat una excepció
            assertTrue(false, "Hauria d'haver llançat UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Esperat: tot correcte
            assertTrue(true);
        }
    }
}
