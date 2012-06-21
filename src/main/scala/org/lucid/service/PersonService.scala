package org.lucid.service

import scalaz._
import Scalaz._

import org.lucid.domain._
import org.lucid.event._

class PersonService(repo: Repository[Person]) {
  
  def createPerson(name: String, age: Int): String = {
    Person.create(name, age)(List[Event]()) match {
      case (events, Failure(error)) => throw new Exception(error)
      case (events, Success(person)) => {
        repo.save(events, person)
        person.id
      }      
    }
  }
  
  def setPersonAge(id: String, age: Int) = {
    repo.load(Person, id) match {
  	  case None => throw new Exception("Oh noes!")    
  	  case Some(person) => {
  	    person.setAge(age)(List[Event]()) match {
  	      case (events, Failure(error)) => throw new Exception(error)
  	      case (events, Success(person)) => {
      	    repo.save(events, person)
  	      }
  	    }
  	  }
  	}
  }
  
  def setPersonName(id: String, name: String) = {
    repo.load(Person, id) match {
  	  case None => throw new Exception("Oh noes!")    
  	  case Some(person) => {
  	    person.setName(name)(List[Event]()) match {
  	      case (events, Failure(error)) => throw new Exception(error)
  	      case (events, Success(person)) => {
      	    repo.save(events, person)
  	      }
  	    }
  	  }
  	}
  } 
}