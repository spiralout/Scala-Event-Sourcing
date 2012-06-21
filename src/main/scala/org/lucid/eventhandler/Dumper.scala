package org.lucid.eventhandler

import akka.actor.Actor
import org.lucid.event.Event

class Dumper extends Actor {
  def receive = {
    case event: Event => println(event)
  }
}