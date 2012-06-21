package org.lucid.domain

import org.lucid.event._
import java.util.UUID

case class Person(id: String, version: Long, name: String, age: Int) extends EventSourced[Person] with AggregateRoot {
  
  def setAge(age: Int) = {
    if (age < 16) {
      reject("Too young!")  
    } else {
      accept(PersonSetAge(id, age))
    }
  }
  
  def setName(name: String) = {
    accept(PersonSetName(id, name))
  }
  
  def handle(event: Event): Person = event match { 
    case PersonSetName(id, name) => copy(version = version + 1, name = name)
    case PersonSetAge(id, age) => copy(version = version + 1, 	age = age)
  }
}

object Person extends EventSourced[Person] with EventSourcedCreator[Person] {
  
  def create(name: String, age: Int) = accept(PersonCreated(UUID.randomUUID.toString(), name, age))    

  def handle(event: Event): Person = event match {
    case PersonCreated(id, name, age) => Person(id, 1, name, age) 
  }
  
}

case class Address(street: String, city: String, state: String, zip: String)

case class PersonCreated(id: String, name: String, age: Int) extends Event
case class PersonSetName(id: String, name: String) extends Event
case class PersonSetAge(id: String, age: Int) extends Event