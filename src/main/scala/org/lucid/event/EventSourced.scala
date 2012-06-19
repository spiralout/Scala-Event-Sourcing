package org.lucid.event

import scalaz._
import Scalaz._

trait Event {
  val id: String
}

trait EventSourced[A] {  
  def accept(event: Event): Validation[String, State[List[Event], A]] = {
    Success(state[List[Event], A](events => (events :::List[Event](event), handle(event))))
  }  
  
  def reject(error: String): Validation[String, State[List[Event], A]] = {
    Failure(error)
  }
  
  def handle(event: Event): A  
}

trait EventSourcedCreator[A <: EventSourced[A]] { 
  self: EventSourced[A] =>  
    
  def handle(events: List[Event]): A = {
    events.drop(1).foldLeft(handle(events(0))) { (domain, event) => domain.handle(event) }
  }
}