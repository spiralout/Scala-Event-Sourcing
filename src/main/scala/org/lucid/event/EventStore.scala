package org.lucid.event

import akka.actor.Actor

class EventStore extends Actor {
  var eventlist = List[Event]()
  
  def receive = {
    case GetEvents(id) => sender ! getFor(id)
    case StoreEvents(events) => store(events)
  }
  
  private def getFor(id: String): Option[List[Event]] = {
	eventlist.filter(e => e.id == id) match {
      case Nil => None
      case events: List[Event] => Some(events)
    }  
  }
  
  private def store(events: List[Event]) {
    eventlist = eventlist ::: events
  }
}

case class GetEvents(id: String)
case class StoreEvents(events: List[Event])
