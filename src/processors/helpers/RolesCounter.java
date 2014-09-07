/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processors.helpers;

import entity.Person;
import java.util.ArrayList;
import services.MoviesService;
import servicesImpl.MoviesServiceImpl;

/**
 *
 * @author Skrzypek
 */
public class RolesCounter {

    private MoviesService ms;

    public RolesCounter() {
        ms = new MoviesServiceImpl();
    }

    public void countRoles() {
        ArrayList<Person> people = ms.getAllPerson();
        for(Person person : people) {
            countRolesForPerson(person);
        }
    }

    private void countRolesForPerson(Person person) {
        int id = person.getId();
        int counter = ms.countRoles(id);
        System.out.println(person + ": "+ counter);
        ms.addCounter(id, counter);
        //Person p = ms.getPersonById(id);
    }
}
