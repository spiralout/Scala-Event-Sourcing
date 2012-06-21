package org.lucid.event

import akka.actor.Actor
import org.lucid.domain.AggregateRoot

class IdentityMap extends Actor {
  var theMap =  Map[String, AggregateRoot]()
  
  def receive = {
    case GetById(id) => {
      if (theMap contains id) 
        sender ! Some(theMap(id)) 
      else 
        sender ! None
    }
    case Store(entity) => theMap += (entity.id -> entity)
    case Clear() => theMap = Map[String, AggregateRoot]()
  }
}

case class GetById(id: String)
case class Store(entity: AggregateRoot)
case class Clear()