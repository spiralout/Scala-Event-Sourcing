package org.lucid.domain

import org.lucid.event._

case class Person(id: String, name: String, age: Int) extends EventSourced[Person] {
  
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
    case PersonSetName(id, name) => copy(name = name)
    case PersonSetAge(id, age) => copy(age = age)
  }
}

object Person extends EventSourced[Person] with EventSourcedCreator[Person] {
  
  def create(name: String, age: Int) = accept(PersonCreated("idString", name, age))    

  def handle(event: Event): Person = event match {
    case PersonCreated(id, name, age) => Person(id, name, age) 
  }
  
}

case class PersonCreated(id: String, name: String, age: Int) extends Event
case class PersonSetName(id: String, name: String) extends Event
case class PersonSetAge(id: String, age: Int) extends Event