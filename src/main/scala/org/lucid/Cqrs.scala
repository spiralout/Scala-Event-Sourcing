package org.lucid

import scalaz._
import Scalaz._
import akka.actor._

import org.lucid.event._
import org.lucid.domain._
import org.lucid.service._
import org.lucid.eventhandler._


object Cqrs  extends App {
  implicit val system = ActorSystem("LucidSystem")
  val eventStore = system.actorOf(Props[EventStore], name = "eventStore")
  val dumper = system.actorOf(Props[Dumper], name = "dumper")
 
  val service = new PersonService(new Repository[Person](eventStore))

  val person_id = service.createPerson("Bob Lablaw", 44)
  service.setPersonAge(person_id, 71)
  service.setPersonName(person_id, "Bob Loblaw")

  val person_id2 = service.createPerson("George Montgomery Booerns", 16)
  service.setPersonAge(person_id2, 22)
  service.setPersonName(person_id2, "George Montgomery Burns")

  system.shutdown()
}
