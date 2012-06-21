package org.lucid.eventhandler

import akka.actor.Actor
import org.lucid.event.Event

class Dumper extends Actor {
  context.system.eventStream.subscribe(self, classOf[Event])
  
  def receive = {
    case event: Event => println(event)
  }
}