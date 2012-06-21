package org.lucid.event

import scalaz._
import Scalaz._

import akka.actor.ActorRef
import akka.pattern.ask
import akka.dispatch.Await
import akka.util.Timeout
import akka.util.duration._

class Repository[A <: EventSourced[A]](store: ActorRef) {
  
  def save(events: List[Event], domain: A) = {
    store ! StoreEvents(events)    
    println(domain)
  }
  
  def load(creator: EventSourcedCreator[A], id: String): Option[A] = {
    implicit val timeout = Timeout(5 seconds)
    val events = Await.result(store ? GetEvents(id), timeout.duration).asInstanceOf[Option[List[Event]]]        
    
    events match {
      case None => None
      case Some(events) => Some(creator.handle(events))
    }
  }
}

