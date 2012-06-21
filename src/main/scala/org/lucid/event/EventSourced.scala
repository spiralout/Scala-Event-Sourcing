package org.lucid.event

import scalaz._
import Scalaz._

trait Event {
  val id: String
}


trait EventSourced[A] { 
  type Update[A] = State[List[Event], Validation[String, A]]

  def accept(event: Event): Update[A] = {
    state[List[Event], Validation[String, A]](events => (events :::List[Event](event), Success(handle(event))))
  }  
  
  def reject(error: String): Update[A] = {
    state[List[Event], Validation[String, A]](events => (events, Failure(error)))
  }
  
  def handle(event: Event): A
}

trait EventSourcedCreator[A <: EventSourced[A]] { 
  self: EventSourced[A] =>  
    
  def handle(events: List[Event]): A = {
    events.drop(1).foldLeft(handle(events(0))) { (domain, event) => domain.handle(event) }
  }
}
