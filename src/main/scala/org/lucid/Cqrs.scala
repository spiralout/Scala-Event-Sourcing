package org.lucid

import scalaz._
import Scalaz._

import org.lucid.event._
import org.lucid.domain._





class PersonService(repo: Repository[Person]) {
  
  def createPerson(name: String, age: Int): String = {
    Person.create(name, age) match {
      case Failure(error) => throw new Exception(error)
      case Success(state) => {
        val (events, person) = state(List[Event]())        
        repo.save(events, person)
        person.id
      }      
    }
  }
  
  def setPersonAge(id: String, age: Int) = {
    repo.load(Person, id) match {
  	  case None => throw new Exception("Oh noes!")    
  	  case Some(person) => {
  	    person.setAge(age) match {
  	      case Failure(error) => throw new Exception(error)
  	      case Success(state) => {
  	        val (events, person) = state(List[Event]())
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
  	    person.setName(name) match {
  	      case Failure(error) => throw new Exception(error)
  	      case Success(state) => {
  	        val (events, person) = state(List[Event]())
      	    repo.save(events, person)
  	      }
  	    }
  	  }
  	}
  } 
}



object Cqrs  extends App {
  val service = new PersonService(new Repository[Person](new EventStore))
  val person_id = service.createPerson("Bob Loblaw", 44)
  service.setPersonAge(person_id, 71)
  service.setPersonName(person_id, "Shut the fuck up!")
}
