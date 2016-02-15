package ru.samples.itis.rxpractice.tasks;

import android.support.annotation.NonNull;

import java.util.List;

import ru.samples.itis.rxpractice.content.Person;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public class RxJavaTask3 {

    /**
     * TODO : implement this method
     *
     * This method takes observable of list of persons as a parameter.
     * Note: observable may have more than one persons list
     *
     * 1) Transform observable of list of persons into single observable of all persons
     * 2) For each person in new observable double person name
     * 3) Remove same persons from observable
     * 4) Create observable of single integer
     *    which represents sum of calls Person#intValue for every persons
     * 5) Return observable from step 4
     *
     * @param observable - observable of persons list
     * @return observable with single integer values
     */
    @NonNull
    public static Observable<Integer> task3Observable(
            @NonNull Observable<List<Person>> observable) {
        return observable.flatMap(Observable::from)
                .map(person -> new Person(person.getName() + person.getName(), person.getAge()))
                .distinct()
                .map(Person::getAge)
                .reduce(0, (i1, i2) -> i1 + i2);
    }

}