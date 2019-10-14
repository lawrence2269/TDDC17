;; This is a problem for shakey domain

(define (problem all-room-lights-on-and-move-object-to-rooms)
  (:domain shakey)
  (:objects 
  			ro1 ro2 ro3  - room
  			s 		 	 - shakey
  			sw1 sw2 sw3	 - switch
  			b			 - box
  			g1 g2		 - grippers
  			o1 		 	 - object
  			
  )
  (:init 
  	;; All things that are set to be true when we start
  			(adjacent ro1 ro2)	(adjacent ro2 ro3)	(robot-at s ro1)		(box-at	b ro2)
  			(switch-at sw1 ro1)	(switch-at sw2 ro2)	(switch-at sw3 ro3)	
  			(wide-entrance ro1 ro2) (wide-entrance ro2 ro3) (object-at	o1 ro1)
  			(empty g1)	(empty g2)
  )
  (:goal 
  ;; The goal is to turn on all the lights and also move one object from room 1 to room 2
  		(and (light ro1)
  			 (light ro2)
  			 (light ro3)
  			 (object-at o1 ro2))
  )
  )