package org.lucid.event

class EventStore {
  var eventlist = List[Event]()
  
  def getFor(id: String): Option[List[Event]] = {
	eventlist.filter(e => e.id == id) match {
      case Nil => None
      case events: List[Event] => Some(events)
    }  
  }
  
  def store(events: List[Event]) {
    eventlist = eventlist ::: events
  }
}