package org.lucid.event

import scalaz._
import Scalaz._

import akka.actor.ActorRef
import akka.pattern.ask
import akka.dispatch.Await
import akka.util.Timeout
import akka.util.duration._

import org.lucid.domain.AggregateRoot

class Repository[A <: EventSourced[A]](store: ActorRef, identityMap: ActorRef) {
  implicit val timeout = Timeout(5 seconds)
    
  def save(events: List[Event], domain: AggregateRoot) = {
    store ! StoreEvents(events)
    identityMap ! Store(domain)
    println(domain)
  }
  
  def load(creator: EventSourcedCreator[A], id: String): Option[A] = {
    loadFromIdentityMap(id) match {
      case None => {
        loadFromHistory(creator, id)
      }
      case Some(domain) => Some(domain)
    }
  }
  
  private def loadFromHistory(creator: EventSourcedCreator[A], id: String): Option[A] = {
    val events = Await.result(store ? GetEvents(id), timeout.duration).asInstanceOf[Option[List[Event]]]        
    
    events match {
      case None => None
      case Some(events) => Some(creator.handle(events))
    }    
  }
  
  private def loadFromIdentityMap(id: String): Option[A] = {
    Await.result(identityMap ? GetById(id), timeout.duration).asInstanceOf[Option[A]]          
  }

}

