package org.lucid.domain

trait AggregateRoot {
  val id: String
  val version: Long 
}