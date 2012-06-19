package org.lucid.event


class Repository[A <: EventSourced[A]](store: EventStore) {
  
  def save(events: List[Event], domain: A) = {
    store.store(events)
    println(domain)
  }
  
  def load(creator: EventSourcedCreator[A], id: String): Option[A] = {
    store.getFor(id) match {
      case None => None
      case Some(events) => Some(creator.handle(events))
    }
  }
}

